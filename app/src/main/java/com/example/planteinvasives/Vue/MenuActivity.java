package com.example.planteinvasives.Vue;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

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
    Controle controle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //lance la page de premiere utilisation si la base de données est vide
        controle = Controle.getInstance(this);
        List<SpinnerData> users;
        users = controle.spinnerDataDao().getAllUser();
        if (!(users.size()>0)) {
            startActivity(new Intent(this,InfoActivity.class));
        }

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
                    /******************/
                    case R.id.MenuProfil:
                        //protégé par un mot de passe
                        String mdp = "gabon";

                        // on recupere la vue de la fenetre contextuelle
                        LayoutInflater li = LayoutInflater.from(MenuActivity.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MenuActivity.this);

                        // on ajoute la fenetre a  alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        final EditText pssword = (EditText) promptsView
                                .findViewById(R.id.passwordField);

                        // ajout de la boite de dialogue
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // get user input and set it to result
                                                System.out.println("mdp: "+ pssword.getText().toString());
                                                if(pssword.getText().toString().equals(mdp)){
                                                    Intent intent3 = new Intent(MenuActivity.this, AdminActivity.class);
                                                    startActivity(intent3);
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Mot de passe Invalide", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                .setNegativeButton("Annuler",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // affichage
                        alertDialog.show();

                        return true;
                        /**********************/

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