package com.bob.vendorapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bob.vendorapp.databinding.ActivityMyLoginBinding;

public class MyLoginActivity extends AppCompatActivity {

    ActivityMyLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}