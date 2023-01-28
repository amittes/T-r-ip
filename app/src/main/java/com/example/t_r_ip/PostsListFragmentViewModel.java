package com.example.t_r_ip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.t_r_ip.model.PostModel;
import com.example.t_r_ip.model.entities.Post;

import java.util.List;

public class PostsListFragmentViewModel extends ViewModel {
    private final LiveData<List<Post>> data = PostModel.instance().getAllPosts();

    LiveData<List<Post>> getData() {
        return data;
    }
}
