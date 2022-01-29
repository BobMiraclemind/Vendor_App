package com.bob.vendorapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bob.vendorapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Astrologers");

        binding.gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(uid).child("name").setValue(binding.name.getText().toString());
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.name.getText().toString().isEmpty() && binding.email.getText().toString().isEmpty() && binding.password.getText().toString().isEmpty()){
                    return;
                }
                binding.progress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            binding.progress.setVisibility(View.GONE);
                            mUser = mAuth.getCurrentUser();
                            if (mUser != null){
                                uid = mUser.getUid();
                            }
                            myRef.child(uid).child("name").setValue(binding.name.getText().toString());
                            myRef.child(uid).child("email").setValue(binding.email.getText().toString());
                            myRef.child(uid).child("password").setValue(binding.password.getText().toString());
                            myRef.child(uid).child("uid").setValue(uid);
                            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}