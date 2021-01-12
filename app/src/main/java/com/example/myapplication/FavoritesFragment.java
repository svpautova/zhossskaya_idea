package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesFragment extends Fragment{

    protected FavoritesPhotoRep model;
    protected IListener mListener;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (requireActivity() instanceof IListener) {
            mListener = (IListener) requireActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = ViewModelProviders.of(this).get(FavoritesPhotoRep.class);
        final RecyclerView recycler = view.findViewById(R.id.recycler);
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
    public void onDetach() {
        super.onDetach();
        model.clear();
        mListener=null;
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
