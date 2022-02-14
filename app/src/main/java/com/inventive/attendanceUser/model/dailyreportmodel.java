package com.inventive.attendanceUser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class dailyreportmodel {
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("empid")
    @Expose
    private String empid;
    @SerializedName("empname")
    @Expose
    private String empname;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("shift_type")
    @Expose
    private String shift_type;

    @SerializedName("shift_time")
    @Expose
    private String shift_time;

    @SerializedName("losttime")
    @Expose
    private String losttime;

    @SerializedName("extratime")
    @Expose
    private String extratime;


    @SerializedName("entrydate")
    @Expose
    private String entrydate;

    @SerializedName("in_time")
    @Expose
    private String in_time;

    @SerializedName("lunchout_time")
    @Expose
    private String lunchout_time;

    @SerializedName("lunchin_time")
    @Expose
    private String lunchin_time;


    @SerializedName("out_time")
    @Expose
    private String out_time;

    @SerializedName("total_hours")
    @Expose
    private String total_hours;

    @SerializedName("job_typename")
    @Expose
    private String job_typename;
    private String upload_status;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }


//getter and setter

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getLunchout_time() {
        return lunchout_time;
    }

    public void setLunchout_time(String lunchout_time) {
        this.lunchout_time = lunchout_time;
    }

    public String getLunchin_time() {
        return lunchin_time;
    }

    public void setLunchin_time(String lunchin_time) {
        this.lunchin_time = lunchin_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public String getEntrydate() {
        return entrydate;
    }

    public void setEntrydate(String entrydate) {
        this.entrydate = entrydate;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShift_type() {
        return shift_type;
    }

    public void setShift_type(String shift_type) {
        this.shift_type = shift_type;
    }

    public String getShift_time() {
        return shift_time;
    }

    public void setShift_time(String shift_time) {
        this.shift_time = shift_time;
    }

    public String getLosttime() {
        return losttime;
    }

    public void setLosttime(String losttime) {
        this.losttime = losttime;
    }

    public String getExtratime() {
        return extratime;
    }

    public void setExtratime(String extratime) {
        this.extratime = extratime;
    }

    public String getJob_typename() {
        return job_typename;
    }

    public void setJob_typename(String job_typename) {
        this.job_typename = job_typename;
    }

    public String getUpload_status() {
        return upload_status;
    }

    public void setUpload_status(String upload_status) {
        this.upload_status = upload_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
