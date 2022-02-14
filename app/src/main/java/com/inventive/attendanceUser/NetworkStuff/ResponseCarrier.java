package com.inventive.attendanceUser.NetworkStuff;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface ResponseCarrier
{
	void Success(Response<ResponseBody> response, int Identifier);
	void Error(Throwable Response, int Identifier);
}
