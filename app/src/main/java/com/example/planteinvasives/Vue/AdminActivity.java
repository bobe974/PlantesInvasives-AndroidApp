package com.example.planteinvasives.Vue;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
/** Page d'administration de l'application
 *
 *
 * @author etienne baillif
 * @version 1.0
 */

public class AdminActivity extends AppCompatActivity {
    private  int etatEleve = 0;

    private Controle controle;
    private TextInputLayout nomPlante1, nomPlante2,nomPlante3,nomPlante4,nomPlante5,nomEtablissemnt;
    private CheckBox ckEleve;
    private Button confirmer, reset;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        controle = Controle.getInstance(this);
        nomPlante1 = findViewById(R.id.PNameSpinner);
        nomPlante2 = findViewById(R.id.PNameSpinner1);
        nomPlante3 = findViewById(R.id.PNameSpinner2);
        nomPlante4 = findViewById(R.id.PNameSpinner3);
        nomPlante5 = findViewById(R.id.PNameSpinner4);
        nomEtablissemnt = findViewById(R.id.nomEtablissement);
        ckEleve = findViewById(R.id.checkBoxEleve);
        confirmer = findViewById(R.id.btnconfirme);
        //reset = findViewById(R.id.reset);

        //preremplir les champs
        remplirChamps();

        //recupere var etat et coche suivant son etat au demarrage de l'activité
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        etatEleve = sp.getInt("etatEleve", etatEleve);
        nomEtablissemnt.getEditText().setText(sp.getString("etablissement",""));
        if(etatEleve == 1){
            ckEleve.setChecked(true);
        }else{
            ckEleve.setChecked(false);
        }


        //evenement
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertion dans la base de données
                controle.spinnerDataDao().insert(new SpinnerData(0,nomPlante1.getEditText().getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(1,nomPlante2.getEditText().getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(2,nomPlante3.getEditText().getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(3,nomPlante4.getEditText().getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(4,nomPlante5.getEditText().getText().toString()));

                if(ckEleve.isChecked()){
                    etatEleve = 1;
                }else{
                    etatEleve=0;
                }
                //enregistre une variable pour l'affichage ou pas des champs de l'eleve
                SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("etatEleve", etatEleve);
                editor.putString("etablissement",nomEtablissemnt.getEditText().getText().toString());
                editor.commit();

                Intent intent = new Intent(AdminActivity.this,MenuActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "données enregistrées", Toast.LENGTH_SHORT).show();

            }
        });


        //Gestion de la navbar
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_admin);
        eventNav   = new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MenuHome:
                        Intent intent = new Intent(AdminActivity.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(AdminActivity.this, PhotoActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.MenuProfil:
                        Intent intent3 = new Intent(AdminActivity.this, AdminActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.MenuMap:
                        Intent intent4 = new Intent(AdminActivity.this, MapActivity.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);
    }

    /**
     * alimente avec le nom des especes les champs du formulaire depuis une base de données
     */
    public void remplirChamps(){
        ArrayList<SpinnerData> lesplantes = new ArrayList<SpinnerData>();
        Cursor cursor = controle.spinnerDataDao().getAll();
        cursor.moveToFirst();

        if (!isCursorEmpty(cursor)){

            while(!cursor.isAfterLast()){
                lesplantes.add(new SpinnerData(cursor.getInt(0),cursor.getString(1)));
                cursor.moveToNext();
            }
            //remplir les champs
            nomPlante1.getEditText().setText(lesplantes.get(0).getNomPlante());
            nomPlante2.getEditText().setText(lesplantes.get(1).getNomPlante());
            nomPlante3.getEditText().setText(lesplantes.get(2).getNomPlante());
            nomPlante4.getEditText().setText(lesplantes.get(3).getNomPlante());
            nomPlante5.getEditText().setText(lesplantes.get(4).getNomPlante());
        }
        cursor.close();
    }

    public boolean isCursorEmpty(Cursor cursor){
        return !cursor.moveToFirst() || cursor.getCount() == 0;
    }

    /**
     * remets les champs et checkbox par défaut
     */
    public void resetField(){
        nomPlante1.setError("erreur");
        nomPlante1.getEditText().getText().clear();
        nomPlante2.getEditText().getText().clear();
        nomPlante3.getEditText().getText().clear();
        nomPlante4.getEditText().getText().clear();
        nomPlante5.getEditText().getText().clear();
        nomEtablissemnt.getEditText().getText().clear();
        ckEleve.setChecked(false);
    }

}