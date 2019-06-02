package hitrash.jumismo.android.uoc.edu.hitrash;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Comment;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Group;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class ConversationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener {

    private Comment comment;
    private TextView nameConversation;
    private TextView dateConversation;
    //private TextView locationConversation;
    private List<Comment> commentsList;
    private EditText insertCommentUserGroup;
    private ImageButton insertButtonCommentUserGroup;
    private ScrollView mScrollView;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private String id_group;

    // Maps variables
    private SharedPreferences sharedPref;
    private GoogleMap mMap;
    private float DEFAULT_ZOOM = 12f;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        nameConversation = findViewById(R.id.nameConversation);
        dateConversation = findViewById(R.id.dateConversation);
        //locationConversation = findViewById(R.id.locationConversation);
        insertCommentUserGroup = findViewById(R.id.insertCommentUserGroup);
        insertButtonCommentUserGroup = findViewById(R.id.insertButtonCommentUserGroup);
        mScrollView = (ScrollView) findViewById(R.id.scrollViewConversation);

        Intent intent = getIntent();
        id_group = intent.getStringExtra("id_group");

        commentsList = new ArrayList<Comment>();

        recycler = (RecyclerView) findViewById(R.id.conversation_recycler_view);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Enable the permissions for the location
        getLocationPermission();

        updateComments();


        insertButtonCommentUserGroup.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(final View v) {
                RequestParams rq = new RequestParams();
                rq.add("comment", insertCommentUserGroup.getText().toString());

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = new Date();
                rq.add("publicationDate", dateFormat.format(date));

                rq.add("id_group", id_group);

                SharedPreferences settings = getSharedPreferences("Preference", 0);
                String idUser = settings.getString("IdUser", "");
                rq.add("id_author", idUser);

                AsyncHttpUtils.post(Constants.URI_NEW_COMMENT, rq, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            Comment commentNew = new Comment();
                            commentNew.parseFromJSON(data);
                            comment = commentNew;

                            RequestParams rq2 = new RequestParams();
                            rq2.add("comments[]", comment.getId());

                            AsyncHttpUtils.put(Constants.URI_UPDATE_USER_GROUP + id_group, rq2, new JsonHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    updateComments();
                                    Toast.makeText(v.getContext(),v.getContext().getString(R.string.commentUpdate), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    Toast.makeText(v.getContext(),v.getContext().getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                            if(data == null){
                                Toast.makeText(v.getContext(), v.getContext().getString(R.string.errorInsertObject), Toast.LENGTH_LONG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(),v.getContext().getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(v.getContext(),v.getContext().getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                insertCommentUserGroup.setText("");

            }
        });
    }

    // Método para actualizar la lista de comentarios del array adapter
    public void updateComments(){
        AsyncHttpUtils.get(Constants.URI_USER_GROUP + id_group, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject data = response.getJSONObject("data");
                    Group group = new Group();
                    group.parseFromJSON(data);

                    nameConversation.setText(group.getName());
                    dateConversation.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(group.getDate()));
                    //locationConversation.setText(group.getLocation());
                    String[] latlong = group.getLocation().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);

                    LatLng location = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(location));
                    moveCamera(location, DEFAULT_ZOOM);

                    adapter = new CommentConversationArrayAdapter(group.getComments());
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(),getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCameraMove() {
        if(DEFAULT_ZOOM != mMap.getCameraPosition().zoom) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("DEFAULT_ZOOM", mMap.getCameraPosition().zoom);
            editor.commit();

            DEFAULT_ZOOM = sharedPref.getFloat("DEFAULT_ZOOM", 12f);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setOnCameraMoveListener(this);
        }

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude));
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if(DEFAULT_ZOOM != mMap.getCameraPosition().zoom) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("DEFAULT_ZOOM", mMap.getCameraPosition().zoom);
            editor.commit();

            DEFAULT_ZOOM = sharedPref.getFloat("DEFAULT_ZOOM", 12f);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Toast.makeText(this, "Permission failed", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    // Método para recuperar la localización del usuario
    private void getDeviceLocation(){

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Toast.makeText(ConversationActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // Método que inicia el fragment del mapa
    private void initMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ConversationActivity.this);
    }

    // Método para solicitar permisos al usuario para la localización

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
