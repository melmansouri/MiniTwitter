package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthTwitterService {
    @GET("tweets/all")
    Call<List<Tweet>> getAllTweet();
}
