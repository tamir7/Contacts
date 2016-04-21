package com.github.tamir7.contacts.sample;

import android.app.Application;

import com.github.tamir7.contacts.Contacts;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contacts.initialize(this);
    }
}
