package com.example.firebase_stickers_notifications;

public class Count {
    private int smileCount;
    private int heartCount;
    private int laughCount;
    private int poutCount;

    public Count(){

    }

    public Count(int smileCount, int heartCount, int laughCount, int poutCount) {
        this.smileCount = smileCount;
        this.heartCount = heartCount;
        this.laughCount = laughCount;
        this.poutCount = poutCount;
    }

    public int getSmileCount() {
        return smileCount;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public int getLaughCount() {
        return laughCount;
    }

    public int getPoutCount() {
        return poutCount;
    }

    public void incrementCount(String sticker){
        switch (sticker){
            case "smile":
                smileCount++;
                break;
            case "hearts":
                heartCount++;
                break;
            case "laugh":
                laughCount++;
                break;
            case "pout":
                poutCount++;
                break;
        }
    }
}
