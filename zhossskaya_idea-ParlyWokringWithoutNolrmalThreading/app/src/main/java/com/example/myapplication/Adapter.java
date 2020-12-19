package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    protected final IListener mListener;


    public Adapter(List<ItemPhoto> data, IListener listener) {

        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View layout = inflater.inflate(R.layout.photo_item, parent, false);

        return new ViewHolder(layout, mListener);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position){


    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
