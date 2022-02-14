package com.inventive.attendanceUser;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.inventive.attendanceUser.NetworkStuff.APIClient;
import com.inventive.attendanceUser.NetworkStuff.APIInterface;
import com.inventive.attendanceUser.NetworkStuff.RegiatrationNetworkCaller;
import com.inventive.attendanceUser.NetworkStuff.ResponseCarrier;
import com.inventive.attendanceUser.Utils.Connectivity;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.Utils.ConstantVariables;
import com.inventive.attendanceUser.Utils.SessionManager;
import com.inventive.attendanceUser.activity.DownloadStatusActivity;
import com.inventive.attendanceUser.activity.MainStableScreenActivity;
import com.inventive.attendanceUser.activity.MessageActivity;
import com.inventive.attendanceUser.activity.MultipleFacesActivity;
import com.inventive.attendanceUser.activity.SetDateTimeActivity;
import com.inventive.attendanceUser.customview.OverlayView;
import com.inventive.attendanceUser.database.DatabaseHelper;
import com.inventive.attendanceUser.env.BorderedText;
import com.inventive.attendanceUser.env.ImageUtils;
import com.inventive.attendanceUser.service.MyService;
import com.inventive.attendanceUser.tflite.SimilarityClassifier;
import com.inventive.attendanceUser.tflite.TFLiteObjectDetectionAPIModel;
import com.inventive.attendanceUser.tracking.MultiBoxTracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import android.media.ImageReader.OnImageAvailableListener;

import androidx.cardview.widget.CardView;

import java.util.Timer;
import java.util.TimerTask;

import com.inventive.attendanceUser.customview.OverlayView.DrawCallback;
import com.inventive.attendanceUser.env.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends CameraActivity implements OnImageAvailableListener, ResponseCarrier {
    private static final Logger LOGGER = new Logger();

    // Progress Dialog
    private ProgressDialog pDialog;

    public static final int progress_bar_type = 0;
    int uploadcount=0;
    boolean isexist=true;
    String companypass="";

    // MobileFaceNet
    private static final int TF_OD_API_INPUT_SIZE = 112;
    private static final boolean TF_OD_API_IS_QUANTIZED = false;
    private static final String TF_OD_API_MODEL_FILE = "mobile_face_net.tflite";

    RegiatrationNetworkCaller networkCaller;
    APIInterface RetroFetcher;
    SessionManager sessionManager;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    Calendar calendar;
    int year,month,day;
    String date,date1,todaydate;
    String single="",abshiftid,abshifttype;

    Handler h = new Handler();
    int delay =2000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;

    Handler h1 = new Handler();
    int delay1 =60000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable1;
    LocationManager locationManager;
    String latitude, longitude;
    private static final int REQUEST_LOCATION = 1;

    private DatabaseHelper db;

    private ArrayList<String> job_type_id=new ArrayList<>();
    private ArrayList<String> shift_type_id=new ArrayList<>();
    private ArrayList<String> designation_id=new ArrayList<>();
    private ArrayList<String> emp_info_id=new ArrayList<>();
    private ArrayList<String> holidays=new ArrayList<>();
    private ArrayList<String> arrfaceid=new ArrayList<>();

    private final int GetEmployeeConstant = 1002;
    private final int GetAddEmpConstant=1003;
    private final int GetCompanyConstant = 1001;
    private final int GetUpdatedEmployeeConstant = 1004;
    private final int UpdatedEmpFaceConstant = 1005;
    private final int GetDesignationConstant = 1006;
    private  final int GetShiftConstant=1007;
    private final int GetJobTypeConstant=1008;
    private final int GetUpdateAttLogConstant=1009;
    private final int GetUpdateAttTempLogConstant=1010;
    private final int GetShifttypeConstant=1011;
    private final int GetHolidaysConstant=1012;
    private final int GetUpdateLocationConstant=1013;
    private final int GetDownloadLogConstant=1014;

    Object dextra = null;
    Bitmap dcrop = null;

    private static final int OUTPUT_SIZE = 192;

    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt";

    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    private static final boolean MAINTAIN_ASPECT = false;

    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);

    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private SimilarityClassifier detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;
    private boolean addPending = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private MultiBoxTracker tracker;

    private BorderedText borderedText;

    // Face detector
    private FaceDetector faceDetector;

    // here the preview image is drawn in portrait way
    private Bitmap portraitBmp = null;
    // here the face is cropped and drawn
    private Bitmap faceBmp = null;

    private FloatingActionButton fabAdd;
    TextView txt_name,txt_memory;
    ImageView img_logo;
    ImageView img_user;
    String getlicense_keyStatus="0";

    CardView card_network;
    ImageView img_network;
    TextView txt_network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        fabAdd = findViewById(R.id.fab_add);
        txt_name = findViewById(R.id.txt_name);
        img_logo = findViewById(R.id.img_logo);
        card_network = findViewById(R.id.card_network);
        img_network = findViewById(R.id.img_network);
        txt_network = findViewById(R.id.txt_network);
        img_user = findViewById(R.id.img_user);

        txt_memory = findViewById(R.id.txt_memory);

        db = new DatabaseHelper(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR); // current year
        month = calendar.get(Calendar.MONTH); // current month
        day = calendar.get(Calendar.DAY_OF_MONTH); // current day

        todaydate=year+"-"+(month+1)+"-"+day;

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick();
            }
        });

        // Real-time contour detection of multiple faces
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();


        FaceDetector detector = FaceDetection.getClient(options);

        faceDetector = detector;

        initVariable();
        getCompanyInfo();

        HashMap<String, String> user = sessionManager.getUserDetails();
        txt_name.setText(user.get(SessionManager.COMPANY_NAME));
        Glide.with(this).load(user.get(SessionManager.IMAGE)).into(img_logo);
        companypass=user.get(SessionManager.COMPANYPASS);

        Cursor cursor = db.getEmployeeInfo();
        int totalemp=cursor.getCount();

        getlicense_keyStatus = ConstantValues.getlicense_keyStatus(getApplicationContext());

         if (totalemp<1 || getlicense_keyStatus.equals("0")){

            DownloadLogs(todaydate);
            Intent i=new Intent(getApplicationContext(), DownloadStatusActivity.class);
            startActivity(i);

        }




        h.postDelayed(new Runnable() {
            public void run() {

                CheckInternate();
                String memory=ConstantVariables.doSomethingMemoryIntensive(getApplicationContext());
                txt_memory.setText(memory);

                getlicense_keyStatus = ConstantValues.getlicense_keyStatus(getApplicationContext());

                if (getlicense_keyStatus.equals("1")){
                    db.deleteData();

                    new BackUp().execute("sqlite");
                    new Upload().execute("sqlite");
                    ConstantVariables.deleteCache(getApplicationContext());

                }

                runnable=this;

                h.postDelayed(runnable, delay);

            }
        }, delay);

    }

    private void DownloadLogs(String todaydate) {
        JsonObject request = new JsonObject();
        request.addProperty("date", todaydate);
        networkCaller.downloadAttendanceLog(RetroFetcher, GetDownloadLogConstant, request);
    }

    private void finishActivityAll() {
        MainActivity.this.finish();
    }

    private static void killProcessesAround(Activity activity) throws PackageManager.NameNotFoundException {
        ActivityManager am = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
        String myProcessPrefix = activity.getApplicationInfo().processName;
        String myProcessName = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0).processName;
        for (ActivityManager.RunningAppProcessInfo proc : am.getRunningAppProcesses()) {
            if (proc.processName.startsWith(myProcessPrefix) && !proc.processName.equals(myProcessName)) {
                android.os.Process.killProcess(proc.pid);
            }
        }
    }

    private void getCompanyInfo() {
        JsonObject request = new JsonObject();
        networkCaller.getStoreInfo(RetroFetcher, GetCompanyConstant, request);
    }



    public class BackUp extends AsyncTask<String, String ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DatabaseHelper(MainActivity.this);

        }

        @Override
        protected String doInBackground(String... strings) {

            getJobtypeId();
            getShifttypeId();
            getdesignationId();
            getemployeeInfoId();
            getemployeeFaceInfoId();


            getUpdatedEmployeeFaces();
            getUpdatedEmployeeInfo();
            getShifttype();
            getShifttypeid();
            getJobtype();
            getDesignation();
            getHolidays();


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public class Upload extends AsyncTask<String, String ,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DatabaseHelper(MainActivity.this);

        }

        @Override
        protected String doInBackground(String... strings) {

            getAttendanceLog();
            getAttendanceTempLog();
//            getAllLocation();

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

        private void CheckInternate() {
        if((ConstantVariables.haveNetworkConnection(getApplicationContext())) == true){
            boolean isFastConnection = Connectivity.isConnectedFast(getApplicationContext());
            if (isFastConnection){
                txt_network.setText("Online");
                img_network.setImageResource(R.drawable.ic_baseline_low_1_24);

            }
        }

        else{
            txt_network.setText("Offline");
            img_network.setImageResource(R.drawable.ic_baseline_high_24);
        }
    }

    private void getHolidays() {

        JsonObject request = new JsonObject();
        request.addProperty("month",month+1);
        request.addProperty("year",year);
        networkCaller.getHolidays(RetroFetcher, GetHolidaysConstant, request);
    }


    @Override
    public synchronized void onResume() {
        super.onResume();
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();


        FaceDetector detector = FaceDetection.getClient(options);

        faceDetector = detector;

        //getemployee();
    }

    private void onAddClick() {

        addPending = true;
        //Toast.makeText(this, "click", Toast.LENGTH_LONG ).show();

    }

    private void initVariable() {
        sessionManager = new SessionManager(MainActivity.this);
        networkCaller = new RegiatrationNetworkCaller(MainActivity.this, this);
        RetroFetcher = APIClient.StringClient().create(APIInterface.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    private void getemployee() {

        Cursor cursor = db.getEmployeefaces();
        if (cursor.moveToFirst()) {
            do {

                SimilarityClassifier.Recognition result = new SimilarityClassifier.Recognition(
                        "0", "", Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EDISTANCE))), new RectF(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFLEFT))),Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFTOP))),Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFRIGHT))),Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFBOTTOM)))));


                result.setEmpId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
                result.setExtra(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXTRAS)));
                result.setLocation(new RectF(Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFLEFT))),Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFTOP))),Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFRIGHT))),Float.parseFloat(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EFBOTTOM)))));

                detector.register(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ENAME)), result);

            } while (cursor.moveToNext());
        }

    }

    private void getJobtypeId() {
        Cursor cursor = db.getJobtype();
        if (cursor.moveToFirst()) {
            do {
                job_type_id.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOBTYPEID)));
            } while (cursor.moveToNext());
        }
    }

    private void getShifttypeId() {
        Cursor cursor = db.getShifttype();
        shift_type_id.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String lastout="12:01 AM";
        String lastoutnight="12:01 AM";

        if (cursor.moveToFirst()) {
            do {
                shift_type_id.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTID)));
                String outtime=cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTOUT));
                String outtimegrace=cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTOUTGRACE));
                String shifttype=cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTTYPEID));

                String indisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.INDISPLAYBEFORE));
                String indisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.INDISPLAYAFTER));
                String outdisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTDISPLAYBEFORE));
                String outdisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.OUTDISPLAYAFTER));
                String lindisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LINDISPLAYBEFORE));
                String lindisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LINDISPLAYAFTER));
                String loutdisplaybefore=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOUTDISPLAYBEFORE));
                String loutdisplayafter=cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOUTDISPLAYAFTER));

                abshiftid=cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTID));
                abshifttype=cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTTYPEID));

                if (shifttype.equals("3") || shifttype.equals("4")) {
                    lastoutnight=ConstantVariables.getlastnighttime(lastoutnight,outtime);

                }else {
                    lastout=ConstantVariables.getlasttime(lastout,outtime);

                }

                String delegate = "hh:mm aaa";
                String time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
//                String time="5:10 PM";

                        String isWindowOpen = ConstantValues.getisWindowOpen(getApplicationContext());

                        if (isWindowOpen.equals("0")) {
                            isexist = ConstantVariables.istimeexist(getApplicationContext(), time, indisplaybefore, indisplayafter, outdisplaybefore, outdisplayafter, lindisplaybefore, lindisplayafter, loutdisplaybefore, loutdisplayafter);

                            if (!isexist) {

                                String visit = ConstantValues.getSingleVisit(getApplicationContext());

                                if (!visit.equals("1")) {

                                    /*Intent i = new Intent(getApplicationContext(), MainStableScreenActivity.class);
                                    startActivity(i);
                                    finishActivityAll();*/

                                    Intent i = new Intent(getApplicationContext(), MainStableScreenActivity.class);
                                    startActivity(i);
                                    String delegate1 = "hh:mm:ss aaa";
                                    String time1= (String) DateFormat.format(delegate1, Calendar.getInstance().getTime());
                                    String timeplus= ConstantVariables.calculateSingleTime(time1,120);

                                    ConstantValues.putSingleVisit("1",timeplus,MainActivity.this);

                                    finishActivityAll();

                                }else {
                                    String SingleVisitTime = ConstantValues.getSingleVisitTime(getApplicationContext());

                                    String delegate1 = "hh:mm:ss aaa";
                                    String time1= (String) DateFormat.format(delegate1, Calendar.getInstance().getTime());
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss aa");

                                    try {
                                        Date timed = sdf1.parse(time1);
                                        Date SingleVisitTimed = sdf1.parse(SingleVisitTime);

                                        if (timed.after(SingleVisitTimed)){
                                            ConstantValues.putSingleVisit("0", "0",getApplicationContext());
                                            Intent i = new Intent(getApplicationContext(), MainStableScreenActivity.class);
                                            startActivity(i);
                                            finishActivityAll();

                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }else {
                            String WindowTime = ConstantValues.getWindowTime(getApplicationContext());

                            try {
                                Date timed = sdf.parse(time);
                                Date WindowTimed = sdf.parse(WindowTime);

                                if (timed.after(WindowTimed)){
                                    ConstantValues.putWindowOpen("0", "0",getApplicationContext());
                                    Intent i = new Intent(getApplicationContext(), MainStableScreenActivity.class);
                                    startActivity(i);
                                    finishActivityAll();

                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }


            } while (cursor.moveToNext());


        }
    }


    private void checkabsetEmployees(String abshiftid, String abshifttype) {

        calendar = Calendar.getInstance();
        int year1 = calendar.get(Calendar.YEAR); // current year
        int month1 = calendar.get(Calendar.MONTH); // current month
        int day1 = calendar.get(Calendar.DAY_OF_MONTH); // current day

        date=year1 + "-"+ (month1+1) + "-" + day1;

        Cursor cursors = db.getShifttypen("2");

        if (cursors.moveToFirst()) {
            do {

                Cursor cursor = db.getAbsentEmployee(date,cursors.getString(cursors.getColumnIndex(DatabaseHelper.SHIFTID)));

                if (cursor.moveToFirst()) {
                    do {

                        db.addLog(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)),date,date,"Absent","","","","","","","","0","0",abshiftid,"0");

                    } while (cursor.moveToNext());
                }

            } while (cursors.moveToNext());
        }
    }

    private void checkNightabsetEmployees(String abshiftid, String abshifttype, String previousDate) {
        Log.d("aaaaaappppppuuuuuuu1",abshifttype);

            try{

                Cursor cursors = db.getShifttypen("4");

                if (cursors.moveToFirst()) {
                    do {
                        Log.d("aaaaaappppppuuuuuuu1222",previousDate);

                        Cursor cursor = db.getAbsentEmployee(previousDate,cursors.getString(cursors.getColumnIndex(DatabaseHelper.SHIFTID)));

                        Log.d("ppppppppoooiii1",String.valueOf(cursor.getCount()));
                        if (cursor.moveToFirst()) {
                            do {

                                db.addLog(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)),previousDate,date,"Absent","","","","","","","","0","0",abshiftid, "0");

                            } while (cursor.moveToNext());
                        }

                    } while (cursors.moveToNext());
                }

            }catch (Exception e){
                e.printStackTrace();
            }

    }

    private void getdesignationId() {
        Cursor cursor = db.getdesignation();
        if (cursor.moveToFirst()) {
            do {
                designation_id.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESIGNATIONID)));
            } while (cursor.moveToNext());
        }
    }

    private void getemployeeInfoId() {
        Cursor cursor = db.getEmployeeInfo();
        if (cursor.moveToFirst()) {
            do {
                emp_info_id.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
            } while (cursor.moveToNext());
        }
    }

    private void getemployeeFaceInfoId() {
        Cursor cursor1 = db.getEmployeefaces();
        if (cursor1.moveToFirst()) {
            do {
                arrfaceid.add(cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.FACEID)));

            } while (cursor1.moveToNext());
        }
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

    private void getAllLocation() {
        JsonArray resultSet  = new JsonArray();
        Cursor cursor = db.getLocationLog();


        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){

        }else {

            uploadcount=uploadcount+cursor.getCount();
            if (cursor.moveToFirst()) {
                do {

                    JsonObject rowObject = new JsonObject();
                    try {

                        rowObject.addProperty("sqlid", cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
                        rowObject.addProperty("empid", cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
                        rowObject.addProperty("date", cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
                        rowObject.addProperty("latitude", cursor.getString(cursor.getColumnIndex(DatabaseHelper.LATITUDE)));
                        rowObject.addProperty("longitude", cursor.getString(cursor.getColumnIndex(DatabaseHelper.LONGITUDE)));

                        resultSet.add(rowObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }

            JsonObject request = new JsonObject();
            request.add("data", resultSet);
            networkCaller.UpdateLocation(RetroFetcher, GetUpdateLocationConstant, request);
        }
    }

    private void getUpdatedEmployeeFaces() {
        JsonObject request = new JsonObject();
        request.addProperty("orglocation", ConstantValues.getorglocation(getApplicationContext()));
        networkCaller.getUpdatedEmpImages(RetroFetcher, GetUpdatedEmployeeConstant, request);
    }

    private void getUpdatedEmployeeInfo() {

        //db.addLog("empid", "date", "time", "grace", "0");
        JsonObject request = new JsonObject();
        request.addProperty("status", "0");
        request.addProperty("orglocation", ConstantValues.getorglocation(getApplicationContext()));
        networkCaller.getEmployee(RetroFetcher, GetEmployeeConstant, request);
    }

    private void getShifttype() {
        JsonObject request = new JsonObject();
        request.addProperty("status", "0");
        networkCaller.getShift(RetroFetcher, GetShiftConstant, request);
    }

    private void getShifttypeid() {
        JsonObject request = new JsonObject();
        request.addProperty("status", "0");
        networkCaller.getShiftType(RetroFetcher, GetShifttypeConstant, request);
    }

    private void getJobtype() {
        JsonObject request = new JsonObject();
        request.addProperty("status", "0");
        networkCaller.getJobType(RetroFetcher, GetJobTypeConstant, request);
    }

    private void getDesignation() {
        JsonObject request = new JsonObject();
        request.addProperty("status", "0");
        networkCaller.getDesignation(RetroFetcher, GetDesignationConstant, request);
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {

        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(getApplicationContext());


        try {
            detector =
                    TFLiteObjectDetectionAPIModel.create(
                            getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_IS_QUANTIZED);
            //cropSize = TF_OD_API_INPUT_SIZE;


        } catch (final IOException e) {
            e.printStackTrace();
            //LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
//            finish();
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
//        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

//        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);

        int targetW, targetH;
        if (sensorOrientation == 90 || sensorOrientation == 270) {
            targetH = previewWidth;
            targetW = previewHeight;
        }
        else {
            targetW = previewWidth;
            targetH = previewHeight;
        }
        int cropW = (int) (targetW / 2.0);
        int cropH = (int) (targetH / 2.0);

        croppedBitmap = Bitmap.createBitmap(cropW, cropH, Bitmap.Config.ARGB_8888);

        portraitBmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
        faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Bitmap.Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropW, cropH,
                        sensorOrientation, MAINTAIN_ASPECT);

//    frameToCropTransform =
//            ImageUtils.getTransformationMatrix(
//                    previewWidth, previewHeight,
//                    previewWidth, previewHeight,
//                    sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);


        Matrix frameToPortraitTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        targetW, targetH,
                        sensorOrientation, MAINTAIN_ASPECT);



        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);


    }


    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;

//        LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        InputImage image = InputImage.fromBitmap(croppedBitmap, 0);
        faceDetector
                .process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {

                        if (faces.size() == 0) {
                            updateResults(currTimestamp, new LinkedList<>());
                            return;
                        }else if (faces.size() > 1){
                            /*String delegate = "hh:mm aaa";
                            String time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
                             Bundle pBundle = new Bundle();
                            pBundle.putString("empid", "0");
                            pBundle.putString("single", single);
                            pBundle.putString("time", time);
                            pBundle.putString("date", todaydate);
                            Intent i=new Intent(getApplicationContext(), MessageActivity.class);
                          //  Intent i=new Intent(getApplicationContext(), SetDateTimeActivity.class);
                            i.putExtras(pBundle);

                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep06);
                            mediaPlayer.start();

                            startActivity(i);
                            finishActivityAll();*/
                        }
                        runInBackground(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        onFacesDetected(currTimestamp, faces, addPending);
                                        addPending = false;
                                    }
                                });
                    }

                });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    @Override
    public void Success(Response<ResponseBody> response, int Identifier) {
        switch (Identifier) {
            case GetEmployeeConstant:
                ExtractEmployeeData(response);
                break;
            case GetAddEmpConstant:
                ExtractAddEmployeeData(response);
                break;
            case GetCompanyConstant:
                ExtractCompanyData(response);
                break;
            case GetUpdatedEmployeeConstant:
                ExtractUpdatedEmployeeData(response);
                break;
            case GetDesignationConstant:
                ExtractDesignationData(response);
                break;
            case GetShiftConstant:
                ExtractShiftData(response);
                break;
            case GetJobTypeConstant:
                ExtractJobTypeData(response);
                break;
            case GetUpdateAttLogConstant:
                ExtractUpdateAttLogData(response);
                break;
            case GetUpdateAttTempLogConstant:
                ExtractUpdateAttTempLogData(response);
                break;
            case GetShifttypeConstant:
                ExtractShiftTypeData(response);
                break;
            case GetHolidaysConstant:
                ExtractHolidayData(response);
                break;
            case GetUpdateLocationConstant:
                ExtractUpdateLocationData(response);
                break;
            case GetDownloadLogConstant:
                ExtractDownloadLogData(response);
                break;
        }
    }

    @Override
    public void Error(Throwable Response, int Identifier) {

    }

    private void ExtractAddEmployeeData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());

            if (object.optString("status").equalsIgnoreCase("success")) {
                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_LONG).show();


            } else {

                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void ExtractCompanyData(Response<ResponseBody> response) {
        try {
             JSONObject object = new JSONObject(response.body().string());

            if (object.optString("status").equalsIgnoreCase("success")) {
                JSONObject data = new JSONObject(object.optString("data"));

                JSONArray store = data.getJSONArray("store");

                for (int p=0; p<store.length(); p++) {

                    JSONObject jsonObject = store.getJSONObject(p);

                    companypass=jsonObject.getString("password");
                    sessionManager.createLoginSession(jsonObject.getString("store_name"),ConstantValues.BASEURL + jsonObject.getString("image"),jsonObject.getString("password"),jsonObject.getString("no_late_markcount"));
                }



            } else {

                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void ExtractEmployeeData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.optString("status").equalsIgnoreCase("success")) {
                JSONObject data = new JSONObject(object.optString("data"));

                JSONArray employee_img = data.getJSONArray("employee");

                for (int p=0; p<employee_img.length(); p++) {

                    JSONObject jsonObject = employee_img.getJSONObject(p);

                    boolean issave=false;

                    byte[] logoImage = new byte[0];

                    if (emp_info_id.contains(jsonObject.getString("id"))){
                        issave=db.updateEmployeeInfo(jsonObject.getString("id"),jsonObject.getString("registerdate"),jsonObject.getString("name"),jsonObject.getString("address"),jsonObject.getString("email"),jsonObject.getString("mobno"),jsonObject.getString("status"),jsonObject.getString("panno"),ConstantValues.BASEURL+jsonObject.getString("emp_thumb"),jsonObject.getString("dob"),jsonObject.getString("designation"),jsonObject.getString("shift_type"),jsonObject.getString("job_type"));

                    }else {
                        issave=db.addEmployeeInfo(jsonObject.getString("id"),jsonObject.getString("registerdate"),jsonObject.getString("name"),jsonObject.getString("address"),jsonObject.getString("email"),jsonObject.getString("mobno"),jsonObject.getString("status"),jsonObject.getString("panno"),ConstantValues.BASEURL+jsonObject.getString("emp_thumb"),jsonObject.getString("dob"),jsonObject.getString("designation"),jsonObject.getString("shift_type"),jsonObject.getString("job_type"));

                    }

                    if (issave){
                        JsonObject request = new JsonObject();
                        request.addProperty("employee_info", "1");
                        request.addProperty("employee_info_id", jsonObject.getString("id"));
                        networkCaller.updateSqliteStatus(RetroFetcher, UpdatedEmpFaceConstant, request);

                    }

                }

            } else {
                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void ExtractUpdatedEmployeeData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.optString("status").equalsIgnoreCase("success")) {
                JSONObject data = new JSONObject(object.optString("data"));


                JSONArray employee_img = data.getJSONArray("employee_img");

                String deviceid=sharedpreferences.getString("deviceid", "");


                for (int p=0; p<employee_img.length(); p++) {

                    JSONObject jsonObject = employee_img.getJSONObject(p);


                    boolean issave=false;

                    if (arrfaceid.contains(jsonObject.getString("id"))){
                        issave=db.updateEmployeeFaces(jsonObject.getString("id"),jsonObject.getString("empid"),jsonObject.getString("tlfid"),jsonObject.getString("name"),jsonObject.getString("title"),jsonObject.getString("distance"),jsonObject.getString("fleft"),jsonObject.getString("fright"),jsonObject.getString("ftop"),jsonObject.getString("fbottom"),jsonObject.getString("imagecrop"),jsonObject.getString("extras"));

                    }else {
                        issave=db.addEmployeeFaces(jsonObject.getString("id"),jsonObject.getString("empid"),jsonObject.getString("tlfid"),jsonObject.getString("name"),jsonObject.getString("title"),jsonObject.getString("distance"),jsonObject.getString("fleft"),jsonObject.getString("fright"),jsonObject.getString("ftop"),jsonObject.getString("fbottom"),jsonObject.getString("imagecrop"),jsonObject.getString("extras"));

                    }

                    if (issave){
                        JsonObject request = new JsonObject();
                        request.addProperty("id", jsonObject.getString("id"));
                        request.addProperty("deviceid", deviceid);
                        networkCaller.updateEmpFace(RetroFetcher, UpdatedEmpFaceConstant, request);

                    }
                }

            } else {
                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void ExtractDesignationData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.optString("status").equalsIgnoreCase("success")) {
                JSONObject data = new JSONObject(object.optString("data"));

                JSONArray designation = data.getJSONArray("designation");

                for (int p=0; p<designation.length(); p++) {

                    JSONObject jsonObject = designation.getJSONObject(p);

                    boolean issave=false;

                    if (designation_id.contains(jsonObject.getString("id"))){
                        issave=db.updateDesignation(jsonObject.getString("id"),jsonObject.getString("designation"));

                    }else {
                        issave=db.addDesignation(jsonObject.getString("id"),jsonObject.getString("designation"));

                    }

                    if (issave){
                        JsonObject request = new JsonObject();
                        request.addProperty("designation", "1");
                        request.addProperty("designation_id", jsonObject.getString("id"));
                        networkCaller.updateSqliteStatus(RetroFetcher, UpdatedEmpFaceConstant, request);
                    }
                }


            } else {
                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void ExtractDownloadLogData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.optString("status").equalsIgnoreCase("success")) {
                JSONObject data = new JSONObject(object.optString("data"));

                JSONArray downloadlog = data.getJSONArray("downloadlog");

                for (int p=0; p<downloadlog.length(); p++) {

                    JSONObject jsonObject = downloadlog.getJSONObject(p);

                    boolean issave=false;

                    Cursor cursor1 = db.getavailableshiftid(jsonObject.getString("empid"),jsonObject.getString("entrydate"),jsonObject.getString("shiftid"));
                    if (cursor1.getCount()==0) {
                        issave=db.addLog(jsonObject.getString("empid"),jsonObject.getString("entrydate"),jsonObject.getString("exitdate"),jsonObject.getString("in_time"),jsonObject.getString("in_grace"),jsonObject.getString("lunchout_time"),jsonObject.getString("lunchout_grace"),jsonObject.getString("lunchin_time"),jsonObject.getString("lunchin_grace"),jsonObject.getString("out_time"),jsonObject.getString("out_grace"),jsonObject.getString("total_hours"),jsonObject.getString("shiftno"),jsonObject.getString("shiftid"),"1");
                    }
                }


            } else {
                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void ExtractShiftData(Response<ResponseBody> response){
        try{
            JSONObject obj=new JSONObject(response.body().string());
            if(obj.optString("status").equalsIgnoreCase("success")){
                JSONObject data=new JSONObject(obj.optString("data"));

                JSONArray designation = data.getJSONArray("shift");

                for (int p=0; p<designation.length(); p++) {

                    JSONObject jsonObject = designation.getJSONObject(p);

                    boolean issave=false;

                    if (shift_type_id.contains(jsonObject.getString("id"))){
                        issave=db.updateShift(jsonObject.getString("id"),jsonObject.getString("shift_typeid"),jsonObject.getString("shift_type"),jsonObject.getString("in_time"),jsonObject.getString("in_grace"),jsonObject.getString("out_time"),jsonObject.getString("out_grace"),jsonObject.getString("lunch_in"),jsonObject.getString("lunch_in_grace"),jsonObject.getString("lunch_out"),jsonObject.getString("lunch_out_grace"),jsonObject.getString("admin_in_grace"),jsonObject.getString("admin_lo_grace"),jsonObject.getString("indisplaybefore"),jsonObject.getString("indisplayafter"),jsonObject.getString("outdisplaybefore"),jsonObject.getString("outdisplayafter"),jsonObject.getString("lindisplaybefore"),jsonObject.getString("lindisplayafter"),jsonObject.getString("loutdisplaybefore"),jsonObject.getString("loutdisplayafter"));

                    }else {
                        issave=db.addShift(jsonObject.getString("id"),jsonObject.getString("shift_typeid"),jsonObject.getString("shift_type"),jsonObject.getString("in_time"),jsonObject.getString("in_grace"),jsonObject.getString("out_time"),jsonObject.getString("out_grace"),jsonObject.getString("lunch_in"),jsonObject.getString("lunch_in_grace"),jsonObject.getString("lunch_out"),jsonObject.getString("lunch_out_grace"),jsonObject.getString("admin_in_grace"),jsonObject.getString("admin_lo_grace"),jsonObject.getString("indisplaybefore"),jsonObject.getString("indisplayafter"),jsonObject.getString("outdisplaybefore"),jsonObject.getString("outdisplayafter"),jsonObject.getString("lindisplaybefore"),jsonObject.getString("lindisplayafter"),jsonObject.getString("loutdisplaybefore"),jsonObject.getString("loutdisplayafter"));


                    }


                    if (issave){
                        JsonObject request = new JsonObject();
                        request.addProperty("shifttype", "1");
                        request.addProperty("shifttype_id", jsonObject.getString("id"));
                        networkCaller.updateSqliteStatus(RetroFetcher, UpdatedEmpFaceConstant, request);
                    }
                }

            }else{
                Toast.makeText(MainActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
        }
    }


    public void ExtractShiftTypeData(Response<ResponseBody> response){
        try{
            JSONObject obj=new JSONObject(response.body().string());
            if(obj.optString("status").equalsIgnoreCase("success")){
                JSONObject data=new JSONObject(obj.optString("data"));

                JSONArray designation = data.getJSONArray("shift");

                for (int p=0; p<designation.length(); p++) {

                    JSONObject jsonObject = designation.getJSONObject(p);

                    boolean issave=false;

                    if (shift_type_id.contains(jsonObject.getString("id"))){
                        issave=db.updateShiftType(jsonObject.getString("id"),jsonObject.getString("name"));
                    }else {
                        issave=db.addShiftTYPE(jsonObject.getString("id"),jsonObject.getString("name"));

                    }


                    if (issave){
                        JsonObject request = new JsonObject();
                        request.addProperty("shift", "1");
                        request.addProperty("shift_id", jsonObject.getString("id"));
                        networkCaller.updateSqliteStatus(RetroFetcher, UpdatedEmpFaceConstant, request);
                    }
                }

            }else{
                Toast.makeText(MainActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
        }
    }

    public void ExtractJobTypeData(Response<ResponseBody> response){
        try{
            JSONObject obj=new JSONObject(response.body().string());
            if(obj.optString("status").equalsIgnoreCase("success")){
                JSONObject data=new JSONObject(obj.optString("data"));

                JSONArray designation = data.getJSONArray("job");

                for (int p=0; p<designation.length(); p++) {

                    JSONObject jsonObject = designation.getJSONObject(p);

                    boolean issave=false;

                    if (job_type_id.contains(jsonObject.getString("id"))){
                        issave=db.updateJobtype(jsonObject.getString("id"),jsonObject.getString("job_type"));
                    }else {
                        issave=db.addJobtype(jsonObject.getString("id"),jsonObject.getString("job_type"));
                    }


                    if (issave){
                        JsonObject request = new JsonObject();
                        request.addProperty("jobtype", "1");
                        request.addProperty("jobtype_id", jsonObject.getString("id"));
                        networkCaller.updateSqliteStatus(RetroFetcher, UpdatedEmpFaceConstant, request);
                    }
                }

            }else{
                Toast.makeText(MainActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
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
                Toast.makeText(MainActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
        }
    }

    private void ExtractHolidayData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.optString("status").equalsIgnoreCase("success")) {
                JSONObject data = new JSONObject(object.optString("data"));

                JSONArray dates = data.getJSONArray("dates");
                for (int p=0; p<dates.length(); p++) {
                    JSONObject jsonObject = dates.getJSONObject(p);
                    holidays.add(jsonObject.getString("date"));
                }

                JSONArray dates1 = data.getJSONArray("dates1");
                for (int p=0; p<dates1.length(); p++) {
                    JSONObject jsonObject = dates1.getJSONObject(p);
                    holidays.add(jsonObject.getString("date"));
                }

            } else {
                Toast.makeText(MainActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException | IOException e) {
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
                Toast.makeText(MainActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
        }
    }

    public void ExtractUpdateLocationData(Response<ResponseBody> response){
        try{
            JSONObject obj=new JSONObject(response.body().string());
            if(obj.optString("status").equalsIgnoreCase("success")){
                JSONObject data=new JSONObject(obj.optString("data"));

                String sqlid = data.getString("sqlid");

                db.updateLocationLog(sqlid);

            }else{
                Toast.makeText(MainActivity.this,obj.optString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException|IOException e){
            e.printStackTrace();
        }
    }
    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(() -> detector.setUseNNAPI(isChecked));
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }


    // Face Processing
    private Matrix createTransform(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation) {

        Matrix matrix = new Matrix();
        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
//                LOGGER.w("Rotation of %d % 90 != 0", applyRotation);
            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }

        if (applyRotation != 0) {

            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;

    }

    private void showAddFaceDialog(SimilarityClassifier.Recognition rec) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_edit_dialog, null);
        ImageView ivFace = dialogLayout.findViewById(R.id.dlg_image);
        TextView tvTitle = dialogLayout.findViewById(R.id.dlg_title);
        EditText etName = dialogLayout.findViewById(R.id.dlg_input);

        tvTitle.setText("Add Face");
        ivFace.setImageBitmap(rec.getCrop());
        etName.setHint("Input name");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int i) {

                String name = etName.getText().toString();
                if (name.isEmpty()) {
                    return;
                }

                Object aaa=rec.getExtra();

                Log.d("vvvvvvvv1", Arrays.toString((float[][]) rec.getExtra()));

                JsonObject request= new JsonObject();
                request.addProperty("id","0");
                request.addProperty("emp_id","2");
                request.addProperty("tlfid",rec.getId());
                request.addProperty("name",name);
                request.addProperty("title",rec.getTitle());
                request.addProperty("distance",String.valueOf(rec.getDistance()));
                request.addProperty("fleft",String.valueOf(rec.getLocation().left));
                request.addProperty("fright",String.valueOf(rec.getLocation().right));
                request.addProperty("ftop",String.valueOf(rec.getLocation().top));
                request.addProperty("fbottom",String.valueOf(rec.getLocation().bottom));
                request.addProperty("extra", new Gson().toJson(rec.getExtra()));
                request.addProperty("crop",String.valueOf(rec.getCrop()));
//                String json = new Gson().toJson(myarraylist);

                networkCaller.addEmployeeImage(RetroFetcher,GetAddEmpConstant,request);


                //knownFaces.put(name, rec);
                dlg.dismiss();
            }
        });
        builder.setView(dialogLayout);
        builder.show();

    }

    private void updateResults(long currTimestamp, final List<SimilarityClassifier.Recognition> mappedRecognitions) {

         tracker.trackResults(mappedRecognitions, currTimestamp);
        trackingOverlay.postInvalidate();
        computingDetection = false;
        //adding = false;


        if (mappedRecognitions.size() > 0) {
//            LOGGER.i("Adding results");
            SimilarityClassifier.Recognition rec = mappedRecognitions.get(0);
            if (rec.getExtra() != null) {
//                Log.d("recccc",rec.getId());
//                Log.d("recccc",rec.getTitle());
//                Log.d("recccc",rec.getDistance().toString());
//                Log.d("recccc",String.valueOf(rec.getLocation().left));
//                Log.d("recccc",String.valueOf(rec.getLocation().bottom));
//                Log.d("recccc",String.valueOf(rec.getLocation().right));
//                Log.d("recccc",String.valueOf(rec.getLocation().top));
                showAddFaceDialog(rec);
            }
        }

        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        showFrameInfo(previewWidth + "x" + previewHeight);
                        showCropInfo(croppedBitmap.getWidth() + "x" + croppedBitmap.getHeight());
                        showInference(lastProcessingTimeMs + "ms");
                    }
                });

    }

    private void onFacesDetected(long currTimestamp, List<Face> faces, boolean add) {
        if(isCameraUsebyApp())
        {
            Log.d("Camera used","Camera used");
        }

        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
        final Canvas canvas = new Canvas(cropCopyBitmap);
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
        switch (MODE) {
            case TF_OD_API:
                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                break;
        }

        final List<SimilarityClassifier.Recognition> mappedRecognitions =
                new LinkedList<SimilarityClassifier.Recognition>();


        //final List<Classifier.Recognition> results = new ArrayList<>();

        // Note this can be done only once
        int sourceW = rgbFrameBitmap.getWidth();
        int sourceH = rgbFrameBitmap.getHeight();
        int targetW = portraitBmp.getWidth();
        int targetH = portraitBmp.getHeight();
        Matrix transform = createTransform(
                sourceW,
                sourceH,
                targetW,
                targetH,
                sensorOrientation);
        final Canvas cv = new Canvas(portraitBmp);

        // draws the original image in portrait mode.
        cv.drawBitmap(rgbFrameBitmap, transform, null);

        final Canvas cvFace = new Canvas(faceBmp);

        boolean saved = false;

        if (faces.size() > 1){



            String delegate = "hh:mm aaa";
            String time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
            Bundle pBundle = new Bundle();
            pBundle.putString("empid", "0");
            pBundle.putString("single", single);
            pBundle.putString("time", time);
            pBundle.putString("date", todaydate);
            Intent i=new Intent(getApplicationContext(), MultipleFacesActivity.class);
            //  Intent i=new Intent(getApplicationContext(), SetDateTimeActivity.class);
            i.putExtras(pBundle);

           /* MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep06);
            mediaPlayer.start();*/

            startActivity(i);
            //finish();
            finishActivityAll();
        }
        else if(faces.size()==1)

            /* for (Face face : faces) */
        {

            Face face = faces.get(0);

            final RectF boundingBox = new RectF(face.getBoundingBox());

            //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
            final boolean goodConfidence = true; //face.get;
            if (boundingBox != null && goodConfidence) {

                // maps crop coordinates to original
                cropToFrameTransform.mapRect(boundingBox);

                // maps original coordinates to portrait coordinates
                RectF faceBB = new RectF(boundingBox);
                transform.mapRect(faceBB);

                // translates portrait to origin and scales to fit input inference size
                //cv.drawRect(faceBB, paint);
                float sx = ((float) TF_OD_API_INPUT_SIZE) / faceBB.width();
                float sy = ((float) TF_OD_API_INPUT_SIZE) / faceBB.height();
                Matrix matrix = new Matrix();
                matrix.postTranslate(-faceBB.left, -faceBB.top);
                matrix.postScale(sx, sy);

                cvFace.drawBitmap(portraitBmp, matrix, null);

                //canvas.drawRect(faceBB, paint);

                String label = "";
                float confidence = -1f;
                Integer color = Color.BLUE;
                Object extra = null;
                Bitmap crop = null;

                if (add) {
                    crop = Bitmap.createBitmap(portraitBmp,
                            (int) faceBB.left,
                            (int) faceBB.top,
                            (int) faceBB.width(),
                            (int) faceBB.height());
                }

                final long startTime = SystemClock.uptimeMillis();
                final List<SimilarityClassifier.Recognition> resultsAux = detector.recognizeImage(faceBmp, add);
                lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                if (resultsAux.size() > 0) {

                    SimilarityClassifier.Recognition result = resultsAux.get(0);

                    extra = result.getExtra();

                    float conf = result.getDistance();

                    if (conf < 0.75f) {

                        confidence = conf;
                        label = result.getTitle();

                        if (result.getId().equals("0")) {

                            Bundle pBundle = new Bundle();
                            pBundle.putString("empid", result.getEmpId());
                            pBundle.putString("single", single);
                            Intent i=new Intent(getApplicationContext(), MessageActivity.class);
                           // Intent i=new Intent(getApplicationContext(), SetDateTimeActivity.class);
                            i.putExtras(pBundle);

                            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep06);
                            mediaPlayer.start();
                            startActivity(i);
                            //finish();
                            finishActivityAll();

                            color = Color.GREEN;
                        }
                        else {
                            color = Color.RED;
                        }
                    }else {
                        color = Color.RED;
                    }

                }

                if (getCameraFacing() == CameraCharacteristics.LENS_FACING_FRONT) {

                    // camera is frontal so the image is flipped horizontally
                    // flips horizontally
                    Matrix flip = new Matrix();
                    if (sensorOrientation == 90 || sensorOrientation == 270) {
                        flip.postScale(1, -1, previewWidth / 2.0f, previewHeight / 2.0f);
                    }
                    else {
                        flip.postScale(-1, 1, previewWidth / 2.0f, previewHeight / 2.0f);
                    }
                    //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
                    flip.mapRect(boundingBox);

                }

                final SimilarityClassifier.Recognition result = new SimilarityClassifier.Recognition(
                        "0", label, confidence, boundingBox);

                dextra=extra;
                dcrop=crop;

                result.setColor(color);
                result.setLocation(boundingBox);
                result.setExtra(extra);
                result.setCrop(crop);

                if (crop != null){
                    dextra=extra;
                    dcrop=crop;
                }

                mappedRecognitions.add(result);

            }


        }
        else{

        }

        updateResults(currTimestamp, mappedRecognitions);
        getemployee();
    }

    public boolean isCameraUsebyApp() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return true;
        } finally {
            if (camera != null) camera.release();
        }
        return false;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(!isexist) {
            h.removeCallbacks(runnable);
        }

    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
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
                    Intent i=new Intent(getApplicationContext(),MainStableScreenActivity.class);
                    startActivity(i);
                    finishActivityAll();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            FaceDetectorOptions options =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                            .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                            .build();


            FaceDetector detector = FaceDetection.getClient(options);

            faceDetector = detector;
        }
    }

}