package com.testcompany.datalayer.retrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
@Module
public class ServiceModule {

    @Provides
    @Singleton
    public RetrofitProvider RetrofitProvider(int requetsCode,int nextRequetsCode, APIResponseInterface responseInterface) {
        return new RetrofitProvider(requetsCode,nextRequetsCode,responseInterface);
    }

    @Provides
    @Singleton
    APIService provideAPIService(final RetrofitProvider retrofitProvider) {
        return retrofitProvider.createService(APIService.class);
    }


}
