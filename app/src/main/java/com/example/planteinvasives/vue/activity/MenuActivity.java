package com.example.planteinvasives.vue.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.planteinvasives.R;
import com.example.planteinvasives.geolocalisation.GpsTracker;
import com.example.planteinvasives.map.MapBoxActivity;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;

import java.util.List;

/**
 *Page principale de l'application
 * @author etienne baillif
 * @version 1.0
 */
public class MenuActivity extends AppCompatActivity {


    private CardView btncreateFiche, btnFiche, btnMap, btnParam;
    private Button btnUpdate;
    private TextView textlongitude, textlatitude;
    GpsTracker gpsTracker;
    Controle controle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //page en plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //lance la page de premiere utilisation si la base de données est vide
        controle = Controle.getInstance(this);
        List<SpinnerData> users;
        users = controle.spinnerDataDao().getAllUser();
        if (!(users.size()>0)) {
            startActivity(new Intent(this, InfoActivity.class));
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
        }else {
            gpsTracker.showSettingsAlert();
        }

        btncreateFiche =  findViewById(R.id.btnNewFiche);
        btnFiche =  findViewById(R.id.btnFiches);
        btnMap =  findViewById(R.id.btnMap);
        btnParam = findViewById(R.id.btnparam);
        btnUpdate = findViewById(R.id.btnactualiser);
        textlatitude = findViewById(R.id.labellattitude);
        textlongitude = findViewById(R.id.labellongitude);

        //affichage lattitude longitude
        textlatitude.setText("lattitude: " + Controle.getGpsTracker().getLatitude());
        textlongitude.setText("longitude: "+ Controle.getGpsTracker().getLongitude());


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
                Intent intent = new Intent(MenuActivity.this, MapBoxActivity.class);
                startActivity(intent);
            }
        });

        btnParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("****longlat="+Controle.getGpsTracker().getLongitude() +Controle.getGpsTracker().getLatitude());
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
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepos();
                System.out.println("update pos");
            }
        });

    }


    public void updatepos(){
        Controle.InstancieGps();
        //mise a jour affichage
        textlatitude.setText("lattitude: " + Controle.getGpsTracker().getLatitude());
        textlongitude.setText("longitude: "+ Controle.getGpsTracker().getLongitude());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatepos();
    }
}