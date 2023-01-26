package com.example.t_r_ip.model.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class OptionsDialogFragment extends DialogFragment {

    public static OptionsDialogFragment newInstance(String title, String[] options) {
        OptionsDialogFragment frag = new OptionsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("options", options);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String[] options = getArguments().getStringArray("options");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(options, (dialogInterface, i) -> {
                    if (getParentFragment() instanceof OptionsDialogFragmentInterface) {
                        ((OptionsDialogFragmentInterface) getParentFragment()).doOptionSelected(i);
                    }
                });
        return builder.create();
    }
}