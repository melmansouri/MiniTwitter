package com.mel.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

/**
 * AndroidViewModel es una clase definida en el paquete de arquitectura de android JetPack
 */
public class TweetViewModel extends AndroidViewModel {
    //Acceso a nuestro ws
    private TweetRepository repository;
    //Es una variable que nos permite obervarlas y en el caso de que haya un cambio pues nos permite capturarlo
    private LiveData<List<Tweet>> allTweets;
    public TweetViewModel(@NonNull Application application) {
        super(application);
        repository=new TweetRepository();
        allTweets=repository.getAllTweets();
    }

    /**
     * Cualquier elemento que quiera comunicarse con nuestro viewmodel quiere obtener los tweets
     * lo unico que tiene que hacer es invocar a este metodo
     * @return
     */
    public LiveData<List<Tweet>> getAllTweets(){
        return allTweets;
    }
}
