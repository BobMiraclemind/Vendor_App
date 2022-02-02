package com.bob.vendorapp.service;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bob.vendorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RequestNotifyService extends Service {

    private final static String TAG = "RequestNotifyService";

    DatabaseReference databaseReference;
    String senderUid,receiverUid;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.i(TAG, "onStartCommand: ");
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null){
            senderUid = mUser.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Astrologers").child(senderUid).child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String receiverName;
                    if (dataSnapshot.child("chat").getValue().equals("requesting")){
                        receiverUid = dataSnapshot.getKey();

//                        Log.i(TAG, "onDataChange: "+receiverName);

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
                                .setContentTitle("New Chat Request")
                                .setContentText(" wants to chat with you")
                                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                                .setPriority(Notification.PRIORITY_MAX); //Important for heads-up notification

                        Notification buildNotification = mBuilder.build();
                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(001, buildNotification);
                        startForeground(001,buildNotification);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return START_STICKY;
    }
}
