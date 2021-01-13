package com.example.myapplication;


import android.annotation.SuppressLint;

import android.app.WallpaperManager;

import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.UUID;


public class PicDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    protected static final String KEY = "PicDetailsKEY";
    private final String croppedIMGname = "Themeder-";
    Spinner wallpaperButton;
    Button cropButton;
    Uri picUri;
    ImageView mPic;
    Bitmap bitmap = null;


    public static PicDetailsFragment newInstance(String picPath)  {

        final Bundle extras = new Bundle();
        extras.putSerializable(KEY, picPath);

        final PicDetailsFragment fragment = new PicDetailsFragment();
        fragment.setArguments(extras);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picdetails, container, false);
        wallpaperButton = v.findViewById(R.id.wallButton);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext().getApplicationContext(),
                R.layout.custom_spinner_item_pic_details,
                getResources().getStringArray(R.array.wallpapers));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        wallpaperButton.setAdapter(adapter);
        wallpaperButton.setOnItemSelectedListener(this);
        cropButton = v.findViewById(R.id.cropButton);
        cropButton.setOnClickListener(this);
        picUri = Uri.parse(info());

        if (picUri != null) {
            mPic = v.findViewById(R.id.detailsPic);
            Glide.with(mPic).load(picUri).into(mPic);
        }
        return v;
    }
    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.cropButton) {
            String destinationFilename = croppedIMGname + UUID.randomUUID().toString();
            Uri imageUriResultCrop = startCrop(picUri, destinationFilename);

            if (imageUriResultCrop != null) {
                Log.d("crop", "before glide");
                Glide.with(mPic).load(imageUriResultCrop).into(mPic);
                Log.d("crop", "after glide");
            }
            else{
                Glide.with(mPic).load(picUri).into(mPic);
            }
        }
    }



    public String info(){
        if (getArguments() == null) {
            return null;
        }

        final Serializable picPath = getArguments().getSerializable(KEY);
        return (String) picPath;
    }
    private Uri startCrop(@NonNull Uri uri, String fileName){

        Uri outputF = Uri.fromFile(new File(getActivity().getCacheDir(), fileName));
        UCrop uCrop = UCrop.of(uri, outputF);
        uCrop.withAspectRatio(3,4);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(getActivity());
        Uri imageUriResultCrop = UCrop.getOutput(uCrop.getIntent(getActivity()));

        Log.d("ucrop.of", imageUriResultCrop.toString());
        return imageUriResultCrop;
    }

    @SuppressLint("ResourceAsColor")
    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        options.setStatusBarColor(R.color.blue1);
        options.setToolbarColor(R.color.blue1);
        options.setToolbarWidgetColor(R.color.blue1);
        options.setActiveControlsWidgetColor(R.color.blue1);
        options.setRootViewBackgroundColor(R.color.blue1);
        options.setToolbarTitle("Crop and rotate");

        return options;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            bitmap = ((BitmapDrawable) mPic.getDrawable()).getBitmap();
            WallpaperManager manager = WallpaperManager.getInstance(getActivity());
            try {
                manager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (position == 2) {

            bitmap = ((BitmapDrawable) mPic.getDrawable()).getBitmap();
            WallpaperManager manager = WallpaperManager.getInstance(getActivity());
            try {
                manager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (position == 3) {

            bitmap = ((BitmapDrawable) mPic.getDrawable()).getBitmap();
            WallpaperManager manager = WallpaperManager.getInstance(getActivity());
            try {
                manager.setBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
