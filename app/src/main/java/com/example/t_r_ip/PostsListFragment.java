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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.t_r_ip.databinding.FragmentPostsListBinding;
import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.entities.Post;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PostsListFragment extends Fragment {
    PostsRecyclerAdapter adapter;
    PostsListFragmentViewModel viewModel;
    private FragmentPostsListBinding binding;
    private String userId;

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

    public LiveData<List<Post>> getPostsData(String id) {
        if (id == null) {
            return viewModel.getData();
        } else {
            return viewModel.getUserPosts(id);
        }
    }

    public List<Post> showData(List<Post> data) {
        List<Post> displayData = data.stream().filter(post -> !post.isDeleted()).collect(Collectors.toList());
        Collections.sort(displayData, (pre, curr) -> (int) (curr.getLastUpdated() - pre.getLastUpdated()));
        return displayData;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPostsListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getArguments() != null) {
            this.userId = getArguments().getString("userId");
        }

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostsRecyclerAdapter(getLayoutInflater(), getPostsData(userId).getValue());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PostsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Post post = showData(getPostsData(userId).getValue()).get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("postId", post.getId());
                Navigation.findNavController(view).navigate(R.id.action_global_postFragment, bundle);
            }
        });
        binding.progressBar.setVisibility(View.GONE);


        getPostsData(userId).observe(getViewLifecycleOwner(), list -> {
            List<Post> showDataList = showData(list);
            Log.d("tag", "posts list size " + showDataList.size());
            if (showDataList.size() == 0) {
                String output;
                if (userId == null) {
                    output = "There is no posts yet";
                } else {
                    output = "This user has no posts yet";
                }
                binding.textView.setVisibility(View.VISIBLE);
                binding.textView.setText(output);
            } else if (binding.textView.getVisibility() == View.VISIBLE) {
                binding.textView.setVisibility(View.GONE);
            }
            adapter.setData(showDataList);

        });

        PostModel.instance().EventPostsListLoadingState.observe(getViewLifecycleOwner(), status -> {
            binding.swipeRefresh.setRefreshing(status == PostModel.LoadingState.LOADING);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            reloadData();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(PostsListFragmentViewModel.class);
    }

    void reloadData() {
        PostModel.instance().refreshAllPosts();
    }
}