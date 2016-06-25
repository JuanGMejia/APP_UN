package com.example.juangui.un_app;

import com.firebase.client.Firebase;

/**
 * Created by Juan Gui on 23/06/2016.
 */
public class database extends android.app.Application {
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
