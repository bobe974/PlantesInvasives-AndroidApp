package com.example.planteinvasives.Vue;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FicheActivity extends AppCompatActivity {
    private ListView mListView;
    private MyArrayAdapter mAdapter;

    //contient toutes les fiches qui seront récupérées depuis la bdd
    private List<Fiche> lesfiches;
    Controle controle;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);

        /************************TEST************************/
        controle = Controle.getInstance(FicheActivity.this);
        lesfiches = new ArrayList<>();
        lesfiches = controle.ficheDao().getAll();
        /************************TEST************************/

        mListView = (ListView) findViewById(R.id.listeFiche);
        mAdapter = new MyArrayAdapter(this, (ArrayList<Fiche>) lesfiches);
        mListView.setAdapter(mAdapter);

       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                /************************TEST************************/
                String Name =((TextView)view.findViewById(R.id.nomPlante)).getText().toString();
                Toast.makeText(FicheActivity.this,Name, Toast.LENGTH_SHORT).show();

                /************************TEST************************/
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
                        System.out.println("profil");
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);


    }


}