package com.example.planteinvasives.Vue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.planteinvasives.R;

import com.example.planteinvasives.roomDataBase.Controle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 *Page principale de l'application
 * @author etienne baillif
 * @version 1.0
 */
public class MenuActivity extends AppCompatActivity {


    private Button btncreateFiche, btnFiche, btnMap;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;
    GpsTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //verifie si la localisation fonctionne et est activé
        gpsTracker = new GpsTracker(MenuActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Toast.makeText(this,"lattitude:"+latitude + " Longitude"+longitude,Toast.LENGTH_SHORT).show();
        }else {
            gpsTracker.showSettingsAlert();
        }

        btncreateFiche = (Button) findViewById(R.id.btnNewFiche);
        btnFiche = (Button) findViewById(R.id.btnFiches);
        btnMap = (Button) findViewById(R.id.btnMap);
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_menu);

        //EVENEMENTS
        btncreateFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //demarrer l'appareil photo
                Intent intent = new Intent(MenuActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });

        btnFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FicheActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });


        //Gestion de la navbar
        eventNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.MenuHome:

                        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(MenuActivity.this, PhotoActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.MenuProfil:
                        Intent intent3 = new Intent(MenuActivity.this, AdminActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.MenuMap:
                        Intent intent4 = new Intent(MenuActivity.this, MapActivity.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);
    }

}