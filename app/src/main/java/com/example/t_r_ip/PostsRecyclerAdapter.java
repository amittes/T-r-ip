package com.example.t_r_ip;


import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t_r_ip.model.entities.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

class PostViewHolder extends RecyclerView.ViewHolder{
//    TextView nameTv;
//    TextView idTv;
//    CheckBox cb;
    List<Post> data;
//    ImageView avatarImage;
    public PostViewHolder(@NonNull View itemView, PostsRecyclerAdapter.OnItemClickListener listener, List<Post> data) {
        super(itemView);
        this.data = data;
//        nameTv = itemView.findViewById(R.id.studentlistrow_name_tv);
//        idTv = itemView.findViewById(R.id.studentlistrow_id_tv);
//        avatarImage = itemView.findViewById(R.id.studentlistrow_avatar_img);
//        cb = itemView.findViewById(R.id.studentlistrow_cb);
//        cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = (int)cb.getTag();
//                Student st = data.get(pos);
//                st.cb = cb.isChecked();
//            }
//        });
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = getAdapterPosition();
//                listener.onItemClick(pos);
//            }
//        });
    }

    public void bind(Post post, int pos) {
//        nameTv.setText(st.name);
//        idTv.setText(st.id);
//        cb.setChecked(st.cb);
//        cb.setTag(pos);
//        if (st.getAvatarUrl()  != "") {
//            Picasso.get().load(st.getAvatarUrl()).placeholder(R.drawable.avatar).into(avatarImage);
//        }else{
//            avatarImage.setImageResource(R.drawable.avatar);
//        }
    }
}

public class PostsRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    OnItemClickListener listener;

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }
}
