package com.unimelb.aichatbot.modules.setting.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.input.DialogInputExtKt;
import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.SettingsActivity;
import com.unimelb.aichatbot.databinding.FragmentSettingBinding;
import com.unimelb.aichatbot.modules.account.activity.ChooseBotActivity;
import com.unimelb.aichatbot.modules.profile.activity.Fragment.InputNameBottomSheetDialogFragment;
import com.unimelb.aichatbot.modules.profile.activity.Fragment.InputPasswordBottomSheetDialogFragment;
import com.unimelb.aichatbot.modules.profile.activity.activity.ProfileActivity;
import com.unimelb.aichatbot.modules.profile.activity.request.ChangePasswordRequest;
import com.unimelb.aichatbot.modules.profile.activity.request.ChangeUsernameRequest;
import com.unimelb.aichatbot.modules.profile.activity.server.ProfileService;
import com.unimelb.aichatbot.network.MyCallback;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.util.LoginManager;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements CustomViewController, InputNameBottomSheetDialogFragment.OnNameUpdatedListener, InputPasswordBottomSheetDialogFragment.OnPasswordUpdatedListener {


    private TextView nameButton;
    private TextView passwordButton;
    private TextView emailButton;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Using ViewBinding for inflated layout
        View root = inflater.inflate(R.layout.profile_layout, container, false);
        emailButton = root.findViewById(R.id.buttonMail);
        nameButton = root.findViewById(R.id.buttonName);
        passwordButton = root.findViewById(R.id.buttonPassword);
        String username = LoginManager.getInstance(requireContext().getApplicationContext()).getUsername();
        nameButton.setText(username);
        emailButton.setText(LoginManager.getInstance(requireContext().getApplicationContext()).getUserId());
        setupActionBar();
        initializeListeners();
        return root;
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Profile");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    private void initializeListeners() {
        // Using method reference for cleaner event handling
        nameButton.setOnClickListener(v -> openInputNameBottomSheet());
        passwordButton.setOnClickListener(v -> openInputPasswordBottomSheet());
    }

    private void showInputDialog(View view, String title, DialogInterface.OnClickListener posListener, DialogInterface.OnClickListener nagListener) {
        // Use the Builder class for convenient dialog construction.
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        builder.setMessage(title)
                .setPositiveButton("OK", posListener)
                .setNegativeButton("Cancel", nagListener);
        // Create the AlertDialog object and return it.
        builder.create();
        builder.show();
    }

    private void openInputNameBottomSheet() {
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_input_name_bottom_sheet_dialog, null);

        showInputDialog(view, "Please enter your name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = ((TextView) view.findViewById(R.id.edit_text_input_name)).getText().toString();
                if (nameButton != null) {
                    nameButton.setText(newName);
                }

                String userId = LoginManager.getInstance(requireContext().getApplicationContext()).getUserId();
                ChangeUsernameRequest nameRequest = new ChangeUsernameRequest(userId, newName);
                RetrofitFactory.createWithAuth(ProfileService.class, getActivity()).changeUsername(nameRequest).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(activity, "Name updated successfully!", Toast.LENGTH_SHORT).show();
                                        nameButton.setText(newName);
                                    } else {
                                        Toast.makeText(activity, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }

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

                                }
                            });
                        }
                    }
                });

            }
        }, (dialog, which) -> {
            // Do nothing
        });
        // InputNameBottomSheetDialogFragment bottomSheetDialogFragment = new InputNameBottomSheetDialogFragment();
        // bottomSheetDialogFragment.setOnNameUpdatedListener(this);
        // bottomSheetDialogFragment.show(getParentFragmentManager(), "InputNameBottomSheetDialogFragment");
    }

    private void openInputPasswordBottomSheet() {
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_input_password_bottom_sheet_dialog, null);
        TextView textViewOld = view.findViewById(R.id.edit_text_old_password);
        TextView textViewNew = view.findViewById(R.id.edit_text_new_password);
        showInputDialog(view, "Please enter your old password and new password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPwd = textViewOld.getText().toString();
                String newPwd = textViewNew.getText().toString();
                passwordButton.setText(oldPwd);
                String userId = LoginManager.getInstance(requireContext().getApplicationContext()).getUserId();
                ChangePasswordRequest request = new ChangePasswordRequest();
                request.setUser_id(userId);
                request.setNew_password(newPwd);
                request.setOld_password(oldPwd);
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
                                    } else {
                                        Toast.makeText(activity, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }

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

                                }
                            });
                        }
                    }
                });
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding when the view is destroyed

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

    @Override
    public void onNameUpdated(String newName) {
        if (nameButton != null) {
            nameButton.setText(newName);
        }
    }

    @Override
    public void onPwdUpdated(String pwd) {
        passwordButton.setText(pwd);

    }
}