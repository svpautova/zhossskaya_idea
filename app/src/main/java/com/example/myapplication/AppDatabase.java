package com.example.myapplication;


import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {ImageFile.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ImageDao getImageDao();
}