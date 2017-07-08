package com.testcompany.dagger;

import com.testcompany.datalayer.retrofit.GzipRequestInterceptor;
import com.testcompany.datalayer.retrofit.ServiceModule;
import com.testcompany.ui.BaseApplication;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AndroidModule.class, ApplicationModule.class,ServiceModule.class})
public interface ApplicationComponent {

    void inject(BaseApplication application);

    GzipRequestInterceptor gzipRequestInterceptor();
}
