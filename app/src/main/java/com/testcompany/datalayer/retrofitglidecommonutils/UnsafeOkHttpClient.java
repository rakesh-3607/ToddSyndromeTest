package com.testcompany.datalayer.retrofitglidecommonutils;

import com.testcompany.datalayer.retrofit.GzipRequestInterceptor;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class UnsafeOkHttpClient {

    private static final int REQUEST_TIME_OUT = 30;

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {

            final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            SSLContext ctx = null;
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());
            final HostnameVerifier hostnameVerifier = (hostname, session) -> true;

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
               //Log.e("response",message);
            });
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            return new OkHttpClient.Builder()
                    .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(ctx.getSocketFactory())
                    .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(new GzipRequestInterceptor())
                    .build();


        } catch (Exception e) {
            return null;
        }

    }
}
