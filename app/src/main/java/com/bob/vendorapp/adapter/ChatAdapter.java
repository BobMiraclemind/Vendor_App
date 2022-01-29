package com.bob.vendorapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.vendorapp.R;
import com.bob.vendorapp.databinding.SingleIncomingItemBinding;
import com.bob.vendorapp.databinding.SingleOutgoingItemBinding;
import com.bob.vendorapp.datamodel.ChatModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<ChatModel> chatModels;
    Context context;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public ChatAdapter(ArrayList<ChatModel> chatModels, Context context) {
        this.chatModels = chatModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.single_outgoing_item,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.single_incoming_item,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = chatModels.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(chatModel.getUid())){
            return ITEM_SENT;
        }else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel chatModel = chatModels.get(position);
        if (holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.outgoingItemBinding.message.setText(chatModel.getMessage());
        }else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.incomingItemBinding.message.setText(chatModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        SingleOutgoingItemBinding outgoingItemBinding;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            outgoingItemBinding = SingleOutgoingItemBinding.bind(itemView);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        SingleIncomingItemBinding incomingItemBinding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            incomingItemBinding = SingleIncomingItemBinding.bind(itemView);
        }
    }
}
