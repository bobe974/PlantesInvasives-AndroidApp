package com.example.planteinvasives.Vue;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FicheActivity extends AppCompatActivity {
    private ListView mListView;
    private MyArrayAdapter mAdapter;

    //contient toutes les fiches qui seront récupérées depuis la bdd
    private ArrayList<Fiche> lesfiches;

    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        /************************TEST************************/
        lesfiches = new ArrayList<>();
        lesfiches.add(new Fiche("SWIGO"));
        lesfiches.add(new Fiche("BOULOP"));
        /************************TEST************************/

        mListView = (ListView) findViewById(R.id.listeFiche);
        mAdapter = new MyArrayAdapter(this, lesfiches);
        mListView.setAdapter(mAdapter);
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_fiche);



        //Gestion de la navbar
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
                        System.out.println("profil");
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);


    }

    public void afficheFiches(ArrayList<Fiche> lesfiches, Context context){
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(context,lesfiches);
    }
}