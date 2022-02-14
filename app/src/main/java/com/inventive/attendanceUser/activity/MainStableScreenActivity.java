package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Response;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.inventive.attendanceUser.CameraActivity;
import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.NetworkStuff.APIClient;
import com.inventive.attendanceUser.NetworkStuff.APIInterface;
import com.inventive.attendanceUser.NetworkStuff.RegiatrationNetworkCaller;
import com.inventive.attendanceUser.NetworkStuff.ResponseCarrier;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.Utils.ConstantVariables;
import com.inventive.attendanceUser.Utils.SessionManager;
import com.inventive.attendanceUser.database.DatabaseHelper;
import com.inventive.attendanceUser.service.MyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainStableScreenActivity extends AppCompatActivity implements ResponseCarrier {
    private DatabaseHelper db;
    Handler h = new Handler();
    int delay =1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;
    Button bt_attendance;
    TextView txt_company,txt_memory;
    SessionManager sessionManager;
    String companypass="";
    ImageView img_logo;
    LinearLayout lay_attendance;

    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    int uploadcount=0;
    RegiatrationNetworkCaller networkCaller;
    APIInterface RetroFetcher;

    private final int GetUpdateAttLogConstant=1009;
    private final int GetUpdateAttTempLogConstant=1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stable_screen);

        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(MainStableScreenActivity.this);
        initVariable();

        bt_attendance = findViewById(R.id.bt_attendance);
        txt_company = findViewById(R.id.txt_company);
        img_logo = findViewById(R.id.img_logo);
        lay_attendance = findViewById(R.id.lay_attendance);
        txt_memory = findViewById(R.id.txt_memory);

        HashMap<String, String> user = sessionManager.getUserDetails();
        txt_company.setText(user.get(SessionManager.COMPANY_NAME));
        companypass=user.get(SessionManager.COMPANYPASS);
        Glide.with(this).load(user.get(SessionManager.IMAGE)).into(img_logo);

        //Declare the timer
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (isMyServiceRunning(MyService.class)) {
                        stopService(new Intent(getApplicationContext(),MyService.class));
                    } else {
                        startService(new Intent(getApplicationContext(),MyService.class));
                    }

                } else {*/
                                      if (isMyServiceRunning(MyService.class)) {
                                          stopService(new Intent(getApplicationContext(),MyService.class));
                                      } else {
                                          startService(new Intent(getApplicationContext(),MyService.class));
                                      }

                                      //}

                                  }
                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                2000,
//Set the amount of time between each execution (in milliseconds)
                2000);

        lay_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallMain();

//                Bundle pBundle = new Bundle();
//                pBundle.putString("empid", "0");
//                pBundle.putString("single", "1");
//                pBundle.putString("time", "12:00 PM");
//                pBundle.putString("date", "2021-26-08");
//                Intent i=new Intent(getApplicationContext(), MessageActivity.class);
//                i.putExtras(pBundle);
//
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep06);
//                mediaPlayer.start();
//
//                startActivity(i);
            }
        });

        bt_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallMain();
//                Bundle pBundle = new Bundle();
//                pBundle.putString("empid", "87");
//                pBundle.putString("single", "1");
////                pBundle.putString("time", "12:00 PM");
////                pBundle.putString("date", "2021-08-27");
//                Intent i=new Intent(getApplicationContext(), MessageActivity.class);
//                i.putExtras(pBundle);
//
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep06);
//                mediaPlayer.start();
//
//                startActivity(i);
            }

        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initVariable() {
      //  sessionManager = new SessionManager(MainStableScreenActivity.this);
        networkCaller = new RegiatrationNetworkCaller(MainStableScreenActivity.this, this);
        RetroFetcher = APIClient.StringClient().create(APIInterface.class);
      //  sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    private void CallMain() {
        String delegate = "hh:mm:ss aaa";
        String time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        String timeplus= ConstantVariables.calculateSingleTime(time,30);

        ConstantValues.putSingleVisit("1",timeplus,MainStableScreenActivity.this);

        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        h.postDelayed(runnable = new Runnable() {
            public void run() {
                h.postDelayed(runnable, delay);

                String memory=ConstantVariables.doSomethingMemoryIntensive(getApplicationContext());
                txt_memory.setText(memory);

                getShifttypeId();
               new Upload().execute("sqlite");
            }
        }, delay);
        super.onResume();
    }

    private void getShifttypeId() {
        Cursor cursor = db.getShifttype();
        if (cursor.moveToFirst()) {
            do {

                String indisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.INDISPLAYBEFORE));
                String indisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.INDISPLAYAFTER));
                String outdisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTDISPLAYBEFORE));
                String outdisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTDISPLAYAFTER));
                String lindisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LINDISPLAYBEFORE));
                String lindisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LINDISPLAYAFTER));
                String loutdisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOUTDISPLAYBEFORE));
                String loutdisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOUTDISPLAYAFTER));

                String delegate = "hh:mm aaa";
                String time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
//                String time="5:10 PM";

//                Toast.makeText(this, time+outdisplaybefore+outdisplayafter, Toast.LENGTH_SHORT).show();

                String isWindowOpen = ConstantValues.getisWindowOpen(MainStableScreenActivity.this);

                if (isWindowOpen.equals("0")) {
                    boolean isexist = ConstantVariables.istimeexist(MainStableScreenActivity.this, time, indisplaybefore, indisplayafter, outdisplaybefore, outdisplayafter, lindisplaybefore, lindisplayafter, loutdisplaybefore, loutdisplayafter);

                    if (isexist) {
                        ConstantValues.putSingleVisit("0","0", MainStableScreenActivity.this);

                        Intent i = new Intent(MainStableScreenActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else {
                    ConstantValues.putSingleVisit("0", "0",MainStableScreenActivity.this);

                    Intent i = new Intent(MainStableScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void Success(Response<ResponseBody> response, int Identifier) {
        switch (Identifier)
        {
            case GetUpdateAttLogConstant:
                ExtractUpdateAttLogData(response);
                break;
            case GetUpdateAttTempLogConstant:
                ExtractUpdateAttTempLogData(response);
                break;
        }
    }

    public void ExtractUpdateAttLogData(Response<ResponseBody> response){
        try{
            JSONObject obj=new JSONObject(response.body().string());
            if(obj.optString("status").equalsIgnoreCase("success")){
                JSONObject data=new JSONObject(obj.optString("data"));

                String empid = data.getString("empid");
                String date = data.getString("date");

                db.updateAttLog(empid,date);

            }else{
                Toast.makeText(MainStableScreenActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException | IOException e){
            e.printStackTrace();
        }
    }

    public void ExtractUpdateAttTempLogData(Response<ResponseBody> response){
        try{
            JSONObject obj=new JSONObject(response.body().string());
            if(obj.optString("status").equalsIgnoreCase("success")){
                JSONObject data=new JSONObject(obj.optString("data"));

                String empid = data.getString("empid");
                String date = data.getString("date");

                db.updateAttTempLog(empid,date);

            }else{
                Toast.makeText(MainStableScreenActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void Error(Throwable Response, int Identifier) {

    }

    public class Upload extends AsyncTask<String, String ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DatabaseHelper(MainStableScreenActivity.this);

        }

        @Override
        protected String doInBackground(String... strings) {

            getAttendanceLog();
            getAttendanceTempLog();
//            getAllLocation();

            return null;
        }

        private void getAttendanceLog() {
            JsonArray resultSet  = new JsonArray();
            Cursor cursor = db.getAttendanceLog();


            if (!(cursor.moveToFirst()) || cursor.getCount() ==0){

            }else {

                uploadcount=uploadcount+cursor.getCount();
                if (cursor.moveToFirst()) {
                    do {

                        JsonObject rowObject = new JsonObject();
                        try {

                            rowObject.addProperty("empid", cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
                            rowObject.addProperty("entrydate", cursor.getString(cursor.getColumnIndex(DatabaseHelper.ENTRYDATE)));
                            rowObject.addProperty("exitdate", cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXITDATE)));
                            rowObject.addProperty("in_time", cursor.getString(cursor.getColumnIndex(DatabaseHelper.INTIME)));
                            rowObject.addProperty("in_grace", cursor.getString(cursor.getColumnIndex(DatabaseHelper.INGRACE)));
                            rowObject.addProperty("lunchout_time", cursor.getString(cursor.getColumnIndex(DatabaseHelper.LUNCHOUT)));
                            rowObject.addProperty("lunchout_grace", cursor.getString(cursor.getColumnIndex(DatabaseHelper.LUNCHOUTGRACE)));
                            rowObject.addProperty("lunchin_time", cursor.getString(cursor.getColumnIndex(DatabaseHelper.LUNCHIN)));
                            rowObject.addProperty("lunchin_grace", cursor.getString(cursor.getColumnIndex(DatabaseHelper.LUNCHINGRACE)));
                            rowObject.addProperty("out_time", cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTTIME)));
                            rowObject.addProperty("out_grace", cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTGRACE)));
                            rowObject.addProperty("shiftno", cursor.getString(cursor.getColumnIndex(DatabaseHelper.TYPENO)));
                            rowObject.addProperty("shiftid", cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTSHIFTID)));
                            rowObject.addProperty("newshiftid", cursor.getString(cursor.getColumnIndex(DatabaseHelper.NEWATTSHIFTID)));
                            rowObject.addProperty("total_hours", cursor.getString(cursor.getColumnIndex(DatabaseHelper.TOTALHRS)));

                            resultSet.add(rowObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } while (cursor.moveToNext());
                }

                JsonObject request = new JsonObject();
                request.add("data", resultSet);
                networkCaller.UpdateAttendanceLogs(RetroFetcher, GetUpdateAttLogConstant, request);
            }
        }

        private void getAttendanceTempLog() {
            JsonArray resultSet  = new JsonArray();
            Cursor cursor = db.getAttendanceTempLog();

            if (!(cursor.moveToFirst()) || cursor.getCount() ==0){

            }else {
                uploadcount=uploadcount+cursor.getCount();
                if (cursor.moveToFirst()) {
                    do {

                        JsonObject rowObject = new JsonObject();
                        try {

                            rowObject.addProperty("empid", cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
                            rowObject.addProperty("date", cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
                            rowObject.addProperty("time", cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME)));
                            rowObject.addProperty("type", cursor.getString(cursor.getColumnIndex(DatabaseHelper.TYPE)));

                            resultSet.add(rowObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } while (cursor.moveToNext());
                }

                JsonObject request = new JsonObject();
                request.add("data", resultSet);
                networkCaller.UpdateAttendanceTempLogs(RetroFetcher, GetUpdateAttTempLogConstant, request);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        h.removeCallbacks(runnable);

    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(MainStableScreenActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = 800;
//        lp.height = 400;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btn_yes = dialog.findViewById(R.id.btn_yes);
        final EditText edt_pass = dialog.findViewById(R.id.edt_pass);
        final ImageView img_close = dialog.findViewById(R.id.img_close);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edt_pass.getText().toString())){
                    edt_pass.setFocusable(true);
                    edt_pass.setError("Enter Password");
                }else if (!companypass.equals(edt_pass.getText().toString())){
                    edt_pass.setFocusable(true);
                    edt_pass.setError("Incorrect Password");
                }else {
                    dialog.dismiss();
                    MainStableScreenActivity.super.onBackPressed();
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }
}