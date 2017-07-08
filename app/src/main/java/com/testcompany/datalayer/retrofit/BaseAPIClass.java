package com.testcompany.datalayer.retrofit;


public class BaseAPIClass<T> {

    public String message = "";
    public boolean isError;
    public String response;
    public int status;
    public T data = null;

}
