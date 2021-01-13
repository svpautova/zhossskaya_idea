package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.switchmaterial.SwitchMaterial;

import com.mikepenz.aboutlibraries.LibsBuilder;



import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    Spinner changeWallpaper;
    Button aboutButton;
    SwitchMaterial changeWallpaperSwitch;
    SwitchMaterial changeLockscreenSwitch;
    SwitchMaterial changeBothSwitch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        changeWallpaper = v.findViewById(R.id.changeOnce);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.wallpapers, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        changeWallpaper.setAdapter(adapter1);
        aboutButton = v.findViewById(R.id.info);
        aboutButton.setOnClickListener(this);
        changeWallpaper.setOnItemSelectedListener(this);
        changeWallpaperSwitch = v.findViewById(R.id.switch_periodic);
        changeLockscreenSwitch = v.findViewById(R.id.switch_periodic_lockscreen);
        changeBothSwitch = v.findViewById(R.id.switch_both_periodic);
        changeWallpaperSwitch.setChecked(ThemederApp.getInstance().getRepo().getPropertyBoolean(getString(R.string.switch_check)));
        if (changeWallpaperSwitch != null) {
            changeWallpaperSwitch.setOnCheckedChangeListener(this);
        }

        changeLockscreenSwitch.setChecked(ThemederApp.getInstance().getRepo().getPropertyBoolean(getString(R.string.switch_check_lock)));
        if (changeLockscreenSwitch != null) {
            changeLockscreenSwitch.setOnCheckedChangeListener(this);
        }
        changeBothSwitch.setChecked(ThemederApp.getInstance().getRepo().getPropertyBoolean(getString(R.string.switch_check_both)));
        if (changeBothSwitch != null) {
            changeBothSwitch.setOnCheckedChangeListener(this);
        }
        return v;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info) {

            LibsBuilder libsBuilder = new LibsBuilder()
                    //.withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .withActivityTitle(getString(R.string.info))
                    .withAboutIconShown(true)
                    .withAboutAppName("Themeder")
                    .withAboutVersionShown(true)
                    .withAboutDescription("Меняем обои с 2020 года.\n Приложение разработано в рамках курса Разработка приложений на Android")
                    //.withAboutSpecial1Description("мэйл")

                    //.withAutoDetect(false)
                    //.withLibraries("ucrop", "lifecycle:extensions")
                    .withExcludedLibraries("androidx_*");
            Log.d("info", "after libs");
            libsBuilder.start(getActivity());

        }

    }
    private static final int PERMISSION_REQUEST_CODE = 0;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        WallpaperChangerConstants.random = 1;
        if (buttonView.getId() == R.id.switch_periodic) {
            Log.d("!!!!!!", "switch wall"+ isChecked);
            WallpaperChangerConstants.wallpaperRegime = 1;
            ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check), isChecked);

        if (isChecked){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) ) {
                    periodicIntent("pwr");
                }
            }else {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)) {
                        periodicIntent("pwr");
                    }else{
                        changeWallpaperSwitch.setChecked(false);
                    }
                } else {
                    periodicIntent("pwr");
                }
            }
        }
        else {
            WorkManager.getInstance().cancelAllWorkByTag("pwr");
        }
        }
        else if (buttonView.getId() == R.id.switch_periodic_lockscreen) {
            Log.d("!!!!!!", "switch lock"+ isChecked);
            WallpaperChangerConstants.wallpaperRegime = 2;
            ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check_lock), isChecked);


            if (isChecked){

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) ) {
                        periodicIntent("pwr_lock");
                    }
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            periodicIntent("pwr_lock");
                        }
                    } else {
                        periodicIntent("pwr_lock");
                    }
                }
            }
            else {
                WorkManager.getInstance().cancelAllWorkByTag("pwr_lock");
            }
        }
        else if (buttonView.getId() == R.id.switch_both_periodic) {
            Log.d("!!!!!!", "switch both"+ isChecked);
            WallpaperChangerConstants.wallpaperRegime = 0;
            ThemederApp.getInstance().getRepo().setPropertyBoolean(getString(R.string.switch_check_both), isChecked);

            if (isChecked){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) ) {
                        periodicIntent("pwr_both");
                    }
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            periodicIntent("pwr_both");
                        }
                    } else {
                        periodicIntent("pwr_both");
                    }
                }
            }
            else {
                WorkManager.getInstance().cancelAllWorkByTag("pwr_both");
            }
        }
    }
    public void periodicIntent(String tag) {
        WorkManager workManager = WorkManager.getInstance();
        ExecutorService executorservice = Executors.newSingleThreadExecutor();
        Runnable runnable = () -> {
            Data myData = new Data.Builder()
                    .putString("keyA", "no")
                    .build();
            PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(PeriodicSetWallpaper.class, 1, TimeUnit.DAYS, 22, TimeUnit.HOURS)
                    .addTag(tag)
                    .setInputData(myData)
                    .build();
            workManager.enqueue(myWorkRequest);
        };
        executorservice.submit(runnable);
    }

    public void intentPositionOne(){
        Log.d("Change wallpaper", String.valueOf(WallpaperChangerConstants.wallpaperRegime));
        WallpaperChangerConstants.wallpaperRegime = 1;
        new Thread(() -> {
            WorkManager workManager = WorkManager.getInstance();
            Data myData = new Data.Builder()
                    .putString("keyA", "no")
                    .build();
            OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                    .setInputData(myData)
                    .build();
            workManager.enqueue(myWorkRequest);
            Log.d("!!!!!!", "click change");
        }).start();
    }


    public void intentPositionTwo(){
        Log.d("Change wallpaper", String.valueOf(WallpaperChangerConstants.wallpaperRegime));
        WallpaperChangerConstants.wallpaperRegime = 2;
        new Thread(() -> {
            WorkManager workManager = WorkManager.getInstance();

            Data myData = new Data.Builder()
                    .putString("keyA", "no")
                    .build();
            OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                    .setInputData(myData)
                    .build();
            workManager.enqueue(myWorkRequest);
            Log.d("!!!!!!", "click change");
        }).start();
    }

    public void intentPositionThree() {
        Log.d("Change wallpaper", String.valueOf(WallpaperChangerConstants.wallpaperRegime));
        WallpaperChangerConstants.wallpaperRegime = 0;
        new Thread(() -> {
            WorkManager workManager = WorkManager.getInstance();
            Data myData = new Data.Builder()
                    .putString("keyA", "no")
                    .build();
            OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(PeriodicSetWallpaper.class)
                    .setInputData(myData)
                    .build();
            workManager.enqueue(myWorkRequest);
            Log.d("!!!!!!", "click change");
        }).start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        WallpaperChangerConstants.random = 1;
        String text = parent.getItemAtPosition(position).toString();
        Log.d("onItemSelected", text + "pos" + String.valueOf(position));

            if (position == 1) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) ) {
                        intentPositionOne();
                    }
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            intentPositionOne();
                        }
                    } else {
                        intentPositionOne();
                    }
                }
            }else if (position == 2) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) ) {
                        intentPositionTwo();
                    }else {
                        changeWallpaperSwitch.setChecked(false);
                    }
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            intentPositionTwo();
                        }
                    } else {
                        intentPositionTwo();
                    }
                }
            } else if (position == 3) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) ) {
                        intentPositionThree();
                    }
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED)) {
                            intentPositionThree();
                        }
                    } else {
                        intentPositionThree();
                    }
                }
            }
        }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("Change wallpaper", String.valueOf(WallpaperChangerConstants.wallpaperRegime));
    }
}