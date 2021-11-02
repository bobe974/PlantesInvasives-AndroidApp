package com.example.planteinvasives.Vue;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private  int etatEleve = 1;

    private Controle controle;
    private EditText nomPlante1, nomPlante2,nomPlante3,nomPlante4,nomPlante5;
    private CheckBox ckEleve;
    private Button confirmer;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        controle = Controle.getInstance(this);
        nomPlante1 = findViewById(R.id.PNameSpinner);
        nomPlante2 = findViewById(R.id.PNameSpinner2);
        nomPlante3 = findViewById(R.id.PNameSpinner3);
        nomPlante4 = findViewById(R.id.PNameSpinner4);
        nomPlante5 = findViewById(R.id.PNameSpinner5);
        ckEleve = findViewById(R.id.checkBoxEleve);
        confirmer = findViewById(R.id.btnconfirme);


        //recupere var etat et coche suivant son etat au demarrage de l'activité
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        etatEleve = sp.getInt("etatEleve", etatEleve);
        if(etatEleve == 0 ){
            ckEleve.setChecked(true);
        }else{
            ckEleve.setChecked(false);
        }


        //evenement
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertion dans la base de données
                controle.spinnerDataDao().insert(new SpinnerData(0,nomPlante1.getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(1,nomPlante2.getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(2,nomPlante3.getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(3,nomPlante4.getText().toString()));
                controle.spinnerDataDao().insert(new SpinnerData(4,nomPlante5.getText().toString()));

                if(ckEleve.isChecked()){
                    etatEleve = 0;
                }else{
                    etatEleve=1;
                }
                //enregistre une variable pour l'affichage ou pas des champs de l'eleve
                SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("etatEleve", etatEleve);
                editor.commit();

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


}
