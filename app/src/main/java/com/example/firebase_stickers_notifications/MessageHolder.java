package com.example.firebase_stickers_notifications;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageHolder extends RecyclerView.ViewHolder {
    public TextView sent;
    public TextView sender;
    public ImageView sticker;

    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        sent = itemView.findViewById(R.id.sent);
        sender = itemView.findViewById(R.id.sender);
        sticker = itemView.findViewById(R.id.sticker);
    }

}