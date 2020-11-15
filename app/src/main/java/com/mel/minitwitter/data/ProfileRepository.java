package com.mel.minitwitter.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.MyApp;
import com.mel.minitwitter.common.SharedPreferenceManager;
import com.mel.minitwitter.retrofit.AuthTwitterClient;
import com.mel.minitwitter.retrofit.AuthTwitterService;
import com.mel.minitwitter.retrofit.request.RequesUpdateUser;
import com.mel.minitwitter.retrofit.request.RequestCreateTweet;
import com.mel.minitwitter.retrofit.response.Like;
import com.mel.minitwitter.retrofit.response.ResponseDelete;
import com.mel.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.mel.minitwitter.retrofit.response.Tweet;
import com.mel.minitwitter.retrofit.response.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Patron MVVM
 * Aqui estara la conexion al ws o a la bd local
 */
public class ProfileRepository {
    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private MutableLiveData<User> user;
    private MutableLiveData<String> photoprofile;

    public ProfileRepository() {
        retrofitInit();
    }
    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        photoprofile=new MutableLiveData<>();
    }

    public MutableLiveData<User> getProfile(){

        if (user==null){
            user=new MutableLiveData<>();
        }
        Call<User> call = authTwitterService.getProfileUser();
        //Metodo que nos permite ejecutar en segundo plano la peticion al servidor
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user.setValue(response.body());

                } else {
                    //Snackbar.make(rootFragmentListTweet, "Algo fue mal, revise sus datos de acceso", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Snackbar.make(rootFragmentListTweet, "Problemas de conexión. Intentelo de nuevo", Snackbar.LENGTH_SHORT).show();
            }
        });

        //return data;
        return user;
    }
    public void updateProfle(RequesUpdateUser requesUpdateUser){

        if (user==null){
            user=new MutableLiveData<>();
        }
        Call<User> call = authTwitterService.updateUser(requesUpdateUser);
        //Metodo que nos permite ejecutar en segundo plano la peticion al servidor
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user.setValue(response.body());

                } else {
                    //Snackbar.make(rootFragmentListTweet, "Algo fue mal, revise sus datos de acceso", Snackbar.LENGTH_SHORT).show();
                    user.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Snackbar.make(rootFragmentListTweet, "Problemas de conexión. Intentelo de nuevo", Snackbar.LENGTH_SHORT).show();
                user.setValue(null);
            }
        });
    }

    public LiveData<String> getPhotoprofileObserver() {
        return photoprofile;
    }

    public void uploadPhoto(String photoPath){
        File file=new File(photoPath);
        RequestBody requesBody= RequestBody.create(MediaType.parse("image/jpg"),file);
        Call<ResponseUploadPhoto> call=authTwitterService.uploadprofilephoto(requesBody);
        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if (response.isSuccessful()){
                    SharedPreferenceManager.setSomeStringValue(Constantes.PREF_PHOTO_URL,response.body().getFilename());
                    photoprofile.setValue(response.body().getFilename());
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {

            }
        });
    }
}
