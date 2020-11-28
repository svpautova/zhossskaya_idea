package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainScreenFragment extends Fragment implements View.OnClickListener {

    Button likeButton;
    Button dislikeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        likeButton = (Button) v.findViewById(R.id.like_button);
        likeButton.setOnClickListener(this);
        dislikeButton = (Button) v.findViewById(R.id.dislike_button);
        dislikeButton.setOnClickListener(this);
        return v;

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.like_button){

            Log.d("!!!!!!", "click OK");
        }
        if (v.getId()==R.id.dislike_button){

            Log.d("!!!!!!", "click decline");
        }

    }
}
