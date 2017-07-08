package com.testcompany.datalayer.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("/patients")
    Call<BaseAPIClass> getPatients();

}




