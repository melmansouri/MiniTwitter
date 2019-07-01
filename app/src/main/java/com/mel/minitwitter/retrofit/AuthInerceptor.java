package com.mel.minitwitter.retrofit;

import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase que va a obtener previamente al envio de la peticion al servidor
 * toda la informacion de esa peticion y le va a adjuntar una cabecera
 * con la info que necesitemos, en esta caso va a ser para el token
 */
public class AuthInerceptor implements Interceptor {
    /**
     * Este metodo va a ser invocado cada vez que queramos en una peticion
     * interceptarla y aplicarle lo que indiquemos en el cuerpo del metodo
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token=SharedPreferenceManager.getSomeStringValue(Constantes.PREF_TOKEN);
        //Chain encale la peticion que recibimos y la que vamos a lanzar.
        // Bearer es algo propio de la forma en la que se estan gesationando los token en este ws de ejemplo
        Request request=chain.request().newBuilder()
                .addHeader("Authorization","Bearer "+token).build();
        return chain.proceed(request);

    }
}
