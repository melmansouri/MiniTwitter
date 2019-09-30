package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.retrofit.request.RequestCreateTweet;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthTwitterService {
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweet();

    @POST("tweets/create")
    Call<Tweet> addTweet(@Body RequestCreateTweet requestCreateTweet);

    @POST("tweets/like/{id}")
    Call<Tweet> likeTweet(@Path("id") int id);

    @GET("tweets/favs")
    Call<Tweet> favTweets();
}
