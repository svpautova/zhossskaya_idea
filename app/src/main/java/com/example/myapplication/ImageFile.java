package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
Класс для поле имени в Room
 */
@Entity
public class ImageFile {
    @PrimaryKey
    String name;
}

