package com.unimelb.aichatbot.modules.profile.activity.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.profile.activity.request.ChangePasswordRequest;
import com.unimelb.aichatbot.modules.profile.activity.server.ProfileService;
import com.unimelb.aichatbot.network.RetrofitFactory;

public class InputPasswordBottomSheetDialogFragment extends BottomSheetDialogFragment {


    public interface OnPasswordUpdatedListener {
        void onPwdUpdated(String pwd);
    }

    private EditText oldPasswordInput;
    private EditText newPasswordInput;
    private Button confirmButton;
    private Button cancelButton;

    private OnPasswordUpdatedListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_password_bottom_sheet_dialog, container, false);
    }

    public void setPasswordUpdatedListener(OnPasswordUpdatedListener listener) {
        mListener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        oldPasswordInput = view.findViewById(R.id.edit_text_old_password);
        newPasswordInput = view.findViewById(R.id.edit_text_new_password);
        // confirmButton = view.findViewById(R.id.button_confirm);
        // cancelButton = view.findViewById(R.id.button_cancel);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPasswordInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();

                String userId = "loading8425@gmail.com";
                Log.i("InputPasswordFragment", "Old Password: " + oldPassword);
                Log.i("InputPasswordFragment", "New Password: " + newPassword);

                ChangePasswordRequest request = new ChangePasswordRequest();
                request.setUser_id(userId);
                request.setOld_password(oldPassword);
                request.setNew_password(newPassword);
                ProfileService service = RetrofitFactory.createWithAuth(ProfileService.class, getActivity());
                Call<Void> call = service.changePassword(request);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(activity, "Pwd updated successfully!", Toast.LENGTH_SHORT).show();
                                        if (mListener != null) {
                                            mListener.onPwdUpdated(newPassword);
                                        }
                                    } else {
                                        Toast.makeText(activity, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                    dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }
                    }
                });

//                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("InputPasswordFragment", "Cancel button clicked");
                dismiss();
            }
        });
    }

}
