// package com.unimelb.aichatbot.modules.chatroom.fragment;
//
// import static android.app.Activity.RESULT_OK;
//
// import android.Manifest;
// import android.app.Activity;
// import android.content.Context;
// import android.content.Intent;
// import android.content.pm.PackageManager;
// import android.graphics.Bitmap;
// import android.location.Location;
// import android.net.Uri;
// import android.os.Bundle;
// import android.provider.MediaStore;
// import android.util.Log;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;
// import android.widget.ImageButton;
// import android.widget.Toast;
//
// import androidx.activity.result.ActivityResultLauncher;
// import androidx.activity.result.contract.ActivityResultContracts;
// import androidx.annotation.NonNull;
// import androidx.annotation.Nullable;
// import androidx.core.app.ActivityCompat;
// import androidx.core.content.ContextCompat;
// import androidx.fragment.app.Fragment;
//
// import com.google.android.gms.location.LocationCallback;
// import com.google.android.gms.location.LocationResult;
// import com.google.android.gms.tasks.OnFailureListener;
// import com.google.android.gms.tasks.OnSuccessListener;
// import com.unimelb.aichatbot.CustomViewController;
// import com.unimelb.aichatbot.R;
// import com.unimelb.aichatbot.modules.chatroom.model.Message;
// import com.unimelb.aichatbot.modules.chatroom.model.RecommendRestaurantRequest;
// import com.unimelb.aichatbot.modules.chatroom.model.RecommendRestaurantResponse;
// import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
// import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;
// import com.unimelb.aichatbot.modules.chatroom.service.ChatService;
// import com.unimelb.aichatbot.network.BaseResponse;
// import com.unimelb.aichatbot.network.MyCallback;
// import com.unimelb.aichatbot.network.RetrofitFactory;
// import com.unimelb.aichatbot.network.dto.ErrorResponse;
// import com.unimelb.aichatbot.util.CameraUtil;
// import com.unimelb.aichatbot.util.LocationUtil;
//
// import java.io.IOException;
// import java.util.Date;
//
// public class BottomFragment2 extends Fragment implements CustomViewController {
//
//
//     private View rootView;
//     private ImageButton btnAddImage, btnMap, btnQuestion, btnCamera;
//     private LocationUtil locationUtil;
//
//     // Define the interface as an inner type of the Fragment
//     public interface OnMessageListener {
//         void appendMessageToUI(Message message);
//     }
//
//     @Nullable
//     @Override
//     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                              @Nullable Bundle savedInstanceState) {
//
//         rootView = inflater.inflate(R.layout.fragment_msg_bottom_sheet, container, false);
//
//         initializeView();
//
//         initializeListener();
//         initLocationUtil();
//         return rootView;
//     }
//
//     // Use a member variable for the listener
//     private OnMessageListener messageListener;
//
//     @Override
//     public void onAttach(@NonNull Context context) {
//         super.onAttach(context);
//         try {
//             messageListener = (OnMessageListener) context;
//         } catch (ClassCastException e) {
//             throw new ClassCastException(context.toString()
//                     + " must implement OnMessageListener");
//         }
//
// //        init here as onAttach is called before onCreate and context is available here
//
//     }
//
//     @Override
//     public void onDetach() {
//         super.onDetach();
//
//     }
//
//     public void initLocationUtil() {
//         OnSuccessListener<Location> locationSuccessListener = location -> {
//             if (location == null) {
//                 Log.i("LocationTest", "null");
//             } else {
//                 Log.i("LocationTest", "Success");
//                 updateUI(location);     // if successful, update the UI
//             }
//         };
//         OnFailureListener locationFailureListener = e -> {
//             Log.i("LocationTest", "Failed");
//             Toast.makeText(requireContext(), "Location Failed", Toast.LENGTH_SHORT).show();
//         };
//         LocationCallback locationCallBack = new LocationCallback() {
//             @Override
//             public void onLocationResult(@NonNull LocationResult locationResult) {
//                 super.onLocationResult(locationResult);
//                 Log.i("LocationTest", "Location updates");
//             }
//         };
//         ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
//                 new ActivityResultContracts.RequestPermission(),
//                 result -> {
//                     if (result) {
//                         // PERMISSION GRANTED
//                         updateLocation();
//                         Log.i("LocationTest", "Permission granted");
//                     } else {
//                         // PERMISSION NOT GRANTED
//                         Log.i("LocationTest", "Permission not granted");
//                     }
//                 }
//         );
//         locationUtil = new LocationUtil(this, locationSuccessListener, locationFailureListener, locationCallBack, requestPermissionLauncher);
//     }
//
//     private void updateUI(Location location) {
//         // update the UI according to the GPS information
//         Toast.makeText(getContext(), "Location: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//         recommendRestaurant(location);
//     }
//
//     private static final int SELECT_IMAGE_REQUEST_CODE = 1;
//     // TODO  handle senor button. dod is select image (compress) then convert to base64 and send request to server and get response
//     //    Function 1: Accessing Gallery to Select Image
// //    Request Permissions:
// //
// //    Request READ_EXTERNAL_STORAGE permission to read images from the gallery.
// //    Open Gallery:
// //
// //    Use an Intent to open the gallery and select an image.
// //    Get Selected Image:
// //
// //    Get the selected image's URI from the result in onActivityResult.
// //    Send Image to Server:
// //
// //    Use Retrofit or another library to send the selected image to the server.
//     // Function to access gallery and select image
//     public static String[] storage_permissions_33 = {
//             Manifest.permission.READ_MEDIA_IMAGES,
//             Manifest.permission.READ_MEDIA_AUDIO,
//             Manifest.permission.READ_MEDIA_VIDEO
//     };
//     // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
//     ActivityResultLauncher<Intent> selectImageActivityResultLauncher = registerForActivityResult(
//             new ActivityResultContracts.StartActivityForResult(),
//             result -> {
//                 if (result.getResultCode() == RESULT_OK) {
//                     // There are no request codes
//                     Intent data = result.getData();
//                     assert data != null;
//                     Uri selectedImageUri = data.getData();
//                     Bitmap bitmap = null;
//                     try {
//                         bitmap = MediaStore.Images.Media.getBitmap(this.requireContext().getContentResolver(), selectedImageUri);
//                         assert bitmap != null;
//                     } catch (IOException e) {
//                         e.printStackTrace();
//                     }
//                     Toast.makeText(getContext(), "Image Selected", Toast.LENGTH_SHORT).show();
//                 }
//             });
//
//     // Declaring ImageButtons
//
//
// //    ----------------------------------------
// // todo request permission for gps and send location to server dod is response is like I know where u are from server
//
// //    Function 2: Showing Map to Send Location
// //    Request Permissions:
// //
// //    Request ACCESS_FINE_LOCATION permission to access the user's location.
// //    Show Map:
// //
// //    Use a Map API like Google Maps API to show a map.
// //    Get Selected Location:
// //
// //    Allow the user to select a location on the map.
// //    Send Location to Server:
//
//     //    Use Retrofit or another library to send the selected location to the server.
//     // Function to show map and send location
//     private CameraUtil cameraUtil;
//     ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
//             new ActivityResultContracts.StartActivityForResult(),
//             result -> {
//                 if (result.getResultCode() == Activity.RESULT_OK) {
//                     Toast.makeText(getContext(), "Picture taken: ", Toast.LENGTH_SHORT).show();
//                     try {
//
//                         Uri photoURI = cameraUtil.getPhotoURI();
//                         assert photoURI != null;
//                         Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), photoURI);
//                     } catch (IOException e) {
//                         e.printStackTrace();
//                         Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
//                     }
//                 } else {
//                     // Handle the error
//                     Toast.makeText(getContext(), "Failed to take picture", Toast.LENGTH_SHORT).show();
//                 }
//             });
//
//
//     public void accessGalleryAndSelectImage() {
//         // Check for permission
//         if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
//             // Request permission
//             ActivityCompat.requestPermissions(this.requireActivity(), storage_permissions_33, 1);
//             Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//         } else {
//             // Open gallery
//             Toast.makeText(getContext(), "Open gallery", Toast.LENGTH_SHORT).show();
//             Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//             selectImageActivityResultLauncher.launch(intent);
//         }
//     }
//
//     void updateLocation() {
//         // if user grants permission
//         locationUtil.updateLocation();
//     }
//
//     // update the UI according to the GPS information
//
//
// // todo dod is taking a image -> compress image  -> convert to base64 -> send to server with special prompt -> get response
//
// //  -----------------------------------------------------
//
//     //    Function 3: Opening Camera to Take Image
// //    Request Permissions:
// //
// //    Request CAMERA and WRITE_EXTERNAL_STORAGE permissions to take pictures and save them.
// //    Open Camera:
// //
// //    Use an Intent to open the camera.
// //    Get Captured Image:
// //
// //    Get the captured image's URI from the result in onActivityResult.
// //    Send Image to Server:
// //
// //    Use Retrofit or another library to send the captured image to the server.
//
//
//     @Override
//     public void initializeView() {
//         // Initializing ImageButtons
//         btnAddImage = rootView.findViewById(R.id.btn_msg__add_image);
//         btnMap = rootView.findViewById(R.id.btn_msg__map);
//         btnQuestion = rootView.findViewById(R.id.message_question_button);
//         btnCamera = rootView.findViewById(R.id.btn_msg__camera);
//     }
//
//     @Override
//     public void initializeListener() {
//         // Set onClickListener to the ImageButtons if needed
//         btnAddImage.setOnClickListener(v -> {
//             Toast.makeText(getContext(), "Add Image", Toast.LENGTH_SHORT).show();
//             // Handle btnAddImage click
//             accessGalleryAndSelectImage();
//         });
//         btnMap.setOnClickListener(v -> {
//             // requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
//             updateLocation();
//         });
//
//         btnQuestion.setOnClickListener(v -> {
//             // Handle btnQuestion click
//         });
//
//         btnCamera.setOnClickListener(v -> {
//             // Handle btnCamera click
//             Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
// //            openCameraAndTakeImage();
//             cameraUtil = new CameraUtil(this, takePictureLauncher);
//             cameraUtil.openCameraAndTakeImage("11");
//         });
//     }
//
//     public void recommendRestaurant(Location location) {
//
//         RetrofitFactory.createWithAuth(ChatService.class, requireContext().getApplicationContext()).getRecommendRestaurants(new RecommendRestaurantRequest(location.getLatitude(), location.getLongitude())).enqueue(new MyCallback<RecommendRestaurantResponse>() {
//             @Override
//             public void onSuccess(BaseResponse<RecommendRestaurantResponse> result) {
//                 Toast.makeText(getContext(), result.getData().getReply(), Toast.LENGTH_SHORT).show();
//                 RecommendRestaurantResponse recommendRestaurantResponse = result.getData();
//                 messageListener.appendMessageToUI(new Message(recommendRestaurantResponse.getReply(), MessageType.TEXT, "11", SenderType.ME, new Date(), "OOO"));
//             }
//
//             @Override
//             public void onError(ErrorResponse error, @NonNull Throwable t) {
//                 Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//             }
//         });
//     }
//
//     @Override
//     public void initializeActionBar() {
//
//     }
//
//     @Override
//     public void initializeViewModel() {
//
//     }
//
//     @Override
//     public void initializeRecyclerView() {
//
//     }
//
//
// //    ---------------------------
//
//
// }