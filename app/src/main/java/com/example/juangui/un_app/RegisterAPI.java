package com.example.juangui.un_app;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Belal on 11/5/2015.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/academia/inicio/do-login")
    public void logear(
            @Field("nombre") String username,
            @Field("password") String password,
            Callback<Response> callback);
}