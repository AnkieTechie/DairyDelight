package com.example.dairydelight.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

        private static final String url="https://trrev.in/";
        private static Retrofit retrofit=null;

        public static Retrofit getRetrofit(){
            if (retrofit==null){
                retrofit=new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } return retrofit;
        }
}
