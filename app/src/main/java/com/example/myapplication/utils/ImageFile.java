package com.example.myapplication.utils;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
Класс для поле имени в Room
 */
@Entity
public class ImageFile {
    @PrimaryKey(autoGenerate = true)
    public long id;

    String name;

}

