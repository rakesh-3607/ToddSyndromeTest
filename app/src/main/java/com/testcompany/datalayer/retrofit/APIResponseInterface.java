package com.testcompany.datalayer.retrofit;

import retrofit2.Call;
import retrofit2.Response;


public interface APIResponseInterface{

    //retrofit success manage method
    void onResponseSuccess(ApiData data, Response<BaseAPIClass> response, int requestCode,int nextRequestCode);
    //retrofit failed manage method
    void onResponseFailed(Call<BaseAPIClass> call, Throwable t, int requestCode,int nextRequestCode);
}
