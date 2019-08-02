
package com.mel.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tweet {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("likes")
    @Expose
    private List<Like> likes = null;
    @SerializedName("user")
    @Expose
    private User user;

    public Tweet(){}
    public Tweet(Tweet tweet) {
        this.id= tweet.getId();
        this.likes=tweet.getLikes();
        this.mensaje=tweet.getMensaje();
        this.user=tweet.getUser();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
