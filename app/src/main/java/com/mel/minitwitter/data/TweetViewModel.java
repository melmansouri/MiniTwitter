package com.mel.minitwitter.data;

import android.app.Application;

import com.mel.minitwitter.retrofit.request.RequestCreateTweet;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
        //->
        allTweets=repository.getAllTweets();
    }

    /**
     * Cualquier elemento que quiera comunicarse con nuestro viewmodel quiere obtener los tweets
     * lo unico que tiene que hacer es invocar a este metodo
     * -> esto se ha hecho de esta forma para que cada vez que llamemos a este metodo no tengamos que
     * descargar todos los tweets (nuevos , y antiguos)
     * Devuelve los tweets que se obtuvieron en el constructor y al ser un livedata pues en el activity
     * o fragment  que hace uso del metodo escucha los cambios para solo obtener esos cambios
     * @return
     */
    public LiveData<List<Tweet>> getAllTweets(){
        return allTweets;
    }

    /**
     * Devuelve todos los tweets ya sean nuevos o antiguos
     * @return
     */
    public LiveData<List<Tweet>> getNewAllTweets(){
        allTweets=repository.getAllTweets();
        return allTweets;
    }

    public void createTweet(RequestCreateTweet tweet){
        repository.createTweet(tweet);
    }
    public void likeTweet(int id){
        repository.likeTweet(id);
    }
}