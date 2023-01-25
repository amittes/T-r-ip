package com.example.t_r_ip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.t_r_ip.model.entities.Post;

import java.util.List;

public class PostsListFragmentViewModel extends ViewModel {
    private LiveData<List<Post>> data = Model.instance().getAllStudents();

    LiveData<List<Student>> getData(){
        return data;
    }
}
