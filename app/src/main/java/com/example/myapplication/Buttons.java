package com.example.myapplication;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class Buttons{

    static void Like_button(Drawable pic){
        Bitmap picture = ((BitmapDrawable) pic).getBitmap();
        Log.d("Buttons", "Save Photo");
        new Thread(() -> {
            try {
                ThemederApp.getInstance().getRepo().saveBitmap(picture, Bitmap.CompressFormat.JPEG, "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
