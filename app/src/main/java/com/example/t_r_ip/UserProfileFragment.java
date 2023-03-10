package com.example.t_r_ip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.example.t_r_ip.databinding.FragmentUserProfileBinding;
import com.example.t_r_ip.model.UserModel;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        Fragment PostsList = new PostsListFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.postlist_fragment_container, PostsList).commit();
        Bundle args = new Bundle();
        args.putString("userId", UserModel.instance().getCurrentUserId());
        PostsList.setArguments(args);

        return binding.getRoot();
    }
}