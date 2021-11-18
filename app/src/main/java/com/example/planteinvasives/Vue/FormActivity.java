package com.example.planteinvasives.Vue;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AutoCompleteTextView;
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
import com.example.planteinvasives.roomDataBase.entity.Eleve;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/** Affiche la premiere partie du  formulaire
 *Deux cas d'utilisation ->
 * premiere insertion et cas mise a jour du formulaire
 * cas premiere insertion: insertion basique d'un relevé
 * cas mise a jour: prérempli les champs et listes déroulantes du formumaire
 * @author etienne baillif
 * @version 1.0
 */

public class FormActivity extends AppCompatActivity {

    private String nomEtablissement;
    private int  etatEleve;
    private String photoPath, date;
    private Cursor cursor;
    private  Controle controle;
    private String latitude, longitude;
    private PhotoActivity photoActivity;
    private int UPDATE;
    private Fiche fiche;
    private Button valideFiche;
    public ImageView photo;
    private TextInputLayout description;
    private TextInputLayout nom;
    private TextInputLayout prenom;
    private AutoCompleteTextView spinner;
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

        //affichage ou non des champs concernant l'eleve
        //recupere la variable serialisé
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        etatEleve = sp.getInt("etatEleve", 0);
        nomEtablissement = sp.getString("etablissement","");

        Log.d("etabissement", "****** etab vaut: "+nomEtablissement);
        if (etatEleve == 1){
            nom.setVisibility(View.INVISIBLE);
            prenom.setVisibility(View.INVISIBLE);
        }


        Intent intent = getIntent();
        UPDATE= Integer.parseInt(intent.getStringExtra("update"));

        /**cas update**/
        if(UPDATE == 100){
            Log.d("***********", "CAS UPDATE");
            //charge les données depuis la base
            fiche = loadFiche(Integer.parseInt(intent.getStringExtra("idfiche")));
            Eleve eleve = loadEleve(Integer.parseInt(intent.getStringExtra("idfiche")));
            photoPath = fiche.getPhoto().getChemin();
            latitude = String.valueOf(fiche.getLieu().getLatittude());
            longitude = String.valueOf(fiche.getLieu().getLongitude());
            //prérempli les champs fu formulaire

            loadfield(fiche,eleve);


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

        //oriente la photo dans la bonne position
        photo.setRotation(photoActivity.getPhotoOrientation(photoPath));

        //rempli le spinner
        loadSpinnerData();

        valideFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //envoie et lance l'activity activityform2
                Intent intent = new Intent(FormActivity.this,FormActivity2.class);
                intent.putExtra("photopath",photoPath);
                intent.putExtra("date",date);
                intent.putExtra("description",description.getEditText().getText().toString());
                intent.putExtra("nom",nom.getEditText().getText().toString());
                intent.putExtra("prenom",prenom.getEditText().getText().toString());
                intent.putExtra("nomplante",spinner.getText().toString());
                intent.putExtra("UPDATE","0");
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("etablissement",nomEtablissement);
                intent.putExtra("etatEleve",etatEleve);

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
     * alimente une liste déroulante depuis une base de données
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
     * retourne une fiche par id depuis la bdd
     * @param id correcpond au champs id d'une table
     * @return un objet de type Fiche
     */
    public Fiche loadFiche(int id){

        Fiche fiche;
        controle = Controle.getInstance(this);
        Cursor cursor = controle.ficheDao().getById(id);
        cursor.moveToFirst();

        Photographie unephoto = new Photographie(cursor.getString(3),cursor.getString(4));
        Plante uneplante = new Plante(cursor.getString(6),cursor.getString(7),
                cursor.getString(8), cursor.getString(9));

        Lieu unlieu = new Lieu(cursor.getString(11), cursor.getString(12),
                cursor.getString(13),cursor.getDouble(14),cursor.getDouble(15),cursor.getString(16));

        fiche = new Fiche(unephoto,uneplante,unlieu);
        fiche.setId_fiche(id);
        cursor.close();

        return fiche;

    }

    /**
     * retourne les données d'un eleve par id depuis la base de données
     * @param id
     * @return un objet de type Eleve
     */
    public  Eleve loadEleve(int id){
        Cursor cursor = controle.eleveDao().getById(id);
        if(!isCursorEmpty(cursor)){
            cursor.moveToFirst();
            Eleve eleve = new Eleve(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            return  eleve;
        }
        return null;
    }


    /**
     * prérempli les champs et une liste déroulante
     * masque les champs nom et prenom si l'objet eleve en parametre n'est pas dans la base de données
     * lil n'est pas dans la base
     * @param fiche
     * @param eleve
     */
    public void loadfield(Fiche fiche, Eleve eleve){

        //description
        description.getEditText().setText(fiche.getPlante().getDescription());

        // nom et prenom
        if(eleve != null){
            nom.getEditText().setText(eleve.getNom());
            prenom.getEditText().setText(eleve.getPrenom());

            //spinner
            spinner.setText(fiche.getPlante().getNom(),false);
        }else{
            //si aucun eleve recuperer donc il est pas dans la base donc pas d'ajout ou update
            nom.setVisibility(View.INVISIBLE);
            prenom.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * verifie si le cursor est vide
     * @param cursor
     * @return
     */
    public boolean isCursorEmpty(Cursor cursor){
        return !cursor.moveToFirst() || cursor.getCount() == 0;
    }
}