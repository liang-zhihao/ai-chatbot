package com.unimelb.aichatbot.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationUtil {

    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private final Context context;
    private final Activity activity;

    private final OnSuccessListener<Location> locationSuccessListener;
    private final OnFailureListener locationFailureListener;
    private final ActivityResultLauncher<String> requestPermissionLauncher;

    public LocationUtil(Fragment fragment, OnSuccessListener<Location> locationSuccessListener, OnFailureListener locationFailureListener, LocationCallback locationCallback, ActivityResultLauncher<String> requestPermissionLauncher) {

        this.context = fragment.requireContext();
        this.activity = fragment.requireActivity();
        this.locationSuccessListener = locationSuccessListener;
        this.locationFailureListener = locationFailureListener;
        this.requestPermissionLauncher = requestPermissionLauncher;
        this.locationCallback = locationCallback;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // location updates every 5 seconds
    }


    public void updateLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            fusedLocationClient.getLastLocation().addOnSuccessListener(activity, locationSuccessListener).addOnFailureListener(activity, locationFailureListener);
        } else {
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    // Other utility methods like handling permission results can also be added here if needed
}
