package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.List;

public class MainScreenFragment extends Fragment implements CardStackListener {


    FloatingActionButton skipButton;
    FloatingActionButton rewindButton;
    FloatingActionButton likeButton;
    CardStackLayoutManager manager;
    CardStackAdapter adapter;
    CardStackView cardStackView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainscreen, container, false);
        cardStackView = v.findViewById(R.id.card_stack_view);
        skipButton = v.findViewById(R.id.skip_button);
        rewindButton = v.findViewById(R.id.rewind_button);
        likeButton = v.findViewById(R.id.like_button);
        manager = new CardStackLayoutManager(getContext().getApplicationContext(), this);
        LiveData<List<Photo>> data = GetPhotos.getImage();
        data.observe(this, photos -> {
            adapter = new CardStackAdapter(photos);
            cardStackView.setAdapter(adapter);
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCardStackView();
    }

    private void setupCardStackView() {
        initialize();
    }


    private void initialize() {
        manager.setStackFrom(StackFrom.Top);
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
        Log.d("CardStackView", "onCardDragging: d = "+ direction.name() + " ratio=" + ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {

        Log.d("CardStackView", "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
        if(direction==Direction.Right){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    2);
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            Buttons.Like_button(adapter);
            }
            Log.d("MainScreenFragment", "Swipe Right");
        }
        if(direction==Direction.Left) {
            Log.d("MainScreenFragment", "Swipe Left");
        }
        if (manager.getTopPosition() == adapter.getItemCount()){
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

        Log.d("CardStackView", "onCardDisappeared: " + position);
    }

    private void paginate() {
        List<Photo> old = adapter.getItems();
        LiveData<List<Photo>> data = GetPhotos.getImage();
        data.observe(this, photos -> {
            CardStackCallback callback = new CardStackCallback(old, photos);
            DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
            adapter.setItems(photos);
            hasil.dispatchUpdatesTo(adapter);
        });

    }

}

