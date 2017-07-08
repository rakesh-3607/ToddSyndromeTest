package com.testcompany.datalayer.retrofit;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.testcompany.BuildConfig;
import com.testcompany.datalayer.retrofitglidecommonutils.UnsafeOkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider implements Callback<BaseAPIClass> {

    private Retrofit mRetrofit;
    private APIResponseInterface responseInterface;
    private int requetsCode;
    private int nextRequestCode = 0;

    @Inject
    public RetrofitProvider(int requetsCode,int nextRequestCode, APIResponseInterface responseInterface) {

        this.responseInterface = responseInterface;
        this.requetsCode = requetsCode;
        this.nextRequestCode = nextRequestCode;
        mRetrofit = getRetrofit();

    }

    /*public RetrofitProvider() {
        mRetrofit = getRetrofit();
    }*/

    public <T> T createService(final Class<T> service) {
        return mRetrofit.create(service);
    }

    private Retrofit getRetrofit() {

      OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.MINUTES)
                .readTimeout(50, TimeUnit.MINUTES)
                .writeTimeout(50, TimeUnit.MINUTES)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.ROOT_URL)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onResponse(@NonNull Call<BaseAPIClass> call, @NonNull Response<BaseAPIClass> response) {

        ApiData mApiData = new ApiData();
        Gson gson = new Gson();
        try {
            if(response != null && response.body()!=null) {
                JsonElement jsonElement = gson.toJsonTree(response.body().data);
                try {
                    if (jsonElement.isJsonArray()) {
                        mApiData.setResponse(jsonElement.toString());

                    } else if (jsonElement.isJsonObject()) {
                        JSONObject mJson = new JSONObject(jsonElement.toString());
                        JSONArray mJsonArray = new JSONArray();
                        mJsonArray.put(mJson);
                        mApiData.setResponse(mJsonArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (responseInterface != null)
                    responseInterface.onResponseSuccess(mApiData, response, requetsCode,nextRequestCode);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Call<BaseAPIClass> call, @NonNull Throwable t) {

        responseInterface.onResponseFailed(call,t,requetsCode,nextRequestCode);

    }
}
