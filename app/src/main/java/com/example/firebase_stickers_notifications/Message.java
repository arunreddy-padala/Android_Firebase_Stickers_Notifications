package com.example.firebase_stickers_notifications;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String sender;
    private String receiver;
    private String sticker;
    private String sent;

    public Message() {

    }

    public Message(String sender, String receiver, String sticker) {
        this.sender = sender;
        this.receiver = receiver;
        this.sticker = sticker;
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.sent = ft.format(dNow);
    }

    public Message(String sender, String receiver, String sticker, String sent) {
        this.sender = sender;
        this.receiver = receiver;
        this.sticker = sticker;
        this.sent = sent;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSticker() {
        return sticker;
    }

    public String getSent() {
        return sent;
    }
}
