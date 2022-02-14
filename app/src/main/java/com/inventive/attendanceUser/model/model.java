package com.inventive.attendanceUser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class model {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("registerdate")
    @Expose
    private String registerdate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobno")
    @Expose
    private String mobno;
    @SerializedName("emp_thumb")
    @Expose
    private String emp_thumb;
    @SerializedName("emp_original")
    @Expose
    private String emp_original;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("shift_type")
    @Expose
    private String shift_type;
    @SerializedName("job_type")
    @Expose
    private String job_type;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("designation_id")
    @Expose
    private String designation_id;

    @SerializedName("shift_type_id")
    @Expose
    private String shift_type_id;
    @SerializedName("job_type_id")
    @Expose
    private String job_type_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(String registerdate) {
        this.registerdate = registerdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getEmp_thumb() {
        return emp_thumb;
    }

    public void setEmp_thumb(String emp_thumb) {
        this.emp_thumb = emp_thumb;
    }

    public String getEmp_original() {
        return emp_original;
    }

    public void setEmp_original(String emp_original) {
        this.emp_original = emp_original;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getShift_type() {
        return shift_type;
    }

    public void setShift_type(String shift_type) {
        this.shift_type = shift_type;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(String designation_id) {
        this.designation_id = designation_id;
    }

    public String getShift_type_id() {
        return shift_type_id;
    }

    public void setShift_type_id(String shift_type_id) {
        this.shift_type_id = shift_type_id;
    }

    public String getJob_type_id() {
        return job_type_id;
    }

    public void setJob_type_id(String job_type_id) {
        this.job_type_id = job_type_id;
    }
}
