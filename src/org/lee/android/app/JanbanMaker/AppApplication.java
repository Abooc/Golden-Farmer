package org.lee.android.app.JanbanMaker;

import android.app.Application;

import org.lee.android.util.Toast;

/**
 * Created by dayu on 14-11-3.
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Toast.init(this);
    }
}
