package com.example.dairydelight.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String base_url="https://trrev.in/just-academy-project/api/auth/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } return retrofit;
    }
}
