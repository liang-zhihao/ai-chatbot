package com.unimelb.aichatbot.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentSettingBinding;
import com.unimelb.aichatbot.ui.profile.ProfileActivity;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    private View ProfileView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        SettingViewModel settingViewModel =
//                new ViewModelProvider(this).get(SettingViewModel.class);
//
//        binding = FragmentSettingBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textSetting;
//        settingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;

        System.out.printf("12121");
        View root = inflater.inflate(R.layout.setting_layout, container, false);
        ProfileView=root.findViewById(R.id.Profile);

        ProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
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
}