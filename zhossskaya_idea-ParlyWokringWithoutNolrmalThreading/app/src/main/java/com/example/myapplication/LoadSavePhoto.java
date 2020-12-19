package com.example.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Room;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadSavePhoto {




    public void saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                           @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                           @NonNull final String displayName) throws IOException
    {
        final String relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "favorite_image";

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

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
        saveNameOfFile(uri.toString(), context);
    }

    /*
    Защищенный метод для доставания нашей картинки из галереии и присвоения её в битмап.
     *//* @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        //super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                saveBitmap(getApplicationContext(), selectedImage, Bitmap.CompressFormat.JPEG, "image/jpeg", "gh");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }(/*/

    /*
    Публичный метод получения картинки из директории приложения.
    nameFile - параметр названия файла, например "image.jpg"
    Возрващает Bitmap объект картинки.
     */
    public Bitmap getImageFromName(String nameFile, Context frContext) throws IOException {
        Uri r = Uri.parse(nameFile);
        ImageDecoder.Source source = ImageDecoder.createSource(frContext.getApplicationContext().getContentResolver(), r);
        return ImageDecoder.decodeBitmap(source);
    }

    /*
    Приватный метод полуения названия картинок из базы данных.
    Возвращает класы картинок с полем - имя для метода getNamesImages()
     */
    private List<ImageFile> loadImageClasses(Context frContext) {
        AppDatabase db = Room.databaseBuilder(frContext.getApplicationContext(), AppDatabase.class, "populus-database").build();
        return db.getImageDao().getAllImageName();
    }

    /*
    Публичный метод получения строк - названий файлов в избранной дирреткории.
    Возвращает список типа стринг, где каждый элемент такой, как "image.jpg"
     */
    public List<String> getNamesImages(Context frContext){
        List<ImageFile> defClass = loadImageClasses(frContext);
        List<String> getListNames = new ArrayList();
        for(int i = 0; i < defClass.size(); i++){
            getListNames.add(defClass.get(i).name.toString());
        }
        return getListNames;
    }

    // приватный метод сохранения имени файла в базу данных.
    // аргумент - имя фалйа как "image.jpg"
    private void saveNameOfFile(String nameOfFIle, Context frContext){
        AppDatabase db = Room.databaseBuilder(frContext, AppDatabase.class, "populus-database").build();
        ImageFile imageDefClass = new ImageFile();
        imageDefClass.name = nameOfFIle;
        db.getImageDao().insert(imageDefClass);
    }

}

