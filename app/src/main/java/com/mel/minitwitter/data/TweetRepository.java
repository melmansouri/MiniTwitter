package com.mel.minitwitter.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.mel.minitwitter.retrofit.AuthTwitterClient;
import com.mel.minitwitter.retrofit.AuthTwitterService;
import com.mel.minitwitter.retrofit.response.Tweet;
import com.mel.minitwitter.ui.MyTweetRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Patron MVVM
 * Aqui estara la conexion al ws o a la bd local
 */
public class TweetRepository {
    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    //A medida que introduzcamos nuevos datos esta lista viva se va a ir actualizando
    private LiveData<List<Tweet>> allTweets;

    public TweetRepository() {
        retrofitInit();
        /**
         * Si se ve modificada la lista de tweets cuando se invoque getAllTweets la lista allTweets
         * notificara al viewmodel ese cambio y por lo tanto alli donde haya un metodo observador
         * se hara eco de este cambio que ha habido en la lista de allTweets y podremos refrescar
         * el adaptador
         */
        allTweets=getAllTweets();
    }
    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
    }

    /**
     * ESto es para cargar de tweets la variable allTweets
     */
    public LiveData<List<Tweet>> getAllTweets(){
        //Un tipo de livedata que puede modificarse en el tiempo
        final MutableLiveData<List<Tweet>> data=new MutableLiveData<>();
        Call<List<Tweet>> call = authTwitterService.getAllTweet();
        //Metodo que nos permite ejecutar en segundo plano la peticion al servidor
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());

                } else {
                    //Snackbar.make(rootFragmentListTweet, "Algo fue mal, revise sus datos de acceso", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                //Snackbar.make(rootFragmentListTweet, "Problemas de conexión. Intentelo de nuevo", Snackbar.LENGTH_SHORT).show();
            }
        });

        return data;
    }

}
