package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SetDateTimeActivity extends AppCompatActivity {
    TextView txt_datepicker,txt_time;
    Button btn_submit;
    String empid,time,date,single;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date_time);

        txt_datepicker=findViewById(R.id.txt_datepicker);
        txt_time=findViewById(R.id.txt_time);

        btn_submit=findViewById(R.id.btn_submit);

        if (getIntent().getExtras() != null){
            empid = getIntent().getExtras().getString("empid");
            single = getIntent().getExtras().getString("single");
        }

        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;

//        txt_datepicker.setText(year+"-"+(month+1)+"-"+day);

         setdate(year,month,day);

        txt_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(SetDateTimeActivity.this,R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
//                        txt_datepicker.setText(year1+"-"+(month1+1)+"-"+dayOfMonth);
                        setdate(year1,month1,dayOfMonth);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int tHour=calendar.get(Calendar.HOUR_OF_DAY);
                int tMinute=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(SetDateTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format=new SimpleDateFormat("h:mm a"	);
                        String time=format.format(c.getTime());
                        txt_time.setText(time);
                    }
                }, tHour,tMinute,false);
                timePickerDialog.updateTime(tHour,tMinute);
                timePickerDialog.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle pBundle = new Bundle();
                pBundle.putString("empid", empid);
                pBundle.putString("single", single);
                pBundle.putString("time", txt_time.getText().toString());
                pBundle.putString("date", txt_datepicker.getText().toString());
                Intent i=new Intent(SetDateTimeActivity.this, MessageActivity.class);
                i.putExtras(pBundle);

                startActivity(i);
                finish();
            }
        });
    }

    private void setdate(int year, int month, int day) {
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

        txt_datepicker.setText(date);
    }
}