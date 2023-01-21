package com.example.t_r_ip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.t_r_ip.databinding.FragmentPostsListBinding;

public class PostsListFragment extends Fragment {
    private FragmentPostsListBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPostsListBinding.inflate(inflater, container, false);

        NavDirections action = PostsListFragmentDirections.actionPostsListFragmentToPostFragment();
        binding.button.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        return binding.getRoot();
    }
}