package com.bob.vendorapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bob.vendorapp.adapter.ChatAdapter;
import com.bob.vendorapp.databinding.ActivityChatBinding;
import com.bob.vendorapp.datamodel.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    ActivityChatBinding binding;
    String name,receiverUid,senderUid,receiverRoom,senderRoom;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<ChatModel> chatModels;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            senderUid = mUser.getUid();
        }

        receiverRoom = receiverUid + senderUid;
        senderRoom = senderUid + receiverUid;
        chatModels = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Messages");

        getMessages();
        chatAdapter = new ChatAdapter(chatModels,getApplicationContext());
        binding.messageView.setLayoutManager(new LinearLayoutManager(this));
        binding.messageView.setAdapter(chatAdapter);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                if (!binding.message.getText().toString().isEmpty()){
                    ChatModel chat = new ChatModel(binding.message.getText().toString(),senderUid,String.valueOf(date.getTime()));
                    myRef.child(receiverRoom).push().setValue(chat);
                    binding.message.setText("");
                }
            }
        });

    }

    private void getMessages() {
        myRef.child(receiverRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModels.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    chatModels.add(chatModel);
                }
                chatAdapter.notifyDataSetChanged();
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