package com.example.myapplication.networks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.utils.ThemederApp;

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
        if(category.equals("All")) {
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
        int flag=0;
        if(category.equals("Разное")){
            enCategory="desktop backgrounds";
            flag=1;
        }
        if(category.equals("Город")){
            enCategory="City";
            flag=1;
        }
        if(category.equals("Авто")){
            enCategory="Cars";
            flag=1;
        }
        if(category.equals("Природа")){
            enCategory="Nature";
            flag=1;
        }
        if(category.equals("Цветы")){
            enCategory="Flowers";
            flag=1;
        }
        if(category.equals("Еда")){
            enCategory="Food";
            flag=1;
        }
        if(category.equals("Животные")){
            enCategory="Animals";
            flag=1;
        }
        if(category.equals("Девушки")){
            enCategory="Bikini";
            flag=1;
        }
        if(flag==1){
            return enCategory;
        }
        else{
            return category;
        }
    }
}
