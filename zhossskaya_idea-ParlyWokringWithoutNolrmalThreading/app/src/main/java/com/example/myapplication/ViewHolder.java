package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    protected final TextView mName;
    protected final IListener mListener;



    public ViewHolder(@NonNull View itemView, IListener listener) {
        super(itemView);

        mListener = listener;
        mName = itemView.findViewById(R.id.picture);

        final View.OnClickListener clickListener = v -> mListener.onClicked();

        itemView.setOnClickListener(clickListener);
    }

}