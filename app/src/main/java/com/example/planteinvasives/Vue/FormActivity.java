package com.example.planteinvasives.Vue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.planteinvasives.Modele.MaFiche;
import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenuView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity {

    private String photoPath, date;
    private Cursor cursor;
    private  Controle controle;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private String latitude, longitude;
    private PhotoActivity photoActivity;
    private int UPDATE;
    private Fiche fiche;

    private Button valideFiche;
    public ImageView photo;
    private EditText description;
    private EditText nom;
    private EditText prenom;
    private Spinner spinner;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        valideFiche =  findViewById(R.id.btnvalideFiche);
        navbar =  findViewById(R.id.bottom_navigation);
        photo =  findViewById(R.id.imgPhoto);
        description = findViewById(R.id.description);
        nom =  findViewById(R.id.nom);
        prenom =  findViewById(R.id.prenom);
        spinner = findViewById(R.id.spinnerform);


        //recupere le chemin absolu et la date de la photo
        Intent intent = getIntent();
        UPDATE= Integer.parseInt(intent.getStringExtra("update"));
        /**cas update**/
        if(UPDATE == 100){
            Log.d("***********", "CAS UPDATE");
            //charge les données depuis la base
            fiche = loadFiche(Integer.parseInt(intent.getStringExtra("idfiche")));
            photoPath = fiche.getPhoto().getChemin();
            latitude = String.valueOf(fiche.getLieu().getLatittude());
            longitude = String.valueOf(fiche.getLieu().getLongitude());
            //prérempli les champs fu formulaire
            loadfield(fiche);
        }else{
            /** cas insertion **/
            photoPath = intent.getStringExtra("path");
            date = intent.getStringExtra("date");
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
            Log.d("RECUP LATTI LONGI", "***************** latitude:"+latitude+"longitude"+longitude);

            Log.d("RECUP P PATH", photoPath +"date "+ date);

        }

        //charge la photo dans l'imageview
        photoActivity.loadImageFromStorage(photoPath, photo);

        //rempli le spinner
        loadSpinnerData();

        valideFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //envoye et lance l'activity activityform2
                Intent intent = new Intent(FormActivity.this,FormActivity2.class);
                intent.putExtra("photopath",photoPath);
                intent.putExtra("date",date);
                intent.putExtra("description",description.getText().toString());
                intent.putExtra("nom",nom.getText().toString());
                intent.putExtra("prenom",prenom.getText().toString());
                intent.putExtra("nomplante",spinner.getSelectedItem().toString());
                intent.putExtra("UPDATE","0");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);

                //envoie des champs supplémentaire si update
                if(UPDATE == 100){
                    intent.putExtra("UPDATE","100");
                    Log.d("IDFICHEFORM1", "***************" + fiche.getId_fiche());
                    intent.putExtra("idfiche",String.valueOf(fiche.getId_fiche()));
                    intent.putExtra("typelieu",fiche.getLieu().getType());
                    intent.putExtra("surface",fiche.getLieu().getSurface());
                    intent.putExtra("nbindividu",fiche.getLieu().getNbIndividu());
                    intent.putExtra("stade",fiche.getPlante().stade);
                    intent.putExtra("etat",fiche.getPlante().getEtat());
                    intent.putExtra("remarques",fiche.getLieu().getRemarques());
                }


                startActivity(intent);
            }
        });

        //Gestion de la navbar
        eventNav   = new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MenuHome:
                        Intent intent = new Intent(FormActivity.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(FormActivity.this, PhotoActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.MenuProfil:
                        Intent intent3 = new Intent(FormActivity.this, AdminActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.MenuMap:
                        Intent intent4 = new Intent(FormActivity.this, MapActivity.class);
                        startActivity(intent4);
                        return true;

                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);

    }


    /**
     * alimente le spinner avec la table Spinnerdata
     */
    private void loadSpinnerData() {
        controle = Controle.getInstance(this);
        cursor = controle.spinnerDataDao().getAll();
        cursor.moveToFirst();

        List<String> leslabels = new ArrayList<String>();

        //parcours du cursor
        while (!cursor.isAfterLast()){

            leslabels.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        // Creation d'un apdater pour le spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,leslabels );
        spinner.setAdapter(dataAdapter);
    }

    /**
     * retourne une fiche correspondant a l'id en paramatre depuis la bdd
     * @param id
     * @return
     */
    public Fiche loadFiche(int id){
        Log.d("***********", "loadFiche: ");
        Fiche fiche;
        controle = Controle.getInstance(this);
        Cursor cursor = controle.ficheDao().getById(id);
        cursor.moveToFirst();
        Log.d("VALEURCURSOR","*********"+cursor.getString(2));
        Photographie unephoto = new Photographie(cursor.getString(2),cursor.getString(3));
        Plante uneplante = new Plante(cursor.getString(5),cursor.getString(6),
                cursor.getString(7), cursor.getString(8));

        Lieu unlieu = new Lieu(cursor.getString(10), cursor.getString(11),
                cursor.getString(12),cursor.getDouble(13),cursor.getDouble(14),cursor.getString(15));

        fiche = new Fiche(unephoto,uneplante,unlieu);
        fiche.setId_fiche(id);
        cursor.close();

        return fiche;

    }

    /**
     * prérempli les champs
     * @param fiche
     */
    public void loadfield(Fiche fiche){
        //spinner

        //description
        description.setText(fiche.getPlante().getDescription());
        // nom et prenom

    }

}
