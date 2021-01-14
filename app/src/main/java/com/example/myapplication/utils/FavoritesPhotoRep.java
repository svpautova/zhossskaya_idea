package com.example.myapplication.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoritesPhotoRep extends ViewModel {

    MutableLiveData<List<String>> mData;
    private Disposable disposable;
    
    public LiveData<List<String>> getPhotoList(){
        mData = new MutableLiveData<>();
        initializeData();
        return mData;
    }

    private void initializeData() {

        disposable = Single.fromCallable(()-> ThemederApp.getInstance().getRepo().getNamesImages())
                .subscribeOn(Schedulers.io())
                .subscribe(v->mData.postValue(v));

    }

    public void clear(){

        if(disposable!=null){
            disposable.dispose();
        }
    }
}
