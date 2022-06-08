package com.example.firebase_stickers_notifications;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    //Key removed
    public static String SERVER_KEY = "";

    public static String fcmHttpConnection(JSONObject jsonObject) {
        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();

            InputStream inputStream = conn.getInputStream();
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            return "NULL";
        }
    }

    public static String convertStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String len;
            while ((len = bufferedReader.readLine()) != null) {
                stringBuilder.append(len);
            }
            bufferedReader.close();
            return stringBuilder.toString().replace(",", ",\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap getEmoji(Resources r, String sticker){
        switch (sticker){
            case "smile":
                return BitmapFactory.decodeResource(r, R.drawable.grinning);
            case "hearts":
                return BitmapFactory.decodeResource(r, R.drawable.hearts);
            case "laugh":
                return BitmapFactory.decodeResource(r, R.drawable.laugh);
            case "pout":
                return BitmapFactory.decodeResource(r, R.drawable.pouting);
        }
        return BitmapFactory.decodeResource(r, R.drawable.unknown);
    }
}
