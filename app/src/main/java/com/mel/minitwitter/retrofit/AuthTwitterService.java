package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.retrofit.request.RequesUpdateUser;
import com.mel.minitwitter.retrofit.request.RequestCreateTweet;
import com.mel.minitwitter.retrofit.response.ResponseDelete;
import com.mel.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.mel.minitwitter.retrofit.response.Tweet;
import com.mel.minitwitter.retrofit.response.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @DELETE("tweets/{idTweet}")
    Call<ResponseDelete> deleteTweet(@Path("idTweet") int idTweet);

    @GET("users/profile")
    Call<User> getProfileUser();

    @PUT("users/profile")
    Call<User> updateUser(@Body RequesUpdateUser user);

    //Indicamos que vamos a enviar un fichero por partes
    @Multipart
    @POST("users/uploadprofilephoto")
    Call<ResponseUploadPhoto> uploadprofilephoto(@Part("file\"; filename=\"photo.jpeg\" ") RequestBody file);
}
