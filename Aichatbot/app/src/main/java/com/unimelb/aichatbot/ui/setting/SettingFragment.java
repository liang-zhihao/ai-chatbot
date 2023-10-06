package com.unimelb.aichatbot.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentSettingBinding;
import com.unimelb.aichatbot.ui.home.HomeFragment;
import com.unimelb.aichatbot.ui.login_signup.choose_chara;
import com.unimelb.aichatbot.ui.profile.ProfileActivity;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.setting_layout, container, false);

        TextView  profileButton = root.findViewById(R.id.Profile);
        TextView  editCharacterButton = root.findViewById(R.id.Edit_Character);
        TextView chatHistoryButton = root.findViewById(R.id.Chat_History);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        editCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), choose_chara.class);
                startActivity(intent);
            }
        });

        chatHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavController navController = Navigation.findNavController(view);

                // navigate HomeFragment
                navController.navigate(R.id.navigation_home);

            }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}