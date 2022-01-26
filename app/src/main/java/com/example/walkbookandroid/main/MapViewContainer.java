package com.example.walkbookandroid.main;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapViewContainer {
    MainActivity activity;
    MapView mapView;
    MapPOIItem currentMarker;

    public MapViewContainer(MainActivity activity) {
        this.activity = activity;
        this.mapView = new MapView(activity);
    }

    public MapView getMapView() {
        return mapView;
    }

    public void startLocationService() {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);

                createCurrentMarker(latitude, longitude);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void createCurrentMarker(double latitude, double longitude) {
        currentMarker = new MapPOIItem();
        currentMarker.setItemName("현재 위치");
        currentMarker.setTag(0);
        currentMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        currentMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        currentMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(currentMarker);
    }

    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
            activity.showToast("latitude : " + latitude + ", longitude : " + longitude);
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }
}
