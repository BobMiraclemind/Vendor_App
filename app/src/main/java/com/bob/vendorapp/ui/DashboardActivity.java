package com.bob.vendorapp.ui;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bob.vendorapp.R;
import com.bob.vendorapp.databinding.ActivityDashboardBinding;
import com.bob.vendorapp.service.RequestNotifyService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;

    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String uid;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            uid = mUser.getUid();
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Astrologers").child(uid);

        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CallUsersListActivity.class));
            }
        });

        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChatUsersListActivity.class));
            }
        });

        binding.notify.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Wait For Notification",Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String name = getString(R.string.channel_name);
                    String description = getString(R.string.channel_description);
                    int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
                    NotificationChannel channel = new NotificationChannel("1", name, importance);
                    channel.setDescription(description);
                    channel.setShowBadge(true);
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("textTitle")
                        .setContentText("textContent")
                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                        .setPriority(Notification.PRIORITY_MAX); //Important for heads-up notification

                Notification buildNotification = mBuilder.build();
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(001, buildNotification);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;
        updateStatus(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status = false;
        updateStatus(status);
    }

    private void updateStatus(boolean status) {
        if (status == true){
            databaseReference.child("status").setValue("online");
        }else {
            databaseReference.child("status").setValue("offline");
        }
    }
}