package com.mel.minitwitter.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mel.minitwitter.retrofit.request.RequesUpdateUser;
import com.mel.minitwitter.retrofit.response.User;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository repository;
    private LiveData<User> user;
    private LiveData<String> photoprofile;
    public ProfileViewModel(Application application){
        super(application);
        repository=new ProfileRepository();
        user=repository.getProfile();
        photoprofile=repository.getPhotoprofileObserver();
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void updateProfile(RequesUpdateUser requesUpdateUser){
        repository.updateProfle(requesUpdateUser);
    }

    public void uploadPhoto(String path){
        repository.uploadPhoto(path);
    }

    public LiveData<String> getPhotoprofileObserver() {
        return photoprofile;
    }
}