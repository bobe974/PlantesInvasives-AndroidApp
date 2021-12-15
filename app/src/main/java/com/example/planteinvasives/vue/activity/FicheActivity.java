package com.example.planteinvasives.vue.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.planteinvasives.modele.MaFiche;
import com.example.planteinvasives.R;
import com.example.planteinvasives.vue.adapter.MyArrayAdapter;
import com.example.planteinvasives.roomDataBase.Controle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
/** classe qui permet d'afficher la liste des fiches
 *
 *
 * @author etienne baillif
 * @version 1.0
 */

public class FicheActivity extends AppCompatActivity {

    private ListView mListView;
    private MyArrayAdapter mAdapter;
    //contient toutes les fiches qui seront récupérées depuis la bdd
    private List<MaFiche> lesfiches;
    Controle controle;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;
    private Cursor cursor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);
        //page en plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Retour accueil");
        //recupere toutes les fiches depuis la bdd
        controle = Controle.getInstance(FicheActivity.this);
        lesfiches = new ArrayList<>();
        cursor = controle.ficheDao().getAll();

        //parcours du cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            lesfiches.add(new MaFiche(cursor.getInt(0),cursor.getString(6),cursor.getString(4),cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();


        mListView = (ListView) findViewById(R.id.listeFiche);
        mAdapter = new MyArrayAdapter(this, (ArrayList<MaFiche>) lesfiches,this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                /************************TEST************************/
                String Name =((TextView)view.findViewById(R.id.nomPlante)).getText().toString();
                String idFiche =((TextView)view.findViewById(R.id.textView_id)).getText().toString();
                String path =((TextView)view.findViewById(R.id.textView_path)).getText().toString();

                //envoie des données a ficheActivity
                /************************TEST************************/
                Intent intent = new Intent(FicheActivity.this, FormActivity.class);
                intent.putExtra("idfiche",idFiche);
                intent.putExtra("update","100");
                startActivity(intent);
                Log.d("***********", "envoie a formActivity: "+ idFiche);
            }
        });

        //Gestion de la navbar
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_fiche);
        eventNav   = new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MenuHome:
                        Intent intent = new Intent(FicheActivity.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(FicheActivity.this, PhotoActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.MenuProfil:
                        //protégé par un mot de passe
                        String mdp = "gabon";

                        // on recupere la vue de la fenetre contextuelle
                        LayoutInflater li = LayoutInflater.from(FicheActivity.this);
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                FicheActivity.this);

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
                                                    Intent intent3 = new Intent(FicheActivity.this, AdminActivity.class);
                                                    startActivity(intent3);
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Mot de passe invalide", Toast.LENGTH_SHORT).show();
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
                        Intent intent4 = new Intent(FicheActivity.this, MapActivity.class);
                        startActivity(intent4);
                        return true;

                    case R.id.MenuFiche:
                        Intent intent5 = new Intent(FicheActivity.this, FicheActivity.class);
                        startActivity(intent5);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);

    }


}