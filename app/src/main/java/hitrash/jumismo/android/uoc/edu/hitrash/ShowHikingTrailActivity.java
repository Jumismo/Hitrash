package hitrash.jumismo.android.uoc.edu.hitrash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class ShowHikingTrailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener {

    private TextView nameHikingTrailLabel;
    private TextView provinceLabel;
    private TextView hardnessLabel;
    private TextView distanceLabel;

    private ImageButton startRouteButton;
    private ImageButton cleaningClaimButton;

    private ImageView imageSignalize;
    private ImageView imageInformationOffice;
    private ImageView imageGuide;
    private ImageView imageHikingTrail;

    private ImageButton imageUserGroupButton;
    private ImageButton imageCleaningGroupButton;

    private String id_hiking_trail;

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
        setContentView(R.layout.activity_show_hiking_trail);

        nameHikingTrailLabel = (TextView) findViewById(R.id.nameHikingTrailLabel);
        provinceLabel = (TextView) findViewById(R.id.provinceLabel);
        hardnessLabel = (TextView) findViewById(R.id.hardnessLabel);
        distanceLabel = (TextView) findViewById(R.id.distanceLabel);

        startRouteButton = (ImageButton) findViewById(R.id.startRouteButton);
        cleaningClaimButton = (ImageButton) findViewById(R.id.cleaningClaimButton);

        imageSignalize = (ImageView) findViewById(R.id.imageSignalize);
        imageInformationOffice = (ImageView) findViewById(R.id.imageInformationOffice);
        imageGuide = (ImageView) findViewById(R.id.imageGuide);
        //imageHikingTrail = (ImageView) findViewById(R.id.imageHikingTrail);

        imageUserGroupButton = (ImageButton) findViewById(R.id.imageUserGroupButton);
        imageCleaningGroupButton = (ImageButton) findViewById(R.id.imageCleaningGroupButton);

        // Save the DEFAULT_ZOOM of the camera of Google Maps
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("DEFAULT_ZOOM", DEFAULT_ZOOM);
        editor.commit();

        // Enable the permissions for the location
        getLocationPermission();

        Intent intent = getIntent();
        id_hiking_trail = intent.getStringExtra("id_hiking_trail");

        AsyncHttpUtils.get(Constants.URI_SHOW_HIKING_TRAIL + id_hiking_trail, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    HikingTrail hikingTrail = new HikingTrail();
                    hikingTrail.parseFromJSON(data);

                    nameHikingTrailLabel.setText(hikingTrail.getName());
                    provinceLabel.setText(hikingTrail.getProvince());
                    hardnessLabel.setText(hikingTrail.getHardness());
                    distanceLabel.setText(hikingTrail.getDistance().toString());

                    String[] latlong = hikingTrail.getLocation().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);

                    LatLng location = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(location));
                    moveCamera(location, DEFAULT_ZOOM);
//                    imageHikingTrail.setImageBitmap(hikingTrail.getImages().get(0));

                    if (hikingTrail.getSignalize()) {
                        imageSignalize.setVisibility(View.VISIBLE);
                    } else {
                        imageSignalize.setVisibility(View.GONE);
                    }

                    if (hikingTrail.getInformationOffice()) {
                        imageInformationOffice.setVisibility(View.VISIBLE);
                    } else {
                        imageInformationOffice.setVisibility(View.GONE);
                    }

                    if (hikingTrail.getGuide()) {
                        imageGuide.setVisibility(View.VISIBLE);
                    } else {
                        imageGuide.setVisibility(View.GONE);
                    }

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

        imageUserGroupButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewGroupActivity.class);
                intent.putExtra("id_hiking_trail", id_hiking_trail);
                intent.putExtra("isCleaningClaim", false);
                startActivity(intent);
            }
        });

        imageCleaningGroupButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewGroupActivity.class);
                intent.putExtra("id_hiking_trail", id_hiking_trail);
                intent.putExtra("isCleaningClaim", true);
                startActivity(intent);
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

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng point) {
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(point));
//            }
//        });

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
                            Toast.makeText(ShowHikingTrailActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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

    private void initMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ShowHikingTrailActivity.this);
    }

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
