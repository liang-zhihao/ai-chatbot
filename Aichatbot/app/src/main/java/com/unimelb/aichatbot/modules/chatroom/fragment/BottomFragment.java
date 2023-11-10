package com.unimelb.aichatbot.modules.chatroom.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatroom.model.Message;
import com.unimelb.aichatbot.modules.chatroom.model.RecommendRestaurantRequest;
import com.unimelb.aichatbot.modules.chatroom.model.RecommendRestaurantResponse;
import com.unimelb.aichatbot.modules.chatroom.model.type.MessageType;
import com.unimelb.aichatbot.modules.chatroom.model.type.SenderType;
import com.unimelb.aichatbot.modules.chatroom.service.ChatService;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.util.LocationUtil;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.Date;

public class BottomFragment extends Fragment implements CustomViewController {


    private View rootView;
    TextView tvCommand;
    private LocationUtil locationUtil;

    // Define the interface as an inner type of the Fragment
    public interface OnMessageListener {
        void appendMessageToUI(Message message);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_msg_bottom_sheet, container, false);
        initializeView();

        initializeListener();
        initLocationUtil();
        return rootView;
    }

    // Use a member variable for the listener
    private OnMessageListener messageListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            messageListener = (OnMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMessageListener");
        }

//        init here as onAttach is called before onCreate and context is available here

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void initLocationUtil() {
        OnSuccessListener<Location> locationSuccessListener = location -> {
            if (location == null) {
                Log.i("LocationTest", "null");
            } else {
                Log.i("LocationTest", "Success");
                updateUI(location);     // if successful, update the UI
            }
        };
        OnFailureListener locationFailureListener = e -> {
            Log.i("LocationTest", "Failed");
            Toast.makeText(requireContext(), "Location Failed", Toast.LENGTH_SHORT).show();
        };
        LocationCallback locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i("LocationTest", "Location updates");
            }
        };
        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        // PERMISSION GRANTED
                        updateLocation();
                        Log.i("LocationTest", "Permission granted");
                    } else {
                        // PERMISSION NOT GRANTED
                        Log.i("LocationTest", "Permission not granted");
                    }
                }
        );
        locationUtil = new LocationUtil(this, locationSuccessListener, locationFailureListener, locationCallBack, requestPermissionLauncher);
    }

    private void updateUI(Location location) {
        // update the UI according to the GPS information
        Toast.makeText(getContext(), "Location: " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        recommendRestaurant(location);
    }


    void updateLocation() {
        // if user grants permission
        locationUtil.updateLocation();

    }

    // update the UI according to the GPS information


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


    @Override
    public void initializeView() {
        tvCommand = rootView.findViewById(R.id.command_recommend_restaurant);

        // Initializing ImageButtons
        // tvCommand = rootView.findViewById(R.id.tv_command_recommend_restaurant);

    }

    @Override
    public void initializeListener() {
        tvCommand.setOnClickListener(v -> {
            // requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            updateLocation();
        });
    }


    public void recommendRestaurant(Location location) {
        tvCommand.setText("Thinking...");
        tvCommand.setClickable(false);
        String userId = LoginManager.getInstance(requireContext().getApplicationContext()).getUserId();
        String senderName = LoginManager.getInstance(requireContext().getApplicationContext()).getUsername();
        RetrofitFactory.createWithAuth(ChatService.class, requireContext().getApplicationContext()).getRecommendRestaurants(new RecommendRestaurantRequest(location.getLatitude(), location.getLongitude())).enqueue(new MyCallback<RecommendRestaurantResponse>() {
            @Override
            public void onSuccess(BaseResponse<RecommendRestaurantResponse> result) {
                tvCommand.setText("As Gordon Ramsay recommends nearby restaurants");
                tvCommand.setClickable(true);

                RecommendRestaurantResponse recommendRestaurantResponse = result.getData();
                messageListener.appendMessageToUI(new Message(recommendRestaurantResponse.getReply(), MessageType.TEXT, userId, SenderType.ME, new Date(), senderName));
            }

            @Override
            public void onError(ErrorResponse error, @NonNull Throwable t) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                tvCommand.setText("As Gordon Ramsay recommends nearby restaurants");
                tvCommand.setClickable(true);
            }
        });
    }

    @Override
    public void initializeActionBar() {

    }

    @Override
    public void initializeViewModel() {

    }

    @Override
    public void initializeRecyclerView() {

    }


//    ---------------------------


}