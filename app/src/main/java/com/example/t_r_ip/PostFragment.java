package com.example.t_r_ip;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.t_r_ip.databinding.FragmentPostBinding;
import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.UserModel;
import com.example.t_r_ip.model.entities.Post;
import com.example.t_r_ip.model.utils.OptionsDialogFragment;
import com.example.t_r_ip.model.utils.OptionsDialogFragmentInterface;
import com.squareup.picasso.Picasso;

public class PostFragment extends Fragment implements OptionsDialogFragmentInterface {

    private FragmentPostBinding binding;
    private PostsListFragmentViewModel viewModel;
    private Post currentPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        binding = FragmentPostBinding.inflate(inflater, container, false);
        binding.editPost.setVisibility(View.INVISIBLE);
        String postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        binding.progressBar.setVisibility(View.GONE);

        viewModel.getPostById(postId).observe(getViewLifecycleOwner(), post -> {
            this.currentPost = post;
            binding.postInfo.setText(post.getPostText());

            UserModel.instance().getUserDataById(post.getAuthorId(), user -> {
                binding.displayName.setText(user.getDisplayName());
                if (!user.getProfilePictureUrl().isEmpty()) {
                    Picasso.get().load(user.getProfilePictureUrl()).into(binding.profileImage);
                }
            });

            if (!post.getLocation().isEmpty()) {
                binding.location.setText(post.getLocation());
            } else {
                binding.location.setVisibility(View.INVISIBLE);
            }
            binding.postInfo.setText(post.getPostText());

            if (!post.getPostPictureUrl().isEmpty()) {
                Picasso.get().load(post.getPostPictureUrl()).into(binding.postImage);
            }

            if (UserModel.instance().getCurrentUserId().equals(post.getAuthorId())) {
                binding.editPost.setVisibility(View.VISIBLE);
                binding.editPost.setOnClickListener(view -> {
                    String title = "What would you like to do?";
                    String[] options = {"Edit post", "Delete post"};
                    DialogFragment dialogFragment = OptionsDialogFragment.newInstance(title, options);
                    dialogFragment.show(getChildFragmentManager(), "EDIT_POST_DIALOG");
                });
            }
        });

        PostModel.instance().EventPostsListLoadingState.observe(getViewLifecycleOwner(), status -> {
            binding.swipeRefresh.setRefreshing(status == PostModel.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            reloadData();
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsListFragmentViewModel.class);
    }

    void reloadData() {
        PostModel.instance().refreshAllPosts();
    }

    @Override
    public void doOptionSelected(int index) {
        getActivity().onBackPressed();
        if (index == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("postId", this.currentPost.getId());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_global_addPostFragment, bundle);
        } else if (index == 1) {
            PostModel.instance().removePost(this.currentPost, unused -> {
            });
        }
    }

}