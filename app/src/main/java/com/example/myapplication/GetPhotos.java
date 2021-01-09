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

    MutableLiveData<List<Photo>> photoList;
    int m = 1;

    private void getPhotos(int count) {

        PexelApi photosApi = new RetrofitClient().createService(PexelApi.class);
        Call<List<Photo>> callPhotos;
        String category = ThemederApp.getInstance().getRepo().getPropertyString("SPcategory");
        Log.d("GetPhotos", "Category: " + category);
        if(category.equals("All")) {
            callPhotos = photosApi.getCurated(count, m);
            m=m+count;
            callPhotos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(@NotNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
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
                public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                    Log.d("GetPhotos", "Failure: "+t.getMessage());
                }
            });

        }
        else{
            callPhotos = photosApi.getSearch(category,count, m);
            m=m+count;
            callPhotos.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(@NotNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        photoList.postValue(response.body());
                        Log.d("GetPhotos", "Download photos");
                    } else {

                        Log.d("GetPhotos", "Don't download photos: "+response.toString());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {

                    Log.d("GetPhotos", "Failure: "+t.getMessage());
                }
            });
        }
    }

    public LiveData<List<Photo>> getImage(int count){

        photoList = new MutableLiveData<>();
        getPhotos(count);
        return photoList;
    }

}
