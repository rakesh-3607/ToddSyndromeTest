package com.testcompany.ui;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.testcompany.dagger.ApplicationComponent;
import com.testcompany.dagger.ApplicationModule;
import com.testcompany.dagger.DaggerApplicationComponent;

import javax.inject.Singleton;


public class BaseApplication extends MultiDexApplication {

    ApplicationComponent component;
    public static boolean isAppWentToBg = true;

    public static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        initDependencyInjection();
        super.onCreate();
        baseApplication= this;
        MultiDex.install(this);

    }

    private void initDependencyInjection() {
        component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        component.inject(this);

    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Singleton
    public static BaseApplication getInstance(){
        return baseApplication;
    }
}
