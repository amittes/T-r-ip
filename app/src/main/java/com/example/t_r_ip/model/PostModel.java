package com.example.t_r_ip.model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.t_r_ip.model.entities.Post;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostModel {
    private static final PostModel _instance = new PostModel();

    private Executor executor;
    private PostFirebaseModel postFirebaseModel;
    private AppLocalDbRepository localDb;
    private LiveData<List<Post>> postsList;

    private PostModel() {
        this.executor = Executors.newSingleThreadExecutor();
        this.postFirebaseModel = PostFirebaseModel.instance();
        this.localDb = AppLocalDb.getAppDb();
    }

    public static PostModel instance(){
        return _instance;
    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventPostsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    public interface Listener<T>{
        void onComplete(T data);
    }

    public LiveData<List<Post>> getAllPosts() {
        if(postsList == null){
            postsList = localDb.postDao().getAll();
            refreshAllPosts();
        }
        return postsList;
    }

    public void refreshAllPosts() {
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        Long localLastUpdate = Post.getLocalLastUpdate();
        postFirebaseModel.getAllPostsSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Post post:list){
                    localDb.postDao().insertAll(post);
                    if (time < post.getLastUpdated()){
                        time = post.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Post.setLocalLastUpdate(time);
                EventPostsListLoadingState.postValue(LoadingState.NOT_LOADING);
            });
        });
    }

    public void addPost(Post st, Listener<Void> listener){
        postFirebaseModel.addPost(st,(Void)-> {
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        postFirebaseModel.uploadImage(name, bitmap, listener);
    }

}
