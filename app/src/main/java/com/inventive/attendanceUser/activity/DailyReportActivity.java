package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.SessionManager;
import com.inventive.attendanceUser.adapter.dailyreportAdapter;
import com.inventive.attendanceUser.database.DatabaseHelper;
import com.inventive.attendanceUser.model.dailyreportmodel;

import java.util.ArrayList;
import java.util.Calendar;

public class DailyReportActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txt_datepicker;
    DatePickerDialog datePickerDialog;
    ProgressDialog progressBar;
    Button txt_rpdf;
    String tdate;

    SessionManager sessionManager;
    TableLayout tableLayout;
    TextView txt_record;

    AppCompatImageView img_back;
    TextView activity_name;

    LinearLayoutManager layoutManager;
    dailyreportAdapter adapter;
    RecyclerView rcv_dailyreport;
    private DatabaseHelper db;

    String present,notlogin,absent,late,all,jobtype,bdate;

    private final int GetdailyreportConstant=1001;

    ArrayList<dailyreportmodel> reportModels=new ArrayList();

    String id="";
    int year,month;
    String monthname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        init();
        initVariable();

        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            present=bundle.getString("present");
            notlogin=bundle.getString("notlogin");
            absent=bundle.getString("absent");
            late=bundle.getString("late");
            all=bundle.getString("all");
            jobtype=bundle.getString("jobtype");
            bdate=bundle.getString("date");

        }

        Calendar cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        int dayOfMonth=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;


        txt_datepicker.setText(dayOfMonth+"-"+(month+1)+"-"+year);
        txt_datepicker.setText(bdate);
        tdate=year+"-"+(month+1)+"-"+dayOfMonth;

        getDailyreport(bdate);


    }

    private void init() {

        txt_datepicker=findViewById(R.id.txt_datepicker);
        txt_record=findViewById(R.id.txt_record);
        txt_datepicker.setOnClickListener(this);
        rcv_dailyreport=findViewById(R.id.rcv_dailyreport);

        img_back = findViewById(R.id.img_back);
        activity_name = findViewById(R.id.activity_name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activity_name.setText("Daily Report");
    }

    private void initVariable() {

        sessionManager = new SessionManager(DailyReportActivity.this);
        db = new DatabaseHelper(this);

    }

    public void datepicker(){
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int dayOfMonth=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog=new DatePickerDialog(DailyReportActivity.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dateCalculation(year,month,dayOfMonth);

//                txt_datepicker.setText(dayOfMonth+"-"+(month+1)+"-"+year);
//                tdate=year+"-"+(month+1)+"-"+dayOfMonth;

                getDailyreport(tdate);

            }
        },year,month,dayOfMonth);
        datePickerDialog.show();
        //datePickerDialog.getWindow().setLayout(600,800);

    }

    private void dateCalculation(int year, int month, int dayOfMonth) {
        month=month + 1;
        String monthstr="",daystr="";
        if(month<10)
        {
            monthstr="0"+month;
        }else {
            monthstr=String.valueOf(month);
        }

        if(dayOfMonth<10)
        {
            daystr="0"+dayOfMonth;
        }else {
            daystr=String.valueOf(dayOfMonth);
        }

        tdate=year + "-"+ monthstr + "-" + daystr;
        txt_datepicker.setText(tdate);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txt_datepicker:
                datepicker();
                break;
            case R.id.bt_go:
                getDailyreport(txt_datepicker.getText().toString());
                break;

        }
    }

    private void getDailyreport(String date){
        String wherejob="";
        if(jobtype.equals("0")){
            wherejob="";
        }else{
            wherejob=" AND B.jobtype='"+jobtype+"'";
        }

        reportModels = db.getAllAttendanceLog(present,notlogin,absent,late,all,jobtype,date,wherejob);

        if (reportModels.size()==0){
            txt_record.setVisibility(View.VISIBLE);
            rcv_dailyreport.setVisibility(View.GONE);
        }else {
            txt_record.setVisibility(View.GONE);
            rcv_dailyreport.setVisibility(View.VISIBLE);
        }

        layoutManager=new LinearLayoutManager(DailyReportActivity.this,RecyclerView.VERTICAL,false);
        rcv_dailyreport.setLayoutManager(layoutManager);
        adapter=new dailyreportAdapter(DailyReportActivity.this,reportModels,this);
        rcv_dailyreport.setAdapter(adapter);

    }


}