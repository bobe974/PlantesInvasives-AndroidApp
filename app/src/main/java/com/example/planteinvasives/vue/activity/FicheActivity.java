package com.example.planteinvasives.vue.activity;


import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
                        Intent intent3 = new Intent(FicheActivity.this, AdminActivity.class);
                        startActivity(intent3);
                        return true;

                    case R.id.MenuMap:
                        Intent intent4 = new Intent(FicheActivity.this, MapActivity.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);

    }


}