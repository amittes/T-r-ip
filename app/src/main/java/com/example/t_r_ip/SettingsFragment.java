package com.example.t_r_ip;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.t_r_ip.databinding.FragmentSettingsBinding;
import com.example.t_r_ip.model.Model;
import com.example.t_r_ip.model.User;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

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
        },this, Lifecycle.State.RESUMED);

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
                if (result != null){
                    binding.profileImage.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    public void setUploadPictureLauncher () {
        cameraLauncher.launch(null);
    }

    public void setGalleryLauncher () {
        galleryLauncher.launch("image/*");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        Model.instance().getUserDataById(Model.instance().getCurrentUserId(), (user)-> {
            if (user != null) {
                binding.email.setText(user.getEmail());
                binding.displayName.setHint(user.getDisplayName());
                if (user.getProfilePictureUrl() != "") {
                    Picasso.get().load(user.getProfilePictureUrl()).into(binding.profileImage);
                }
            }
        });

        binding.profileImage.setOnClickListener(view -> {
            new ProfilePictureDialogFragment().show(
                    getChildFragmentManager(), ProfilePictureDialogFragment.TAG);
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model currentUser = Model.instance();
                String email = currentUser.getCurrentUser().getEmail();
                String displayName = binding.displayName.getText().toString();

                User user = new User(currentUser.getCurrentUserId(), email, displayName, "");

                String password = binding.password.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    currentUser.updateUserPassword(password);
                }

                if (isAvatarSelected) {
                    binding.profileImage.setDrawingCacheEnabled(true);
                    binding.profileImage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.profileImage.getDrawable()).getBitmap();
                    Model.instance().uploadImage(email, bitmap, url -> {
                        if (url != null) {
                            user.setProfilePictureUrl(url);
                        }

                        currentUser.saveUser(user, (unused) -> {});
                    });
                }
                new AlertDialogFragment().show(
                        getChildFragmentManager(), AlertDialogFragment.TAG);
            }
        });

        return binding.getRoot();
    }


}