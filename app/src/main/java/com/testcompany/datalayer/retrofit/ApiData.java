package com.testcompany.datalayer.retrofit;

import com.google.gson.Gson;
import com.testcompany.datalayer.models.ListParameterizedType;

import java.lang.reflect.Type;
import java.util.List;



public class ApiData {

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Method to get api response in "data" jason object.
     * @param c Classname
     * @param <T> T
     * @return return response in pojo class.
     */
    public <T> List<T> getData(Class<T> c){
        Type type = new ListParameterizedType<T>(c);
        return new Gson().fromJson(response, type);
    }

    public static <T> List<T> getData(Class<T> c,String response){
        Type type = new ListParameterizedType<T>(c);
        return new Gson().fromJson(response, type);
    }

    public <T> T getDataInClass(Class<T> c){
        Type type = new ListParameterizedType<T>(c);
        return new Gson().fromJson(response, type);
    }

}
