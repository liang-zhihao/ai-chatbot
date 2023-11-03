package com.unimelb.aichatbot.modules.setting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.SettingsActivity;
import com.unimelb.aichatbot.databinding.FragmentSettingBinding;
import com.unimelb.aichatbot.modules.account.activity.ChooseBotActivity;
import com.unimelb.aichatbot.modules.profile.activity.activity.ProfileActivity;

public class SettingFragment extends Fragment implements CustomViewController {

    private FragmentSettingBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.setting_layout, container, false);

        TextView profileButton = root.findViewById(R.id.Profile);
        TextView editCharacterButton = root.findViewById(R.id.Edit_Character);
        TextView chatHistoryButton = root.findViewById(R.id.Chat_History);

        profileButton.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        editCharacterButton.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), ChooseBotActivity.class);
            startActivity(intent);
        });

        chatHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // NavController navController = Navigation.findNavController(view);
                //
                // // navigate HomeFragment
                // navController.navigate(R.id.navigation_chat_history);

                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);

            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void initializeView() {

    }

    @Override
    public void initializeListener() {

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

}