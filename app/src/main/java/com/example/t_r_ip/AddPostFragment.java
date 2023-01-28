package com.example.t_r_ip;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.t_r_ip.databinding.FragmentAddPostBinding;
import com.example.t_r_ip.model.Model;
import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.UserModel;
import com.example.t_r_ip.model.entities.Post;
import com.example.t_r_ip.model.utils.AlertDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragmentInterface;
import com.squareup.picasso.Picasso;

public class AddPostFragment extends Fragment implements OptionsDialogFragmentInterface {

    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;
    private FragmentAddPostBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.main_menu_addPost);
                menu.removeItem(R.id.main_menu_logout);
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
                    binding.postImage.setImageBitmap(result);
                    isAvatarSelected = true;
                }
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    binding.postImage.setImageURI(result);
                    isAvatarSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        binding.displayName.setText(UserModel.instance().getCurrentUser().getDisplayName());
        UserModel.instance().getUserDataById(UserModel.instance().getCurrentUserId(), (user) -> {
            if (user != null) {
                binding.displayName.setText(user.getDisplayName());
                if (user.getProfilePictureUrl() != "") {
                    Picasso.get().load(user.getProfilePictureUrl()).into(binding.profileImage);
                }
            }
        });
        binding.attachPostData.setOnClickListener(view -> {
            String title = "What would you like to do?";
            String[] options = {"Take picture from gallery", "Upload picture", "Add location"};
            DialogFragment dialogFragment = OptionsDialogFragment.newInstance(title, options);
            dialogFragment.show(getChildFragmentManager(), "ATTACH_TO_POST_DIALOG");
        });

        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePost(binding.postInfo.getText().toString());
            }
        });
        return binding.getRoot();
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
        } else if (index == 1) {
            setUploadPictureLauncher();
        }
    }

    private void sharePost(String postInfo) {
        Model.Listener<Void> listener = new Model.Listener<Void>() {
            @Override
            public void onComplete(Void aVoid) {

            }
        };
        Post post = new Post();
        post.setPostText(postInfo);
        post.setPostPictureUrl("");
        post.setAuthorId(UserModel.instance().getCurrentUserId());
        if (isAvatarSelected) {
            binding.postImage.setDrawingCacheEnabled(true);
            binding.postImage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) binding.postImage.getDrawable()).getBitmap();
            PostModel.instance().uploadImage(post.getId(), bitmap, url -> {
                if (url != null) {
                    post.setPostPictureUrl(url);
                }
                PostModel.instance().addPost(post, (unused) -> {
                });
            });
        }
        PostModel.instance().addPost(post, (unused) -> {
        });

        String message = "Your post has been uploaded successfully";
        DialogFragment dialogFragment = AlertDialogFragment.newInstance(message);
        dialogFragment.show(getChildFragmentManager(), "UPDATE_PROFILE_DETAILS");
    }
}