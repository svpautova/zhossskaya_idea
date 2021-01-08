package com.example.myapplication;


import android.annotation.SuppressLint;

import android.app.WallpaperManager;

import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
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

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.UUID;


public class PicDetailsFragment extends Fragment implements View.OnClickListener{

    protected static final String KEY = "PicDetailsKEY";
    private final String croppedIMGname = "Themeder-";
    Button wallpaperButton;
    Button cropButton;
    Uri picUri;
    ImageView mPic;

    public static PicDetailsFragment newInstance(String picPath) {

        final Bundle extras = new Bundle();
        extras.putSerializable(KEY, (Serializable) picPath);

        final PicDetailsFragment fragment = new PicDetailsFragment();
        fragment.setArguments(extras);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picdetails, container, false);
        wallpaperButton = v.findViewById(R.id.wallButton);
        wallpaperButton.setOnClickListener(this);
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
        if (v.getId()==R.id.wallButton) {

            Bitmap bitmap = null;
            bitmap = ((BitmapDrawable) mPic.getDrawable()).getBitmap();

            WallpaperManager manager = WallpaperManager.getInstance(getActivity());
            try {
                manager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (v.getId()==R.id.cropButton) {
            String destinationFilename = croppedIMGname + UUID.randomUUID().toString();
            Uri imageUriResultCrop = startCrop(picUri, destinationFilename);
            Glide.with(mPic).load(imageUriResultCrop).into(mPic);
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
        //uCrop.withAspectRatio(1,1);
        uCrop.withAspectRatio(3,4);
        //uCrop.useSourceImageAspectRatio();
        //uCrop.withAspectRatio(2,3);
        //uCrop.withAspectRatio(16,9);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(getActivity());
        Uri imageUriResultCrop = UCrop.getOutput(uCrop.getIntent(getActivity()));
        //Bitmap bitmap = (Bitmap) UCrop.getOutput(uCrop.getIntent(getActivity()));

        Log.d("ucrop.of", imageUriResultCrop.toString());
        return imageUriResultCrop;
    }

    @SuppressLint("ResourceAsColor")
    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        options.setStatusBarColor(R.color.blue1);
        options.setToolbarColor(R.color.blue1);

        options.setToolbarTitle("Crop");

        return options;
    }

}
