package com.example.myapplication;


import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.WallpaperManager;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import com.bumptech.glide.Glide;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static io.reactivex.internal.schedulers.SchedulerPoolFactory.start;


public class PicDetailsFragment extends Fragment implements View.OnClickListener{

    protected static final String KEY = "PicDetailsKEY";
    private static final int RESULT_DELETE_IMAGE = 5;
    private final String croppedIMGname = "Themeder-";
    Button wallpaperButton;
    Button cropButton;
    Button delButton;
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
        delButton = v.findViewById(R.id.detailsButton);
        delButton.setOnClickListener(this);
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
            System.out.println("GfsdasDERVW");
        }else if (v.getId()==R.id.detailsButton) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
              ArrayList<Uri> uriList = new ArrayList<>();
              uriList.add(picUri);
              IntentSender   r =  MediaStore.createDeleteRequest(getContext().getApplicationContext().getContentResolver(), uriList).getIntentSender();
                try {
                    startIntentSenderForResult(r, RESULT_DELETE_IMAGE,null,0,0,0,null);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            }

           // Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);



           // startActivityForResult(Intent, RESULT_LOAD_IMAGE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == RESULT_DELETE_IMAGE) && (resultCode == RESULT_OK)) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Disposable disposable = Single.fromCallable(()-> ThemederApp.getInstance().getRepo().deleteImageFromUri(picUri))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(c -> transaction.replace(R.id.fragment_picdetails, new FavoritesFragment()).commit(), error -> Log.d("tag", "error")
            );



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
