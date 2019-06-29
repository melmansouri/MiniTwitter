package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {
    private static MiniTwitterClient instance;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    private MiniTwitterClient() {
        retrofit=new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    public static MiniTwitterClient getInstance(){
        if (instance ==null) {
            instance=new MiniTwitterClient();
        }
        return instance;
    }

    public MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }
}
