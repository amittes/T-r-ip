package com.example.t_r_ip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.t_r_ip.databinding.FragmentAddPostBinding;
import com.example.t_r_ip.model.Model;
import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.entities.Post;

public class AddPostFragment extends Fragment {

    private FragmentAddPostBinding binding;
    private PostModel postModel = new PostModel();

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
        },this, Lifecycle.State.RESUMED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        binding.username.setText(Model.instance().getCurrentUser().getDisplayName());
        binding.profileImage.setImageURI(Model.instance().getCurrentUser().getPhotoUrl());

        binding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePost();
            }
        });

        return binding.getRoot();
    }

    private void sharePost() {
        Model.Listener<Void> listener = new Model.Listener<Void>() {
            @Override
            public void onComplete(Void aVoid) {

            }
        };
        Post post = new Post();
        post.setPostText("hello");
        post.setPostPictureUrl("bla");
        post.setAuthorEmail(Model.instance().getCurrentUser().getEmail());
        post.setDisplayName(Model.instance().getCurrentUser().getDisplayName());
        post.setAuthorPictureUrl("");
        postModel.addPost(post, listener);

    }
}