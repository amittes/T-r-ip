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
    final public MutableLiveData<LoadingState> EventPostsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);
    private Executor executor;
    private PostFirebaseModel postFirebaseModel;
    private AppLocalDbRepository localDb;
    private LiveData<List<Post>> postsList;
    private LiveData<List<Post>> userPostsList;

    private PostModel() {
        this.executor = Executors.newSingleThreadExecutor();
        this.postFirebaseModel = PostFirebaseModel.instance();
        this.localDb = AppLocalDb.getAppDb();
    }

    public static PostModel instance() {
        return _instance;
    }

    public LiveData<List<Post>> getAllPosts() {
        if (postsList == null) {
            postsList = localDb.postDao().getAll(false);
            refreshAllPosts();
        }
        return postsList;
    }

    public LiveData<List<Post>> getUserPosts(String id) {
        if (userPostsList == null) {
            Log.d("TAL", "getUserPosts " + id);
            userPostsList = localDb.postDao().getPostsByAuthorId(id);
            refreshAllUserPosts(id);
            Log.d("TAL", "#### from local db " + userPostsList.getValue());

        }
        return postsList;
    }

    public LiveData<Post> getPostById(String id) {
        LiveData<Post> post = localDb.postDao().getPostById(id);
        refreshPostById(id);
        return post;
    }

    public void refreshAllPosts() {
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        Long localLastUpdate = Post.getLocalLastUpdate();
        Log.d("TAL", "localLastUpdate " + localLastUpdate);
        postFirebaseModel.getAllPostsSince(localLastUpdate, list -> {
            executor.execute(() -> {
                Long time = localLastUpdate;
                for (Post post : list) {
                    if (time < post.getLastUpdated()) {
                        time = post.getLastUpdated();
                    }
                    if (!post.isDeleted()) {
                        localDb.postDao().insertAll(post);
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

    public void refreshAllUserPosts(String id) {
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        Long localLastUpdate = Post.getLocalLastUpdate();
        postFirebaseModel.getAllUserPostsSince(id, localLastUpdate, list -> {
            executor.execute(() -> {
                Long time = localLastUpdate;
                for (Post post : list) {
                    localDb.postDao().insertAll(post);
                    if (time < post.getLastUpdated()) {
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

   public void refreshPostById(String id) {
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        Long localLastUpdate = Post.getLocalLastUpdate();
        Log.d("TAL", "localLastUpdate " + localLastUpdate );
        postFirebaseModel.getPostByIdSince(id, localLastUpdate, post -> {
            if (post != null) {
                executor.execute(() -> {
                    Long time = localLastUpdate;
                    localDb.postDao().insertAll(post);
                    if (time < post.getLastUpdated()) {
                        time = post.getLastUpdated();
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Post.setLocalLastUpdate(time);
                    EventPostsListLoadingState.postValue(LoadingState.NOT_LOADING);
                });
            }

        });
    }

    public void addPost(Post post, Listener<Void> listener) {
        postFirebaseModel.addPost(post, (Void) -> {
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public void removePost(Post post, Listener<Void> listener) {
        post.setDeleted(true);
        postFirebaseModel.addPost(post, (Void) -> {
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener) {
        postFirebaseModel.uploadImage(name, bitmap, listener);
    }

    public enum LoadingState {
        LOADING,
        NOT_LOADING
    }

    public interface Listener<T> {
        void onComplete(T data);
    }

}
