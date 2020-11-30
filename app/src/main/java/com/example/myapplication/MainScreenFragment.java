package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainScreenFragment extends Fragment implements View.OnClickListener {

    Button likeButton;
    Button dislikeButton;
    private List<Photo> photoList = new ArrayList<>();
    private PexelApi photosApi;
    ImageView imageView;
    int n = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        likeButton = (Button) v.findViewById(R.id.like_button);
        likeButton.setOnClickListener(this);
        dislikeButton = (Button) v.findViewById(R.id.dislike_button);
        dislikeButton.setOnClickListener(this);
        getPhotos(v);
        return v;

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.like_button) {
         //   n++;
            imageView.setImageDrawable(null);
        //    ImageGlide(v);
            Log.d("!!!!!!", String.valueOf(n));
        }
        if (v.getId() == R.id.dislike_button) {
         //   n++;
            imageView.setImageDrawable(null);
        //    ImageGlide(v);
            Log.d("!!!!!!", String.valueOf(n));
        }

    }


    public void getPhotos(View v) {
        photosApi = new RetrofitClient().createService(PexelApi.class);
        Call<List<Photo>> callPhotos;

        callPhotos = photosApi.getCurated(15, 1);

        callPhotos.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photoList.addAll(response.body());
                    Log.d("!!!!!!", "yes");
                    ImageGlide(v);
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

    public void ImageGlide(View v){
        imageView = v.findViewById(R.id.image_View);
        Glide.with(this)
                .load(photoList.get(n).getMedium())
                .into(imageView);
        n++;
    }

}

