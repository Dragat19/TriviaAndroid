package com.playtown.triviaplay;

import android.app.Application;

import com.playtown.triviaplay.api.RequestController;

/**
 * Created by albertsanchez on 25/10/17.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    public static RequestController requestController;

    public static BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestController = RequestController.getInstance(this);
    }
}
