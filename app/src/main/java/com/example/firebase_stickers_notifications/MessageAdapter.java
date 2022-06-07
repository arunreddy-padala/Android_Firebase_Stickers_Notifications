package com.example.firebase_stickers_notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
    private final ArrayList<MessageRow> messages;

    public MessageAdapter(ArrayList<MessageRow> messages){
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        MessageRow message = messages.get(position);
        holder.sent.setText(message.getSent());
        holder.sender.setText(message.getSender());
        switch (message.getSticker()){
            case "smile":
                holder.sticker.setImageResource(R.drawable.grinning);
                break;
            case "hearts":
                holder.sticker.setImageResource(R.drawable.hearts);
                break;
            case "laugh":
                holder.sticker.setImageResource(R.drawable.laugh);
                break;
            case "pout":
                holder.sticker.setImageResource(R.drawable.pouting);
                break;
            default:
                holder.sticker.setImageResource(R.drawable.unknown);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
