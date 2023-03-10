package com.example.t_r_ip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.t_r_ip.databinding.FragmentAddPostBinding;
import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.UserModel;
import com.example.t_r_ip.model.api.Location;
import com.example.t_r_ip.model.api.LocationModel;
import com.example.t_r_ip.model.entities.Post;
import com.example.t_r_ip.model.utils.AlertDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragmentInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddPostFragment extends Fragment implements OptionsDialogFragmentInterface {

    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    Boolean isAvatarSelected = false;
    String currentPostId = "";
    Boolean isLocationSelected = false;
    private FragmentAddPostBinding binding;
    private ArrayAdapter<String> adapter;
    private List<String> locations = new ArrayList<>();
    private PostsListFragmentViewModel viewModel;

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
        if (getArguments().getString("postId") != null) {
            this.currentPostId = getArguments().getString("postId");
            this.isLocationSelected = true;
        }

        if (!currentPostId.isEmpty()) {
            viewModel.getPostById(currentPostId).observe(getViewLifecycleOwner(), post -> {
                binding.postInfo.setText(post.getPostText());
                if (!post.getPostPictureUrl().isEmpty()) {
                    isAvatarSelected = true;
                    Picasso.get().load(post.getPostPictureUrl()).into(binding.postImage);
                }
                if (!post.getLocation().isEmpty()) {
                    binding.locationSearch.setText(post.getLocation());
                    binding.lvLocations.setVisibility(View.GONE);
                }
            });
        }


        UserModel.instance().getUserDataById(UserModel.instance().getCurrentUserId(), (user) -> {
            if (user != null) {
                binding.displayName.setText(user.getDisplayName());
                if (!user.getProfilePictureUrl().isEmpty()) {
                    Picasso.get().load(user.getProfilePictureUrl()).into(binding.profileImage);
                } else {
                    binding.profileImage.setImageResource(R.drawable.avatar);
                }
            }
        });
        binding.attachPostData.setOnClickListener(view -> {
            String title = "What would you like to do?";
            String[] options = {"Take picture from gallery", "Upload picture"};
            DialogFragment dialogFragment = OptionsDialogFragment.newInstance(title, options);
            dialogFragment.show(getChildFragmentManager(), "ATTACH_TO_POST_DIALOG");
        });

        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePost(currentPostId);
            }
        });

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, locations);
        binding.lvLocations.setAdapter(adapter);

        binding.locationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String locationForSearch = binding.locationSearch.getText().toString();
                if (!locationForSearch.isEmpty() && !isLocationSelected) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                }
                LiveData<List<Location>> data = LocationModel.instance.searchLocationByName(locationForSearch);
                data.observe(getViewLifecycleOwner(), list -> {
                    locations.clear();
                    if (list != null && !locationForSearch.isEmpty() && !isLocationSelected) {
                        list.forEach(location -> {
                            locations.add(location.getFormatted());
                        });
                        adapter.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                        binding.lvLocations.setVisibility(View.VISIBLE);
                    } else {
                        locations.clear();
                        adapter.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                        binding.lvLocations.setVisibility(View.GONE);
                        isLocationSelected = false;
                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = locations.get(position);
                binding.locationSearch.setText(selectedLocation);
                isLocationSelected = true;
                binding.lvLocations.setVisibility(View.GONE);
                }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsListFragmentViewModel.class);
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

    private void sharePost(String postId) {
        if (binding.postInfo.getText().toString().isEmpty()) {
            Toast.makeText(getContext(),
                            "Please add description to your post",
                            Toast.LENGTH_LONG)
                    .show();
        } else {
            Post post = new Post();
            if (!postId.isEmpty()) {
                post.setId(postId);
            }
            post.setPostText(binding.postInfo.getText().toString());
            post.setPostPictureUrl("");
            post.setAuthorId(UserModel.instance().getCurrentUserId());
            post.setLocation(binding.locationSearch.getText().toString());
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
            dialogFragment.show(getChildFragmentManager(), "ADD_POST_SUCCESSFULLY");
        }
    }
}