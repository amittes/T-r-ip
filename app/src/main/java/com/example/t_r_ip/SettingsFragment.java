package com.example.t_r_ip;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.example.t_r_ip.databinding.FragmentSettingsBinding;
import com.example.t_r_ip.model.UserModel;
import com.example.t_r_ip.model.entities.User;
import com.example.t_r_ip.model.utils.AlertDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragmentInterface;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment implements OptionsDialogFragmentInterface {

    FragmentSettingsBinding binding;
    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;

    Boolean isAvatarSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.main_menu_addPost);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                if (result != null) {
                    binding.profileImage.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    binding.profileImage.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    public void setUploadPictureLauncher() {
        cameraLauncher.launch(null);
    }

    public void setGalleryLauncher() {
        galleryLauncher.launch("image/*");
    }

    public void doOptionSelected(int index) {
        if (index == 0) {
            setGalleryLauncher();
        } else {
            setUploadPictureLauncher();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        UserModel.instance().getUserDataById(UserModel.instance().getCurrentUserId(), (user) -> {
            if (user != null) {
                binding.email.setText(user.getEmail());
                binding.displayName.setHint(user.getDisplayName());
                if (!user.getProfilePictureUrl().isEmpty()) {
                    Picasso.get().load(user.getProfilePictureUrl()).into(binding.profileImage);
                } else {
                    binding.profileImage.setImageResource(R.drawable.avatar);
                }
            }
        });

        binding.profileImage.setOnClickListener(view -> {
            String title = "What would you like to do?";
            String[] options = {"Take picture from gallery", "Upload picture"};
            DialogFragment dialogFragment = OptionsDialogFragment.newInstance(title, options);
            dialogFragment.show(getChildFragmentManager(), "SET_PROFILE_IMAGE_DIALOG");
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = UserModel.instance();
                String email = userModel.getCurrentUserEmail();
                String displayName = binding.displayName.getText().toString();

                User user = new User(userModel.getCurrentUserId(), email, displayName, "");

                String password = binding.password.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    userModel.updateUserPassword(password);
                }
                if (isAvatarSelected) {
                    binding.profileImage.setDrawingCacheEnabled(true);
                    binding.profileImage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.profileImage.getDrawable()).getBitmap();
                    UserModel.instance().uploadImage(email, bitmap, url -> {
                        if (url != null) {
                            user.setProfilePictureUrl(url);
                        }
                        if (user.getDisplayName().isEmpty()) {
                            userModel.getUserDataById(userModel.getCurrentUserId(), (userCurrentData) -> {
                                user.setDisplayName(userCurrentData.getDisplayName());
                                userModel.saveUser(user, (unused) -> {
                                });
                            });
                        } else {
                            userModel.saveUser(user, (unused) -> {
                            });
                        }
                    });
                } else {
                    userModel.getUserDataById(userModel.getCurrentUserId(), (userCurrentData) -> {
                        user.setProfilePictureUrl(userCurrentData.getProfilePictureUrl());
                        if (user.getDisplayName().isEmpty()) {
                            user.setDisplayName(userCurrentData.getDisplayName());
                        }
                        userModel.saveUser(user, (unused) -> {
                        });
                    });
                }

                String message = "Your profile has been updated successfully";
                DialogFragment dialogFragment = AlertDialogFragment.newInstance(message);
                dialogFragment.show(getChildFragmentManager(), "UPDATE_PROFILE_DETAILS");
            }
        });

        return binding.getRoot();
    }
}