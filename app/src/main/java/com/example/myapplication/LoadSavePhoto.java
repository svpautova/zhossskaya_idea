package com.example.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadSavePhoto {

    public static final String STORAGE_NAME = "THEMEDERStorage";
    private final Context applicationContext;
    private final AppDatabase db;
    private final SharedPreferences settings;

    /*
    конструтор класса
    */
    public LoadSavePhoto(Context inputContext) {
        applicationContext = inputContext;
        db = Room.databaseBuilder(applicationContext, AppDatabase.class, "populus-database")
                .fallbackToDestructiveMigration()
                .build();
        settings = applicationContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
    }



    /*
    Публичное сохранение bitmap, получение uri
    */
    public Uri saveBitmap(@NonNull final Bitmap bitmap, @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType) throws IOException {
            final String relativeLocation = Environment.DIRECTORY_PICTURES;
            final ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, String.valueOf(System.currentTimeMillis()));
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            Uri uri;
            OutputStream stream;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
                final ContentResolver resolver = applicationContext.getContentResolver();
                final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                uri = resolver.insert(contentUri, contentValues);
                stream = resolver.openOutputStream(uri);
            } else {
                File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                // contentValues.put(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)), relativeLocation);
                File image = new File(imagesDir, (applicationContext.getString(R.string.app_name) + System.currentTimeMillis()));
                stream = new FileOutputStream(image);
                // уже записал в файловый поток через этот иетод

                uri = Uri.fromFile(image);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(uri);
                applicationContext.sendBroadcast(mediaScanIntent);
            }
            bitmap.compress(format, 100, stream);
            stream.flush();
            stream.close();
            saveNameOfFile(uri.toString());
            return uri;

    }

    /*
    Публичный метод получения картинки из директории приложения.
    nameFile - параметр названия файла, например "image.jpg"
    Возрващает Bitmap объект картинки.
     */
    public Bitmap getImageFromName(Uri uriImage) throws IOException {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(applicationContext.getApplicationContext().getContentResolver(), uriImage);
            return ImageDecoder.decodeBitmap(source);
        } else {
            final InputStream imageStream = applicationContext.getContentResolver().openInputStream(uriImage);
            return BitmapFactory.decodeStream(imageStream);
        }
    }

    /*
    Приватный метод полуения названия картинок из базы данных.
    Возвращает класы картинок с полем - имя для метода getNamesImages()
     */
    private List<ImageFile> loadImageClasses() {
        return db.getImageDao().getAllImageName();
    }

    /*
    Публичный метод получения строк - названий файлов в избранной дирреткории.
    Возвращает список типа стринг, где каждый элемент такой, как "image.jpg"
     */
    public List<String> getNamesImages() {
        List<ImageFile> defClass = loadImageClasses();
        List<String> getListNames = new ArrayList<>();
        for(int i = 0; i < defClass.size(); i++){
            getListNames.add(defClass.get(i).name);
        }
        return getListNames;
    }


    // приватный метод сохранения имени файла в базу данных.
    // аргумент - имя фалйа как "image.jpg"
    public void saveNameOfFile(String nameOfFIle) {
        ImageFile imageDefClass = new ImageFile();
        imageDefClass.name = nameOfFIle;
        db.getImageDao().insert(imageDefClass);
    }

    public void setPropertyBoolean(String name, Boolean value){
        settings.edit().putBoolean(name, value).apply();
    }

    public Boolean getPropertyBoolean(String name){
        return settings.getBoolean(name, false);
    }

    public void setPropertyString(String name,String value){
        settings.edit().putString(name, value).apply();
    }

    public String getPropertyString(String name){
        return settings.getString(name, "All");
    }

    public void setPropertyInteger(String name, int value){
        settings.edit().putInt(name, value).apply();
    }

    public int getPropertyInteger(String name){
        return settings.getInt(name, 1);
    }

}