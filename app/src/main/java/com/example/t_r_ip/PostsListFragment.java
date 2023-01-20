package com.example.t_r_ip;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PostsListFragment extends Fragment {

    private PostsListViewModel mViewModel;

    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);

        View button = view.findViewById(R.id.postslist_btn);
        NavDirections action = PostsListFragmentDirections.actionPostsListFragmentToPostFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        return view;
    }
}