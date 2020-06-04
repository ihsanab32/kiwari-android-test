package com.ihsan.chataja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ihsan.chataja.utils.Constants;
import com.ihsan.chataja.utils.User;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    LinearLayout layout;
    FloatingActionButton sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        preferences = getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://self-237610.firebaseio.com/messages/" + preferences.getString(Constants.KEY_METHOD, "") + "_" + User.chatWith);
        reference2 = new Firebase("https://self-237610.firebaseio.com/messages/" + User.chatWith + "_" + preferences.getString(Constants.KEY_METHOD, ""));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<>();
                    map.put("message", messageText);
                    map.put("user", preferences.getString(Constants.KEY_METHOD, ""));
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }

            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(preferences.getString(Constants.KEY_METHOD, ""))){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(User.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
