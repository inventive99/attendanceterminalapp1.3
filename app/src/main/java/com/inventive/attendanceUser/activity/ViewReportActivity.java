package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.SessionManager;
import com.inventive.attendanceUser.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewReportActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txt_datepicker;
    Spinner spinner_rjobtype;
    Button bt_go,btn_log;
    LinearLayout lay_summary;
    TextView txt_totalemp,txt_present,txt_notlogin,txt_absent,txt_latemarks,txt_nupresent;

    DatePickerDialog datePickerDialog;
    ProgressDialog progressBar;
    SessionManager sessionManager;
    private DatabaseHelper db;

    AppCompatImageView img_back;
    TextView activity_name;

    CardView card_total,card_present,card_notlogin,card_absent,card_late,card_notuppresent;

    String tdate;
    int year,month;
    String jobtypeid="0";

    private ArrayList<String> job_typename=new ArrayList<>();
    private ArrayList<String> job_typename_id=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        init();
        initVariable();
        getJobType();

        Calendar cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        int dayOfMonth=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;

        dateCalculation(year,month,dayOfMonth);

        spinner_rjobtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index=job_typename.indexOf(spinner_rjobtype.getSelectedItem().toString());
                jobtypeid=job_typename_id.get(index);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private void init() {

        txt_totalemp=findViewById(R.id.txt_totalemp);
        txt_present=findViewById(R.id.txt_present);
        txt_notlogin=findViewById(R.id.txt_notlogin);
        txt_absent=findViewById(R.id.txt_absent);
        txt_latemarks=findViewById(R.id.txt_latemarks);
        txt_nupresent=findViewById(R.id.txt_nupresent);

        txt_datepicker=findViewById(R.id.txt_datepicker);
        bt_go=findViewById(R.id.bt_go);
        bt_go.setOnClickListener(this);
        btn_log=findViewById(R.id.btn_log);
        btn_log.setOnClickListener(this);
        txt_datepicker.setOnClickListener(this);


        card_total=findViewById(R.id.card_total);
        card_total.setOnClickListener(this);
        card_present=findViewById(R.id.card_present);
        card_present.setOnClickListener(this);
        card_notlogin=findViewById(R.id.card_notlogin);
        card_notlogin.setOnClickListener(this);
        card_absent=findViewById(R.id.card_absent);
        card_absent.setOnClickListener(this);
        card_late=findViewById(R.id.card_late);
        card_late.setOnClickListener(this);
        card_notuppresent=findViewById(R.id.card_notuppresent);
        card_notuppresent.setOnClickListener(this);

        lay_summary=findViewById(R.id.lay_summary);

        spinner_rjobtype=findViewById(R.id.spinner_rjobtype);

        img_back = findViewById(R.id.img_back);
        activity_name = findViewById(R.id.activity_name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                Intent i = new Intent(ViewReportActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        activity_name.setText("Daily Summary");
    }

    private void initVariable() {
        sessionManager = new SessionManager(ViewReportActivity.this);
        db = new DatabaseHelper(this);

    }

    private void getJobType(){
        job_typename.clear();
        job_typename_id.clear();

        job_typename.add("All");
        job_typename_id.add("0");

        Cursor cursor = db.getJobtype();
        if (cursor.moveToFirst()) {
            do {
                job_typename_id.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOBTYPEID)));
                job_typename.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOBTYPENAME)));
            } while (cursor.moveToNext());
        }

        setNonEmptyJobTypeList();
    }

    private void setNonEmptyJobTypeList() {
        ArrayAdapter jobAdapter = new ArrayAdapter(ViewReportActivity.this,android.R.layout.simple_spinner_item,job_typename);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rjobtype.setAdapter(jobAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txt_datepicker:
                datepicker();
                break;
            case R.id.bt_go:
                validate("summary","","","","","");
                break;
            case R.id.card_total:
                validate("total","","","","","");
                break;
            case R.id.card_present:
                if(!txt_present.getText().toString().equals("0")){
                    validate("","present","","","","");
                }else {
                    Toast.makeText(this, "No Record Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.card_notuppresent:
                if(!txt_nupresent.getText().toString().equals("0")){
                    validate("","nupresent","","","","");
                }else {
                    Toast.makeText(this, "No Record Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.card_notlogin:
                if(!txt_notlogin.getText().toString().equals("0")){
                    validate("","","notlogin","","","");
                }else {
                    Toast.makeText(this, "No Record Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.card_absent:
                if(!txt_absent.getText().toString().equals("0")){
                    validate("","","","absent","","");
                }else {
                    Toast.makeText(this, "No Record Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.card_late:
                if(!txt_latemarks.getText().toString().equals("0")){
                    validate("","","","","late","");
                }else {
                    Toast.makeText(this, "No Record Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_log:
                validate("","","","","","all");

//                Intent i=new Intent(getApplicationContext(),DailyReport.class);
//                startActivity(i);
                break;

        }
    }

    private void validate(String summary,String present, String notlogin, String absent, String late, String all) {

        if (summary.equals("summary")){
            progressBar = new ProgressDialog(ViewReportActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Please Wait Loading ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
            getDailySummary();
            lay_summary.setVisibility(View.VISIBLE);

        }else if (summary.equals("total")) {
            Intent i=new Intent(getApplicationContext(),EmployeeList.class);
            i.putExtra("active",summary);
            startActivity(i);

        }else {
            Bundle extras=new Bundle();
            extras.putString("present",present);
            extras.putString("notlogin",notlogin);
            extras.putString("absent",absent);
            extras.putString("late",late);
            extras.putString("all",all);
            extras.putString("jobtype",jobtypeid);
            extras.putString("date",txt_datepicker.getText().toString());

            Intent i=new Intent(getApplicationContext(),DailyReportActivity.class);
            i.putExtras(extras);
            startActivity(i);
        }

    }

    private void getDailySummary() {



        String wherejob="";
        if(jobtypeid.equals("0")){
            wherejob="";
        }else{
            wherejob=" AND B.jobtype='"+jobtypeid+"'";
        }

        Cursor cursor = db.getEmployeeInfo();
        int totalemp=cursor.getCount();
        txt_totalemp.setText(String.valueOf(totalemp));

        Cursor cursor1 = db.getPresentEmployee(tdate,wherejob);
        int presentemp=cursor1.getCount();
        txt_present.setText(String.valueOf(presentemp));

        Cursor cursor5 = db.getnuPresentEmployee(tdate,wherejob);
        int nupresentemp=cursor5.getCount();
        txt_nupresent.setText(String.valueOf(nupresentemp));

        Cursor cursor2 = db.getNotLoginEmployee(tdate,wherejob);
        int notlogin=cursor2.getCount();
        txt_notlogin.setText(String.valueOf(notlogin));

        Cursor cursor3 = db.getAbEmployee(tdate,wherejob);
        int absent=cursor3.getCount();
        txt_absent.setText(String.valueOf(absent));

        Cursor cursor4 = db.getLateEmployee(tdate,wherejob);
        int late=cursor4.getCount();
        txt_latemarks.setText(String.valueOf(late));

        progressBar.dismiss();
    }

    public void datepicker(){
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int dayOfMonth=cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog=new DatePickerDialog(ViewReportActivity.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateCalculation(year,month,dayOfMonth);

//                txt_datepicker.setText(dayOfMonth+"-"+(month+1)+"-"+year);
//                tdate=year+"-"+(month+1)+"-"+dayOfMonth;
            }
        },year,month,dayOfMonth);
        datePickerDialog.show();
        //datePickerDialog.getWindow().setLayout(600,800);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}