package br.com.stonesdk.sdkdemo.activities;

import android.app.Application;

import java.util.List;

import stone.application.StoneStart;
import stone.user.UserModel;
import stone.utils.Stone;

/**
 * @author frodrigues
 * @since 28/06/2018
 */
public class DemoApplication extends Application {
    private static List<UserModel> users;

    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * Este deve ser o primeiro método
         * a ser chamado para inicializar o SDK
         */
        users = StoneStart.init(this);
        Stone.setAppName("StoneDemoApplication");
    }

    public static List<UserModel> getUsers() {
        return users;
    }
}
