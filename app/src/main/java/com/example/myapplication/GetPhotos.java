package com.example.myapplication;

import android.util.Log;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPhotos extends MainScreenFragment {

    public static void getPhotos() {
        photosApi = new RetrofitClient().createService(PexelApi.class);
        Call<List<Photo>> callPhotos;

        callPhotos = photosApi.getCurated(31, 2);

        callPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(@NotNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    photoList.addAll(response.body());
                    Log.d("!!!!!!", "Download photos");
                    ImageGlide();
                } else {
                    Log.d("!!!!!!", "Don't download photos");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                Log.d("!!!!!!", "no");
            }
        });
    }

    public static void ImageGlide(){
        Glide.with(MainScreenFragment.imageView)
                .load(photoList.get(n).getMedium())
                .into(imageView);
        n++;
    }
}
