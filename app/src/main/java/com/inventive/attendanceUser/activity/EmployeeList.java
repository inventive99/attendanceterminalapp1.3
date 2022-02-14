package com.inventive.attendanceUser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.SessionManager;
import com.inventive.attendanceUser.adapter.myAdapter;
import com.inventive.attendanceUser.database.DatabaseHelper;
import com.inventive.attendanceUser.model.dailyreportmodel;
import com.inventive.attendanceUser.model.model;

import java.util.ArrayList;

public class EmployeeList extends AppCompatActivity {
    RecyclerView rv_emp;
    TextView txt_record;
    myAdapter adapter;
    ArrayList<model> models = new ArrayList<>();
    LinearLayoutManager layoutManager;

    AppCompatImageView img_back;
    TextView activity_name;

    EditText et_search;
    ImageButton bt_close;

    SessionManager sessionManager;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        init();
        initVariable();
        getemployee();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input

                filter(s.toString());

            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.getText().clear();

            }
        });
    }

    void filter(String text){

        ArrayList<model> temp = new ArrayList();
        for(model d: models){

            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }else if(d.getMobno().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }else if(d.getAddress().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }


        }

            adapter.updateList( temp);

    }

    private void init() {
        rv_emp=findViewById(R.id.rv_emp);
        txt_record=findViewById(R.id.txt_record);

        et_search = findViewById(R.id.et_search);
        bt_close = findViewById(R.id.bt_close);

        img_back = findViewById(R.id.img_back);
        activity_name = findViewById(R.id.activity_name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activity_name.setText("Employee List");
    }

    private void initVariable() {
        sessionManager = new SessionManager<>(EmployeeList.this);
        db = new DatabaseHelper(this);

    }

    private void getemployee() {

        Cursor cursor = db.getEmployeeInfo();

        try{
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    model modellist = new model();

                    modellist.setId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMPID)));
                    modellist.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ENAME)));
                    modellist.setAddress(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS)));
                    modellist.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMAIL)));
                    modellist.setEmp_thumb(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHOTO)));
                    modellist.setMobno(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOBNO)));

                    models.add(modellist);
                } while (cursor.moveToNext());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (models.size()==0){
            txt_record.setVisibility(View.VISIBLE);
            rv_emp.setVisibility(View.GONE);
        }else {
            txt_record.setVisibility(View.GONE);
            rv_emp.setVisibility(View.VISIBLE);
        }
        layoutManager = new LinearLayoutManager(EmployeeList.this, RecyclerView.VERTICAL, false);
        rv_emp.setLayoutManager(layoutManager);
        adapter = new myAdapter(EmployeeList.this,models);
        rv_emp.setAdapter(adapter);
    }
}