package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/*
Интерфейс с методами взаимодействия с базой данных
 */
@Dao
public interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ImageFile... imagefile);

    @Query("SELECT * FROM imagefile")
    List<ImageFile> getAllImageName();
}
