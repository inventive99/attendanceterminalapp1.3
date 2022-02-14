package com.inventive.attendanceUser.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager<T> {
    public static final String FCM_TOKEN = "fcm_token";
    public static final String KEY_ID = "user_id";
    public static final String USER_DATA = "user_data";
    public static final String USER_DOCUMENT = "user_doc";
    public static final String USER_Profile_pic = "user_profile_pic";
    public static final String KEY_NAME = "name";
    public static final String USER_NAME = "user_name";
    public static final String LOGIN_TYPE = "user_type";
    public static final String PASS_CODE = "pass_code";
    public static final String USER_ID = "user_id";
    public static final String GID = "gid";
    public static final String USER_TYPE = "user_type";
    public static final String STATUS = "status";
    public static final String GROUP_ID = "group_id";
    public static final String COMPANY_NAME = "company_name";
    public static final String IMAGE = "image";
    public static final String COMPANYPASS = "company_pass";
    public static final String LATEMARKCOUNT = "latemarkcount";


    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn ";

    private static String TAG = com.inventive.attendanceUser.Utils.SessionManager.class.getSimpleName();
    private static com.inventive.attendanceUser.Utils.SessionManager instance;
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    //constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(context.getPackageName(), PRIVATE_MODE);
        editor = pref.edit();
    }

    private SessionManager() {
    }

    public static synchronized com.inventive.attendanceUser.Utils.SessionManager getInstance() {
        if (instance == null) {
            instance = new com.inventive.attendanceUser.Utils.SessionManager();
        }
        return instance;
    }

    public static String getData(Context ctx, String key) {

        SharedPreferences preferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public boolean putData(Context ctx, String key, T Value) {

        try {
            SharedPreferences preferences = ctx.getSharedPreferences(ctx.getPackageName(), MODE_PRIVATE);

            Editor editor = preferences.edit();

            if (Value.getClass().equals(String.class)) {
                editor.putString(key, (String) Value);
            } else if (Value.getClass().equals(Boolean.class)) {
                editor.putBoolean(key, (Boolean) Value);
            } else if (Value.getClass().equals(Integer.class)) {
                editor.putInt(key, (Integer) Value);
            }
            editor.apply();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void setFcmToken(String FcmToken) {
        // Storing name in pref
        editor.putString(FCM_TOKEN, FcmToken);
        // commit changes
        editor.commit();
    }

    public String getFcmToken() {
        String FcmToken = "";
        // user name
        FcmToken = pref.getString(FCM_TOKEN, "");
        // return user
        return FcmToken;
    }

    public void setProfilePic(String Url) {
        // Storing name in pref
        editor.putString(USER_Profile_pic, Url);
        // commit changes
        editor.commit();
    }

    public String getProfilePic() {
        String Url = "";
        // user name
        Url = pref.getString(USER_Profile_pic, "");
        // return user
        return Url;
    }
/*
    public void setUserData(UserDataModel userData) {
        // Storing name in pref
        editor.putString(USER_DATA, new Gson().toJson(userData));

        // commit changes
        editor.commit();
    }

    public UserDataModel getUserData() {
        UserDataModel userData;

        String detailsjson = pref.getString(USER_DATA, "");
        userData = new Gson().fromJson(detailsjson, UserDataModel.class);
        // return user
        return userData;
    }*/


    // Create login session

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
//    public void checkLogin(){
//        // Check login status
//        if(!this.isLoggedIn()){
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(_context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            _context.startActivity(i);
//        }
//
//    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String image, String pass, String no_late_markcount){

        // Storing name in pref
        editor.putString(COMPANY_NAME, name);

        editor.putString(IMAGE, image);
        editor.putString(COMPANYPASS, pass);
        editor.putString(LATEMARKCOUNT, no_late_markcount);
        // commit changes
        editor.commit();
    }

    public void createLoginSession(String toString, String toString1, String userid, String gid, String utype, String name, String email, String status, String group_id) {

        editor.putString(KEY_NAME, name);
        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        editor.putString(USER_NAME, toString);
        editor.putString(PASS_CODE, toString1);
        editor.putString(USER_ID, userid);
        editor.putString(GID, gid);
        editor.putString(USER_TYPE, utype);
        editor.putString(KEY_EMAIL, email);
        editor.putString(STATUS, status);
        editor.putString(GROUP_ID, group_id);

        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(COMPANY_NAME, pref.getString(COMPANY_NAME, null));

        user.put(IMAGE, pref.getString(IMAGE, null));
        user.put(COMPANYPASS, pref.getString(COMPANYPASS, null));
        user.put(LATEMARKCOUNT, pref.getString(LATEMARKCOUNT, null));


        // return user
        return user;
    }


    /**
     * Clear session details
     */

//    public void logoutUser() {
//        // Clearing all data from Shared Preferences
//        editor.clear();
//        editor.commit();
//        // After logout redirect user to Loing Activity
//        Intent i = new Intent(_context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        // Staring Login Activity
//        _context.startActivity(i);
//        ((Activity) _context).finish();
//    }

    /**
     * Clear session details
     * */
//    public void logoutUser(){
//        // Clearing all data from Shared Preferences
//        editor.clear();
//        editor.commit();
//
//        // After logout redirect user to Loing Activity
//        Intent i = new Intent(_context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Staring Login Activity
//        _context.startActivity(i);
//    }



}
