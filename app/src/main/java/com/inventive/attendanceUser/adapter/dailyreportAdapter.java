package com.inventive.attendanceUser.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.activity.DailyReportActivity;
import com.inventive.attendanceUser.model.dailyreportmodel;

import java.util.ArrayList;

public class dailyreportAdapter extends RecyclerView.Adapter<dailyreportAdapter.dailyreportViewHolder> {
    ArrayList<dailyreportmodel> data;
    Context context;
    RecyclerView rcv_dailyreport;
    int count=0;

    public dailyreportAdapter(Context context, ArrayList<dailyreportmodel> data, DailyReportActivity itemclick){
        this.data=data;
        this.context=context;

    }

    @NonNull
    @Override
    public dailyreportAdapter.dailyreportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.single_row_dailyreport,parent,false);
        return new dailyreportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dailyreportAdapter.dailyreportViewHolder holder, int position) {
        dailyreportmodel listmodel=data.get(position);
        holder.txtrcv_name.setText(listmodel.getEmpname());
        holder.txtrcv_date.setText(listmodel.getId()+"---"+listmodel.getEmpid());



        //Late mark condition on In time

        String Intime=listmodel.getIn_time();
        if(!Intime.isEmpty()){
            String intimestr=Intime.replace("L","");
            holder.txtrcv_intime.setText(intimestr);

            if(Intime.contains("L")){

                holder.txt_Ltextin.setVisibility(View.VISIBLE);
            }else{
                holder.txt_Ltextin.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.txt_Ltextin.setVisibility(View.INVISIBLE);
            holder.txtrcv_intime.setText(Intime);
        }


        //Late mark condition on lunch start time
        String Lunchstarttime=listmodel.getLunchout_time();
        if(!Lunchstarttime.isEmpty()){
            String LunchStart=Lunchstarttime.replace("L","");
            holder.txtrcv_lunchstarttime.setText(LunchStart);

            if(Lunchstarttime.contains("L")){
                holder.txt_LtextLunchstart.setVisibility(View.VISIBLE);
            }else{
                holder.txt_LtextLunchstart.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.txt_LtextLunchstart.setVisibility(View.INVISIBLE);
            holder.txtrcv_lunchstarttime.setText(Lunchstarttime);
        }


        //Late mark condition on Lunch end time
        String Lunchendtime=listmodel.getLunchin_time();
        if(!Lunchendtime.isEmpty()){
            String lunchinstr=Lunchendtime.replace("L","");
            holder.txtrcv_lunchendtime.setText(lunchinstr);
            if(Lunchendtime.contains("L")){
                holder.txt_LtextLunchend.setVisibility(View.VISIBLE);
            }else{
                holder.txt_LtextLunchend.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.txt_LtextLunchend.setVisibility(View.INVISIBLE);
            holder.txtrcv_lunchendtime.setText(Lunchendtime);
        }


        //Late mark condition on Lunch in time
        String outtime=listmodel.getOut_time();
        if(!outtime.isEmpty()){

            String outstr=outtime.replace("L","");
            holder.txtrcv_outime.setText(outstr);
            if(outtime.contains("L")){
                holder.txt_Ltextout.setVisibility(View.VISIBLE);
            }else{
                holder.txt_Ltextout.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.txt_Ltextout.setVisibility(View.INVISIBLE);
            holder.txtrcv_outime.setText(outtime);
        }
        holder.txtrcv_totalhrstime.setText(listmodel.getTotal_hours());
        holder.txt_shifttimercv.setText(listmodel.getShift_time());

        //set employee image
        if(listmodel.getImage() != null && !listmodel.getImage().equals("")) {
            Glide.with(context).load(listmodel.getImage()).into(holder.img_rcvprofile);
        }

//        if (!listmodel.getImage().contains("noimage")){
//            Glide.with(context).load(listmodel.getImage()).into(holder.img_rcvprofile);
//        }

        if (listmodel.getUpload_status().equals("0")){
            holder.img_upload.setImageResource(R.drawable.ic_baseline_high_24);
        }else {
            holder.img_upload.setImageResource(R.drawable.ic_baseline_low_1_24);
        }

        if (Intime.equals("Absent")){
            holder.lay_in.setVisibility(View.GONE);
            holder.lay_lunch.setVisibility(View.GONE);
            holder.lay_total.setVisibility(View.GONE);
            holder.txt_absent.setVisibility(View.VISIBLE);
        }else if (Intime.equals("notlogin")){
            holder.lay_in.setVisibility(View.GONE);
            holder.lay_lunch.setVisibility(View.GONE);
            holder.lay_total.setVisibility(View.GONE);
            holder.txt_absent.setVisibility(View.VISIBLE);
            holder.txt_absent.setText("Not Logged In");
            holder.img_upload.setVisibility(View.GONE);
        }else {
            holder.lay_in.setVisibility(View.VISIBLE);
            holder.lay_lunch.setVisibility(View.VISIBLE);
            holder.lay_total.setVisibility(View.VISIBLE);
            holder.txt_absent.setVisibility(View.GONE);
        }
//        Toast.makeText(context, listmodel.getLunchout_time(), Toast.LENGTH_SHORT).show();
        if (listmodel.getLunchout_time().equals("0") || listmodel.getLunchout_time().equals("")){
            holder.lay_lunch.setVisibility(View.GONE);
        }else{
            holder.lay_lunch.setVisibility(View.VISIBLE);
        }

//        holder.lay_emp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemclick.ItemClick(listmodel);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class dailyreportViewHolder extends RecyclerView.ViewHolder {

        TextView txt_jobtypetrcv,txtrcv_name,txtrcv_intime,txtrcv_lunchstarttime,txtrcv_lunchendtime,txtrcv_outime,txtrcv_totalhrstime,
                txt_shifttimercv,txt_Ltextin,txt_LtextLunchend,txt_LtextLunchstart,txt_Ltextout,txt_absent,txtrcv_date;
        ImageView img_rcvprofile,img_upload;
        LinearLayout lay_lunch,lay_total,lay_in,lay_emp;


        public dailyreportViewHolder(@NonNull View itemView) {
            super(itemView);
            txtrcv_name=itemView.findViewById(R.id.txtrcv_name);
            txtrcv_intime=itemView.findViewById(R.id.txtrcv_intime);
            txtrcv_lunchstarttime=itemView.findViewById(R.id.txtrcv_lunchstarttime);
            txtrcv_lunchendtime=itemView.findViewById(R.id.txtrcv_lunchendtime);
            txtrcv_outime=itemView.findViewById(R.id.txtrcv_outime);
            txtrcv_totalhrstime=itemView.findViewById(R.id.txtrcv_totalhrstime);
            txtrcv_date=itemView.findViewById(R.id.txtrcv_date);
//            txt_jobtypetrcv=itemView.findViewById(R.id.txt_jobtypetrcv);
            txt_shifttimercv=itemView.findViewById(R.id.txt_shifttimercv);
            img_rcvprofile=itemView.findViewById(R.id.img_rcvprofile);
            txt_Ltextin=itemView.findViewById(R.id.txt_Ltextin);
            txt_LtextLunchend=itemView.findViewById(R.id.txt_LtextLunchend);
            txt_LtextLunchstart=itemView.findViewById(R.id.txt_LtextLunchstart);
            txt_Ltextout=itemView.findViewById(R.id.txt_Ltextout);
            txt_absent=itemView.findViewById(R.id.txt_absent);

            img_upload=itemView.findViewById(R.id.img_upload);

            lay_lunch=itemView.findViewById(R.id.lay_lunch);
            lay_total=itemView.findViewById(R.id.lay_total);
            lay_in=itemView.findViewById(R.id.lay_in);
            lay_emp=itemView.findViewById(R.id.lay_emp);

        }
    }
}
