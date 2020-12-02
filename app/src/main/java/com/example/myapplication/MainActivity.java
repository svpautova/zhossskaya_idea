package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    // переменная для дирректории к папке
    private String directoryToImage = Environment.DIRECTORY_PICTURES + File.separator + R.string.favorites_folder;
    private static int RESULT_LOAD_IMAGE = 1;
   AppDatabase db;
    // private static Context context;

    private static MainActivity instance;


    public static MainActivity getInstance() {
        return instance;
    }
    /*
            объявления абстрактного класса для работы с бд
             */
    @Database(entities = {ImageFile.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract ImageDao getImageDao();
    }

    /*
    Публичный метод для открытия галлереи с последующим выбором нужного изображения
     */
    public void openGalleryForLoadFile(){
        ExecutorService executorservice = Executors.newSingleThreadExecutor();
        Runnable runnable =() -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        };
        executorservice.submit(runnable);
    }

    /*
    Публичный метод для сохранения Bitmap в нужную дирректорию.
    Вызывать внешне в потоке через экзекьютер.
    Пример вызова - аргумент контекст, битмап изображение, Bitmap.CompressFormat.JPEG, "image/jpeg", и имя файла без расширения
    saveBitmap(getApplicationContext(), selectedImage, Bitmap.CompressFormat.JPEG, "image/jpeg", "imgji");

     */
    public void saveBitmap(@NonNull final Context context, @NonNull final Bitmap bitmap,
                           @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                           @NonNull final String displayName) throws IOException {
        ExecutorService executorservice = Executors.newSingleThreadExecutor();
        Runnable runnable =() -> {
            final String relativeLocation = directoryToImage;

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
                    resolver.delete(uri, null, null);
                }

                try {
                    throw e;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            finally
            {
                if (stream != null)
                {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            saveNameOfFile(displayName);
            Log.d("!!!!!!", "saveBitmap");
        };
        executorservice.submit(runnable);
    }

    /*
    Защищенный метод для доставания нашей картинки из галереии и присвоения её в битмап.
    Где комментирии в метода - прописать imageview, в который будет сохраняться открытое в галлереи изображение
     */
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ExecutorService executorservice = Executors.newSingleThreadExecutor();
                Runnable runnable =() -> {
                    //ImageView imageActivity = findViewById(R.id.)
                    //imageActivity.setImageBitmap(selectedImage);
                };
                executorservice.submit(runnable);
                File f = new File("" +  imageUri);
                saveNameOfFile(f.getName());
                saveBitmap(getApplicationContext(), selectedImage, Bitmap.CompressFormat.JPEG, "image/jpeg", "imgji");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    Публичный метод получения картинки из директории приложения.
    nameFile - параметр названия файла, например "image"
    Возрващает Bitmap объект картинки.
    Внешне в потоке вызывать
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public Bitmap getImageFromName(String nameFile) throws IOException {
        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), Uri.parse(directoryToImage + File.separator + nameFile));
        Log.d("!!!!!!", "getImageFromName");
        return ImageDecoder.decodeBitmap(source);
    }

    /*
    Приватный метод полуения названия картинок из базы данных.
    Возвращает класы картинок с полем - имя для метода getNamesImages()
     */
    private List<ImageFile> loadImageClasses() {
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "populus-database").build();
        Log.d("!!!!!!", "loadImageClasses");
        return db.getImageDao().getAllImageName();
    }

    /*
    Публичный метод получения строк - названий файлов в избранной дирреткории.
    Возвращает список типа стринг, где каждый элемент такой, как "image"
    getNamesImages внешне в потоке вызывать
     */
    public List<String> getNamesImages(){
        List<ImageFile> defClass = loadImageClasses();
                List<String> getListNames = null;
                for(int i = 0; i < defClass.size(); i++){
                    getListNames.add(defClass.get(i).name);
                }
System.out.println("thrgtewdgrfeds");
                return getListNames;


    }

    // приватный метод сохранения имени файла в базу данных.
    // аргумент - имя файла как "image"
    private void saveNameOfFile(String nameOfFIle){
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "populus-database").build();
        ImageFile imageDefClass = null;
        imageDefClass.name = nameOfFIle;
        db.getImageDao().insertAll(imageDefClass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new MainScreenFragment())
                    .commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.add_photo:
                fragmentClass = AddPhotoFragment.class;
                break;
            case R.id.favorites:
                fragmentClass = FavoritesFragment.class;
                break;
            default:
                fragmentClass = MainScreenFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        drawerLayout.closeDrawers();

        return true;
    }

}