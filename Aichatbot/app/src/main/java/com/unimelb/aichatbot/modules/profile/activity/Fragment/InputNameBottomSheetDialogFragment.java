package com.unimelb.aichatbot.modules.profile.activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.profile.activity.request.ChangeUsernameRequest;
import com.unimelb.aichatbot.modules.profile.activity.server.ProfileService;
import com.unimelb.aichatbot.network.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputNameBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public interface OnNameUpdatedListener {
        void onNameUpdated(String newName);
    }

    private OnNameUpdatedListener mListener;
    private EditText input;
    private Button confirmButton;
    private Button cancelButton;
    private ProfileService service = RetrofitFactory.create(ProfileService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input_name_bottom_sheet_dialog, container, false);
    }

    public void setOnNameUpdatedListener(OnNameUpdatedListener listener) {
        mListener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input = view.findViewById(R.id.edit_text_input_name);
        // confirmButton = view.findViewById(R.id.button_confirm);
        // cancelButton = view.findViewById(R.id.button_cancel);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = input.getText().toString();
                String userId = "loading8425@gmail.com";

                ChangeUsernameRequest nameRequest = new ChangeUsernameRequest(userId, newName);
                service = RetrofitFactory.createWithAuth(ProfileService.class, getActivity());
                Call<Void> call = service.changeUsername(nameRequest);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(activity, "Name updated successfully!", Toast.LENGTH_SHORT).show();
                                        if (mListener != null) {
                                            mListener.onNameUpdated(newName);
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
