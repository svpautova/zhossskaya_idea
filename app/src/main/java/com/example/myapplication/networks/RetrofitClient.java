package com.example.myapplication.networks;
import com.example.myapplication.networks.GsonDeserialiser;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(new TypeToken<List<String>>() {
            }.getType(), new GsonDeserialiser()).create()))
            .build();

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
