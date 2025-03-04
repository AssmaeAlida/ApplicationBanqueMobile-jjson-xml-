package com.example.webservicefront.data.api;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Config {
    private static Retrofit retrofit;

    // api bach kanjibo les données en format b xml et json
    public static Retrofit getClient(String format) {
        String finalAcceptHeader = format.equals("application/xml") ? "application/xml" : "application/json";

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", finalAcceptHeader)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8082/banque/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
