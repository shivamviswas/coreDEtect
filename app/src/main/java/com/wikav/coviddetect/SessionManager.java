package com.wikav.coviddetect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {


        public static SharedPreferences sharedPref;
        SharedPreferences sharedPreferences,login;
        public static SharedPreferences.Editor editorForResponse;
        public SharedPreferences.Editor loginEditor;
        public SharedPreferences.Editor updateEditor;
        public Context context;
        int Private = 0;

        private static final String PRIF = "LOGIN";
        private static final String LOGIN = "IS_LOGIN";
        public static final String NAME = "NAME";
        public static final String UID = "UID";
        public static final String MOBILE = "MOBILE";
        public static final String MAC = "MAC";
        public static final String USERID = "USERID";

        private static final String PREFERENCE_NAME = "APP_PREFERENCE";


        public SessionManager(Context context) {
            this.context = context;
            sharedPreferences = context.getSharedPreferences(PRIF, Private);
            login = context.getSharedPreferences(PRIF, Private);
            loginEditor = login.edit();
            updateEditor = sharedPreferences.edit();
        }

        public static void putString(Context context, String key, String value) {
            sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            editorForResponse = sharedPref.edit();
            editorForResponse.putString(key, value);
            editorForResponse.commit();
        }

        public static String getString(Context context, String key) {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            return sharedPref.getString(key, "");
        }

        public void createSession(String name, String uid, String mobile, String mac,String user_id) {
            loginEditor.putBoolean(LOGIN, true);
            updateEditor.putString(NAME, name);
            updateEditor.putString(UID, uid);
            updateEditor.putString(MAC, mac);
            updateEditor.putString(MOBILE, mobile);
            updateEditor.putString(USERID, user_id);
            loginEditor.apply();
            updateEditor.apply();
        }

        public void updateSession(String name, String email,  String mobile) {
            updateEditor.putString(NAME, name);
            updateEditor.putString(UID, email);
            updateEditor.putString(MOBILE, mobile);
            updateEditor.apply();
        }
        public void clerlast()
        {
            updateEditor.clear();
            updateEditor.commit();
        }


        public boolean isLoging() {

            return login.getBoolean(LOGIN, false);

        }

        public void checkLogin() {
            if (!isLoging()) {

                Intent intent = new Intent(context, RegisterActivity.class);
                context.startActivity(intent);
                ((MainActivity) context).finish();
            }

        }


        public HashMap<String, String> getUserDetail() {
            HashMap<String, String> user = new HashMap<>();
            user.put(NAME, sharedPreferences.getString(NAME, null));
            user.put(UID, sharedPreferences.getString(UID, null));
            user.put(MOBILE, sharedPreferences.getString(MOBILE, null));
               user.put(MAC, sharedPreferences.getString(MAC, null));
            return user;
        }

        public void logOut() {
            loginEditor.clear();
            updateEditor.clear();
            loginEditor.commit();
            updateEditor.commit();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }



    }



