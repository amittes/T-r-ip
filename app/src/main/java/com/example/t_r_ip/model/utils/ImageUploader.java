package com.example.t_r_ip.model.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.t_r_ip.model.FirebaseModel;
import com.example.t_r_ip.model.Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ImageUploader {

    FirebaseModel firebaseModel;
    String folder;
    String imageName;
    Bitmap bitmap;
    Model.Listener<String> listener;

    public ImageUploader(FirebaseModel firebaseModel, String folder, String imageName, Bitmap bitmap, Model.Listener<String> listener) {
        this.firebaseModel = firebaseModel;
        this.folder = folder;
        this.imageName = imageName;
        this.bitmap = bitmap;
        this.listener = listener;
    }

    public void upload() {
        StorageReference storageRef = this.firebaseModel.getStorage().getReference();
        StorageReference imagesRef = storageRef.child(this.folder + "/" + this.imageName + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }
}
