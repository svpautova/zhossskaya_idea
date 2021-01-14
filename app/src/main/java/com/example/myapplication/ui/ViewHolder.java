package com.example.myapplication.ui;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.utils.IListener;
import com.example.myapplication.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    protected final ImageView mPic;
    protected final IListener mListener;
    String mPicPath;

    public ViewHolder(@NonNull View itemView, IListener listener) {
        super(itemView);

        mListener = listener;
        mPic = itemView.findViewById(R.id.picture);

        final View.OnClickListener clickListener = v -> mListener.onClicked(mPicPath);

        itemView.setOnClickListener(clickListener);
    }


    void bind(String picPath) {
        mPicPath=picPath;
        Glide.with(mPic).load(Uri.parse(picPath)).into(mPic);
    }
}