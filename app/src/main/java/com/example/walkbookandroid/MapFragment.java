package com.example.walkbookandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {
    MainActivity activity;
    Map map;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);
        activity = (MainActivity) rootView.getContext();

        map = new Map(activity);
        ViewGroup mapViewContainer = rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(map.getMapView());

        map.startLocationService();

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ViewGroup) map.getMapView().getParent()).removeView(map.getMapView());
    }
}
