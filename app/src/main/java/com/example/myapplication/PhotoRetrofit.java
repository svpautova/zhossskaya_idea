package com.example.myapplication;
import com.example.myapplication.Photo;
import com.example.myapplication.GsonDeserialiser;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoRetrofit {
    private  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(new TypeToken<List<Photo>>() {
            }.getType(), new GsonDeserialiser()).create()))
            .build();

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}

