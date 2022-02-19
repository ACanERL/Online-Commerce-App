package com.example.commerceapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    public static final String BASE_URL="https://run.mocky.io/";

    public static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }
}
