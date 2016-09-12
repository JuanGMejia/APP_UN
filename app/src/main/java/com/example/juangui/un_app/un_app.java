package com.example.juangui.un_app;

/**
 * Created by Felipe on 11/09/2016.
 */

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class un_app extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();


        //Install CustomActivityOnCrash
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setShowErrorDetails(false);
        //Now initialize your error handlers as normal
        //i.e., ACRA.init(this);
        //or Fabric.with(this, new Crashlytics())
    }
}
