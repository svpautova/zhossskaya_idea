package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.List;

public class MainScreenFragment extends Fragment implements CardStackListener {


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
        manager = new CardStackLayoutManager(getContext().getApplicationContext(), this);
        model = ViewModelProviders.of(this).get(GetPhotos.class);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCardStackView();

        LiveData<List<Photo>> data = model.getImage(1);
        data.observe(getViewLifecycleOwner(), photos -> {
            adapter = new CardStackAdapter(photos);
            cardStackView.setAdapter(adapter);

            Log.d("MainScreenFragment", "setAdapter");
        });

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
        //Log.d("CardStackView", "onCardDragging: d = "+ direction.name() + " ratio=" + ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {

        Log.d("CardStackView", "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
        if(direction==Direction.Right){
            Log.d("MainScreenFragment", "Swipe Right");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    2);
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            Buttons.Like_button(picture);
            }

        }
        if(direction==Direction.Left) {
            Log.d("MainScreenFragment", "Swipe Left");
        }


        if (manager.getTopPosition() == adapter.getItemCount()){
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

        //это пока не удалять запасной вариант!!!!!!!!!
        /*
        List<Photo> old = adapter.getItems();
        LiveData<List<Photo>> data = model.getImage();
        data.observe(getViewLifecycleOwner(), photos -> {
            CardStackCallback callback = new CardStackCallback(old, photos);
            DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
            adapter.setItems(photos);
            Log.d("MainScreenFragment", "setItems");
            hasil.dispatchUpdatesTo(adapter);
        });
*/
        List<Photo> old = adapter.getItems();
        LiveData<List<Photo>> data = model.getImage(1);
        data.observe(getViewLifecycleOwner(), photos -> {
            old.remove(0);
            old.addAll(photos);
            adapter.notifyDataSetChanged();

    });

    }

}

