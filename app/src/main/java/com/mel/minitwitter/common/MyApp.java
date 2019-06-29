package com.mel.minitwitter.common;

import android.app.Application;
import android.content.Context;

/**
 * Para gestionar de una manera mas sencilla el acceso al contexto en ciertos puntos de la aplicacion
 */
public class MyApp extends Application {
    private static MyApp instance;
    public static MyApp getInstance(){
        return instance;
    }

    /**
     * Metodo para obtener en cualquier momento el contexto de la aplicacion
     * ya que va a haber ciertos momentos de la aplicacion en los  que a lo mejor no tengamos facilidad para
     * obtener el contexto
     * @return
     */
    public static Context getContext(){
        return instance;
    }

    /**
     * Este metodo se crea solo una vez cuando se abra la aplicacion
     * de esta forma estamos tambien creando MyApp mediante singleton
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
