package com.unimelb.aichatbot.modules.chatroom.model;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unimelb.aichatbot.R;
import com.google.android.gms.location.LocationResult;
import com.unimelb.aichatbot.util.CameraUtil;
import com.unimelb.aichatbot.util.LocationUtil;

import java.io.IOException;

public class BottomFragment extends Fragment {

    private static final int SELECT_IMAGE_REQUEST_CODE = 1;
    // TODO  handle senor button. dod is select image (compress) then convert to base64 and send request to server and get response
    //    Function 1: Accessing Gallery to Select Image
//    Request Permissions:
//
//    Request READ_EXTERNAL_STORAGE permission to read images from the gallery.
//    Open Gallery:
//
//    Use an Intent to open the gallery and select an image.
//    Get Selected Image:
//
//    Get the selected image's URI from the result in onActivityResult.
//    Send Image to Server:
//
//    Use Retrofit or another library to send the selected image to the server.
    // Function to access gallery and select image
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> selectImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.requireContext().getContentResolver(), selectedImageUri);
                        assert bitmap != null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Image Selected", Toast.LENGTH_SHORT).show();
                }
            });
    OnSuccessListener<Location> locationSuccessListener = location -> {
        if (location == null) {
            Log.d("LocationTest", "null");
        } else {
            Log.d("LocationTest", "Success");
            updateUI(location);     // if successful, update the UI
        }
    };
    OnFailureListener locationFailureListener = e -> {
        Log.d("LocationTest", "Failed");
        Toast.makeText(requireContext(), "Location Failed", Toast.LENGTH_SHORT).show();
    };
    // Declaring ImageButtons
    private ImageButton btnAddImage, btnMap, btnQuestion, btnCamera;
    private LocationUtil locationUtil;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    // PERMISSION GRANTED
                    updateLocation();
                } else {
                    // PERMISSION NOT GRANTED
                }
            }
    );


//    ----------------------------------------
// todo request permission for gps and send location to server dod is response is like I know where u are from server

//    Function 2: Showing Map to Send Location
//    Request Permissions:
//
//    Request ACCESS_FINE_LOCATION permission to access the user's location.
//    Show Map:
//
//    Use a Map API like Google Maps API to show a map.
//    Get Selected Location:
//
//    Allow the user to select a location on the map.
//    Send Location to Server:

    //    Use Retrofit or another library to send the selected location to the server.
    // Function to show map and send location
    private CameraUtil cameraUtil;
    ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), "Picture taken: ", Toast.LENGTH_SHORT).show();
                    try {

                        Uri photoURI = cameraUtil.getPhotoURI();
                        assert photoURI != null;
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), photoURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the error
                    Toast.makeText(getContext(), "Failed to take picture", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocationCallback locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.d("LocationTest", "Location updates");
            }
        };
//        init here as onAttach is called before onCreate and context is available here
        locationUtil = new LocationUtil(this, locationSuccessListener, locationFailureListener, locationCallBack, requestPermissionLauncher);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_msg_bottom_sheet, container, false);

        // Initializing ImageButtons
        btnAddImage = view.findViewById(R.id.btn_msg__add_image);
        btnMap = view.findViewById(R.id.btn_msg__map);
        btnQuestion = view.findViewById(R.id.message_question_button);
        btnCamera = view.findViewById(R.id.btn_msg__camera);

        // Set onClickListener to the ImageButtons if needed
        btnAddImage.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add Image", Toast.LENGTH_SHORT).show();
            // Handle btnAddImage click
            accessGalleryAndSelectImage();
        });
        btnMap.setOnClickListener(v -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            updateLocation();
        });

        btnQuestion.setOnClickListener(v -> {
            // Handle btnQuestion click
        });

        btnCamera.setOnClickListener(v -> {
            // Handle btnCamera click
            Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
//            openCameraAndTakeImage();
            cameraUtil = new CameraUtil(this, takePictureLauncher);
            cameraUtil.openCameraAndTakeImage("11");
        });

        return view;
    }

    public void accessGalleryAndSelectImage() {
        // Check for permission
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this.requireActivity(), storage_permissions_33, 1);
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            // Open gallery
            Toast.makeText(getContext(), "Open gallery", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageActivityResultLauncher.launch(intent);
        }
    }

    void updateLocation() {
        // if user grants permission
        locationUtil.updateLocation();
    }

    // update the UI according to the GPS information


// todo dod is taking a image -> compress image  -> convert to base64 -> send to server with special prompt -> get response

//  -----------------------------------------------------

    //    Function 3: Opening Camera to Take Image
//    Request Permissions:
//
//    Request CAMERA and WRITE_EXTERNAL_STORAGE permissions to take pictures and save them.
//    Open Camera:
//
//    Use an Intent to open the camera.
//    Get Captured Image:
//
//    Get the captured image's URI from the result in onActivityResult.
//    Send Image to Server:
//
//    Use Retrofit or another library to send the captured image to the server.

    private void updateUI(Location location) {
        Toast.makeText(requireContext(), "Location: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }


//    ---------------------------


}