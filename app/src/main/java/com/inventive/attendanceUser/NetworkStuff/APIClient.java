package com.inventive.attendanceUser.NetworkStuff;

import com.inventive.attendanceUser.Utils.ConstantValues;
import com.inventive.attendanceUser.Utils.ConstantVariables;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {


    private static Retrofit retrofit = null;

    public static Retrofit StringClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(ConstantVariables.OKHTTPVariables.MaxTimeOutInMins,
                        ConstantVariables.OKHTTPVariables.ConnectionTimeOutUnit)
                .retryOnConnectionFailure(true)
                .writeTimeout(ConstantVariables.OKHTTPVariables.MaxTimeOutInMins, ConstantVariables.OKHTTPVariables.ConnectionTimeOutUnit)
                .retryOnConnectionFailure(true).callTimeout(ConstantVariables.OKHTTPVariables.MaxTimeOutInMins, ConstantVariables.OKHTTPVariables.ConnectionTimeOutUnit)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantValues.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();

        return retrofit;
    }

    public static com.inventive.attendanceUser.NetworkStuff.APIInterface getAPiInstance() {
        return StringClient().create(com.inventive.attendanceUser.NetworkStuff.APIInterface.class);
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantValues.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
