package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class PicDetailsFragment extends Fragment{

    protected static final String KEY = "PicDetailsKEY";

    public static PicDetailsFragment newInstance(String picPath) {

        final Bundle extras = new Bundle();
        extras.putSerializable(KEY, (Serializable) picPath);

        final PicDetailsFragment fragment = new PicDetailsFragment();
        fragment.setArguments(extras);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picdetails, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Uri picUri = Uri.parse(info());

        if (picUri != null) {
            final ImageView mPic = view.findViewById(R.id.detailsPic);
            Glide.with(mPic).load(picUri).into(mPic);
            }
        }

    public String info(){
        if (getArguments() == null) {
            return null;
        }

        final Serializable picPath = getArguments().getSerializable(KEY);
        return (String) picPath;
    }

}
