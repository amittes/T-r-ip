package com.example.t_r_ip;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    static private Context context;

    public static Context getMyContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
