package com.inventive.attendanceUser.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ConstantValues {


//    public static final String BASEURL = "http://iattendance.inv91.in/webservicesbg/";
   //public static final String BASEURL = "http://iattendance.inv91.in/webservices/";
//    public static final String BASEURL = "http://iattendance.inv91.in/webservicebgcopy/";
//    public static final String BASEURL = "http://deltaerp.inv91.in/webservices/";
//    public static final String BASEURL = "http://payroll.inv91.in/webservices/";
//    public static final String BASEURL = "http://deltaerp.inv91.in/webservicescopy/";

//public static final String BASEURL = "http://demomangalmurticlinic.inv97.in/webservices/";

//public static final String BASEURL = "http://mangalmurticlinic.inv97.in/webservices/";
//public static final String BASEURL = "http://shivramindustries.inv97.in/webservices/";
//public static final String BASEURL = "http://samarthhospital.inv97.in/webservices/";
//    public static final String BASEURL = "http://saiamruthospital.inv98.in/webservices/";
//    public static final String BASEURL = "http://nimishamotors.inv97.in/webservices/";
//public static final String BASEURL = "http://poojapetroleum.inv97.in/webservices/";

//    public static final String BASEURL = "http://payrolldemo.inv97.in/webservices/";

//public static final String BASEURL = "http://ajinkyatarahospital.inv97.in/webservices/";
//    public static final String BASEURL = "http://asianerp.inv98.in/webservices/";
//public static final String BASEURL = "http://abdemo1.1.inv98.in/webservices/";
  public static final String BASEURL = "http://aruncollection.inv98.in/webservices/";
   //public static final String BASEURL = "http://payrolldemo.inv94.in/webservices/";
     //public static final String BASEURL = "http://pacificconsultancy.inv94.in/webservices/";
// public static final String BASEURL = "http://hoteljsab.inv98.in/webservices/";

//    public static final String BASEURL = "http://samarthhospital.inv98.in/webservicescopy/";
//    public static final String BASEURL = "http://samarthhospital.inv98.in/webservices/";
//public static final String BASEURL = "http://samarthhospital.inv98.in/webservicescopynew/";




    private static SharedPreferences mSharedPreferences = null;
    private final static String ALLOW_UNINSTALL = "Allow_UnInstall";
    private final static String SINGLE_VISIT = "single_visit";
    private final static String SINGLE_VISIT_TIME = "single_visit_time";
    private final static String ISWINDOWOPEN = "iswindowopen";
    private final static String WINDOW_CLOSE_TIME = "window_close_time";
    private final static String LICENSEKEY = "license_key";
    private final static String ORGLOCATION = "orglocation";


    public static void putUnInstallStatus(String value, Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        mSharedPreferences.edit().putString(ALLOW_UNINSTALL, value).apply();
    }

    public static String getUninstallStatus() {
        return mSharedPreferences.getString(ALLOW_UNINSTALL, null);
    }

    public static void putlicense_keyStatus(String value,String orglocation, Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        mSharedPreferences.edit().putString(LICENSEKEY, value).apply();
        mSharedPreferences.edit().putString(ORGLOCATION, orglocation).apply();

    }

    public static String getlicense_keyStatus(Context context) {
         mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(LICENSEKEY, "0");
    }

    public static String getorglocation(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(ORGLOCATION, "0");
    }

    public static void putSingleVisit(String value,String single_visit_time, Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        mSharedPreferences.edit().putString(SINGLE_VISIT, value).apply();
        mSharedPreferences.edit().putString(SINGLE_VISIT_TIME, single_visit_time).apply();
    }

    public static String getSingleVisit(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(SINGLE_VISIT, null);
    }

    public static String getSingleVisitTime(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(SINGLE_VISIT_TIME, null);
    }

    public static void putWindowOpen(String value,String closetime, Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        mSharedPreferences.edit().putString(ISWINDOWOPEN, value).apply();
        mSharedPreferences.edit().putString(WINDOW_CLOSE_TIME, closetime).apply();
    }

    public static String getisWindowOpen(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(ISWINDOWOPEN, null);
    }

    public static String getWindowTime(Context context) {
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        return mSharedPreferences.getString(WINDOW_CLOSE_TIME, null);
    }
}
