package com.bob.vendorapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.vendorapp.R;
import com.bob.vendorapp.databinding.SingleUserItemBinding;
import com.bob.vendorapp.datamodel.UsersModel;
import com.bob.vendorapp.ui.ChatActivity;

import java.util.ArrayList;

public class UsersChatListAdapter extends RecyclerView.Adapter<UsersChatListAdapter.MyViewHolder> {

    private ArrayList<UsersModel> users;
    private Context context;

    public UsersChatListAdapter(ArrayList<UsersModel> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsersModel usersModel = users.get(position);

        holder.binding.name.setText(usersModel.getName());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, ChatActivity.class);
                intent.putExtra("name",usersModel.getName());
                intent.putExtra("uid",usersModel.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SingleUserItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleUserItemBinding.bind(itemView);
        }
    }
}
