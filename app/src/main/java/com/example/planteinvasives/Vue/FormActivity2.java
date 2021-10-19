package com.example.planteinvasives.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FormActivity2 extends AppCompatActivity {

    private String photopath,description, nomPlante, nom, prenom, date;
    private int latitude  , longitude, idfiche;
    private Spinner spinnerLieu, spinnerSurface, spinnerIndividu;
    private CheckBox vegetatif, enFleur, enFruit;
    private CheckBox plantule, jeuneplant, plant;
    private EditText remarques;
    private Button btnvalider;
    private String etat = "", stade = "";
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;
    private Controle controle;
    private int UPDATE ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);

        remarques = findViewById(R.id.remarques);
        spinnerLieu = findViewById(R.id.Slieu);
        spinnerSurface = findViewById(R.id.Ssurface);
        spinnerIndividu = findViewById(R.id.Sindividu);
        vegetatif = findViewById(R.id.Cvegetatif);
        enFleur = findViewById(R.id.Cfleur);
        enFruit = findViewById(R.id.Cfruit);
        plantule = findViewById(R.id.Cplantule);
        jeuneplant = findViewById(R.id.Cjeuneplant);
        plant = findViewById(R.id.Cplant);
        btnvalider = findViewById(R.id.btnvalideForm2);


        //recupere les données du formulaire 1
        Intent intent = getIntent();
        if (intent != null){
            photopath = intent.getStringExtra("photopath");
            date = intent.getStringExtra("date");
            description = intent.getStringExtra("description");
            nom = intent.getStringExtra("nom");
            prenom = intent.getStringExtra("prenom");
            nomPlante = intent.getStringExtra("nomplante");
            UPDATE= Integer.parseInt(intent.getStringExtra("UPDATE"));
            Log.d("tes recup form", "************"+ nomPlante+ photopath + nom + description + prenom );
        }
        if(UPDATE == 100){

            // recupere l'id de la fiche
            Log.d("ID FICHE", "*******************: "+intent.getStringExtra("idfiche"));
            idfiche = Integer.parseInt(intent.getStringExtra("idfiche"));
            Log.d("ID FICHE", "*******************: "+idfiche);
            // regle les spinners sur la bonne position
            updatePosSpinner(spinnerLieu,intent.getStringExtra("typelieu"),4);
            updatePosSpinner(spinnerIndividu,intent.getStringExtra("nbindividu"),5);
            updatePosSpinner(spinnerSurface,intent.getStringExtra("surface"),4);

            //checkbox
            updateCheckbox(plant,intent.getStringExtra("stade"));
            updateCheckbox(jeuneplant,intent.getStringExtra("stade"));
            updateCheckbox(plantule,intent.getStringExtra("stade"));
            updateCheckbox(vegetatif,intent.getStringExtra("etat"));
            updateCheckbox(enFleur,intent.getStringExtra("etat"));
            updateCheckbox(enFruit,intent.getStringExtra("etat"));

            remarques.setText(intent.getStringExtra("remarques"));


        }
        //evenements
        btnvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertion dans la base de données

                //recupere les valeurs checkboxes
                verifEtat();
                verifstade();

                Photographie unephoto = new Photographie(photopath,date);
                Plante uneplante = new Plante(nomPlante,etat,stade, description);
                Lieu unlieu = new Lieu(spinnerLieu.getSelectedItem().toString(),
                        spinnerSurface.getSelectedItem().toString(), spinnerIndividu.getSelectedItem().toString(),1,1,remarques.getText().toString());

                controle = Controle.getInstance(FormActivity2.this);

                if(UPDATE == 100){
                    /********cas update de l'existant*********/
                    unlieu.setId_lieu(idfiche);
                    uneplante.setId_plante(idfiche);
                    controle.lieuDao().update(unlieu);
                    controle.planteDao().update(uneplante);
                    Log.d("CAS UPDATE", "******onClick:");

                }else{
                    /********* cas premiere insertion*******/
                    Log.d("CAS INSERT", "******onClick:");
                    controle.ficheDao().insert(new Fiche(unephoto,uneplante,unlieu));
                    controle.photoDao().insert(unephoto);
                    controle.planteDao().insert(uneplante);
                    controle.lieuDao().insert(unlieu);
                }


            }
        });

        //Gestion de la navbar
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_form);
        eventNav   = new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MenuHome:
                        Intent intent = new Intent(FormActivity2.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(FormActivity2.this, PhotoActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.MenuProfil:
                        Intent intent3 = new Intent(FormActivity2.this, AdminActivity.class);
                        startActivity(intent3);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);
    }

    /**
     *
     */
    public void verifEtat(){
        if (vegetatif.isChecked()){
            etat = vegetatif.getText().toString() +"  ";
        }
        if (enFruit.isChecked()){
            etat = etat + enFruit.getText().toString() + "  ";
        }
        if(enFleur.isChecked()){
            etat = etat+ enFleur.getText().toString();
        }
    }

    /**
     *
     */
    public void verifstade(){
        if (plantule.isChecked()){
            stade = plantule.getText().toString() +"  ";
        }
        if (jeuneplant.isChecked()){
            stade = stade + jeuneplant.getText().toString() + "  ";
        }
        if(plant.isChecked()){
            stade = stade+ plant.getText().toString();
        }
    }

    /**
     * met a jour la position du spinner
     * @param spinner
     * @param target
     * @param nbrow
     */
    public void updatePosSpinner(Spinner spinner, String target,int nbrow){
        int row = 0;
        for (int i =0;i<nbrow;i++){
            Log.d("TAG", "updatespinner: " +spinner.getSelectedItem().toString()+ "target"+target);
            spinner.setSelection(i);
            if(spinner.getSelectedItem().toString().equals(target)){
                Log.d("MATCHH", "updatespinner: " +spinner.getSelectedItem().toString()+ "target"+target);
                row = i;
            }
        }
        spinner.setSelection(row);
    }

    /**
     * coche les checkboxes par rapport au champs de la bdd
     * @param checkBox
     * @param target
     */
    public void updateCheckbox(CheckBox checkBox, String target){
        String[] tab;
        tab = target.split("  ");
        for (int i = 0; i < tab.length ; i++) {
            if(checkBox.getText().toString().equals(tab[i]) && !checkBox.isChecked()){
                checkBox.setChecked(true);
            }
        }

    }

}
