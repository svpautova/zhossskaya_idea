package com.example.myapplication;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.IOException;

public class Buttons{

    static void Like_button(CardStackAdapter adapter){
        Bitmap picture = ((BitmapDrawable) adapter.viewHolder.image.getDrawable()).getBitmap();
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
