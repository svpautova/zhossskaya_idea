package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IListener {
    private DrawerLayout drawerLayout;
    protected static final String TAG_DETAILS = "PicDetails";
    // переменная для дирректории к папке
   // private String directoryToImage = Environment.DIRECTORY_PICTURES + File.separator + R.string.favorites_folder;
    private static final int RESULT_LOAD_IMAGE = 1;

    Spinner spinner;
    Toolbar toolbar;
    // private static Context context;

    /*
    Публичный метод для открытия галлереи с последующим выбором нужного изображения
     *//*
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", "Choose "+ parent.getItemAtPosition(position).toString());
                ThemederApp.getInstance().getRepo().setPropertyString(getString(R.string.SPcategory), parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
                spinner.setVisibility(View.INVISIBLE);
                break;
            case R.id.add_photo:
                fragmentClass = AddPhotoFragment.class;
                spinner.setVisibility(View.INVISIBLE);
                break;
            case R.id.favorites:
                fragmentClass = FavoritesFragment.class;
                spinner.setVisibility(View.INVISIBLE);
                break;
            default:
                fragmentClass = MainScreenFragment.class;
                spinner.setVisibility(View.VISIBLE);
        }
        Log.d("!!!!!!", ""+fragmentClass);
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


    @Override
    public void onClicked(String picPath) {
        Log.d("Buttons", "Show Details");
        PicDetailsFragment detailsFragment = PicDetailsFragment.newInstance(picPath);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailsFragment, TAG_DETAILS)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ThemederApp.getInstance().getRepo().setPropertyString(getString(R.string.SPcategory), "All");
    }
}