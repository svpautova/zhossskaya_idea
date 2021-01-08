package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPhotos extends ViewModel {

    static MutableLiveData<List<Photo>> photoList;
    static int m = 1;

    public static void getPhotos() {

        PexelApi photosApi = new RetrofitClient().createService(PexelApi.class);
        Call<List<Photo>> callPhotos;
        String category = ThemederApp.getInstance().getRepo().getPropertyString("SPcategory");
        Log.d("GetPhotos", "Category: " + category);
        if(category.equals("All")) {
            callPhotos = photosApi.getCurated(1, m);
            m++;
            callPhotos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(@NotNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        photoList.postValue(response.body());
                        Log.d("GetPhotos", "Download photos");
                    } else {
                        Log.d("GetPhotos", "Don't download photos");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                    Log.d("GetPhotos", "Failure");
                }
            });

        }
        else{
            callPhotos = photosApi.getSearch(category,1, m);
            callPhotos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(@NotNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        photoList.postValue(response.body());
                        Log.d("GetPhotos", "Download photos");
                    } else {
                        Log.d("GetPhotos", "Don't download photos");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                    Log.d("GetPhotos", "Failure");
                }
            });
        }
    }

    public static LiveData<List<Photo>> getImage(){

        photoList = new MutableLiveData<>();
        getPhotos();
        return photoList;
    }

}
