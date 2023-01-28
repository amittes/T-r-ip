package com.example.t_r_ip;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.t_r_ip.databinding.FragmentPostsListBinding;
import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.entities.Post;

public class PostsListFragment extends Fragment {
    PostsRecyclerAdapter adapter;
    PostsListFragmentViewModel viewModel;
    private FragmentPostsListBinding binding;

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
        binding = FragmentPostsListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostsRecyclerAdapter(getLayoutInflater(), viewModel.getData().getValue());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("TAG", "Row was clicked " + pos);
                Post post = viewModel.getData().getValue().get(pos);
                PostsListFragmentDirections.ActionPostsListFragmentToPostFragment action = PostsListFragmentDirections.actionPostsListFragmentToPostFragment(post.getId());
                Navigation.findNavController(view).navigate(action);
            }
        });

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getData().observe(getViewLifecycleOwner(), list -> {
            adapter.setData(list);
        });

        PostModel.instance().EventPostsListLoadingState.observe(getViewLifecycleOwner(), status -> {
            binding.swipeRefresh.setRefreshing(status == PostModel.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            reloadData();
        });

//        NavDirections action = PostsListFragmentDirections.actionPostsListFragmentToPostFragment();
//        binding.button.setOnClickListener(Navigation.createNavigateOnClickListener(action));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsListFragmentViewModel.class);
    }

    void reloadData() {
//        binding.progressBar.setVisibility(View.VISIBLE);
        PostModel.instance().refreshAllPosts();
    }
}