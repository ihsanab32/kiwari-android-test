package com.ihsan.chataja.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ihsan.chataja.LoginActivity;
import com.ihsan.chataja.MainActivity;

/**
 * Created by
 * Name     : Ihsan Abdurahman
 * Email    : ihsanab31@gmail.com
 * WA       : 085749729115
 * Date     : 04/06/2020.
 * ------------------------------
 * This class for
 */
public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constants.PREF_NAME, Constants.PRIVATE_MODE);
        editor = pref.edit();
    }


    public void saveLoginMethod(String method) {
        editor.putBoolean(Constants.IS_LOGIN, true);
        editor.putString(Constants.KEY_METHOD, method);
        editor.commit();
    }

    public void checkLogin() {
        if (this.isLoggedIn()) {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        } else {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constants.IS_LOGIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }


}
