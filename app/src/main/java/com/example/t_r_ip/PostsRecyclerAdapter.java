package com.example.t_r_ip;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t_r_ip.model.entities.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

class PostViewHolder extends RecyclerView.ViewHolder{
    TextView displayName;
    TextView postInfo;
    ImageView profilePic;

    List<Post> data;

    public PostViewHolder(@NonNull View itemView, PostsRecyclerAdapter.OnItemClickListener listener, List<Post> data) {
        super(itemView);
        this.data = data;

        displayName = itemView.findViewById(R.id.postlistrow_displayName);
        postInfo = itemView.findViewById(R.id.postlistrow_post_info);
        profilePic = itemView.findViewById(R.id.postlistrow_profile_pic);
//        cb = itemView.findViewById(R.id.studentlistrow_cb);
//        cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = (int)cb.getTag();
//                Student st = data.get(pos);
//                st.cb = cb.isChecked();
//            }
//        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });
    }

    public void bind(Post post, int pos) {
        displayName.setText(post.getDisplayName());
        postInfo.setText(post.getPostText());
//        cb.setChecked(st.cb);
//        cb.setTag(pos);
        if (post.getAuthorPictureUrl() != "") {
            Picasso.get().load(post.getAuthorPictureUrl()).placeholder(R.drawable.avatar).into(profilePic);
        }else{
            profilePic.setImageResource(R.drawable.avatar);
        }
    }
}

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }
    LayoutInflater inflater;
    List<Post> data;

    public void setData(List<Post> data){
        this.data = data;
        notifyDataSetChanged();
    }
    public PostsRecyclerAdapter(LayoutInflater inflater, List<Post> data){
        this.inflater = inflater;
        this.data = data;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_list_row,parent,false);
        return new PostViewHolder(view,listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = data.get(position);
        holder.bind(post,position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }
}
