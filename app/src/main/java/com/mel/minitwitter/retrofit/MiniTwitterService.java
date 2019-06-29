package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.pojo.RequestLogin;
import com.mel.minitwitter.pojo.RequestSignup;
import com.mel.minitwitter.pojo.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {
     String URL_BASE="https://www.minitwitter.com:3001/apiv1/";
    @POST("/auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("/auth/signup")
    Call<ResponseAuth> doSignup(@Body RequestSignup requestSignup);
}
