package com.example.myapplication;
import android.view.View;
import android.widget.ImageView;
public final class Buttons extends MainScreenFragment{

    private Buttons(){
        throw new AssertionError("Instantiating utility class.");
    }

    static void Like_button() {
        GetPhotos.ImageGlide();
    }

    static void Dislike_button(){
        GetPhotos.ImageGlide();
    }

    static void Change_Wallpaper() {

    }

    static void Switch_on() {

    }
}
