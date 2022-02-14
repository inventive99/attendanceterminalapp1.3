package com.inventive.attendanceUser.NetworkStuff;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.inventive.attendanceUser.Utils.ProgressBar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegiatrationNetworkCaller {
    String TAG = "NetworkCaller";
    private Context context;
    private com.inventive.attendanceUser.NetworkStuff.ResponseCarrier carrier;
    private RegiatrationNetworkCaller RetroFetcher;

    public RegiatrationNetworkCaller(Context context, com.inventive.attendanceUser.NetworkStuff.ResponseCarrier carrier) {
        this.context = context;
        this.carrier = carrier;
    }

    public void getEmployee(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.getEmployee(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void downloadAttendanceLog(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.downloadAttendanceLog(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void addDevice(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.addDevice(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void ValidateLicenseKey(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.ValidateLicenseKey(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void getEmployeeImages(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.getEmployeeImages(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void getUpdatedEmpImages(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.getUpdatedEmpImages(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void addEmployeeImage(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.addEmployeeImage(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void AddAttendanceLog(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.AddAttendanceLog(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void getStoreInfo(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.getStoreInfo(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void updateEmpFace(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.updateEmpFace(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void updateSqliteStatus(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.updateSqliteStatus(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void getShift(APIInterface retroFetcher, final int getshiftConstant, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody>caller=retroFetcher.getShift(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,"onResponse:response" + response.body());
                carrier.Success(response,getshiftConstant);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t,getshiftConstant);
                ProgressBar.hideProgress();

            }
        });
    }

    public void getShiftType(APIInterface retroFetcher, final int getshiftConstant, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody>caller=retroFetcher.getShiftType(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,"onResponse:response" + response.body());
                carrier.Success(response,getshiftConstant);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t,getshiftConstant);
                ProgressBar.hideProgress();

            }
        });
    }

    public void getDesignation(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.getDesignation(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success (response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void getJobType(APIInterface retroFetcher, final int getjobTypeConstant, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody>caller=retroFetcher.getJobType(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,"onResponse:response" + response.body());
                carrier.Success(response,getjobTypeConstant);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t,getjobTypeConstant);
                ProgressBar.hideProgress();

            }
        });
    }

    public void UpdateAttendanceLogs(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.UpdateAttendanceLogs(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void UpdateLocation(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.UpdateLocation(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void UpdateAttendanceTempLogs(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.UpdateAttendanceTempLogs(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }

    public void getHolidays(APIInterface RetroFetcher, final int RequestIdentifier, JsonObject request) {
        ProgressBar.showProgress(context);
        Call<ResponseBody> caller = RetroFetcher.getHolidays(request);
        caller.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse:response " + response.body());
                carrier.Success(response, RequestIdentifier);
                ProgressBar.hideProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                carrier.Error(t, RequestIdentifier);
                ProgressBar.hideProgress();
            }
        });
    }


}

