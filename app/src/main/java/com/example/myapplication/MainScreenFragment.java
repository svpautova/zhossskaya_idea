package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreenFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 2;
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


int  a = 0;
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.like_button){
             ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
            a++;
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            Bitmap picture = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            new Thread(() -> {
                try {
                    ThemederApp.getInstance().getRepo().saveBitmap(picture, Bitmap.CompressFormat.JPEG, "image/jpeg");
                } catch (IOException e) {
                    e.printStackTrace();
                }


              //  try {
                   // imageView.setImageBitmap(ls.getImageFromName(name, getContext()));
              //  } catch (IOException e) {
              //      e.printStackTrace();
              //  }

            }).start();
            }
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

