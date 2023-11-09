package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.databinding.FragmentRecentChatBinding;
import com.unimelb.aichatbot.modules.chatHistory.ChatHistoryViewModel;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;
import com.unimelb.aichatbot.modules.chatHistory.activity.RecommendationActivity;
import com.unimelb.aichatbot.modules.chatHistory.adapter.ChatHistoryItemAdapter;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.RecentChatResponse;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.newChat.activity.NewChatActivity;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.socketio.SocketClient;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/*
 *
 * Request recent chat
 * Render recent chat
 *
 *
 * */

public class RecentChatFragment extends Fragment implements CustomViewController, SensorEventListener {

    private static final String TAG = "RecentChatFragment";
    private RecyclerView recyclerView;
    ProgressBar progressBar;

    private FragmentRecentChatBinding binding;
    private Button newChatBtn;

    private ChatHistoryViewModel historyViewModel;

    List<RecentChatResponse> chats;
    private SensorManager mSensorManager;
    private Vibrator mVibrator;

    private static final long SHAKE_COOLDOWN_TIME = 2000; // shake CD
    private long lastShakeTime = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // TODO Loading animation
        initializeView();
        initializeRecyclerView();
        initializeActionBar();
        initializeViewModel();
        loadUserRecentChat();
        initializeListener();

        // init socket client
        SocketClient.startConnection();

        SocketClient.getInstance().emitInitializeConnection(LoginManager.getInstance(requireContext().getApplicationContext()).getUserId());
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void showLoading(boolean isLoading) {
        progressBar.bringToFront();
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }


    @Override
    public void initializeView() {

        newChatBtn = binding.button;
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void initializeViewModel() {
        ChatHistoryItemAdapter.OnItemClickListener listener = (pos, item) -> {
            // to do when click
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra("roomId", item.getRoomId());
            intent.putExtra("roomName", item.getRoomName());
            startActivity(intent);
        };

        historyViewModel = new ViewModelProvider(this).get(ChatHistoryViewModel.class);
        historyViewModel.getHistoryItems().observe(getViewLifecycleOwner(), items -> {
            // Update UI here
            recyclerView.setAdapter(new ChatHistoryItemAdapter(getContext(), items, listener));
        });

    }

    @Override
    public void initializeRecyclerView() {
        recyclerView = binding.recentChatRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void initializeListener() {
        newChatBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NewChatActivity.class));
        });
    }

    @Override
    public void initializeActionBar() {
        // OnBackPressed is different from the back button in the action bar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        String username = LoginManager.getInstance(requireContext().getApplicationContext()).getUsername();

        if (actionBar != null) {
            actionBar.show();
            // actionBar.setTitle("Recent Chat: " + username);
            // hide back button
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public boolean containsParticipant(List<String> participants, String userId) {
        for (String participant : participants) {
            if (participant.equals(userId)) {
                return true;
            }
        }
        return false;
    }


    public void loadUserRecentChat() {

        String userId = LoginManager.getInstance(requireContext().getApplicationContext()).getUserId();


        ChatHistoryService chatHistoryService = RetrofitFactory.create(ChatHistoryService.class);
        chatHistoryService.getUserChatList(userId).enqueue(new MyCallback<List<RecentChatResponse>>() {
            @Override
            public void onSuccess(BaseResponse<List<RecentChatResponse>> result) {
                //  render chat list
                List<RecentChatResponse> chats = result.getData();
                RecentChatFragment.this.chats = chats;
                for (RecentChatResponse chat : chats) {

                    Log.i(TAG, "get room id: " + chat.getRoomId());
                }

                List<HistoryItem> historyItems = chats.stream()
                        .map(chat -> {
                            List<String> participants = chat.getParticipants().stream()
                                    .filter(participant -> !participant.equals(userId))
                                    .collect(Collectors.toList());
                            return new HistoryItem(chat.getRoomId(), chat.getLastMessage(), "https://dummyimage.com/300", chat.getName(), participants);
                        }).collect(Collectors.toList());

                //     toast
                historyViewModel.setHistoryItems(historyItems);
                // Toast.makeText(getContext(), "Load recent chat success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error == null) {
                    //     toast error
                    Toast.makeText(getContext(), "Load failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //     print throwable
                    t.printStackTrace();
                    return;
                } else {
                    //     toast error
                    Toast.makeText(getContext(), "Load failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onResume() {
        // Log.e("DEBUG", "onResume of HomeFragment");
        loadUserRecentChat();
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // ... 检测摇一摇逻辑 ...
            float[] values = sensorEvent.values;
            if ((Math.abs(values[0]) > 15) || Math.abs(values[1]) > 15 || Math.abs(values[2]) > 15) {
                long now = System.currentTimeMillis();
                // 只有当上次摇动的时间与现在的时间差超过冷却时间时，才执行操作
                if ((now - lastShakeTime) > SHAKE_COOLDOWN_TIME) {
                    Toast.makeText(getContext(), "shake！", Toast.LENGTH_SHORT).show();

                    // 创建一个Intent并启动新的Activity
                    Intent intent = new Intent(getContext(), RecommendationActivity.class);
                    startActivity(intent);

                    mVibrator.vibrate(500);

                    lastShakeTime = now; // 更新最后一次摇动的时间
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // @Override
    // public void onPause() {
    //     Log.e("DEBUG", "OnPause of HomeFragment");
    //     super.onPause();
    // }


}