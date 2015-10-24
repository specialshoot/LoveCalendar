package com.example.lovecalendar;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by 轾 on 2015/10/10.
 */
public class App extends Application {

    private static final String DB_NAME = "lovecalendar.db";
    public static Context sContext;
    public static LiteOrm sDb;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sDb = LiteOrm.newSingleInstance(this, DB_NAME);
        if (BuildConfig.DEBUG) {
            sDb.setDebugged(true);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
