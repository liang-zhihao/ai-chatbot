package com.unimelb.aichatbot.modules.chatHistory.activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;
import com.unimelb.aichatbot.modules.chatHistory.RecommendUserRequest;
import com.unimelb.aichatbot.modules.chatHistory.RecommendUserResponse;
import com.unimelb.aichatbot.modules.chatHistory.fragment.RecentChatFragment;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.RecentChatResponse;
import com.unimelb.aichatbot.modules.chatHistory.service.ChatHistoryService;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomRequest;
import com.unimelb.aichatbot.modules.newChat.model.NewChatRoomResponse;
import com.unimelb.aichatbot.modules.newChat.service.NewChatService;
import com.unimelb.aichatbot.network.BaseResponse;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.network.dto.ErrorResponse;
import com.unimelb.aichatbot.util.ImgUtil;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationActivity extends AppCompatActivity {

    String friendName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        requestRecommendation();
        Button cancelButton = findViewById(R.id.cancel_button);
        Button startButton = findViewById(R.id.start_button);

        //random set avatar
        int[] imageResources = new int[] {
                R.drawable.troye_sivan,
                R.drawable.donald_trump,
                R.drawable.einstein,
                R.drawable.elizabeth_ii,
                R.drawable.gordon_ramsay,
                R.drawable.mark_zuckerberg,
                R.drawable.steve_jobs,
                R.drawable.troye_sivan


        };
        Random random = new Random();
        int randomImageIndex = random.nextInt(imageResources.length);

        CircleImageView avatarImageView = findViewById(R.id.recommend_avatar);
        avatarImageView.setImageResource(imageResources[randomImageIndex]);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startChat();
                finish();
            }
        });
    }

    public void requestRecommendation(){

        // Toast.makeText(RecommendationActivity.this, "点击了", Toast.LENGTH_SHORT).show();
        //finish();
        String userId = LoginManager.getInstance(RecommendationActivity.this.getApplicationContext()).getUserId();

        ChatHistoryService chatHistoryService = RetrofitFactory.create(ChatHistoryService.class);
        RecommendUserRequest request = new RecommendUserRequest(userId);
        Call<RecommendUserResponse> call = chatHistoryService.getRecommendUser(request);
        call.enqueue(new Callback<RecommendUserResponse>() {
            @Override
            public void onResponse(Call<RecommendUserResponse> call, Response<RecommendUserResponse> response) {
                if (response.isSuccessful()) {
                    // 处理成功的响应
                    RecommendUserResponse userInfo = response.body();

                    TextView textView = findViewById(R.id.name);
                    textView.setText(userInfo.getData().getUserId());
                    Log.d("11", userInfo.getData().getUserId());
                    friendName=userInfo.getData().getUserId();
                    //ImgUtil.setImgView(RecommendationActivity.this, current.getAvatarUrl(), binding.imageAvatar);

                } else {

                }
            }

            @Override
            public void onFailure(Call<RecommendUserResponse> call, Throwable t) {

            }
        });




    }

    public void startChat(){

        String userId = LoginManager.getInstance(RecommendationActivity.this.getApplicationContext()).getUserId();
        List<String> participants = new ArrayList<>();
        participants.add(userId);
        participants.add(friendName);
        String chatName = userId + ", " + friendName;
        RetrofitFactory.create(NewChatService.class).createChatroom(new NewChatRoomRequest(participants, chatName, userId)).enqueue(new MyCallback<NewChatRoomResponse>() {
            @Override
            public void onSuccess(BaseResponse<NewChatRoomResponse> result) {
                Toast.makeText(RecommendationActivity.this, "Create room success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecommendationActivity.this, MessageActivity.class);
                intent.putExtra("roomId", result.getData().getChatRoomId());
                startActivity(intent);
            }

            @Override
            public void onError(ErrorResponse error, Throwable t) {
                if (error != null) {
                    Toast.makeText(RecommendationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (t != null) {
                    Toast.makeText(RecommendationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

    }
}
