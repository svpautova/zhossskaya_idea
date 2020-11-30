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
    static List<Photo> photoList = new ArrayList<>();
    static PexelApi photosApi;
    static ImageView imageView;
    static int n = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        imageView = v.findViewById(R.id.image_View);
      
        likeButton = v.findViewById(R.id.like_button);
        likeButton.setOnClickListener(this);
      
        dislikeButton = v.findViewById(R.id.dislike_button);
        dislikeButton.setOnClickListener(this);
      
        GetPhotos.getPhotos(v);
        return v;

    }


    @Override
    public void onClick(View v) {
        
        if (v.getId()==R.id.like_button){

            Buttons.Like_button();
            Log.d("!!!!!!", "click OK");
        }
        if (v.getId()==R.id.dislike_button){

            Buttons.Dislike_button();
            Log.d("!!!!!!", "click decline");
        }

    }

}

