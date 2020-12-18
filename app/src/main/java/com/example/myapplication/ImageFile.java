package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/*
Класс для поле имени в Room
 */
@Entity
public class ImageFile {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    public long id;

    String name;

}

