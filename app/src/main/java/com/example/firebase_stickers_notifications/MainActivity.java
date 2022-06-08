package com.example.firebase_stickers_notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences p;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        p = PreferenceManager.getDefaultSharedPreferences(this);
        String user = p.getString("logged_in_user", "");
        if (!user.equals("")) {
            Intent intent = new Intent(this, SendStickers.class);
            intent.putExtra("usernameSelected", user);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.usernameEdit);
    }


    public void sendSticker(View view) {
        String userNameEntered = username.getText().toString();
        Log.i("username", userNameEntered);
        if (!TextUtils.isEmpty(userNameEntered)) {
            SharedPreferences.Editor user = p.edit();
            user.putString("logged_in_user", userNameEntered);
            user.apply();
            Intent intent = new Intent(this, SendStickers.class);
            intent.putExtra("usernameSelected", userNameEntered);
            startActivity(intent);
            Log.i("username not empty", userNameEntered);
        } else {
            Log.i("username empty", userNameEntered);
            Toast.makeText(this, "No username selected!", Toast.LENGTH_SHORT).show();
        }
    }
}
