package com.example.t_r_ip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.t_r_ip.model.Model;


public class ProfilePictureDialogFragment extends DialogFragment {
    public static String TAG = "Test";
    private String[] options = {"Take picture from gallery", "Upload picture"};
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What would you like to do?")
                .setItems(options, (dialogInterface, i) -> {
                    if (getParentFragment() instanceof SettingsFragment) {
                        if (i == 0) {
                            ((SettingsFragment) getParentFragment()).setGalleryLauncher();
                        } else {
                            ((SettingsFragment) getParentFragment()).setUploadPictureLauncher();
                        }
                    }
                });
        return builder.create();
    }
}