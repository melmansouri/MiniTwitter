package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.retrofit.request.RequestLogin;
import com.mel.minitwitter.retrofit.request.RequestSignup;
import com.mel.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {
    @POST("auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("auth/signup")
    Call<ResponseAuth> doSignup(@Body RequestSignup requestSignup);
}
