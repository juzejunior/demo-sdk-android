package br.com.stonesdk.sdkdemo.activities;

import android.app.Application;

import stone.application.StoneStart;
import stone.utils.Stone;

/**
 * @author frodrigues
 * @since 28/06/2018
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * Este deve ser o primeiro método
         * a ser chamado para inicializar o SDK
         */
        StoneStart.init(this);
        Stone.setAppName("StoneDemoApplication");
    }
}
