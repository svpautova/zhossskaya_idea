package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainScreenFragment extends Fragment implements View.OnClickListener {

    Button likeButton;
    Button dislikeButton;
    static List<Photo> photoList = new ArrayList<>();
    static PexelApi photosApi;
    @SuppressLint("StaticFieldLeak")
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
        GetPhotos.getPhotos();
        return v;
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.like_button){


            String name = "kkjj";
            Bitmap picture = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ExecutorService executorservice = Executors.newSingleThreadExecutor();
            Runnable runnable =() -> {
                try {
                MainActivity.getInstance().saveBitmap(getActivity(), picture, Bitmap.CompressFormat.JPEG, "image/jpeg", name);
                    imageView.setImageBitmap(   MainActivity.getInstance().getImageFromName(name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            executorservice.submit(runnable);
            GetPhotos.ImageGlide();
            //Buttons b = new Buttons(getActivity());
            //b.Like_button();
            Log.d("!!!!!!", "click OK");
        }
        if (v.getId()==R.id.dislike_button){
            Buttons.Dislike_button();
            Log.d("!!!!!!", "click decline");
        }
    }
}

