package com.solipsism.seekpick.Search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.solipsism.seekpick.Dash.Shopkeeper;
import com.solipsism.seekpick.R;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    ArrayList<Shopkeeper> items;
    ArrayList<String> markerTitles;
    float zoom;

    static Shopkeeper resultShop;
    BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        markerTitles = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();
        zoom = intent.getFloatExtra("zoom",(float)12.0);
        Log.e("zoom","   "+zoom);
        try {
            items = (ArrayList<Shopkeeper>) intent.getSerializableExtra("itemList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("No.of items,", items.size() + "");
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        for (int i = 0; i < items.size(); i++) {
            mMap = googleMap;
            Shopkeeper p = items.get(i);
            String name = p.getShopname();
            double lat = p.getLat();
            double lng = p.getLng();
            // Add a marker in Sydney and move the camera
            LatLng newPlace = new LatLng(lat, lng);
            float zoomLevel = zoom;
            String markerTitle = name;
            Log.e("zoom", ""+zoom);
            markerTitles.add(i, markerTitle);
            mMap.addMarker(new MarkerOptions().position(newPlace).title(markerTitle));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPlace, zoomLevel));
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                resultShop = items.get(markerTitles.indexOf(arg0.getTitle()));
                bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }

        });
    }
}