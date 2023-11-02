package com.unimelb.aichatbot.modules.profile.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.unimelb.aichatbot.R;

public class InputPasswordBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText input;
    private Button confirmButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_password_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = view.findViewById(R.id.edit_text_input_name);
        confirmButton = view.findViewById(R.id.button_confirm);
        cancelButton = view.findViewById(R.id.button_cancel);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = input.getText().toString();
                // 在这里处理用户输入的password

                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
