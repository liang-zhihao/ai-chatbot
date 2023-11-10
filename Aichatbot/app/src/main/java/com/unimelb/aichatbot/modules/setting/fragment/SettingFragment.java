package com.unimelb.aichatbot.modules.setting.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.modules.profile.activity.Fragment.InputNameBottomSheetDialogFragment;
import com.unimelb.aichatbot.modules.profile.activity.Fragment.InputPasswordBottomSheetDialogFragment;
import com.unimelb.aichatbot.modules.profile.activity.request.ChangePasswordRequest;
import com.unimelb.aichatbot.modules.profile.activity.request.ChangeUsernameRequest;
import com.unimelb.aichatbot.modules.profile.activity.server.ProfileService;
import com.unimelb.aichatbot.network.RetrofitFactory;
import com.unimelb.aichatbot.util.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements CustomViewController, InputNameBottomSheetDialogFragment.OnNameUpdatedListener, InputPasswordBottomSheetDialogFragment.OnPasswordUpdatedListener {


    private TextView nameButton;
    private TextView passwordButton;
    private TextView emailButton;

    private static final String TAG = "SettingFragment";

    CircularProgressButton logoutBtn;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Using ViewBinding for inflated layout
        View root = inflater.inflate(R.layout.profile_layout, container, false);
        emailButton = root.findViewById(R.id.buttonMail);
        nameButton = root.findViewById(R.id.buttonName);
        passwordButton = root.findViewById(R.id.buttonPassword);
        logoutBtn = root.findViewById(R.id.logoutBtn);
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
        logoutBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            // Set the message show for the Alert time
            builder.setMessage("Do you want to logout?");
            builder.setTitle("Logout");
            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                LoginManager.getInstance(requireContext().getApplicationContext()).logout();
                // to landing page
                Intent intent = new Intent(requireActivity(), com.unimelb.aichatbot.modules.account.activity.LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });

            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();

        });
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

        showInputDialog(view, "Change username", new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(activity, "Your name has been updated successfully.\n", Toast.LENGTH_SHORT).show();
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
        showInputDialog(view, "Change password", new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(activity, "Password changed successfully! Keep your new password secure.", Toast.LENGTH_SHORT).show();
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