package com.inventive.attendanceUser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventive.attendanceUser.R;
import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.model.model;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
    ArrayList<model> data;
    Context context;
    RecyclerView rv_emp;
    ItemClick listItemClick;

    public myAdapter(Context context, ArrayList<model> data) {
        this.data = data;
        this.context=context;
        this.listItemClick = listItemClick;
    }

    public interface ItemClick {
        //void SubCategoryItemClick(int position, String name, String delete, String category,String image);

        void ItemClick(String models);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singlerow,parent,false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        model listModel = data.get(position);

        holder.t1.setText(listModel.getName());
        holder.t2.setText(listModel.getMobno());
        holder.t3.setText(listModel.getAddress());
//        holder.img.setImageResource(listModel.getImage());

        if(listModel.getEmp_thumb() != null && !listModel.getEmp_thumb().isEmpty()) {
            Glide.with(context).load(listModel.getEmp_thumb()).into(holder.img);
        }

//        holder.lay_cust.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listItemClick.ItemClick(listModel.getId());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateList(ArrayList<model> list){
        data = list;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView t1,t2,t3;
        LinearLayout lay_cust;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img_employee);

            t1=itemView.findViewById(R.id.txt_name);
            t2=itemView.findViewById(R.id.txt_email);
            t3=itemView.findViewById(R.id.txt_number);

            lay_cust=itemView.findViewById(R.id.lay_cust);
        }
    }

}
