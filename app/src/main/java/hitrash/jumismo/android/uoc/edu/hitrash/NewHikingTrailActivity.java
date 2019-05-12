package hitrash.jumismo.android.uoc.edu.hitrash;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hitrash.jumismo.android.uoc.edu.hitrash.Model.HikingTrail;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.AsyncHttpUtils;
import hitrash.jumismo.android.uoc.edu.hitrash.Utils.Constants;

public class NewHikingTrailActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener{

    private EditText nameHikingTrailEditText;
    private EditText provinceHikingTrailEditText;
    private EditText distanceHikingTrailEditText;

    private CheckBox signalizeHikingTrailCheckbox;
    private CheckBox informationOfficeHikingTrailCheckbox;
    private CheckBox guideHikingTrailCheckbox;

    private Spinner hardnessTypeHikingTrailSpinner;

    private ImageButton imageAddButton;
    private ImageButton acceptButtonHikingTrail;

    private List<String> imageList;
    private static int RESULT_LOAD_IMAGE = 1;

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
        setContentView(R.layout.activity_new_hiking_trail);

        nameHikingTrailEditText = (EditText) findViewById(R.id.nameHikingTrailEditText);
        provinceHikingTrailEditText = (EditText) findViewById(R.id.provinceHikingTrailEditText);
        distanceHikingTrailEditText = (EditText) findViewById(R.id.distanceHikingTrailEditText);

        signalizeHikingTrailCheckbox = (CheckBox) findViewById(R.id.signalizeHikingTrailCheckbox);
        informationOfficeHikingTrailCheckbox = (CheckBox) findViewById(R.id.informationOfficeHikingTrailCheckbox);
        guideHikingTrailCheckbox = (CheckBox) findViewById(R.id.guideHikingTrailCheckbox);

        hardnessTypeHikingTrailSpinner = (Spinner) findViewById(R.id.hardnessTypeHikingTrailSpinner);

        imageAddButton = (ImageButton) findViewById(R.id.imageAddButton);
        acceptButtonHikingTrail = (ImageButton) findViewById(R.id.acceptButtonHikingTrail);

        imageList = new ArrayList<String>();

        // Enable the permissions for the location
        getLocationPermission();

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id_user");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_LOAD_IMAGE);
            }
        }

        acceptButtonHikingTrail.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(final View v) {
                RequestParams rp = new RequestParams();
                rp.add("name", nameHikingTrailEditText.getText().toString());
                rp.add("province", provinceHikingTrailEditText.getText().toString());
                rp.add("distance", distanceHikingTrailEditText.getText().toString());
                rp.add("signalize", String.valueOf(signalizeHikingTrailCheckbox.isChecked()));
                rp.add("informationOffice", String.valueOf(informationOfficeHikingTrailCheckbox.isChecked()));
                rp.add("guide", String.valueOf(guideHikingTrailCheckbox.isChecked()));
                rp.add("hardness", hardnessTypeHikingTrailSpinner.getSelectedItem().toString());
//                rp.add("location", "0.0, 0.0");
                rp.add("id_user", id);
                rp.add("location", locationMarker.getPosition().latitude + "," + locationMarker.getPosition().longitude);

                for(String file : imageList)
                {
                    rp.add("images[]", file);
                }

                AsyncHttpUtils.post(Constants.URI_NEW_HIKING_TRAILS, rp, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            HikingTrail hikingTrailNew = new HikingTrail();
                            hikingTrailNew.parseFromJSON(data);
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.hikingTrailCreated), Toast.LENGTH_SHORT);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), getString(R.string.errorParseObject), Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(v.getContext(), HikingTrailsActivity.class);
                        intent.putExtra("id_user", id);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getApplicationContext(), getString(R.string.errorRequest) + ": " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        imageAddButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    // Método que inicia el fragment del mapa
    private void initMap(){

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewHikingTrailActivity.this);
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
                            Toast.makeText(NewHikingTrailActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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

    //TODO: Implementar funcionalidad para cargar imágenes en base de datos
//    Implementación para cargar las imágenes en una ruta de senderismo (En proceso)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImageUri = data.getData();
//            try {
//                byte[] imageBytes = loadImage(getRealPathFromURI(selectedImageUri));
//                String encode = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//                imageList.add(encode);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }
//
//    public static byte[] loadImage(String filePath) throws Exception {
//        File file = new File(filePath);
//        int size = (int)file.length();
//        byte[] buffer = new byte[size];
//        FileInputStream in = new FileInputStream(file);
//        in.read(buffer);
//        in.close();
//        return buffer;
//    }
}
