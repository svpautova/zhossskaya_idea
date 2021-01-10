package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Observable;
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

        Observable<String> observable = Observable.just("1");
        disposable = observable.
                subscribeOn(Schedulers.io()).
                map(i->ThemederApp.getInstance().getRepo().getNamesImages()).
                subscribe(v->mData.postValue(v));
    }

    public void clear(){
        disposable.dispose();
    }
}
