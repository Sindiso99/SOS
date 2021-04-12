package com.swansea.sindiso;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationProviderClient;;
    private SupportMapFragment mapFragment;
    private DataBaseHandler dataBaseHandler;
    private LatLng myLatLng;
    private EditText physicalAddress;
    private EditText emailAddress;
    private Button saveDetails;
    private Button leaveActivity;
    private Intent intent;
    private double latitude;
    private double longitude;
    private User user;
    private boolean existingAddress = true;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dataBaseHandler = new DataBaseHandler(MapsActivity.this);
        physicalAddress = (EditText) findViewById(R.id.address_editText);
        emailAddress = (EditText) findViewById(R.id.email_editText);

        if (getIntent().hasExtra("com.swansea.sindiso.user")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.user");
            user = dataBaseHandler.getAddressData(user);
                if (user.getFullAddress() != null){
                    physicalAddress.setText(user.getFullAddress());
                    if (user.getEmail() != null){
                        emailAddress.setText(user.getEmail());
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "No address found", Toast.LENGTH_SHORT).show();
                    existingAddress = false;
                }
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        drawMap();
        saveDetails = (Button) findViewById(R.id.save_address_details_btn);
        saveDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    user.setEmail(emailAddress.getText().toString());
                    user.setFullAddress(physicalAddress.getText().toString());
                    user.setLocation(latitude, longitude);
                    if (dataBaseHandler.addAddress(user, existingAddress)) {
                        Toast.makeText(MapsActivity.this, "Address Details Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapsActivity.this, "Failed to Save Details", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MapsActivity.this, R.string.missing_input, Toast.LENGTH_SHORT).show();
                }
            }
        });

        leaveActivity = (Button) findViewById(R.id.leave_details_activity);
        leaveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MapsActivity.this, HomePage.class);
                intent.putExtra("com.swansea.sindiso.takeUser", user);
                startActivity(intent);
            }
        });
    }

    private void drawMap(){
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        Task<Location> task = locationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().setZoomGesturesEnabled(true);
                            mMap.getUiSettings().setCompassEnabled(true);
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            if (existingAddress) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user.getLocation(), 16));
                                markerOptions = new MarkerOptions();
                                markerOptions.position(user.getLocation());
                                markerOptions.title("Your Saved Location");
                                mMap.addMarker(markerOptions);
                            } else {
                                if (myLatLng != null) {
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16));
                                }
                            }
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    myLatLng = latLng;
                                    markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    String postcode = getPostCode(myLatLng.latitude, myLatLng.longitude);
                                    markerOptions.title(postcode);
                                    mMap.clear();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                    mMap.addMarker(markerOptions);
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    private String getPostCode(double latitude, double longitude){
        try {
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
            physicalAddress.setText(addressToString(addresses.get(0)));
            this.latitude = latitude;
            this.longitude = longitude;
            return addresses.get(0).getPostalCode();
        } catch (Exception e) {
            return ("No Postcode");
        }
    }

    private String addressToString(Address address) {
        return (address.getAddressLine(0) + ", " +
                address.getSubAdminArea() + ", " +
                address.getLocality() + ", " +
                address.getAdminArea());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}