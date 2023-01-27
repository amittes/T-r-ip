package com.example.t_r_ip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.example.t_r_ip.databinding.FragmentPostBinding;
import com.example.t_r_ip.model.UserModel;
import com.example.t_r_ip.model.entities.Post;
import com.squareup.picasso.Picasso;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private Post post;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = new Post();
        FragmentActivity parentActivity = getActivity();
        parentActivity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.removeItem(R.id.main_menu_logout);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding =  FragmentPostBinding.inflate(inflater, container, false);
        binding.deletePost.setVisibility(View.INVISIBLE);
        binding.displayName.setText(post.getDisplayName());
        binding.location.setText(post.getLocation());
        binding.postInfo.setText(post.getPostText());
        if (post.getAuthorPictureUrl() != "") {
            Picasso.get().load(post.getAuthorPictureUrl()).into(binding.profileImage);
        }
        if (post.getPostPictureUrl() != "") {
            Picasso.get().load(post.getPostPictureUrl()).into(binding.postImage);
        }

        if (UserModel.instance().getCurrentUser().getDisplayName().equals(post.getDisplayName())) {
            binding.deletePost.setVisibility(View.VISIBLE);
            binding.deletePost.setOnClickListener(view -> { post.setDeleted(true);});
        }
        return binding.getRoot();
    }
}