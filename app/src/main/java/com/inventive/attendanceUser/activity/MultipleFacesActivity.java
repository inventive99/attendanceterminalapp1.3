package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inventive.attendanceUser.MainActivity;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.database.DatabaseHelper;

import java.util.Calendar;

public class MultipleFacesActivity extends AppCompatActivity {

    String empid,time,date,single;
    TextView txt_msg,txt_name,txt_time;
    LinearLayout lay_back;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_faces);

        txt_msg=findViewById(R.id.txt_msg);
        lay_back=findViewById(R.id.lay_back);
        txt_name=findViewById(R.id.txt_name);
        txt_time=findViewById(R.id.txt_time);

        String delegate = "hh:mm aaa";
        time= (String) DateFormat.format(delegate, Calendar.getInstance().getTime());

        txt_time.setText(time);

        if (getIntent().getExtras() != null){
            empid = getIntent().getExtras().getString("empid");
            single = getIntent().getExtras().getString("single");
            //  time = getIntent().getExtras().getString("time");
            //  date = getIntent().getExtras().getString("date");

        }
        db = new DatabaseHelper(MultipleFacesActivity.this);

        if (empid.equals("0")){

            txt_name.setVisibility(View.GONE);
            String msg = "Multiple Faces Detected\nPlease Try Again!";
            showMessage(msg, "2");

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String visit = ConstantValues.getSingleVisit(MultipleFacesActivity.this);

                if (visit.equals("1")) {
                    ConstantValues.putSingleVisit("0","0",MultipleFacesActivity.this);

                    Intent i = new Intent(MultipleFacesActivity.this, MainStableScreenActivity.class);
                    startActivity(i);
                    db.close();
                    finish();

                    System.exit(0);

                }else {

                    Intent i = new Intent(MultipleFacesActivity.this, MainActivity.class);
                    startActivity(i);
                    db.close();
                    finish();

                    System.exit(0);
                }

//                setResult(REQUEST_LOCATION);

            }
        },3000);




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
}