package com.example.t_r_ip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class AlertDialogFragment extends DialogFragment {
    public static String TAG = "Test";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Your details has been updated successfully");
        builder.setMessage("Your details has been updated successfully");
        builder.setPositiveButton("OK", (DialogInterface dialogInterface, int i)->{
//            Toast.makeText(getContext(),"Alert is OK",Toast.LENGTH_LONG).show();
        });
        return builder.create();
    }
}