package com.bob.vendorapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bob.vendorapp.adapter.UsersChatListAdapter;
import com.bob.vendorapp.databinding.ActivityUserListBinding;
import com.bob.vendorapp.datamodel.UsersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatUsersListActivity extends AppCompatActivity {

    private static final String TAG = "ChatUsersListActivity";

    ActivityUserListBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    String uid;

    ArrayList<UsersModel> usersList;
    UsersChatListAdapter usersChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Users List");
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

        usersChatListAdapter = new UsersChatListAdapter(usersList,getApplicationContext());
        binding.userListView.setLayoutManager(new LinearLayoutManager(this));
        binding.userListView.setAdapter(usersChatListAdapter);
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
                usersChatListAdapter.notifyDataSetChanged();
                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}