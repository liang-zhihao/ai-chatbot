package com.unimelb.aichatbot.modules.chatHistory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentHomeBinding;

public class ChatHistoryFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_layout, container, false);

        TextView seeMoreButton = root.findViewById(R.id.see_more);
        seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemFragment itemFragment = (ItemFragment) getChildFragmentManager().findFragmentById(R.id.chatHistory);
                if (itemFragment != null) {
                    itemFragment.updateList();
                }
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