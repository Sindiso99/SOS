package com.swansea.sindiso;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MyMatches extends AppCompatActivity {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationProviderClient;
    private SupportMapFragment mapFragment;
    private DataBaseHandler dataBaseHandler;
    private User user;
    private MarkerOptions markerOptions;
    private List<Integer> matchIds;
    private List<User> matches;
    private Drawable vector;
    private Bitmap bitmap;
    private Canvas canvas;
    private Space myHolderSpace;
    private User myHolder;
    private ListView myUsers;
    private TextView matchesTextView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_matches);
        dataBaseHandler = new DataBaseHandler(MyMatches.this);
        matches = new ArrayList<>();
        myUsers = (ListView) findViewById(R.id.matches_listView);
        matchesTextView = (TextView) findViewById(R.id.matches_textView);

        if (getIntent().hasExtra("com.swansea.sindiso.user")) {
            user = getIntent().getParcelableExtra("com.swansea.sindiso.user");
        }
        user = dataBaseHandler.getAddressData(user);
        matchIds = dataBaseHandler.getMatches(user);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_matches_map);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (user.isStudent()) {
            handleStudent();
        } else {
            handleHolder();
        }
        drawMap();
    }

    public void handleStudent() {
        matchesTextView.setText("My Match");
        myHolder = dataBaseHandler.getUserDetails(matchIds.get(0));
        myHolder = dataBaseHandler.getAddressData(myHolder);
        myHolderSpace = dataBaseHandler.getHolderSpace(myHolder.getId());
        matches.add(myHolder);
        userAdapter = new UserAdapter(this, matches, myHolderSpace);
        myUsers.setAdapter(userAdapter);
    }

    public void handleHolder() {
        for (Integer curId : matchIds) {
            User tempUser;
            tempUser = dataBaseHandler.getUserDetails(curId);
            tempUser = dataBaseHandler.getAddressData(tempUser);
            tempUser.setContainers(dataBaseHandler.getAllContainers(tempUser.getId()));
            matches.add(tempUser);
        }
        userAdapter = new UserAdapter(this, matches);
        myUsers.setAdapter(userAdapter);
    }

    private void drawMap() {
        if (ActivityCompat.checkSelfPermission(MyMatches.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyMatches.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        Task<Location> task = locationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().setZoomGesturesEnabled(true);
                            mMap.getUiSettings().setCompassEnabled(true);
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user.getLocation(), 14));
                            markerOptions = new MarkerOptions();
                            markerOptions.position(user.getLocation());
                            markerOptions.title("Your Current Location");
                            mMap.addMarker(markerOptions);

                            for (User curMatch : matches) {
                                markerOptions = new MarkerOptions();
                                markerOptions.position(curMatch.getLocation());
                                markerOptions.title(curMatch.getUserName());
                                if (user.isStudent()){
                                    markerOptions.icon(drawIcon(MyMatches.this, R.drawable.house_icon));
                                } else {
                                    markerOptions.icon(drawIcon(MyMatches.this, R.drawable.box_icon));
                                }
                                mMap.addMarker(markerOptions);
                            }

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {

                                }
                            });

                        }
                    });
                }
            }
        });
    }

    private BitmapDescriptor drawIcon(Context context, int vectorId) {
        vector = ContextCompat.getDrawable(context, vectorId);
        vector.setBounds(0, 0, vector.getIntrinsicWidth(), vector.getIntrinsicHeight());

        bitmap = Bitmap.createBitmap(vector.getIntrinsicWidth(), vector.getMinimumHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        vector.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}