package com.mel.minitwitter.data;

import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;
import com.mel.minitwitter.retrofit.AuthTwitterClient;
import com.mel.minitwitter.retrofit.AuthTwitterService;
import com.mel.minitwitter.retrofit.request.RequestCreateTweet;
import com.mel.minitwitter.retrofit.response.Like;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
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
    //private LiveData<List<Tweet>> allTweets;//Al ser LiveData no podemos modificar ese listado
    private MutableLiveData<List<Tweet>> allTweets;//He tenido que ponerlo como mutable para que puede añadirle el tweet que acabo de añadir
    private MutableLiveData<List<Tweet>> favTweets;
    private String username;

    public TweetRepository() {
        retrofitInit();
        /**
         * Si se ve modificada la lista de tweets cuando se invoque getAllTweets la lista allTweets
         * notificara al viewmodel ese cambio y por lo tanto alli donde haya un metodo observador
         * se hara eco de este cambio que ha habido en la lista de allTweets y podremos refrescar
         * el adaptador.
         * Esto no se usa en ningun lado asi que lo he comentado hasta que vea para que servi. Sin esto funciona sin problemas
         */
        //allTweets=getAllTweets();
        username= SharedPreferenceManager.getSomeStringValue(Constantes.PREF_USERNAME);
    }
    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
    }

    /**
     * ESto es para cargar de tweets la variable allTweets
     */
    public MutableLiveData<List<Tweet>> getAllTweets(){
        //Un tipo de livedata que puede modificarse en el tiempo
        //final MutableLiveData<List<Tweet>> data=new MutableLiveData<>();Se comenta esto para hacer uso del objeto allTweets
        //Ya que vamos a realizar cambios sobre esa lista que estamos observando cuando creamos un tweets y asi se refresque la lista de tweets.
        if (allTweets==null){
            allTweets=new MutableLiveData<>();
        }
        Call<List<Tweet>> call = authTwitterService.getAllTweet();
        //Metodo que nos permite ejecutar en segundo plano la peticion al servidor
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    allTweets.setValue(response.body());

                } else {
                    //Snackbar.make(rootFragmentListTweet, "Algo fue mal, revise sus datos de acceso", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                //Snackbar.make(rootFragmentListTweet, "Problemas de conexión. Intentelo de nuevo", Snackbar.LENGTH_SHORT).show();
            }
        });

        //return data;
        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getFavsTweets(){
        if (favTweets==null) {
            favTweets=new MutableLiveData<>();
        }
        //Tendran solo los que sean favoritos
        List<Tweet> newFavList=new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //TODO Para mas adelante
            //Lambdas
        }else{
            Iterator iterator=allTweets.getValue().iterator();
            while(iterator.hasNext()){
                Tweet current= (Tweet) iterator.next();
                Iterator itLikes=current.getLikes().iterator();
                //indica si se ha encontrado al usuario logueado en la lista de likes
                boolean encontrado=false;
                while (itLikes.hasNext() && !encontrado){
                    Like like=(Like)itLikes.next();
                    if (like.getUsername().equals(username)){
                        encontrado=true;
                        newFavList.add(current);
                    }
                }
            }
        }
        favTweets.setValue(newFavList);

        return favTweets;
    }

    public void createTweet(RequestCreateTweet tweet){
        authTwitterService.addTweet(tweet).enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()){
                    /**
                     * No haremos allTweets.setValue ya que asi setea una lista entera
                     * Recorreremos la lista de allTweets, la clonaremos ya que no la podemos modificar directamente
                     * y sobre una lista clonada vamos a incluir el nuevo tweet y los tweets que ya teniamos
                     */
                    List<Tweet> listaClonada=new ArrayList<>();
                    //Añadimos en primer lugar el nuevo tweet que nos llega del servidor
                    listaClonada.add(response.body());
                    //De cada elemento vamos a hacer una copia en la lista clonada
                    for (int i=0;i<allTweets.getValue().size();i++){
                        //Hacemos esto new Tweet(allTweets.getValue().get(i)) para que sea un nuevo objeto
                        listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    //Si seteamos directamente el nuevo tweet perderiamos los que ya tenemos en la lista
                    allTweets.setValue(listaClonada);
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
            }
        });
    }

    public void likeTweet(int id){
        authTwitterService.likeTweet(id).enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()){
                    /**
                     * No haremos allTweets.setValue ya que asi setea una lista entera
                     * Recorreremos la lista de allTweets, la clonaremos ya que no la podemos modificar directamente
                     * y sobre una lista clonada vamos a incluir el nuevo tweet y los tweets que ya teniamos
                     */
                    List<Tweet> listaClonada=new ArrayList<>();
                    //De cada elemento vamos a hacer una copia en la lista clonada
                    for (int i=0;i<allTweets.getValue().size();i++){
                        if (allTweets.getValue().get(i).getId()==id){
                            /**
                             * Si hemos encontrado en la lista original
                             * el elemento sobre el que hemos hecho like,
                             * introducimos el elemento que nos ha llegado del servidor
                             */
                            listaClonada.add(response.body());
                        }else {
                            listaClonada.add(new Tweet(allTweets.getValue().get(i)));
                        }
                    }
                    //Si seteamos directamente el nuevo tweet perderiamos los que ya tenemos en la lista
                    allTweets.setValue(listaClonada);

                    //Hay que refrescar la lista de tweets favoritos ya que hay un like o dislike nuevo
                    getFavsTweets();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
            }
        });
    }

}
