package com.example.myapplication;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities = {ImageFile.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ImageDao getImageDao();


}