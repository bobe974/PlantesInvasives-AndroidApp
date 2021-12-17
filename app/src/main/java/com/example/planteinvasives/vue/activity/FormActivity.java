package com.example.planteinvasives.vue.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.map.MapBoxActivity;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Eleve;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
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
    private Button valideFiche, btnnext,btnprevious;
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
        //page en plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        valideFiche =  findViewById(R.id.btnvalideFiche);
        btnnext = findViewById(R.id.btnnextFiche);
        btnprevious = findViewById(R.id.btnpreviousFiche);
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

            //affiche les boutton suivant et précédent
            btnnext.setVisibility(View.VISIBLE);
            btnprevious.setVisibility(View.VISIBLE);


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
        System.out.println("path"+ photoPath);
        Bitmap bitmap = photoActivity.loadImageFromStorage(photoPath,this);
        photo.setImageBitmap(bitmap);

        //rempli le spinner
        loadSpinnerData();

        //EVENT
        /*****************TEST******************/
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int idnext =  getNextFiche(fiche.getId_fiche());
                if(idnext != 0){
                    //on affecte la nouvelle instance a la fiche de la classe
                    nom.setVisibility(View.VISIBLE);
                    prenom.setVisibility(View.VISIBLE);
                    fiche = loadFiche(idnext);
                    if (etatEleve == 1){
                        nom.setVisibility(View.INVISIBLE);
                        prenom.setVisibility(View.INVISIBLE);
                    }
                    photoPath = fiche.getPhoto().getChemin();
                    date = fiche.getPhoto().getDate();
                    latitude = String.valueOf(fiche.getLieu().getLatittude());
                    longitude = String.valueOf(fiche.getLieu().getLongitude());
                    nomEtablissement = fiche.getNom_etablissement();

                    //prérempli les champs du formulaire
                    Eleve uneleve = loadEleve(fiche.getId_fiche());
                    loadfield(fiche,uneleve);
                    //charge la nouvelle photo
                    Bitmap bitmap = photoActivity.loadImageFromStorage(fiche.getPhoto().getChemin(),FormActivity.this);
                    photo.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(FormActivity.this," Dernière fiche!",Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idnext =  getPreviousFiche(fiche.getId_fiche());

                if(idnext != 0){
                    nom.setVisibility(View.VISIBLE);
                    prenom.setVisibility(View.VISIBLE);
                    //on affecte la nouvelle instance a la fiche de la classe
                    fiche = loadFiche(idnext);
                    if (etatEleve == 1){
                        nom.setVisibility(View.INVISIBLE);
                        prenom.setVisibility(View.INVISIBLE);
                    }
                    photoPath = fiche.getPhoto().getChemin();
                    date = fiche.getPhoto().getDate();
                    latitude = String.valueOf(fiche.getLieu().getLatittude());
                    longitude = String.valueOf(fiche.getLieu().getLongitude());
                    nomEtablissement = fiche.getNom_etablissement();

                    //prérempli les champs du formulaire
                    Eleve uneleve = loadEleve(fiche.getId_fiche());
                    loadfield(fiche,uneleve);
                    //charge la nouvelle photo
                    Bitmap bitmap = photoActivity.loadImageFromStorage(fiche.getPhoto().getChemin(),FormActivity.this);
                    photo.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(FormActivity.this,"Première fiche!",Toast.LENGTH_SHORT).show();
                }


            }
        });
        /*****************TEST******************/
        valideFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //envoie et lance l'activity activityform2
                Intent intent = new Intent(FormActivity.this, FormActivity2.class);
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

        //agrandissement de l'image
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //envoie du path a pour afficher la preview
                Intent intent = new Intent(FormActivity.this,PreviewActivity.class);
                intent.putExtra("chemin",photoPath);
                startActivity(intent);
            }
        });
        //event focus pour cacher le clavier
        setEventOnFocus(description.getEditText());
        setEventOnFocus(nom.getEditText());
        setEventOnFocus(prenom.getEditText());

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
                        //protégé par un mot de passe
                        String mdp = "gabon";

                        // on recupere la vue de la fenetre contextuelle
                        LayoutInflater li = LayoutInflater.from(FormActivity.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                FormActivity.this);

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
                                                    Intent intent3 = new Intent(FormActivity.this, AdminActivity.class);
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

                    case R.id.MenuMap:
                        Intent intent4 = new Intent(FormActivity.this, MapBoxActivity.class);
                        startActivity(intent4);
                        return true;

                    case R.id.MenuFiche:
                        Intent intent5 = new Intent(FormActivity.this, FicheActivity.class);
                        startActivity(intent5);
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

    /**
     * cache le clavier par default
     * @param view
     */
    public void hideSoftKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * ajoute l'event onfocus sur une edittext
     * @param editText
     */
    public void setEventOnFocus(EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    Log.d("focus", "focus lost");
                    hideSoftKeyboard(view);
                } else {
                    Log.d("focus", "focused");
                }
            }
        });
    }

    /**
     * retourne l'id de fiche a  la pos suivante
     * le premier id de la base commence a 1
     * @param currentid
     * @return
     */
    public int getNextFiche(int currentid){
        List<Fiche> fiches;
        int next = 0;
        fiches = Controle.getInstance(getApplicationContext()).getAllFiche();
        //parcours des fiches
        for (int i = 0; i < fiches.size() ; i++) {

            if(fiches.get(i).getId_fiche() == currentid){
                //System.out.println("fiche n:" + fiches.get(i).getId_fiche() );
                //System.out.println("current id:" + currentid );
                //cas fiche precedente (liste inversé car odre decroissant donc fiche suivant en vérité)
                //cas fiche suivante
                if(i != 0){
                    next = fiches.get(i-1).getId_fiche();
                }

            }
        }


        return next;
    }

    /**
     * retourne l'id de la fiche de la  pos suivante
     * le premier id de la base commence a 1
     * @param currentid
     * @return
     */
    public int getPreviousFiche(int currentid){
        List<Fiche> fiches;
        int previous = 0;
        fiches = Controle.getInstance(getApplicationContext()).getAllFiche();
        //parcours des fiches
        for (int i = 0; i < fiches.size() ; i++) {

            if(fiches.get(i).getId_fiche() == currentid){
                //cas fiche precedente (liste inversé car odre decroissant donc fiche suivant en vérité)
                //cas fiche suivante
                if(i < fiches.size()-1){
                    System.out.println("current "+currentid);
                    System.out.println("match i= "+i  );
                    System.out.println("fiche size = "+fiches.size()  );
                    previous= fiches.get(i+1).getId_fiche();
                }
            }
        }
        System.out.println("precedent "+previous);

        return previous;
    }
}