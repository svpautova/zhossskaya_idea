package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadSavePhoto {
    Context currentContext;
    AppDatabase db;
    LoadSavePhoto(Context inputContext){
        currentContext = inputContext;
        db = Room.databaseBuilder(currentContext , AppDatabase.class, "populus-database").build();
    }



    public void saveBitmap(@NonNull final Bitmap bitmap,
                           @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                           @NonNull final String displayName) throws IOException
    {
        final String relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "favorite_image";

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        }else{
            contentValues.put(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)), relativeLocation);
        }

        final ContentResolver resolver = currentContext.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try
        {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, contentValues);

            if (uri == null)
            {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);

            if (stream == null)
            {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false)
            {
                throw new IOException("Failed to save bitmap.");
            }
        }
        catch (IOException e)
        {
            if (uri != null)
            {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        }
        finally
        {
            if (stream != null)
            {
                stream.close();
            }
        }
        saveNameOfFile(uri.toString());
    }

    /*
    Публичный метод получения картинки из директории приложения.
    nameFile - параметр названия файла, например "image.jpg"
    Возрващает Bitmap объект картинки.
     */
    public Bitmap getImageFromName(String nameFile) throws IOException {
        if (ContextCompat.checkSelfPermission(currentContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Uri uriImage = Uri.parse(nameFile);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            {
                ImageDecoder.Source source = ImageDecoder.createSource(currentContext.getApplicationContext().getContentResolver(), uriImage);
                return ImageDecoder.decodeBitmap(source);
            }else{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(currentContext.getApplicationContext().getContentResolver(), uriImage);
                return bitmap;
            }
        }
        else {
            return null;
        }
    }

    /*
    Приватный метод полуения названия картинок из базы данных.
    Возвращает класы картинок с полем - имя для метода getNamesImages()
     */
    private List<ImageFile> loadImageClasses() {
        AppDatabase db = Room.databaseBuilder(currentContext.getApplicationContext(), AppDatabase.class, "populus-database").build();
        return db.getImageDao().getAllImageName();
    }

    /*
    Публичный метод получения строк - названий файлов в избранной дирреткории.
    Возвращает список типа стринг, где каждый элемент такой, как "image.jpg"
     */
    public List<String> getNamesImages(){
        List<ImageFile> defClass = loadImageClasses();
        List<String> getListNames = new ArrayList();
        for(int i = 0; i < defClass.size(); i++){
            getListNames.add(defClass.get(i).name);
        }
        return getListNames;
    }

    // приватный метод сохранения имени файла в базу данных.
    // аргумент - имя фалйа как "image.jpg"
   public void saveNameOfFile(String nameOfFIle){
        ImageFile imageDefClass = new ImageFile();
        imageDefClass.name = nameOfFIle;
        db.getImageDao().insert(imageDefClass);
    }

    @Override
    protected void finalize() {
        db.close();
    }
}
