package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {
    private static AuthTwitterClient instance;
    private AuthTwitterService authTwitterService;
    private Retrofit retrofit;

    private AuthTwitterClient() {
       //Incluir en la cabezara de la peticion el token que autoriza al usuario
       OkHttpClient.Builder okBuilder=new OkHttpClient.Builder();
       okBuilder.addInterceptor(new AuthInerceptor());
       //Con esto creamos un cliente que permite asociar informacion atraves del authinterceptor a la peticion
       OkHttpClient client=okBuilder.build();
        retrofit=new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //Adjnta a todas las peticiones el token
                .client(client)
                .build();
        authTwitterService = retrofit.create(AuthTwitterService.class);
    }

    public static AuthTwitterClient getInstance(){
        if (instance ==null) {
            instance=new AuthTwitterClient();
        }
        return instance;
    }

    public AuthTwitterService getAuthTwitterService(){
        return authTwitterService;
    }
}
