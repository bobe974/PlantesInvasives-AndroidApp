package com.example.planteinvasives.map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class MapBoxActivity extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap map;
    private Controle controle;
    private List<Fiche> lesfiches;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapbox);
        Mapbox.getInstance(this,getString(R.string.access_token));
        mapView = findViewById(R.id.mapboxid);
        mapView.onCreate(savedInstanceState);

        //chargement des fiches depuis la base
        controle = Controle.getInstance(this);
        lesfiches = controle.getAllFiche();



        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                LatLng posReunion = new LatLng(Controle.getGpsTracker().getLatitude(), Controle.getGpsTracker().getLongitude());
                ArrayList<MarkerOptions> lesmarkers = loadMarker();


                for (MarkerOptions unmarkerOptions : lesmarkers){
                    mapboxMap.addMarker(unmarkerOptions);

                }

                //position camera
                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                        .target(posReunion)
                        .zoom(18) //9.6
                        .bearing(10)
                        .build());


                /**
                 * On affiche une view sur un marker
                 */
                map.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull com.mapbox.mapboxsdk.annotations.Marker marker) {

                        View v = getLayoutInflater().inflate(R.layout.custom_marker_display, null);
                        TextView titre = (TextView) v.findViewById(R.id.title);
                        TextView snippet = (TextView) v.findViewById(R.id.snippet);
                        TextView remarques = v.findViewById(R.id.cardremarques);
                        ImageView imgDisplay = (ImageView) v.findViewById(R.id.imageDisplay);

                        for (Fiche f : lesfiches){
                            System.out.println("**************Cmarker "+marker.getSnippet());
                            System.out.println("**************Cfiche "+f.getPhoto().getChemin());

                            if (f.getPhoto().getChemin().equals(marker.getSnippet())) {
                                titre.setText(f.getPlante().getNom());
                                snippet.setText("Description: "+f.getPlante().getDescription());
                                remarques.setText("Remarques: "+ f.getLieu().getRemarques());
                                Glide.with(getApplicationContext())
                                        .load(f.getPhoto().getChemin())
                                        .centerCrop()
                                        .into(imgDisplay);
                                System.out.println("**************GLIDE");
                            }
                        }

                        v.setPadding(0, 0, 0, 30);
                        return v;
                    }
                });
            }



        });



    }

    /**
     * créer un marqueur pour chaque fiche
     * @return
     */
    public ArrayList<MarkerOptions>loadMarker(){
        List<Fiche> lesfiches;
        ArrayList<MarkerOptions> lesmarkers = new ArrayList<>();

        lesfiches = Controle.getInstance(this).getAllFiche();
        for(Fiche unefiche : lesfiches){
            lesmarkers.add(new MarkerOptions().title(unefiche.getPlante().nom)
                    .snippet(unefiche.getPhoto().getChemin())
                    .position(new LatLng(unefiche.getLieu().getLatittude(),unefiche.getLieu().getLongitude())));
            System.out.println("nom "+unefiche.getPlante().nom+"lat "+unefiche.getLieu().getLatittude()+ "long" +unefiche.getLieu().getLongitude());
        }
        return  lesmarkers;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
