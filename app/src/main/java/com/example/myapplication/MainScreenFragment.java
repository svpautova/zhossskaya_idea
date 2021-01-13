package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainScreenFragment extends Fragment implements CardStackListener {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final String S_TAG = "MainScreenFragmentSaveInstantState";

    FloatingActionButton skipButton;
    FloatingActionButton likeButton;
    FloatingActionButton refreshButton;
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
        refreshButton = v.findViewById(R.id.rewind_button);
        manager = new CardStackLayoutManager(getContext().getApplicationContext(), this);
        model = ViewModelProviders.of(this).get(GetPhotos.class);
        setupCardStackView();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isOnline(getContext().getApplicationContext())){

            if (savedInstanceState!=null){
                Log.d("qqqq","savedInstanceState");
                adapter = new CardStackAdapter(savedInstanceState.getStringArrayList(S_TAG));
                cardStackView.setAdapter(adapter);
            }
            else{
                Log.d("qqqq","!savedInstanceState");
                reloadAdapter();
            }

        }
         else{
             Toast toast =  Toast.makeText(getContext().getApplicationContext(),
                     getString(R.string.refresh_text),Toast.LENGTH_SHORT);
             toast.show();
             refreshButton.setVisibility(View.VISIBLE);
        }

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

        refreshButton.setOnClickListener(v -> {
            if(isOnline(getContext().getApplicationContext())){
                reloadAdapter();
                refreshButton.setVisibility(View.INVISIBLE);
            }
            else{
                Toast toast =  Toast.makeText(getContext().getApplicationContext(),
                        getString(R.string.refresh_text),Toast.LENGTH_SHORT);
                toast.show();
            }
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
                if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    if ((ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        List<String> old = adapter.getItems();
        if(isOnline(getContext().getApplicationContext())){
            LiveData<List<String>> data = model.getImage(5);
            data.observe(getViewLifecycleOwner(), photos -> {
                Random r = new Random(56);
                int num = r.nextInt(5)-1;
                old.remove(0);
                old.add(photos.get(num));
                adapter.notifyDataSetChanged();
            });
        }
        else{
            Toast toast =  Toast.makeText(getContext().getApplicationContext(),
                    getString(R.string.refresh_text),Toast.LENGTH_SHORT);
            toast.show();
            old.clear();
            adapter.notifyDataSetChanged();
            refreshButton.setVisibility(View.VISIBLE);
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

    void reloadAdapter(){
        LiveData<List<String>> data = model.getImage(5);
        data.observe(getViewLifecycleOwner(), photos -> {
            Random r = new Random(33);
            int num = r.nextInt(5)-1;
            List<String> toAdapter = new ArrayList<>();
            toAdapter.add(photos.get(num));
            toAdapter.add(photos.get(inFive(num+1)));
            toAdapter.add(photos.get(inFive(num+2)));
            adapter = new CardStackAdapter(toAdapter);
            cardStackView.setAdapter(adapter);

            Log.d("MainScreenFragment", "resetAdapter");
        });
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if (adapter!=null){
            savedInstanceState.putStringArrayList(S_TAG, (ArrayList<String>) adapter.getItems());
        }
    }
}

