package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity implements ResponseCarrier {
    ImageView img_employee;
    TextView txt_msg,txt_time,txt_name,txt_latemark;
    LinearLayout lay_back;
    String empid,time,date,single,newouttime="",firstDateOfMonth,latemarkcount="";

    RegiatrationNetworkCaller networkCaller;
    APIInterface RetroFetcher;
    SessionManager session;

    Calendar calendar;
    int year,month,day;
    int hours,min,sec;

    private DatabaseHelper db;
    String empshifttype="",empshifttypearr="";
    String empphoto;
    String empname,empstatus;
    String entrydate="",alin_time="",allunchout_time="",allunchin_time="",alout_time="",altotal_hours="",typeno="";
    String shifttypeid,intime,outtime,lunchin,lunchout,ingrace,outgrace,lunchingrace,lunchoutgrace,adminingrace,adminlograce;

    List<String> al = new ArrayList<String>();
    private ArrayList<String> arrintime=new ArrayList<>();
    private ArrayList<String> arrshiftid=new ArrayList<>();

    private ArrayList<Date> outdates=new ArrayList<>();

    private ArrayList<String> arrouttime=new ArrayList<>();
    private ArrayList<String> arrshiftoutid=new ArrayList<>();

    private static final int REQUEST_LOCATION = 1;

    private final int GetattendanceConstant = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (getIntent().getExtras() != null){
            empid = getIntent().getExtras().getString("empid");
            single = getIntent().getExtras().getString("single");
            time = getIntent().getExtras().getString("time");
            date = getIntent().getExtras().getString("date");

        }

        db = new DatabaseHelper(MessageActivity.this);

        init();
        initVariable();
        
        String delegate = "hh:mm aaa";
        time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

        txt_time.setText(time);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR); // current year
        month = calendar.get(Calendar.MONTH); // current month
        day = calendar.get(Calendar.DAY_OF_MONTH); // current day

        month=month + 1;
        String monthstr="",daystr="";
        if(month<10)
        {
            monthstr="0"+month;
        }else {
            monthstr=String.valueOf(month);
        }

        if(day<10)
        {
            daystr="0"+day;
        }else {
            daystr=String.valueOf(day);
        }

        date=year + "-"+ monthstr + "-" + daystr;
        firstDateOfMonth=year + "-"+ monthstr + "-01";

        HashMap<String, String> user = session.getUserDetails();
        latemarkcount=user.get(SessionManager.LATEMARKCOUNT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String visit = ConstantValues.getSingleVisit(MessageActivity.this);

                if (visit.equals("1")) {
                    ConstantValues.putSingleVisit("0","0",MessageActivity.this);

                    Intent i = new Intent(MessageActivity.this, MainStableScreenActivity.class);
                    startActivity(i);
                    db.close();
                    finish();

                    System.exit(0);

                }else {

                    Intent i = new Intent(MessageActivity.this, MainActivity.class);
                    startActivity(i);
                    db.close();
                    finish();

                    System.exit(0);
                }

//                setResult(REQUEST_LOCATION);

            }
        },3000);

        if (empid.equals("0")){

            txt_name.setVisibility(View.GONE);
            String msg = "Multiple Faces Detected\nPlease Try Again!";
            showMessage(msg, "2");

        }else {
            SaveAttendance();
        }

    }

    private void init() {

        txt_msg=findViewById(R.id.txt_msg);
        txt_name=findViewById(R.id.txt_name);
        txt_time=findViewById(R.id.txt_time);
        img_employee=findViewById(R.id.img_employee);
        txt_latemark=findViewById(R.id.txt_latemark);

        lay_back=findViewById(R.id.lay_back);


    }


    private void initVariable() {
        session = new SessionManager(MessageActivity.this);
        networkCaller = new RegiatrationNetworkCaller(MessageActivity.this, this);
        RetroFetcher = APIClient.StringClient().create(APIInterface.class);

       // db = new DatabaseHelper(this);

    }

    private void SaveAttendance() {

            Cursor cursor = db.getemployeedetails(empid);
            if (cursor.moveToFirst()) {
                do {
                    empname=cursor.getString(cursor.getColumnIndex(DatabaseHelper.ENAME));
                    empphoto=cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHOTO));
                    empshifttypearr=cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTTYPE));
                    empstatus=cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATUS));

                    if(isNetworkConnected())
                    {
                        if (empphoto.equals("")){
                            img_employee.setImageResource(R.drawable.userdemo);
                        }else {
                            Glide.with(this).load(empphoto).into(img_employee);
                        }
                    }
                    else
                    {
                        img_employee.setImageResource(R.drawable.no_connection);
                    }

                    txt_name.setText(empname);

                } while (cursor.moveToNext());
            }

            if (empstatus.equals("0")){
                String msg = "Your Profile is Inactive";
                showMessage(msg, "3");
            }else {
                if (!empshifttypearr.equals("")) {

                    String str[] = empshifttypearr.split(",");

                    al = Arrays.asList(str);

                    String recenttime="12:05 AM";
                    String recentshifttype="";

                    if (al.size()==1){

                        empshifttype=empshifttypearr;

                    }else {
                        Cursor cursor1 = db.gettodayspresentshift(empid,date,time);
                        if (cursor1.getCount()>0) {

                            if (cursor1.moveToFirst()) {
                                do {
                                    empshifttype = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.ATTSHIFTID));

                                } while (cursor1.moveToNext());
                            }

                        }else {
                            arrintime.clear();
                            arrshiftid.clear();

                            outerloop:
                                for (int i=0;i<al.size();i++) {
                                    String empshifttype1 = al.get(i);

                                    Cursor scursor = db.getemployeeshift(empshifttype1);

                                    if (scursor.moveToFirst()) {
                                        do {

                                            intime = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTIN));
                                            adminingrace = scursor.getString(scursor.getColumnIndex(DatabaseHelper.ADMININGRACE));

                                            arrintime.add(date +" "+intime);
                                            arrshiftid.add(empshifttype1);

                                            String intimebefore = "",intimeafter="",timenow="",datetimenow="";
                                            try {
                                               intimebefore = ConstantVariables.calculateTime24(intime,time, -Integer.parseInt(adminingrace),date);
                                               intimeafter = ConstantVariables.calculateTime24(intime,time, Integer.parseInt(adminingrace),date);

                                                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
                                                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                                                Date time1 = df.parse(time);
                                                timenow=displayFormat.format(time1);


                                            }catch (Exception e){
                                               e.printStackTrace();
                                           }


                                            datetimenow=date+" "+timenow;

                                            boolean istimeexist = ConstantVariables.istime24exist1(intimebefore, datetimenow, intimeafter);

                                            if (istimeexist) {
                                                empshifttype=empshifttype1;

                                                break outerloop;
                                            }


                                        } while (scursor.moveToNext());
                                    }

                                }
                        }
                    }

                    if (empshifttype.equals("")){

                        String newshiftid = calculateAfterBeforeTime(arrintime,arrshiftid,time);

                        empshifttype=newshiftid;
                        Log.d("aaaaaaaasssss444",empshifttype);

                    }

                    if (!empshifttype.equals("")) {
                        Cursor scursor = db.getemployeeshift(empshifttype);
                        if (scursor.moveToFirst()) {
                            do {
                                shifttypeid = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTTYPEID));
                                intime = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTIN));
                                outtime = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTOUT));
                                lunchin = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTLIN));
                                lunchout = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTLOUT));
                                ingrace = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTINGRACE));
                                outgrace = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTOUTGRACE));
                                lunchoutgrace = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTLOUTGRACE));
                                lunchingrace = scursor.getString(scursor.getColumnIndex(DatabaseHelper.SHIFTLINGRACE));
                                adminingrace = scursor.getString(scursor.getColumnIndex(DatabaseHelper.ADMININGRACE));
                                adminlograce = scursor.getString(scursor.getColumnIndex(DatabaseHelper.ADMINLOGRACE));

                                Log.d("shifttypeid",shifttypeid);

                            } while (scursor.moveToNext());
                        }
                    }
                }

                if (!empshifttype.equals("")) {
                    Cursor cursor1 = db.gettodayspresentshiftid(empid,date,empshifttype);
                    if (cursor1.getCount()>0) {
                        Cursor cursortemp = db.getAttendanceTempSameLog(empid, date, time);

                        if (!(cursortemp.moveToFirst()) || cursortemp.getCount() ==0){
                            boolean issave = false;
                            issave = db.addTempLog(empid, date, time, "5");
                            if (issave) {
                                MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.extra);
                                mediaPlayer.start();

                                String msg="Extra Log has been Added!";
                                showMessage(msg,"5");
                            }
                        }else {
                            MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.extra);
                            mediaPlayer.start();

                            String msg="Extra Log has been Added!";
                            showMessage(msg,"5");
                        }


                    }else {

                        Log.d("shifttypeid",shifttypeid);
                        if (shifttypeid.equals("3") || shifttypeid.equals("4")) {
                            CalculateNightLogs();
                        } else {
                            SaveToSqlite();
                        }
                    }
                }else {

                    Cursor cursortemp = db.getAttendanceTempSameLog(empid, date, time);

                    if (!(cursortemp.moveToFirst()) || cursortemp.getCount() ==0){

                        boolean issave = false;
                        issave = db.addTempLog(empid, date, time, "5");
                        if (issave) {

                            MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.extra);
                            mediaPlayer.start();

                            String msg="Extra Log has been Added";
                            showMessage(msg,"5");
                        }
                    }else{
                        MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.extra);
                        mediaPlayer.start();

                        String msg="Extra Log has been Added!";
                        showMessage(msg,"5");
                    }
                }
            }
    }



    private void showMessage(String msg, String s) {
        if (s.equals("0")){
            lay_back.setBackgroundResource(R.color.red_800);
        }else if (s.equals("1")){
            lay_back.setBackgroundResource(R.color.green_800);
        }else if (s.equals("2")){
            lay_back.setBackgroundResource(R.color.blue_grey_800_overlay);
        }else if (s.equals("5")){
            lay_back.setBackgroundResource(R.color.green_800);
        }else {
            lay_back.setBackgroundResource(R.color.red_800);
        }

        txt_msg.setText(msg);
    }

    public void SaveToSqlite() {
        Cursor cursor1 = db.gettodayspresent(empid,date);

            if (!(cursor1.moveToFirst()) || cursor1.getCount() ==0){

            String[] arrOfStr = ingrace.split("-", 2);
            String inmin=arrOfStr[1];

            String intimeplus=ConstantVariables.calculateNewTime(intime,Integer.parseInt(inmin));


            String arr[]=ConstantVariables.Check_time(intime,time,intimeplus);

            String msg=arr[0];
            String grace=arr[1];

            if (grace.contains("L")){
                String latecount=db.getInTimeLateCount(empid,firstDateOfMonth);

                int latemarkcountint=0;
                int latecountint=0;

                if(!latemarkcount.equals("null"))
                    latemarkcountint=Integer.parseInt(latemarkcount);

                if(!latecount.equals("null"))
                    latecountint=Integer.parseInt(latecount);

                if (latecountint>=latemarkcountint){
                    txt_latemark.setVisibility(View.VISIBLE);
                    txt_latemark.setText("Late Mark Count : "+latecount);
                }else {
                    txt_latemark.setVisibility(View.VISIBLE);
                    txt_latemark.setText("Late Mark Count : "+latecount);
                }


            }
            boolean issave=false;

            if (shifttypeid.equals("2")){
                issave=db.addLog(empid,date,date,time,grace,"0","T 0-0","0","T 0-0","","","0","0",empshifttype, "0");
            }else {
                issave=db.addLog(empid,date,date,time,grace,"","","","","","","0","0",empshifttype, "0");
            }

            if (issave){
                MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.login);
                mediaPlayer.start();

                lay_back.setBackgroundResource(R.color.green_800);
                txt_msg.setText("Login Successfully!\n"+msg);
            }

        }else {
            if (cursor1.moveToFirst()) {
                do {
                    alin_time=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.INTIME));
                    allunchout_time=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.LUNCHOUT));
                    allunchin_time=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.LUNCHIN));
                    alout_time=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.OUTTIME));
                    altotal_hours=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TOTALHRS));
                    typeno=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.TYPENO));
                    empshifttype=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.ATTSHIFTID));

                    if (allunchout_time.equals("")){

                        String[] arrOfStr = lunchoutgrace.split("-", 2);
                        String min=arrOfStr[1];

                        String lunchoutminus=ConstantVariables.calculateNewTime(lunchout,-Integer.parseInt(min));
                        String lunchoutplus=ConstantVariables.calculateNewTime(lunchout,Integer.parseInt(min));

                        try {
                          /*  SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

                             Date now = sdf.parse(time);
                            Date begin = sdf.parse(intime);
                            Date end = sdf.parse(lunchoutminus);

                           String in_timeplus= ConstantVariables.calculateNewTime(lunchout,-Integer.parseInt(adminingrace));
                            String alin_timeminus= ConstantVariables.calculateNewTime(alin_time,-5);

                            Date begin1 = sdf.parse(alin_timeminus);
                            Date end1 = sdf.parse(in_timeplus);

                            if (now.after(begin1) && now.before(end1)){

                                boolean issave=false;
                                issave=db.addTempLog(empid,date,time,"1");
                                if (issave){
                                    String msg="You are Already Logged in";
                                    showMessage(msg,"0");
                                }

                            }else if (now.after(begin) && now.before(end)){
                                boolean issave=false;
                                issave=db.addTempLog(empid,date,time,"1");
                                if (issave){
                                    String msg="You are Already Logged in";
                                    showMessage(msg,"0");
                                }

                            }else {*/
                            Log.v("Calculate_intime",alin_time);
                            Log.v("Calculate_time",time);

                                long hrsmin[]=ConstantVariables.CalculateHrsMin(alin_time,time);

                                long hrsc=hrsmin[0];
                                long minc=hrsmin[1];

                                long totalmin;
                                if(hrsc==0){
                                    totalmin=minc;
                                }else{
                                    long minfromhrs=hrsc*60;

                                    if(minc==0){
                                        totalmin=minfromhrs;
                                    }else{
                                        totalmin=minfromhrs+minc;
                                    }
                                }

                                String arr[]=ConstantVariables.Check_time(lunchout,time,lunchoutplus);

                                String msg=arr[0];
                                String grace=arr[1];

                                boolean issave=false;
                                issave=db.updateEMPLunchOut(empid,date,time,grace,String.valueOf(totalmin),empshifttype);
                                if (issave){
                                   // showMessage(msg,"1");
                                    MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.logout);
                                    mediaPlayer.start();

                                    showMessage("Log Out Successfully!\n", "1");
                                }
                           // }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (allunchin_time.equals("")){

                        String[] arrOfStr = lunchoutgrace.split("-", 2);
                        String min=arrOfStr[1];

                        String lunch_break=ConstantVariables.calculateNewTime(allunchout_time,Integer.parseInt(min));

                        String lunch_inplus=ConstantVariables.calculateNewTime(allunchout_time,Integer.parseInt(adminlograce));
                        String lunch_outminus=ConstantVariables.calculateNewTime(allunchout_time,-5);

                        try {
             /*               SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

                            Date now = sdf.parse(time);
                            Date begin = sdf.parse(lunch_outminus);
                            Date end = sdf.parse(lunch_inplus);

                            if (now.after(begin) && now.before(end)){
                                boolean issave=false;
                                issave=db.addTempLog(empid,date,time,"3");
                                if (issave){
                                    String msg="Lunch Out Log Already Exists";
                                    showMessage(msg,"0");
                                }

                            }else {*/

                                String arr[]=ConstantVariables.Check_time(lunch_break,time,lunch_break);

                                String msg=arr[0];
                                String grace=arr[1];

                                boolean issave=false;
                                allunchin_time=time;
                                issave=db.updateEMPLunchIn(empid,date,time,grace,empshifttype);
                                if (issave){
                                  //  showMessage(msg,"1");
                                    MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.login);
                                    mediaPlayer.start();

                                    lay_back.setBackgroundResource(R.color.green_800);
                                    txt_msg.setText("Login Successfully!\n");
                                }
                            //}
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else if (alout_time.equals("")){

                        if (alin_time.equals("Absent")){
                            String msg="Absent log Already Exist";
                            showMessage(msg,"0");
                        }else {

                            String inplusout;

                            try {
/*                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

                                if (shifttypeid.equals("2")) {
                                    inplusout = ConstantVariables.calculateNewTime(alin_time, 5);
                                } else {
                                    inplusout = ConstantVariables.calculateNewTime(allunchin_time, 5);
                                }

                                Date now = sdf.parse(time);
                                Date end = sdf.parse(inplusout);

                                Log.d("aaaffgg",time);
                                Log.d("aaaffgg",inplusout);

                                if (now.before(end)) {

                                    Cursor cursortemp = db.getAttendanceTempSameLog(empid, date, time);

                                    if (!(cursortemp.moveToFirst()) || cursortemp.getCount() ==0){
                                        boolean issave = false;
                                        issave = db.addTempLog(empid, date, time, "4");
                                        if (issave) {
                                            String msg;
                                            if (shifttypeid.equals("2")) {
                                                msg = "Already Log In ";

                                            } else {
                                                msg = "Lunch In Log Already Exists";

                                            }
                                            showMessage(msg, "0");
                                        }
                                    }else {
                                        String msg;
                                        if (shifttypeid.equals("2")) {
                                            msg = "Already Log In ";

                                        } else {
                                            msg = "Lunch In Log Already Exists";

                                        }
                                        showMessage(msg, "0");
                                    }

                                } else {*/

                                    String shiftidnew="";
                                    if (al.size()!=0) {
                                        shiftidnew = getouttimes(intime, time);
                                    }else {
                                        shiftidnew=empshifttype;
                                        newouttime=outtime;
                                    }

                                    long hrsmin[];
                                    if (shifttypeid.equals("2")) {
                                        hrsmin = ConstantVariables.CalculateHrsMin(alin_time, time);
                                    } else {
                                        hrsmin = ConstantVariables.CalculateHrsMin(allunchin_time, time);
                                    }

                                    long hrsc = hrsmin[0];
                                    long minc = hrsmin[1];

                                    long totalmin;
                                    if (hrsc == 0) {
                                        totalmin = minc;
                                    } else {
                                        long minfromhrs = hrsc * 60;

                                        if (minc == 0) {
                                            totalmin = minfromhrs;
                                        } else {
                                            totalmin = minfromhrs + minc;
                                        }
                                    }

                                    long finalhrs = Integer.parseInt(altotal_hours) + totalmin;

                                    String newouttime1="";
                                    if (newouttime.equals("")){
                                        newouttime1 = outtime;
                                    }else {
                                        newouttime1 = newouttime;
                                    }
                                    String arr[] = ConstantVariables.Check_out_time(newouttime1, time);


                                    String msg = arr[0];
                                    String grace = arr[1];

                                    boolean issave = true;
                                    int typenoi=Integer.parseInt(typeno);
                                    typenoi++;
                                    Toast.makeText(this, shiftidnew, Toast.LENGTH_SHORT).show();
                                    issave = db.updateEMPOut(empid, date, time, grace, String.valueOf(finalhrs), date,String.valueOf(typenoi),empshifttype,shiftidnew);
                                    if (issave) {
                                        MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.logout);
                                        mediaPlayer.start();

                                        showMessage("Log Out Successfully!\n", "1");
                                    }

                               // }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }else {

                        boolean issave=false;
                        issave=db.addTempLog(empid,date,time,"4");
                        if (issave){
                            String msg="You are Logout";
                            showMessage(msg,"0");
                        }
                    }

                } while (cursor1.moveToNext());
            }

        }
    }

    private String getouttimes(String alin_time, String time) {

        for (int i=0;i<al.size();i++) {
            String empshifttype1 = al.get(i);

            Cursor cursor = db.getOuttime(alin_time,empshifttype1);
            if (cursor.moveToFirst()) {
                do {

                    arrouttime.add(date+" "+cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTOUT)));
                    arrshiftoutid.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SHIFTID)));

                } while (cursor.moveToNext());
            }

        }

        String newshiftid = calculateAfterBeforeTime(arrouttime,arrshiftoutid,time);

        return newshiftid;
    }

    private String calculateAfterBeforeTime(ArrayList<String> arrouttime, ArrayList<String> arrshiftoutid, String time) {

        String beforetime="",aftertime="";
        String beforeshift="",aftershift="",newshiftid="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

        if (arrouttime.size()>1) {
            long hrsmin1[], hrsmin2[];

            for (int i = 0; i < arrouttime.size(); i++) {

                try {

                    Date arrouttimed = sdf.parse(arrouttime.get(i));
                    Date now = sdf.parse(date+" "+time);

                    if (arrouttimed.before(now)) {
                        if (!beforetime.equals("")) {
                            Date beforetimed = sdf.parse(beforetime);

                            if (beforetimed.after(arrouttimed)) {
                                beforetime = beforetime;
                                beforeshift=beforeshift;
                            } else {
                                beforetime = arrouttime.get(i);
                                beforeshift=arrshiftoutid.get(i);
                            }

                        } else {
                            beforetime = arrouttime.get(i);
                            beforeshift=arrshiftoutid.get(i);
                        }
                    }else {
                        if (!aftertime.equals("")){
                            Date aftertimed = sdf.parse(aftertime);

                            if (aftertimed.before(arrouttimed)){
                                aftertime=aftertime;
                                aftershift=aftershift;

                            }else {
                                aftertime=arrouttime.get(i);
                                aftershift=arrshiftoutid.get(i);

                            }
                        }else {
                            aftertime=arrouttime.get(i);
                            aftershift=arrshiftoutid.get(i);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (beforetime.equals("")){
                return aftershift;
            }else if (aftertime.equals("")){
                return beforeshift;
            }else {


            try {


                SimpleDateFormat sdf24 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date time24 = sdf.parse(date+" "+time);
                String time24str=sdf24.format(time24);

                Date beforetime24 = sdf.parse(beforetime);
                String beforetime24str=sdf24.format(beforetime24);

                Date aftertime24 = sdf.parse(aftertime);
                String aftertime24str=sdf24.format(aftertime24);

                hrsmin1 = ConstantVariables.CalculateNightHrsMin(beforetime24str, time24str);
                hrsmin2 = ConstantVariables.CalculateNightHrsMin(aftertime24str, time24str);

                long min1 = CalculateTotalmin(hrsmin1);
                long min2 = CalculateTotalmin(hrsmin2);

                if (min1<min2){
                    newshiftid=beforeshift;
                    SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm aa");
                    Date beforetimeout = sdf.parse(beforetime);
                    newouttime=sdf12.format(beforetimeout);

                }else {
                    newshiftid=aftershift;
                    SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm aa");
                    Date aftertimeout = sdf.parse(aftertime);
                    newouttime=sdf12.format(aftertimeout);

                }

                return newshiftid;

            }catch (Exception e){
                e.printStackTrace();
            }
            }
        }else {
            newshiftid=arrshiftoutid.get(0);
        }

        return newshiftid;
    }

    private long CalculateTotalmin(long[] hrsmin) {
        long hrsc = hrsmin[0];
        long minc = hrsmin[1];

        long totalmin;
        if (hrsc == 0) {
            totalmin = minc;
        } else {
            long minfromhrs = hrsc * 60;

            if (minc == 0) {
                totalmin = minfromhrs;
            } else {
                totalmin = minfromhrs + minc;
            }
        }

        return totalmin;
    }


    @Override
    public void Success(Response<ResponseBody> response, int Identifier) {
        switch (Identifier) {
            case GetattendanceConstant:
                ExtractAddAttendanceData(response);
                break;

        }
    }

    @Override
    public void Error(Throwable Response, int Identifier) {

    }

    private void ExtractAddAttendanceData(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());

            if (object.optString("status").equalsIgnoreCase("success")) {

                JSONObject data = new JSONObject(object.optString("data"));
                String photo=data.optString("photo");
                txt_name.setText(data.optString("name"));
                Glide.with(this).load(ConstantValues.BASEURL + photo).into(img_employee);

                if (data.optString("status").equals("1")){
                    lay_back.setBackgroundResource(R.color.green_800);
                }else {
                    lay_back.setBackgroundResource(R.color.red_800);
                }
                txt_msg.setText(object.optString("message"));
            } else {

            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

public void CalculateNightLogs() {

            Cursor cursor1 = db.gettodaysnightpresent(empid,date);

            String nextDate= null,previousDate = null;

            nextDate=ConstantVariables.calculateNextDay(date);
            previousDate=ConstantVariables.calculatePreviousDay(date);

    Cursor cursorn2 = db.gettodayspresentshift(empid,date,time);
    calculateAllLogs(cursorn2,nextDate,previousDate);

    }

    void calculateAllLogs(Cursor cursorn1, String nextDate, String previousDate){


        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date time1 = null,intime1=null;
        try {
            time1 = parseFormat.parse(time);
            intime1 = parseFormat.parse(intime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String time24=date+" "+displayFormat.format(time1);

        String intime24;
        if (time.contains("a") || time.contains("A")){
            if (intime.contains("a") || intime.contains("A")){
                intime24=date+" "+displayFormat.format(intime1);
            }else {
                intime24=previousDate+" "+displayFormat.format(intime1);
            }

        }else {
            if (intime.contains("a") || intime.contains("A")){
                intime24=nextDate+" "+displayFormat.format(intime1);
            }else {
                intime24=date+" "+displayFormat.format(intime1);
            }

        }

        if (!(cursorn1.moveToFirst()) || cursorn1.getCount() ==0){

            String[] arrOfStr = ingrace.split("-", 2);
            String inmin=arrOfStr[1];

            String intimeplus=ConstantVariables.calculateNewTime24(intime,time,Integer.parseInt(inmin),date);


            String arr[]=ConstantVariables.Check_datetime(intime24,time24,intimeplus);

            String msg=arr[0];
            String grace=arr[1];

            boolean issave=false;

            String exitdate="";
            if (time.contains("a") || time.contains("A")){
                exitdate=date;
            }else {
                exitdate=nextDate;
            }

            if (grace.contains("L")){
                String latecount=db.getInTimeLateCount(empid,firstDateOfMonth);

                int latemarkcountint=0;
                int latecountint=0;

                if(!latemarkcount.equals("null"))
                    latemarkcountint=Integer.parseInt(latemarkcount);

                if(!latecount.equals("null"))
                    latecountint=Integer.parseInt(latecount);

                if (latecountint>=latemarkcountint){
                    txt_latemark.setVisibility(View.VISIBLE);
                    txt_latemark.setText("Late Mark Count : "+latecount);
                }else {
                    txt_latemark.setVisibility(View.VISIBLE);
                    txt_latemark.setText("Late Mark Count : "+latecount);
                }
            }

            if (shifttypeid.equals("4")){
                issave=db.addLog(empid,date,exitdate,time,grace,"0","T 0-0","0","T 0-0","","","0","0",empshifttype, "0");
            }else {
                issave=db.addLog(empid,date,exitdate,time,grace,"","","","","","","0","0",empshifttype, "0");
            }

            if (issave){
                MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.login);
                mediaPlayer.start();

                lay_back.setBackgroundResource(R.color.green_800);
                txt_msg.setText("Login Successfully!\n"+msg);
            }

        }else {
            if (cursorn1.moveToFirst()) {
                do {
                    try {

                        entrydate = cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.ENTRYDATE));
                        alin_time = cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.INTIME));
                        allunchout_time = cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.LUNCHOUT));
                        allunchin_time = cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.LUNCHIN));
                        alout_time = cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.OUTTIME));
                        altotal_hours = cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.TOTALHRS));
                        typeno=cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.TYPENO));
                        empshifttype=cursorn1.getString(cursorn1.getColumnIndex(DatabaseHelper.ATTSHIFTID));

                        if (allunchout_time.equals("")) {

                            String[] arrOfStr = lunchoutgrace.split("-", 2);
                            String min = arrOfStr[1];

                            String lunchoutminus = ConstantVariables.calculateNewTime(lunchout, -Integer.parseInt(min));
                            String lunchoutplus = ConstantVariables.calculateNewTime(lunchout, Integer.parseInt(min));

                            try {
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date lunchoutminus1 = parseFormat.parse(lunchoutminus);

                                String lunchoutminus24;
                                if (time.contains("a") || time.contains("A")){
                                    lunchoutminus24=date+" "+displayFormat.format(lunchoutminus1);
                                }else {
                                    lunchoutminus24=nextDate+" "+displayFormat.format(lunchoutminus1);
                                }

                                Date now = sdf.parse(time24);
                                Date begin = sdf.parse(intime24);
                                Date end = sdf.parse(lunchoutminus24);

                                String in_timeplus = ConstantVariables.calculateNewTime(lunchout, -Integer.parseInt(adminingrace));
                                String alin_timeminus = ConstantVariables.calculateNewTime(alin_time, -5);

                                Date alin_timeminus1 = parseFormat.parse(alin_timeminus);
                                Date in_timeplus1 = parseFormat.parse(in_timeplus);
                                Date alin_time1 = parseFormat.parse(alin_time);

                                String alin_timeminus24;

                                String in_timeplus24,alin_time24;
                                if (time.contains("a") || time.contains("A")){
                                    in_timeplus24=date+" "+displayFormat.format(in_timeplus1);
                                    alin_time24=previousDate+" "+displayFormat.format(alin_time1);
                                    alin_timeminus24=previousDate+" "+displayFormat.format(alin_timeminus1);
                                }else {
                                    in_timeplus24=nextDate+" "+displayFormat.format(in_timeplus1);
                                    alin_time24=date+" "+displayFormat.format(alin_time1);
                                    alin_timeminus24=date+" "+displayFormat.format(alin_timeminus1);
                                }


                                Date begin1 = sdf.parse(alin_timeminus24);
                                Date end1 = sdf.parse(in_timeplus24);

                                if (now.after(begin1) && now.before(end1)) {

                                    boolean issave = false;
                                    issave = db.addTempLog(empid, date, time, "1");
                                    if (issave) {
                                        String msg = "You are Already Logged in";
                                        showMessage(msg, "0");
                                    }

                                } else if (now.after(begin) && now.before(end)) {

                                    boolean issave = false;
                                    issave = db.addTempLog(empid, date, time, "1");
                                    if (issave) {
                                        String msg = "You are Already Logged in";
                                        showMessage(msg, "0");
                                    }

                                } else {

                                    long hrsmin[] = ConstantVariables.CalculateNightHrsMin(alin_time24, time24);

                                    long hrsc = hrsmin[0];
                                    long minc = hrsmin[1];

                                    long totalmin;
                                    if (hrsc == 0) {
                                        totalmin = minc;
                                    } else {
                                        long minfromhrs = hrsc * 60;

                                        if (minc == 0) {
                                            totalmin = minfromhrs;
                                        } else {
                                            totalmin = minfromhrs + minc;
                                        }
                                    }

                                    Date lunchout1 = parseFormat.parse(lunchout);
                                    Date lunchoutplus1 = parseFormat.parse(lunchoutplus);

                                    String lunchout24;
                                    String lunchoutplus24;
                                    if (time.contains("a") || time.contains("A")){
                                        lunchout24=date+" "+displayFormat.format(lunchout1);
                                        lunchoutplus24=date+" "+displayFormat.format(lunchoutplus1);
                                    }else {
                                        lunchout24=nextDate+" "+displayFormat.format(lunchout1);
                                        lunchoutplus24=nextDate+" "+displayFormat.format(lunchoutplus1);
                                    }

                                    String arr[] = ConstantVariables.Check_Nighttime(lunchout24, time24, lunchoutplus24);

                                    String msg = arr[0];
                                    String grace = arr[1];

                                    boolean issave = false;
                                    issave = db.updateEMPLunchOut(empid, previousDate, time, grace, String.valueOf(totalmin),empshifttype);
                                    if (issave) {
                                        showMessage(msg, "1");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (allunchin_time.equals("")){

                            String[] arrOfStr = lunchoutgrace.split("-", 2);
                            String min=arrOfStr[1];

                            String lunch_break=ConstantVariables.calculateNewTime(allunchout_time,Integer.parseInt(min));

                            Date lunch_break1 = parseFormat.parse(lunch_break);
                            String lunch_break24=date+" "+displayFormat.format(lunch_break1);

                            String lunch_inplus=ConstantVariables.calculateNewTime(allunchout_time,Integer.parseInt(adminlograce));
                            String lunch_outminus=ConstantVariables.calculateNewTime(allunchout_time,-5);

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date lunch_outminus1 = parseFormat.parse(lunch_outminus);
                                String lunch_outminus24=date+" "+displayFormat.format(lunch_outminus1);

                                Date lunch_inplus1 = parseFormat.parse(lunch_inplus);
                                String lunch_inplus24=date+" "+displayFormat.format(lunch_inplus1);

                                Date now = sdf.parse(time24);
                                Date begin = sdf.parse(lunch_outminus24);
                                Date end = sdf.parse(lunch_inplus24);

                                if (now.after(begin) && now.before(end)){
                                    boolean issave=false;
                                    issave=db.addTempLog(empid,date,time,"3");
                                    if (issave){
                                        String msg="Lunch Out Log Already Exists";
                                        showMessage(msg,"0");
                                    }

                                }else {

                                    String arr[]=ConstantVariables.Check_Nighttime(lunch_break24,time24,lunch_break24);

                                    String msg=arr[0];
                                    String grace=arr[1];

                                    boolean issave=false;
                                    allunchin_time=time;
                                    issave=db.updateEMPLunchIn(empid,previousDate,time,grace, empshifttype);
                                    if (issave){
                                        showMessage(msg,"1");
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else if (alout_time.equals("")){


                            String inplusout,inplusoutdate;

                            if (shifttypeid.equals("4")){

//                                inplusout=ConstantVariables.calculateNewTime(alin_time,Integer.parseInt(adminingrace));

//                                inplusoutdate=ConstantVariables.calculateNewTime24(alin_time,time,Integer.parseInt(adminingrace),date);
                                inplusoutdate=ConstantVariables.calculateNewTime24(alin_time,time,5,date);

//                                Date allunchinminus1 = parseFormat.parse(inplusout);
//
//                                if (time.contains("a") || time.contains("A")){
//                                    inplusoutdate=entrydate+" "+displayFormat.format(allunchinminus1);
//                                }else {
//                                    inplusoutdate=date+" "+displayFormat.format(allunchinminus1);
//                                }

                            }else {

//                                inplusoutdate=ConstantVariables.calculateNewTime24(allunchin_time,time,Integer.parseInt(adminlograce),date);
                                inplusoutdate=ConstantVariables.calculateNewTime24(allunchin_time,time,5,date);

//                                inplusout=ConstantVariables.calculateNewTime(allunchin_time,Integer.parseInt(adminlograce));
//
//                                //allunchinminus=ConstantVariables.calculateNewTime(allunchin_time,-5);
//
//                                Date allunchinminus1 = parseFormat.parse(inplusout);
//                                inplusoutdate=date+" "+displayFormat.format(allunchinminus1);
                            }

                            try {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date now = sdf.parse(time24);
                                 Date end = sdf.parse(inplusoutdate);

                                if (now.before(end)){

                                    Cursor cursortemp = db.getAttendanceTempSameLog(empid, date, time);

                                    if (!(cursortemp.moveToFirst()) || cursortemp.getCount() ==0){

                                        boolean issave=false;
                                        issave=db.addTempLog(empid,date,time,"4");
                                        if (issave){
                                            String msg;
                                            if (shifttypeid.equals("4")){
                                                msg="Already Log In ";
                                            }else {
                                                msg="Lunch In Log Already Exists";
                                            }
                                            showMessage(msg,"0");
                                        }
                                    }else {
                                        String msg;
                                        if (shifttypeid.equals("4")){
                                            msg="Already Log In ";
                                        }else {
                                            msg="Lunch In Log Already Exists";
                                        }
                                        showMessage(msg,"0");
                                    }


                                }else {

                                    long hrsmin[];
                                    if (shifttypeid.equals("4")){
                                        Date alin_time1 = parseFormat.parse(alin_time);
                                        String alin_time24=entrydate+" "+displayFormat.format(alin_time1);

                                        hrsmin=ConstantVariables.CalculateNightHrsMin(alin_time24,time24);
                                    }else {
                                        Date allunchin_time1 = parseFormat.parse(allunchin_time);
                                        String allunchin_time24=date+" "+displayFormat.format(allunchin_time1);

                                        hrsmin=ConstantVariables.CalculateNightHrsMin(allunchin_time24,time24);
                                    }


                                    long hrsc=hrsmin[0];
                                    long minc=hrsmin[1];

                                    long totalmin;
                                    if(hrsc==0){
                                        totalmin=minc;
                                    }else{
                                        long minfromhrs=hrsc*60;

                                        if(minc==0){
                                            totalmin=minfromhrs;
                                        }else{
                                            totalmin=minfromhrs+minc;
                                        }
                                    }

                                    long finalhrs=Integer.parseInt(altotal_hours)+totalmin;

                                    Date outtime1 = parseFormat.parse(outtime);

                                    String outtime124;
                                    if (time.contains("a") || time.contains("A")){
                                        if (outtime.contains("a") || outtime.contains("A")){
                                            outtime124=date+" "+displayFormat.format(outtime1);
                                        }else {
                                            outtime124=date+" "+displayFormat.format(outtime1);
                                        }

                                    }else {
                                        if (outtime.contains("a") || outtime.contains("A")){
                                            outtime124=nextDate+" "+displayFormat.format(outtime1);
                                        }else {
                                            outtime124=date+" "+displayFormat.format(outtime1);
                                        }

                                    }

                                    String[] arrOfStr = outgrace.split("-", 2);
                                    String outmin=arrOfStr[1];
                                    String outtimeplus=ConstantVariables.calculateNewTime24(outtime,time,Integer.parseInt(outmin),date);

                                    String arr[]=ConstantVariables.Check_datetime(outtime124,time24,outtimeplus);

                                    String msg=arr[0];
                                    String grace=arr[1];

                                    boolean issave=false;
                                    int typenoi=Integer.parseInt(typeno);
                                    typenoi++;
                                    issave=db.updateEMPOut(empid,entrydate,time,grace,String.valueOf(finalhrs),date,String.valueOf(typenoi), empshifttype,empshifttype);
                                    if (issave){

                                        MediaPlayer mediaPlayer = MediaPlayer.create(MessageActivity.this, R.raw.logout);
                                        mediaPlayer.start();

                                        showMessage("Log Out Successfully!\n"+msg, "1");

//                                        showMessage(msg,"1");
                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            boolean issave = false;
                            issave = db.addTempLog(empid, date, time, "4");
                            if (issave) {
                                String msg = "You are Logout";
                                showMessage(msg, "0");
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } while (cursorn1.moveToNext());
            }

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
