package com.example.myapplication;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit getRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pexels.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static PexelApi getInterface(){
        PexelApi pexelApi = getRetrofit().create(PexelApi.class);
        return pexelApi;
    }

}
