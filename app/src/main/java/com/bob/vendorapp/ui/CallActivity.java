package com.bob.vendorapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bob.vendorapp.databinding.ActivityCallBinding;

public class CallActivity extends AppCompatActivity {

    ActivityCallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}