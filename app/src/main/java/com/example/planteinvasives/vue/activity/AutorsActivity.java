package com.example.planteinvasives.vue.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planteinvasives.R;
import com.example.planteinvasives.map.MapActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AutorsActivity extends AppCompatActivity {

    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autors
        );

        //Gestion de la navbar
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_info);
        eventNav   = new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MenuHome:
                        Intent intent = new Intent(AutorsActivity.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(AutorsActivity.this, PhotoActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.MenuProfil:
                        //protégé par un mot de passe
                        String mdp = "root";

                        // on recupere la vue de la fenetre contextuelle
                        LayoutInflater li = LayoutInflater.from(AutorsActivity.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                AutorsActivity.this);

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
                                                    Intent intent3 = new Intent(AutorsActivity.this, AdminActivity.class);
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
                        Intent intent4 = new Intent(AutorsActivity.this, MapActivity.class);
                        startActivity(intent4);
                        return true;

                    case R.id.MenuFiche:
                        Intent intent5 = new Intent(AutorsActivity.this, FicheActivity.class);
                        startActivity(intent5);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);
    }
}