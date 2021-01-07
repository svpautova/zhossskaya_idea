package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoritesPhotoRep {

    private List<String> mData = new ArrayList<>();
    private Disposable disposable;
    protected FavoritesPhotoRep(){
        initializeData();
    }

    private void initializeData() {

        Observable<String> observable = Observable.just("1");
        disposable = observable.
                subscribeOn(Schedulers.io()).
                map(i->ThemederApp.getInstance().getRepo().getNamesImages()).
                subscribe(this::setmData);
    }

    private void setmData(List<String> data){
        mData=data;
    }

    public void clear(){
        disposable.dispose();
    }

    public List<String> list() {
        return mData;
    }

    public int size() {
        return mData.size();
    }

    public String item(int index) {
        return mData.get(index);
    }
}
