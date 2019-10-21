package com.example.ftpmanage.entity;

import android.app.Application;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }
}
