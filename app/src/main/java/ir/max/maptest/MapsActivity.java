package ir.max.maptest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener
        ,View.OnFocusChangeListener{

    private MapManager mapManager;

    private EditText v,v1;
    private EditText maxZoomLevel;

    private RadioButton normal;
    private RadioButton topographic;
    private RadioButton satellite;
    private RadioButton satelliteRoad;
    private RadioButton none;


    private CheckBox justOneMarker;
    private CheckBox zoomWhenAddMarker;

    private ToggleButton mFollow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapManager = new MapManager(this);

        v = findViewById(R.id.v);
        v1 = findViewById(R.id.v1);

        justOneMarker = findViewById(R.id.just_one_marker);
        justOneMarker.setOnCheckedChangeListener(this);

        normal = findViewById(R.id.normal);
        satellite = findViewById(R.id.satellite);
        satelliteRoad = findViewById(R.id.satellite_road);
        topographic = findViewById(R.id.topographic);
        none = findViewById(R.id.none);

        normal.setOnCheckedChangeListener(this);
        satelliteRoad.setOnCheckedChangeListener(this);
        satellite.setOnCheckedChangeListener(this);
        topographic.setOnCheckedChangeListener(this);
        none.setOnCheckedChangeListener(this);


        zoomWhenAddMarker = findViewById(R.id.zoom_when_add_marker);
        zoomWhenAddMarker.setOnCheckedChangeListener(this);

        maxZoomLevel = findViewById(R.id.max_zoom);
        maxZoomLevel.setOnFocusChangeListener(this);

        mFollow = findViewById(R.id.follow);
        mFollow.setOnCheckedChangeListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapManager);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

    }

    public void addMarkerClicked(View view){
        float v = Float.parseFloat(this.v.getText().toString());
        float v1 = Float.parseFloat(this.v1.getText().toString());
        mapManager.addMarker(new LatLng(v,v1));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id){
            case R.id.just_one_marker:
                mapManager.setJustOnMarker(b);
                break;
            case R.id.zoom_when_add_marker:
                mapManager.setZoomWhenAddMarker(b);
                break;
            case R.id.follow:
                mapManager.startFollow(b);
                break;
        }

        if (b){
            switch (id){
                case R.id.normal:
                    mapManager.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case R.id.satellite:
                    mapManager.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case R.id.satellite_road:
                    mapManager.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case R.id.topographic:
                    mapManager.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case R.id.none:
                    mapManager.setMapType(GoogleMap.MAP_TYPE_NONE);
                    break;
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();
        switch (id){
            case R.id.max_zoom:
                if (!b){
                    mapManager.setMaxZoomLevel(Float.parseFloat(this.maxZoomLevel.getText().toString()));
                }
        }

    }
}
