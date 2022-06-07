package com.example.firebase_stickers_notifications;

public class MessageRow {
        private String sender;
        private String sticker;
        private String sent;

        public MessageRow(String sender, String sticker, String sent) {
            this.sender = sender;
            this.sticker = sticker;
            this.sent = sent;
        }

        public String getSender() {
            return sender;
        }

        public String getSticker() {
            return sticker;
        }

        public String getSent() {
            return sent;
        }

        public static MessageRow ConvertMessage(Message m){
            return new MessageRow(m.getSender(),m.getSticker(), m.getSent());
        }
    }

