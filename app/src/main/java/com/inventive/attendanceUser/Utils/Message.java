package com.inventive.attendanceUser.Utils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inventive.attendanceUser.NetworkStuff.APIInterface;
import com.inventive.attendanceUser.NetworkStuff.RegiatrationNetworkCaller;
import com.inventive.attendanceUser.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Vrushali on 8/26/2021.
 */
public class Message {
    ImageView img_employee;
    TextView txt_msg,txt_time,txt_name;
    LinearLayout lay_back;
    String empid,time,date,single,newouttime="";

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


    public static void calculateTime(String empId, String single) {

    }
}
