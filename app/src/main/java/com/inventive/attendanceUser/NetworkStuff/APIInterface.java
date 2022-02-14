package com.inventive.attendanceUser.NetworkStuff;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("getEmployee.php")
    Call<ResponseBody> getEmployee(@Body JsonObject request_data);

    @POST("downloadAttendanceLog.php")
    Call<ResponseBody> downloadAttendanceLog(@Body JsonObject request_data);

    @POST("addEmployeeImage.php")
    Call<ResponseBody> addEmployeeImage(@Body JsonObject request_data);

    @POST("getEmployeeImages.php")
    Call<ResponseBody> getEmployeeImages(@Body JsonObject request_data);

    @POST("getUpdatedEmpImages.php")
    Call<ResponseBody> getUpdatedEmpImages(@Body JsonObject request_data);

    @POST("AddAttendanceLog.php")
    Call<ResponseBody> AddAttendanceLog(@Body JsonObject request_data);

    @POST("getJobType.php")
    Call<ResponseBody> getJobType(@Body JsonObject request_data);

    @POST("getDesignation.php")
    Call<ResponseBody> getDesignation(@Body JsonObject request_data);

    @POST("getShift.php")
    Call<ResponseBody> getShift(@Body JsonObject request_data);

    @POST("UpdateAttendanceLogs.php")
    Call<ResponseBody> UpdateAttendanceLogs(@Body JsonObject request_data);

    @POST("UpdateAttendanceTempLogs.php")
    Call<ResponseBody> UpdateAttendanceTempLogs(@Body JsonObject request_data);

    @POST("getShiftType.php")
    Call<ResponseBody> getShiftType(@Body JsonObject request_data);

    @POST("getHolidays.php")
    Call<ResponseBody> getHolidays(@Body JsonObject request_data);

    @POST("addDevice.php")
    Call<ResponseBody> addDevice(@Body JsonObject request_data);

    @POST("UpdateLocation.php")
    Call<ResponseBody> UpdateLocation(@Body JsonObject request_data);

    @POST("ValidateLicenseKey.php")
    Call<ResponseBody> ValidateLicenseKey(@Body JsonObject request_data);



    @POST("getStoreInfo.php")
    Call<ResponseBody> getStoreInfo(@Body JsonObject request_data);

    @POST("updateEmpFace.php")
    Call<ResponseBody> updateEmpFace(@Body JsonObject request_data);

    @POST("updateSqliteStatus.php")
    Call<ResponseBody> updateSqliteStatus(@Body JsonObject request_data);

}
