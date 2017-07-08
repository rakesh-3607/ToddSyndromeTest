package com.testcompany.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.testcompany.ui.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {

    public static final String SHARED_PREFERENCE_FILE_NAME = "preference_file";

    @Singleton
    @Provides
    public Application providesApplication(BaseApplication app) {
        return app;
    }

    @Provides
    @Singleton
    public Context provideContext(final Application app) {
        return app;
    }

    @Provides
    @Singleton
    public Resources provideResources(final Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    public PackageInfo providePackageInfo(final Application app) {
        try {
            return app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(final Application app) {
        return app.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }
}
