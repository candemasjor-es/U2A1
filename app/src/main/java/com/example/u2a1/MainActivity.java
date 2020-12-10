package com.example.u2a1;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.widget.Toast;


public class MainActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 0;
    private boolean permissionDenied = false;
    private GoogleMap mapas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapas = googleMap;
        LatLng numero = new LatLng(39.156471395887884, -0.43757321708975905);
        mapas.addMarker(new MarkerOptions().position(numero).title("Xuquer"));
        mapas.moveCamera(CameraUpdateFactory.newLatLng(numero));
        mapas.moveCamera(CameraUpdateFactory.newLatLngZoom(numero, 14));
        mapas.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mapas != null) {
                mapas.setMyLocationEnabled(true);
            }
        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Se hizo clic en el botón Mi ubicación", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}
