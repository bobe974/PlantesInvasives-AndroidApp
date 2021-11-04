package com.example.planteinvasives.Vue;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private MapView map;
    IMapController mapController; //interface
    private Controle controle;
    private Cursor cursor;
    GpsTracker gpsTracker;
    double latitude,longitude;
    GeoPoint geoPoint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instance opensstreetmap
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_map);

        //recuperer les données de la base
        controle = Controle.getInstance(this);
        cursor = controle.ficheDao().getAll();

        //parametrage map
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls(true); // active le zoom sur la map

        gpsTracker = new GpsTracker(MapActivity.this);

        //point de depart a la pos actuelle si gps active
        //sinon par defaut = la reunion
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            geoPoint = new GeoPoint(latitude,longitude);
            Log.d("GETLOCATION", "latitude: "+ latitude + "longitude"+longitude);
        }else{
            gpsTracker.showSettingsAlert();
            geoPoint = new GeoPoint(-21.08,55.32);
        }
        GeoPoint startpoint = geoPoint;

        mapController = map.getController();
        mapController.setCenter(startpoint); //centre de la map
        mapController.setZoom(20.0); //niveau de zoom par default

        //overlay items (point sur la map)
        ArrayList<OverlayItem> items = new ArrayList<>();

        //parcour cursor
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            items.add(new OverlayItem(cursor.getString(6),
                    cursor.getString(9),
                    new GeoPoint(cursor.getDouble(14), cursor.getDouble(15))));
            Log.d("MAPPPP", "$$$$$$$$$$$$$$ NOM"+cursor.getString(6)+" LATITUDE:"+ cursor.getDouble(14)+" LONGITUDE: "+cursor.getDouble(15));

        }

        cursor.close();


        //OverlayItem home = new OverlayItem("nom plante","sous titre", new GeoPoint(-21.16493,55.308284));
       // Drawable m =  home.getMarker(0); //forme du marqueur
       // items.add(home);

        //items.add(new OverlayItem("point2", "soustitre",new GeoPoint(-22.233,55.7373)));

        //associer la map et les overlays
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });

        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();

    }

    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
    }
}
