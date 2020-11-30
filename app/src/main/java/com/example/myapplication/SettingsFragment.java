package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    Button changeWallpaper;
    SwitchMaterial changeWallpaperSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        changeWallpaper = v.findViewById(R.id.change_wallpaper_button);
        changeWallpaper.setOnClickListener(this);
        changeWallpaperSwitch = v.findViewById(R.id.switch_periodic);
        if (changeWallpaperSwitch != null) {
            changeWallpaperSwitch.setOnCheckedChangeListener(this);
        }
        return v;

    }

    @Override
    public void onClick(View v) {

        Buttons.Change_Wallpaper();
        Log.d("!!!!!!", "click change");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){

            Buttons.Switch_on();
            Log.d("!!!!!!", "switch on");
        }
        if(!isChecked){
            Log.d("!!!!!!", "switch off");
        }

    }
}