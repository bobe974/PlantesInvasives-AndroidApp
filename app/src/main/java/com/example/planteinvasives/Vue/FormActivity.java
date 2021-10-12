package com.example.planteinvasives.Vue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenuView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FormActivity extends AppCompatActivity {

    private String photoPath;
    private String nom_appareil;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;

    private Button valideFiche;
    private TextView position, username;
    public ImageView photo;


    //********************TEST********************
    Controle controle;
    Plante plante = new Plante("PIEDMANG");
    Photographie unephoto = new Photographie(photoPath,
            "23 mai",23,322);

//********************TEST********************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        valideFiche = (Button) findViewById(R.id.btnvalideFiche);
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        username = (TextView) findViewById(R.id.userName);
        photo = (ImageView) findViewById(R.id.imgPhoto);

        nom_appareil = android.os.Build.MODEL;
        valideFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controle = Controle.getInstance(FormActivity.this);
                controle.ficheDao().insert(new Fiche(nom_appareil,unephoto,plante));
                controle.photoDao().insert(unephoto);
                controle.planteDao().insert(plante);

                Log.d("TAG", "onClick: ENREGISTRER");
            }
        });

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
                        System.out.println("profil");
                        return true;
                }
                return false;
            }
        };

        navbar.setOnNavigationItemSelectedListener(eventNav);

        //recupere le chemin absolu de la photo
        Intent intent = getIntent();
        photoPath = intent.getStringExtra("path");
        Log.d("RECUP P PATH", photoPath);
        loadImageFromStorage(photoPath, photo);

    }

    /**
     * charge une image depuis un dossier et l'affecte dans un Imageview
     * @param path
     */
    private void loadImageFromStorage(String path, ImageView image)
    {
        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            image.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
