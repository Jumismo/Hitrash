package hitrash.jumismo.android.uoc.edu.hitrash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.Group;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.User;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class NewGroupActivity extends AppCompatActivity implements
        CalendarDatePickerDialogFragment.OnDateSetListener,
        RadialTimePickerDialogFragment.OnTimeSetListener,
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener  {

    private TextView dateGroupEditText;
    private TextView groupNameInput;
    private TextView groupDescriptionInput;
//    private TextView locationGroupEditText;

    private EditText latitudText;
    private EditText longitudText;

    private ListView listViewUsers;

    private ImageButton acceptButtonGroup;
    private ImageButton addMarker;

    private ScrollView mScrollView;

    private List<User> userList;
    private List<String> userListName;
    private boolean isCleaningClaim;
    private String id_hiking_trail;

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    // Maps variables
    private SharedPreferences sharedPref;
    private GoogleMap mMap;
    private float DEFAULT_ZOOM = 12f;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private MarkerOptions locationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        dateGroupEditText = (TextView) findViewById(R.id.dateGroupEditText);
        groupNameInput = (TextView) findViewById(R.id.groupNameInput);
        groupDescriptionInput = (TextView) findViewById(R.id.groupDescriptionInput);
//        locationGroupEditText = (TextView) findViewById(R.id.locationGroupEditText);

        latitudText = (EditText) findViewById(R.id.textLatitud);
        longitudText = (EditText) findViewById(R.id.textLongitud);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);

        acceptButtonGroup = (ImageButton) findViewById(R.id.acceptButtonGroup);
        addMarker = (ImageButton) findViewById(R.id.addMarker);

        mScrollView = (ScrollView) findViewById(R.id.mScrollView);

        // Enable the permissions for the location
        getLocationPermission();

        dateGroupEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(NewGroupActivity.this);
                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });

        final List<User> userList = new ArrayList<User>();
        final List<String> userListName = new ArrayList<String>();

        AsyncHttpUtils.get(Constants.URI_ACTIVE_USERS, null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try{
                    JSONArray data = response.getJSONArray("data");
                    for(int i = 0; i < data.length(); i++)
                    {
                        JSONObject userJSON = data.getJSONObject(i);
                        User user = new User();
                        user.parseFromJSON(userJSON);

                        userList.add(user);
                        userListName.add(user.getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewUsers = (ListView)findViewById(R.id.listViewUsers);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, userListName);
        listViewUsers.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        isCleaningClaim = intent.getBooleanExtra("isCleaningClaim", false);
        id_hiking_trail = intent.getStringExtra("id_hiking_trail");

        acceptButtonGroup.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(final View v) {
                RequestParams rp = new RequestParams();
                rp.add("name", groupNameInput.getText().toString());
                rp.add("description", groupDescriptionInput.getText().toString());
                rp.add("location", locationMarker.getPosition().latitude + "," + locationMarker.getPosition().longitude);
                //rp.add("location", locationGroupEditText.getText().toString());

                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateGroupEditText.getText().toString());
                    String dateRP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
                    rp.add("date", dateRP);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                }


                rp.add("id_hiking_trail", id_hiking_trail);
                rp.add("isCleaningGroup", String.valueOf(isCleaningClaim));
                int len = listViewUsers.getCount();
                SparseBooleanArray checked = listViewUsers.getCheckedItemPositions();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        User item = userList.get(i);
                        rp.add("users[]", item.getId());
                    }
                }

                AsyncHttpUtils.post(Constants.URI_NEW_GROUP, rp, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            Group group = new Group();
                            group.parseFromJSON(data);
                            Toast.makeText(getApplicationContext(), getString(R.string.groupCreated), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(v.getContext(), HikingTrailsActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getApplicationContext(), getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        addMarker.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Double lat = Double.parseDouble(latitudText.getText().toString());
                Double lon = Double.parseDouble(longitudText.getText().toString());

                if(lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180) {
                    LatLng point = new LatLng(lat, lon);

                    mMap.clear();
                    locationMarker = new MarkerOptions().position(point);
                    mMap.addMarker(locationMarker);

                    moveCamera(point, DEFAULT_ZOOM);
                }
            }
        });
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        dateGroupEditText.setText(getString(R.string.calendar_date_picker_result_values, String.format("%02d", dayOfMonth), String.format("%02d", monthOfYear), year));

        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(NewGroupActivity.this);
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }


    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = (CalendarDatePickerDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialogFragment != null) {
            calendarDatePickerDialogFragment.setOnDateSetListener(this);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        dateGroupEditText.setText(dateGroupEditText.getText().toString() + " " + getString(R.string.time_picker_result_value, String.format("%02d", hourOfDay), String.format("%02d", minute)));
    }


    // Configuración del mapa

    // Método que inicia el fragment del mapa
    private void initMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewGroupActivity.this);
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setOnCameraMoveListener(this);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                locationMarker = new MarkerOptions().position(point);
                mMap.addMarker(locationMarker);
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if(DEFAULT_ZOOM != mMap.getCameraPosition().zoom) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("DEFAULT_ZOOM", mMap.getCameraPosition().zoom);
            editor.commit();

            DEFAULT_ZOOM = sharedPref.getFloat("DEFAULT_ZOOM", 12f);
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
                            Toast.makeText(NewGroupActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
        }
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
