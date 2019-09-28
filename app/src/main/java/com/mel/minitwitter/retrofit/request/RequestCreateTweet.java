package com.mel.minitwitter.retrofit.request;

public class RequestCreateTweet {
    private String mensaje;

    public RequestCreateTweet(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
