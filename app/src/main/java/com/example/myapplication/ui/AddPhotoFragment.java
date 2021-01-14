package com.example.myapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.utils.ThemederApp;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class AddPhotoFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String SAVE_URI_TAG = "AddPhotoFragmentSaveUriTAG";
    ImageView imageView;
    Uri imageUri;


    private static final int RESULT_LOAD_IMAGE = 1;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addphoto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonOpenGallery = view.findViewById(R.id.open_gallery_button);
        buttonOpenGallery.setOnClickListener(mButtonClickListener);
        imageView = view.findViewById(R.id.picture_from_gallery);
        if(savedInstanceState!=null){
            Log.d("onViewCreated", ""+savedInstanceState.getString(SAVE_URI_TAG));
            imageUri= Uri.parse(savedInstanceState.getString(SAVE_URI_TAG));
            Glide.with(imageView).load(imageUri).into(imageView);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentGallery();
                }
            }else{
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    intentGallery();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // кнопка с пермишеными
    private final View.OnClickListener mButtonClickListener = v -> {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) ) {
                intentGallery();
            }else {
                requestPermissions(
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }else{
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)) {
                    intentGallery();
                }else {
                    requestPermissions(
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            PERMISSION_REQUEST_CODE);
                }
            }else{
                intentGallery();
            }
        }
    };

    // намерение открыть галерею
    public void intentGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    // после работы с галереей
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContext().getApplicationContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                Single.fromCallable(() ->
                        ThemederApp.getInstance().getRepo().saveBitmap(selectedImage, Bitmap.CompressFormat.JPEG, "image/jpeg"))
                        .subscribeOn(Schedulers.io())
                        .subscribe();

                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if (imageUri!=null){
            savedInstanceState.putString(SAVE_URI_TAG, String.valueOf(imageUri));
        }
    }
}
