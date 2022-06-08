package com.example.firebase_stickers_notifications;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SendStickers extends AppCompatActivity {
    private String user;
    private static String CLIENT_REGISTRATION_TOKEN;
    boolean flag = false;
    private String receiver;
    MessageAdapter messageAdapter;
    private final ArrayList<MessageRow> messages = new ArrayList<>();
    private DatabaseReference mDatabase;
    private TextView smileCount;
    private TextView heartCount;
    private TextView laughCount;
    private TextView poutCount;
    private ArrayList<String> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_stickers);

        smileCount = findViewById(R.id.countSmile);
        heartCount = findViewById(R.id.countHeart);
        laughCount = findViewById(R.id.countLaugh);
        poutCount = findViewById(R.id.countPout);
        final Spinner spinner = findViewById(R.id.friends);

        user = getIntent().getStringExtra("usernameSelected");

        TextView loggedin = findViewById(R.id.loggedInUser);
        loggedin.setText("Logged in User:\n" + user);

        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.history);
        recyclerView.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(rLayoutManager);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SendStickers.this);
                builder1.setMessage("Are you sure you want to log out?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        (dialog, id) -> logOut());
                builder1.setNegativeButton(
                        "No",
                        (dialog, id) -> dialog.cancel());
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        };
        getOnBackPressedDispatcher().addCallback(backPressedCallback);
        backPressedCallback.setEnabled(true);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(SendStickers.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
            } else {
                if (CLIENT_REGISTRATION_TOKEN == null) {
                    CLIENT_REGISTRATION_TOKEN = task.getResult();
                }
                Log.e("CLIENT_REGISTRATION_TOKEN", CLIENT_REGISTRATION_TOKEN);
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic(user).addOnCompleteListener(task -> Log.v("notifications", "notifications for " + user));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String TAG = "Database";

        users = new ArrayList<>();
        Log.i("users_added", "test");
        users.add("Select a username to send message");

        List<String> username = getUsers();

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,username);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    flag = true;
                    receiver = selectedItemText;

                }
                else if(position == 0){
                    flag = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDatabase.child(user + "_count").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateCounts(snapshot);
                Log.e(TAG, "onChildAdded: count = " + snapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateCounts(snapshot);
                Log.e(TAG, "onChildAdded: count = " + snapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child(user).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message m = dataSnapshot.getValue(Message.class);
                        messages.add(MessageRow.ConvertMessage(m));
                        messageAdapter.notifyItemInserted(messages.size() - 1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled:" + databaseError);
                        Toast.makeText(getApplicationContext()
                                , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void sendSmile(View v){
        sendMessage(v, "smile");
    }

    public void sendHearts(View v){
        sendMessage(v, "hearts");
    }

    public void sendLaugh(View v){
        sendMessage(v, "laugh");
    }

    public void sendPout(View v){
        sendMessage(v, "pout");
    }

    private void sendMessage(View v, String sticker) {
        if(flag) {
            Toast.makeText(this, sticker + " Sent " + receiver, Toast.LENGTH_SHORT).show();
            doAddDataToDb(sticker);
        }
        else {
            Toast.makeText(this, "Message not sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUser(){
        mDatabase.child("users").orderByChild("username").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null && snapshot.getChildren()!=null &&
                        snapshot.getChildren().iterator().hasNext()){
                    Log.i("users_added", "exists");
                }else {
                    Log.i("users_added", user);
                    DatabaseReference push = mDatabase.child("users").push();
                    push.setValue(new User(user));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<String> getUsers(){
        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("users_added", snapshot.toString());
                users.add(snapshot.getValue(User.class).getUsername());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("users_changed", snapshot.toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("users_list1", users.toString());
        addUser();
        Log.i("users_list2", users.toString());
        return users;
    }

    private void doAddDataToDb(String sticker) {
        DatabaseReference push = mDatabase.child(receiver).push();
        push.setValue(new Message(user, receiver, sticker));
        incrementCount(sticker);
        sendMessageToTopic(sticker);
    }

    private void incrementCount(String sticker){
        DatabaseReference countDb = mDatabase.child(user + "_count").child("count");
        countDb.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Count count = currentData.getValue(Count.class);
                if(count == null){
                    count = new Count(0,0,0,0);
                }
                count.incrementCount(sticker);
                currentData.setValue(count);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                // Transaction completed
                String TAG = "Count";
                Log.d(TAG, "postTransaction:onComplete:" + error);
            }
        });

    }

    public void logOutClick(View v) {
        logOut();
    }

    private void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to log out?");
        builder.setPositiveButton("Log Out", (DialogInterface dialog, int id) -> {
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor userEdit = p.edit();
            userEdit.putString("logged_in_user", "");
            userEdit.apply();
            FirebaseMessaging.getInstance().unsubscribeFromTopic(user).addOnCompleteListener(task -> Log.v("notifications", "unsubscribed from" + user));
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (DialogInterface dialog, int id) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateCounts(DataSnapshot dataSnapshot){
        Log.e("count", dataSnapshot.getValue().toString());
        Count counts = dataSnapshot.getValue(Count.class);
        updateCount(smileCount, counts.getSmileCount());
        updateCount(heartCount, counts.getHeartCount());
        updateCount(laughCount, counts.getLaughCount());
        updateCount(poutCount, counts.getPoutCount());
    }

    private void updateCount(TextView countView, int count){
        countView.setText("Sent: " + count);
    }

    public void sendMessageToTopic(String sticker) {
        new Thread(() -> {
            JSONObject jPayload = new JSONObject();
            JSONObject jNotification = new JSONObject();
            try {
                jNotification.put("title", "Message Received from " + user);
                jNotification.put("body", sticker);
                jNotification.put("sound", "default");
                jNotification.put("badge", "1");
                jNotification.put("click_action", "OPEN_ACTIVITY_1");

                jPayload.put("to", "/topics/" + receiver);
                jPayload.put("priority", "high");
                jPayload.put("notification", jNotification);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            final String resp = Utils.fcmHttpConnection(jPayload);
            Log.v("notification response", resp);
        }).start();
    }
}
