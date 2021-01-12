package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainScreenFragment extends Fragment implements CardStackListener {

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        if (savedInstanceState != null) {

            Bitmap bitmap = (Bitmap)savedInstanceState.getParcelable("picture");
            // Convert Bitmap to Drawable:
            picture = new BitmapDrawable(bitmap);
        }
    }

    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap bitmap = (Bitmap)((BitmapDrawable) picture).getBitmap();
        outState.putParcelable("picture", bitmap);
    }

    private static final int PERMISSION_REQUEST_CODE = 3;
    FloatingActionButton skipButton;
    FloatingActionButton likeButton;
    CardStackLayoutManager manager;
    CardStackAdapter adapter;
    CardStackView cardStackView;
    GetPhotos model;
    Drawable picture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        cardStackView = v.findViewById(R.id.card_stack_view);
        skipButton = v.findViewById(R.id.skip_button);
        likeButton = v.findViewById(R.id.like_button);
        manager = new CardStackLayoutManager(Objects.requireNonNull(getContext()).getApplicationContext(), this);
        model = ViewModelProviders.of(this).get(GetPhotos.class);
        setupCardStackView();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("ddd", "onViewCreated");
        changeCategory();
        likeButton.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().setDirection(Direction.Right)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        skipButton.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder().setDirection(Direction.Left)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });


    }

    private void setupCardStackView() {
        initialize();
    }


    private void initialize() {
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentPressButton();
                }
            }else{
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    intentPressButton();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void intentPressButton(){
        Buttons.Like_button(picture);
    }

    @Override
    public void onCardSwiped(Direction direction) {

        Log.d("CardStackView", "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
        if(direction==Direction.Right){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) ) {
                    intentPressButton();
                }else {
                    requestPermissions(
                            new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            PERMISSION_REQUEST_CODE);
                }
            }else{
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if ((ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)) {
                        intentPressButton();
                    }else {
                        requestPermissions(
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },
                                PERMISSION_REQUEST_CODE);
                    }
                }else{
                    intentPressButton();
                }
            }
        }
        if(direction==Direction.Left) {
            Log.d("MainScreenFragment", "Swipe Left");
        }
        if (manager.getTopPosition() == adapter.getItemCount()-2){
            Log.d("MainScreenFragment", "Paginate");
            paginate();
        }
    }

    @Override
    public void onCardRewound() {
        Log.d("CardStackView", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: " + manager.getTopPosition());
    }

    @Override
    public void onCardAppeared(View view, int position) {
        Log.d("CardStackView", "onCardAppeared: " + position);


    }

    @Override
    public void onCardDisappeared(View view, int position) {
        ImageView imageView = view.findViewById(R.id.item_image);
        picture = imageView.getDrawable();
        Log.d("CardStackView", "onCardDisappeared: " + position);
    }

    private void paginate() {
        List<Photo> old = adapter.getItems();
        LiveData<List<Photo>> data = model.getImage(5);
        data.observe(getViewLifecycleOwner(), photos -> {
            Random r = new Random(56);
            int num = r.nextInt(5)-1;
            old.remove(0);
            old.remove(0);
            old.add(photos.get(num));
            old.add(photos.get(inFive(num+1)));
            adapter.notifyDataSetChanged();
        });

    }


    private int inNine(int num){
        if (num>9){
            return num-9;
        }
        else{
            return num;
        }
    }
    private int inFive(int num){
        if (num>5){
            return num-5;
        }
        else{
            return num;
        }
    }

    void changeCategory(){
        LiveData<List<Photo>> data = model.getImage(10);
        data.observe(getViewLifecycleOwner(), photos -> {
            Random r = new Random(33);
            int num = r.nextInt(10)-1;
            List<Photo> toAdapter = new ArrayList<>();
            toAdapter.add(photos.get(inNine(num)));
            toAdapter.add(photos.get(inNine(num+1)));
            toAdapter.add(photos.get(inNine(num+2)));
            toAdapter.add(photos.get(inNine(num+5)));
            adapter = new CardStackAdapter(toAdapter);
            cardStackView.setAdapter(adapter);
            Log.d("MainScreenFragment", "resetAdapter");
        });
    }

}

