package com.example.webservicefront.data.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofitJson;
    private static Retrofit retrofitXml;
    private static final String BASE_URL = "http://10.0.2.2:8082/banque/";
    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            .build();

    static  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) .addConverterFactory(GsonConverterFactory.create())
            .build();

    public  static  Retrofit getRetrofit() {
        return retrofit;
    }
    public static CompteApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(CompteApi.class);
    }

}