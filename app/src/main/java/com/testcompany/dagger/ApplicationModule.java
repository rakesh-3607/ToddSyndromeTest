package com.testcompany.dagger;

import com.testcompany.ui.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private BaseApplication baseApplication;

    public ApplicationModule(final BaseApplication application){
        this.baseApplication = application;
    }

    @Singleton
    @Provides
    public BaseApplication provideBaseApplication(){
        return baseApplication;
    }
}
