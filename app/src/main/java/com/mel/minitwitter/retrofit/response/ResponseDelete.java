package com.mel.minitwitter.retrofit.response;

public class ResponseDelete {
    private String mensaje;
    private User user;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
