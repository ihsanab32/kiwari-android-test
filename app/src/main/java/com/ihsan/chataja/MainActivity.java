package com.ihsan.chataja;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ihsan.chataja.utils.Constants;
import com.ihsan.chataja.utils.SessionManager;
import com.ihsan.chataja.utils.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    SessionManager sessionManager;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        usersList = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://self-237610.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User.chatWith = al.get(position);
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key;

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(preferences.getString(Constants.KEY_METHOD,""))) {
                    al.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
           sessionManager.logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
