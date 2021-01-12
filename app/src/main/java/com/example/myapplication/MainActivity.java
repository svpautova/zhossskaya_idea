package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    protected static final String S_TAG = "MainActivitySaveInstantState";

    Spinner spinner;
    Toolbar toolbar;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbarTitle);

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
                String oldCategory = ThemederApp.getInstance().getRepo().getPropertyString(getString(R.string.SPcategory));
                ThemederApp.getInstance().getRepo().setPropertyString(getString(R.string.SPcategory), parent.getItemAtPosition(position).toString());
                String newCategory = ThemederApp.getInstance().getRepo().getPropertyString(getString(R.string.SPcategory));
                Fragment fragment= getSupportFragmentManager()
                        .findFragmentByTag(getResources().getString(R.string.menu_mainscreen));
                if (fragment instanceof MainScreenFragment) {
                    if(!oldCategory.equals(newCategory)) {
                        ((MainScreenFragment) fragment).reloadAdapter();
                        Log.d("qqqq","onItemSelected");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new MainScreenFragment(), getResources().getString(R.string.menu_mainscreen))
                    .commit();
        }
        else {
            spinner.setVisibility(savedInstanceState.getInt(S_TAG));
        }
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment;
        String tag;
        switch(menuItem.getItemId()) {
            case R.id.settings:
                fragment = new SettingsFragment();
                toolbarTitle.setText(R.string.text_settings);
                tag=getResources().getString(R.string.text_settings);
                spinner.setVisibility(View.GONE);
                break;
            case R.id.add_photo:
                fragment = new AddPhotoFragment();
                toolbarTitle.setText(R.string.text_addphoto);
                tag=getResources().getString(R.string.menu_addphoto);
                spinner.setVisibility(View.GONE);
                break;
            case R.id.favorites:
                fragment = new FavoritesFragment();
                toolbarTitle.setText(R.string.text_favorites);
                tag=getResources().getString(R.string.menu_favorites);
                spinner.setVisibility(View.GONE);
                break;
            default:
                fragment = new MainScreenFragment();
                tag=getResources().getString(R.string.menu_mainscreen);
                spinner.setVisibility(View.VISIBLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit();

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putInt(S_TAG, spinner.getVisibility());
    }
}