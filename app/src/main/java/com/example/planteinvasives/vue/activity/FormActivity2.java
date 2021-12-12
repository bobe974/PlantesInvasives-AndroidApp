package com.example.planteinvasives.vue.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.planteinvasives.roomDataBase.entity.Eleve;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Affiche la seconde partie du  formulaire et recupere données qui ont été complété depuis FormActivity
 *deux cas d'utilisation
 * premiere insertion et cas mise a jour du formulaire
 * cas premiere insertion: insertion basique d'un relevé
 * cas mise a jour: prérempli les champs, listes déroulantes et checkbox du formumaire
 * @author etienne baillif
 * @version 1.0
 */
public class FormActivity2 extends AppCompatActivity {

    private String photopath,description, nomPlante, nom, prenom, date,nomEtablissement;
    private double latitude  , longitude;
    private int idfiche, etatEleve;
    private AutoCompleteTextView spinnerLieu, spinnerSurface, spinnerIndividu;
    private CheckBox vegetatif, enFleur, enFruit;
    private CheckBox plantule, jeuneplant, plant;
    private EditText remarques;
    private Button btnvalider, btretour;
    private String etat = "", stade = "";
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;
    private Controle controle;
    private  Cursor cursor;
    private int UPDATE;
    private String[] tablieu, tabsurface,tabindividu;





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
        btretour = findViewById(R.id.btnretour);
        //chargement des spinners
        Resources res = getResources();
        tablieu = res.getStringArray(R.array.typeDeLieu);
        tabsurface = res.getStringArray(R.array.surface);
        tabindividu = res.getStringArray(R.array.individu);

        ArrayAdapter adapterlieu = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tablieu);
        ArrayAdapter adaptersurface = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tabsurface);
        ArrayAdapter adapterindividu = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tabindividu);

        spinnerLieu.setAdapter(adapterlieu);
        spinnerSurface.setAdapter(adaptersurface);
        spinnerIndividu.setAdapter(adapterindividu);


        //recupere les données du formulaire 1
        Intent intent = getIntent();
        if (intent != null){
            photopath = intent.getStringExtra("photopath");
            date = intent.getStringExtra("date");
            description = intent.getStringExtra("description");
            nom = intent.getStringExtra("nom");
            prenom = intent.getStringExtra("prenom");
            nomPlante = intent.getStringExtra("nomplante");
            nomEtablissement = intent.getStringExtra("etablissement");
            etatEleve = intent.getIntExtra("etatEleve",1);
            UPDATE= Integer.parseInt(intent.getStringExtra("UPDATE"));

        }
        if(UPDATE == 100){

            // recupere l'id de la fiche
            idfiche = Integer.parseInt(intent.getStringExtra("idfiche"));

            // regle les spinners sur la bonne position
            spinnerLieu.setText(intent.getStringExtra("typelieu"),false);
            spinnerIndividu.setText(intent.getStringExtra("nbindividu"),false);
            spinnerSurface.setText(intent.getStringExtra("surface"),false);

            //checkbox
            updateCheckbox(plant,intent.getStringExtra("stade"));
            updateCheckbox(jeuneplant,intent.getStringExtra("stade"));
            updateCheckbox(plantule,intent.getStringExtra("stade"));
            updateCheckbox(vegetatif,intent.getStringExtra("etat"));
            updateCheckbox(enFleur,intent.getStringExtra("etat"));
            updateCheckbox(enFruit,intent.getStringExtra("etat"));

            remarques.setText(intent.getStringExtra("remarques"));

        }
        latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        longitude = Double.parseDouble(intent.getStringExtra("longitude"));

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
                Lieu unlieu = new Lieu(spinnerLieu.getText().toString(),
                        spinnerSurface.getText().toString(), spinnerIndividu.getText().toString(),latitude,longitude,remarques.getText().toString());

                controle = Controle.getInstance(FormActivity2.this);

                if(UPDATE == 100){
                    /********cas update de l'existant*********/
                    unlieu.setId_lieu(idfiche);
                    uneplante.setId_plante(idfiche);
                    controle.lieuDao().update(unlieu);
                    controle.planteDao().update(uneplante);

                    //si les champs de l'eleve sont pas vides et option eleve desactiver, on update
                    if(etatEleve == 0 && nom.length() != 0 && prenom.length() !=0){
                        //TODO si existant dans la base on update
                        controle.eleveDao().update(new Eleve(idfiche,nom,prenom));
                    }else{
                        // TODO sinon on insert dans la base l'eleve

                    }
                    Toast.makeText(getApplicationContext(), "fiche mis à jour", Toast.LENGTH_SHORT).show();

                }else{
                    /********* cas premiere insertion*******/
                    controle.ficheDao().insert(new Fiche(nomEtablissement));
                    controle.photoDao().insert(unephoto);
                    controle.planteDao().insert(uneplante);
                    controle.lieuDao().insert(unlieu);

                    // si option retirer eleve desactiver alors on insert
                    if(etatEleve == 0 && nom.length() != 0 && prenom.length() !=0){

                        //recupere le dernier idfiche et on affecte a l'eleve le memme id
                        cursor = controle.ficheDao().getLastId();
                        cursor.moveToFirst();
                        int lastid = cursor.getInt(0);
                        controle.eleveDao().insert(new Eleve(lastid,nom,prenom));
                        Log.d("CAS INSERT CAS ELEVE OK", "**********on insert");
                    }
                    Toast.makeText(getApplicationContext(), "fiche enregistré !", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(FormActivity2.this, MenuActivity.class);
                startActivity(intent);

            }
        });


        btretour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        //protégé par un mot de passe
                        String mdp = "gabon";

                        // on recupere la vue de la fenetre contextuelle
                        LayoutInflater li = LayoutInflater.from(FormActivity2.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                FormActivity2.this);

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
                                                    Intent intent3 = new Intent(FormActivity2.this, AdminActivity.class);
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
                        Intent intent4 = new Intent(FormActivity2.this, MapActivity.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);
    }

    /**
     *vérifie si les checkboxs etat sont cochés
     * si c'est le cas on recupere son libellé
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
     *vérifie si les checkboxs stade sont cochés
     * si c'est le cas on recupere son libellé
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
     * met à jour la position d'une liste déroulante
     * @param spinner liste déroulante du formulaire
     * @param target  champ a afficher
     * @param nbrow
     */
    public void updatePosSpinner(Spinner spinner, String target,int nbrow){
        int row = 0;
        for (int i =0;i<nbrow;i++){
            spinner.setSelection(i);
            if(spinner.getSelectedItem().toString().equals(target)){

                row = i;
            }
        }
        spinner.setSelection(row);
    }

    /**
     * coche les checkboxs par rapport au champ récupéré  depuis la base de données
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