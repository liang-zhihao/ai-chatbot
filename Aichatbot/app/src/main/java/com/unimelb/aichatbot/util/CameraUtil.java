package com.unimelb.aichatbot.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtil {

    private final Fragment fragment;
    private final ActivityResultLauncher<Intent> takePictureLauncher;
    private Uri photoURI;

    public CameraUtil(Fragment fragment, ActivityResultLauncher<Intent> takePictureLauncher) {
        this.fragment = fragment;
        this.takePictureLauncher = takePictureLauncher;
    }

    public void openCameraAndTakeImage(String name) {
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            Toast.makeText(fragment.getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            // Open camera
            dispatchTakePictureIntent(name);
        }
    }

    public Uri getPhotoURI() {
        return photoURI;
    }

    private void dispatchTakePictureIntent(String name) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile(name);
            photoURI = FileProvider.getUriForFile(fragment.requireActivity(), "com.unimelb.aichatbot.provider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            takePictureLauncher.launch(takePictureIntent);
        }

    }

    private File createImageFile(String name) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = fragment.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(name + imageFileName, ".jpg", storageDir);
    }
}

