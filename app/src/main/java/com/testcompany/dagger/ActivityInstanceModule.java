package com.testcompany.dagger;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityInstanceModule {
    private final Activity activity;

    public ActivityInstanceModule(final Activity activity){
        this.activity = activity;
    }

    @Provides
    public Context provideContext(){
        return activity;
    }
}
