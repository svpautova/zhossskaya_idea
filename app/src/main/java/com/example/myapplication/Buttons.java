package com.example.myapplication;


import static com.example.myapplication.MainScreenFragment.category;
import static com.example.myapplication.MainScreenFragment.categoryPrev;

public class Buttons{
    static void Like_button()  { // зеленая кнопка
        category = ThemederApp.getInstance().getRepo().getPropertyString("SPcategory");
        if(category.equals(categoryPrev)) {
            GetPhotos.getPhotos();
        }
        else{
            GetPhotos.getPhotos();
            categoryPrev = category;
        }
    }

    static void Dislike_button(){

        category = ThemederApp.getInstance().getRepo().getPropertyString("SPcategory");
        if(category.equals(categoryPrev)) {
            GetPhotos.getPhotos();
        }
        else{
            GetPhotos.getPhotos();
            categoryPrev = category;
        }
    }
}
