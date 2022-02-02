package com.bob.vendorapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bob.vendorapp.R;
import com.bob.vendorapp.adapter.UsersCallListAdapter;
import com.bob.vendorapp.adapter.UsersChatListAdapter;
import com.bob.vendorapp.databinding.ActivityCallUsersListBinding;
import com.bob.vendorapp.datamodel.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CallUsersListActivity extends AppCompatActivity {

    private static final String TAG = "CallUsersListActivity";

    ActivityCallUsersListBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String uid;
    ArrayList<UsersModel> usersList;
    UsersCallListAdapter usersCallListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Users for Call");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.progress.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            uid = mUser.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        usersList = new ArrayList<>();

        getUsersData();

        usersCallListAdapter = new UsersCallListAdapter(usersList,getApplicationContext());
        binding.userListView.setLayoutManager(new LinearLayoutManager(this));
        binding.userListView.setAdapter(usersCallListAdapter);
    }

    private void getUsersData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (!uid.matches(dataSnapshot.getKey())){
                        UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                        System.out.println(usersModel.getUid());
                        System.out.println(usersModel.getfirstname());
                        Log.i(TAG, "onDataChange: "+usersModel.getfirstname()+"\n "+usersModel.getUid());
                        usersList.add(usersModel);
                    }
                }
                usersCallListAdapter.notifyDataSetChanged();
                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}