package com.example.myapplication;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.IOException;

public class Buttons{

    static void Like_button(CardStackAdapter adapter){
        Bitmap picture = ((BitmapDrawable) adapter.viewHolder.image.getDrawable()).getBitmap();

        new Thread(() -> {
            try {
                ThemederApp.getInstance().getRepo().saveBitmap(picture, Bitmap.CompressFormat.JPEG, "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
