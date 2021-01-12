package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPhotos extends ViewModel {

    MutableLiveData<List<String>> photoList;


    private void getPhotos(int count) {

        PexelApi photosApi = new RetrofitClient().createService(PexelApi.class);
        Call<List<String>> callPhotos;
        String trCategory = "";
        String category = ThemederApp.getInstance().getRepo().getPropertyString("SPcategory");
        Log.d("GetPhotos", "Category: " + category);
        Random r = new Random();
        int m = r.nextInt(150);
        if(category.equals("Разное")) {
            callPhotos = photosApi.getSearch("desktop backgrounds", count, m);
            callPhotos.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        Log.d("GetPhotos", ""+response.headers().get("x-RateLimit-Limit"));
                        Log.d("GetPhotos", ""+response.headers().get("x-RateLimit-Remaining"));
                        photoList.postValue(response.body());

                        Log.d("GetPhotos", "Download photos");
                    } else {
                        Log.d("GetPhotos", "Don't download photos: "+response.toString());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                    Log.d("GetPhotos", "Failure: "+t.getMessage());
                }
            });

        }
        else{
            trCategory=translater(category);
            callPhotos = photosApi.getSearch(trCategory,count, m);
            callPhotos.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        Log.d("GetPhotos", ""+response.headers().get("x-RateLimit-Limit"));
                        Log.d("GetPhotos", ""+response.headers().get("x-RateLimit-Remaining"));
                        photoList.postValue(response.body());

                        Log.d("GetPhotos", "Download photos");
                    } else {
                        Log.d("GetPhotos", "Don't download photos: "+response.toString());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                    Log.d("GetPhotos", "Failure: "+t.getMessage());
                }
            });
        }
    }

    public LiveData<List<String>> getImage(int count){

        photoList = new MutableLiveData<>();
        getPhotos(count);
        return photoList;
    }

    public String translater(String category){
        String enCategory = "";
        if(category.equals("Город")){
            enCategory="City";
        }
        if(category.equals("Авто")){
            enCategory="Cars";
        }
        if(category.equals("Природа")){
            enCategory="Nature";
        }
        if(category.equals("Цветы")){
            enCategory="Flowers";
        }
        if(category.equals("Еда")){
            enCategory="Food";
        }
        if(category.equals("Животные")){
            enCategory="Animals";
        }
        if(category.equals("Девушки")){
            enCategory="Bikini";
        }
        return enCategory;
    }
}
