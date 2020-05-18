package com.ausadhi.mvvm.data.network.services;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitIntercepter {
    private static RetrofitIntercepter sInstance;

    private RetrofitIntercepter() {
        // This class is not publicly instantiable
    }

    public static synchronized RetrofitIntercepter getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitIntercepter();
        }
        return sInstance;
    }

    public OkHttpClient.Builder getHttpClient(){
         OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            httpClient.addInterceptor(logging);


            httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .build();
                                      //.addHeader(AppConstants.ApiParamKey.MYU_AUTH_TOKEN, auth)

                return chain.proceed(request);
            }
        });
    return httpClient;
    }
}
