package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class FavoritesFragment extends Fragment{
    protected FavoritesPhotoRep model;
    protected IListener mListener;
    private boolean clearR = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (requireActivity() instanceof IListener) {
            mListener = (IListener) requireActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //подписка
       }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    private void doMVM(){
        model = ViewModelProviders.of(this).get(FavoritesPhotoRep.class);
        final RecyclerView recycler = Objects.requireNonNull(getView()).findViewById(R.id.recycler);
        GridLayoutManager mLayout = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.cols), LinearLayoutManager.VERTICAL, false);
        LiveData<List<String>> data = model.getPhotoList();
        data.observe(getViewLifecycleOwner(), photos -> {
            Adapter mAdapter = new Adapter(photos, new ClickChecker());
            recycler.setAdapter(mAdapter);
        });
        recycler.setLayoutManager(mLayout);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                clearR = true;
                doMVM();
            }
        }else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)){
                clearR = true;
                doMVM();
            }
        }else{
            clearR = true;
            doMVM();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (clearR) {
            model.clear();
            mListener = null;
        }
    }

    class ClickChecker implements IListener{
        @Override
        public void onClicked(String picPath) {
            if (mListener != null) {
                Log.d("FavoritesFragment", ""+picPath);
                mListener.onClicked(picPath);
            }
        }
    }


}
