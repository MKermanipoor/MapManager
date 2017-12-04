package ir.max.maptest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sazgar on 11/13/2017.
 */

public class MapManager implements OnMapReadyCallback {
    private boolean justOnMarker = true;
    private boolean zoomWhenAddMarker = true;

    private GoogleMap map;

    private Context mContext;

    private Polyline polyline = null;
    private PolylineOptions mPolylineOptions = new PolylineOptions();

    private List<Marker> mMarkers = new ArrayList<>();

    private LocationManager mLocationManager;
    int LOCATION_UPDATE_MIN_DISTANCE = 10;
    int LOCATION_UPDATE_MIN_TIME = 5000;

    private LocationListener mLocationFollowingListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if (polyline != null)
                    polyline.remove();

                mPolylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));

                polyline = map.addPolyline(mPolylineOptions);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public MapManager(final Context context) {
        this.mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public MapManager(final Context context, GoogleMap map){
        this.mContext = context;
        this.map = map;
    }

    public void startFollow(boolean start) {
        if (start) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                boolean isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isNetworkEnabled)
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME,
                            LOCATION_UPDATE_MIN_DISTANCE,
                            mLocationFollowingListener);

                if (isGpsEnabled)
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME,
                            LOCATION_UPDATE_MIN_DISTANCE,
                            mLocationFollowingListener);

            }
        }
        else{
            mLocationManager.removeUpdates(mLocationFollowingListener);
            mPolylineOptions = new PolylineOptions();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(0, 100);
        addMarker(latLng);

        setMaxZoomLevel(30);

        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }

    public Marker addMarker(LatLng position){
        if (map != null){
            if (justOnMarker) {
                for(Marker marker : mMarkers)
                    marker.remove();
                mMarkers.clear();
            }
            if (zoomWhenAddMarker)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 25));
            Marker temp = map.addMarker(new MarkerOptions().position(position));
            mMarkers.add(temp);
            return temp;
        }
        return null;
    }

    public Marker addMarker(float latitude, float longitude){
        return addMarker(new LatLng(latitude, longitude));
    }

    public boolean isJustOnMarker() {
        return justOnMarker;
    }

    public void setJustOnMarker(boolean justOnMarker) {
        this.justOnMarker = justOnMarker;
    }

    public boolean isZoomWhenAddMarker() {
        return zoomWhenAddMarker;
    }

    public void setZoomWhenAddMarker(boolean zoomWhenAddMarker) {
        this.zoomWhenAddMarker = zoomWhenAddMarker;
    }

    public float getMaxZoomLevel() {
        if (map != null)
            return map.getMaxZoomLevel();
        return 30;
    }

    public void setMaxZoomLevel(float maxZoomLevel) {
        if (map != null)
            map.setMaxZoomPreference(maxZoomLevel);
    }

    public void setMapType(int mapType){
        if (map != null){
            map.setMapType(mapType);
        }
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setFollowPolylineWidth(float width){
        mPolylineOptions.width(width);
    }

    public float getFollowPolylineWidth(){
        return mPolylineOptions.getWidth();
    }

    public void setFollowPolylineColor(int color){
        mPolylineOptions.color(color);
    }

    public void setFollowPolylineColor(int a,int r,int g, int b){
        setFollowPolylineColor(Color.argb(a, r, g, b));
    }

    public int getFollowPolylineColor(){
        return mPolylineOptions.getColor();
    }




//    @SuppressLint("MissingPermission")
//    private void getCurrentLocation() {
//        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//
//        Location location = null;
//        if (isNetworkEnabled) {
//            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationFollowingListener);
//            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        }
//
//        if (isGPSEnabled) {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationFollowingListener);
//            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        }
//
//        if (location != null){
//            addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
//        }
//    }


}
