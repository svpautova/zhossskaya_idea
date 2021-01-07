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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesFragment extends Fragment{

    protected FavoritesPhotoRep repo = new FavoritesPhotoRep();
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

        final RecyclerView recycler = view.findViewById(R.id.recycler);
        GridLayoutManager mLayout = new GridLayoutManager(getActivity(),
                3, LinearLayoutManager.VERTICAL, false);
        Adapter mAdapter = new Adapter(repo.list(), new ClickChecker());
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(mLayout);


    }
    @Override
    public void onDetach() {
        super.onDetach();
        repo.clear();
        repo=null;
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
