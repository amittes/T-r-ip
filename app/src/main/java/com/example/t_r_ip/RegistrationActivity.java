package com.example.t_r_ip;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.t_r_ip.databinding.ActivityRegistrationBinding;
import com.example.t_r_ip.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        binding.btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
        setContentView(binding.getRoot());
    }

    private void registerNewUser() {

        binding.progressbar.setVisibility(View.VISIBLE);

        String email, displayName, password;
        email = binding.email.getText().toString();
        displayName = binding.displayName.getText().toString();
        password = binding.password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(displayName)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter display name!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Model.instance().mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && !result.getSignInMethods().isEmpty()) {
                                Toast.makeText(getApplicationContext(),
                                                "Email is already registered",
                                                Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                Model.instance().mAuth
                                        .createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task)
                                            {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(),
                                                                    "Registration successful!",
                                                                    Toast.LENGTH_LONG)
                                                            .show();

                                                    binding.progressbar.setVisibility(View.GONE);
                                                    Model.instance().updateUserDisplayName(displayName);
                                                    Intent intent
                                                            = new Intent(RegistrationActivity.this,
                                                            MainActivity.class);
                                                    startActivity(intent);
                                                }
                                                else {

                                                    Toast.makeText(
                                                                    getApplicationContext(),
                                                                    "Registration failed!!"
                                                                            + " Please try again later",
                                                                    Toast.LENGTH_LONG)
                                                            .show();

                                                    binding.progressbar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w("TAG", "fetchSignInMethodsForEmail:failure", task.getException());
                        }
                    }
                });
    }
}