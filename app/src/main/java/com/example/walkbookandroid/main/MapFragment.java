package com.example.walkbookandroid.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkbookandroid.R;

public class MapFragment extends Fragment {
    MainActivity activity;
    MapViewContainer mapViewContainer;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        activity = (MainActivity) rootView.getContext();

        mapViewContainer = new MapViewContainer(activity);
        ViewGroup mapViewContainer = rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(this.mapViewContainer.getMapView());

        this.mapViewContainer.startLocationService();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ViewGroup) mapViewContainer.getMapView().getParent()).removeView(mapViewContainer.getMapView());
    }
}
