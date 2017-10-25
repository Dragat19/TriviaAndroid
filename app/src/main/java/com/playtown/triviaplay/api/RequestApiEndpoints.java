package com.playtown.triviaplay.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RequestApiEndpoints {

    @GET("public//?action=log_sms")
    Observable<ResponseBody> exampleCall(@Query("param1") String param1, @Query("param2") String param2);

   /* //Consulta de Login por Email
    @GET("2/?action=login")
    Observable<LoginResponse> login(@Query("email") String param1, @Query("password") String param2);

    //Consulta de Registro de Usuario
    @GET("2/?action=register")
    Observable<LoginResponse> register(@Query("first_name") String firstName, @Query("last_name") String lastName, @Query("email") String email, @Query("username") String user, @Query("password") String pass);

    //Consulta de Verificacion de Usuario
    @GET("2/?action=verify_user")
    Observable<LoginResponse> verify_user(@Query("username") String param1, @Query("email") String param2);*/
}