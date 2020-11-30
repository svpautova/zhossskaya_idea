package com.example.myapplication;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPhotos extends MainScreenFragment {

    public static void getPhotos(View v) {
        photosApi = new RetrofitClient().createService(PexelApi.class);
        Call<List<Photo>> callPhotos;

        callPhotos = photosApi.getCurated(15, 1);

        callPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photoList.addAll(response.body());
                    Log.d("!!!!!!", "yes");
                    ImageGlide();
                } else {
                    Log.d("!!!!!!", "ne yes");
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d("!!!!!!", "no");
            }
        });
    }

    public static void ImageGlide(){
        if(n==0) {
            Glide.with(MainScreenFragment.imageView)
                    .load(photoList.get(n).getMedium())
                    .into(imageView);
        }else{
            Glide.with(MainScreenFragment.imageView)
                    .load(photoList.get(n).getMedium())
                    .into(imageView);
        }
        n++;
    }
}
