package com.example.planteinvasives.Vue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;

import java.util.ArrayList;
import java.util.List;

public class FormActivity2 extends AppCompatActivity {

    private String photopath,description, nomPlante, nom, prenom, date;
    private int latitude  , longitude;
    private Spinner spinnerLieu, spinnerEtat, spinnerSurface, spinnerIndividu;
    private CheckBox végétatif, enFleur, enFruit;
    private EditText remarques;
    private Button btnvalider;

    private Controle controle;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form2);

        remarques = findViewById(R.id.remarques);
        spinnerLieu = findViewById(R.id.Slieu);
        spinnerEtat = findViewById(R.id.Setat);
        spinnerSurface = findViewById(R.id.Ssurface);
        spinnerIndividu = findViewById(R.id.Sindividu);
        végétatif = findViewById(R.id.Cvegetatif);
        enFleur = findViewById(R.id.Cfleur);
        enFruit = findViewById(R.id.Cfruit);
        btnvalider = findViewById(R.id.btnvalideForm2);


        //recupere les données du formulaire 1
        Intent intent = getIntent();
        if (intent != null){
            photopath = intent.getStringExtra("photopath");
            date = intent.getStringExtra("date");
            description = intent.getStringExtra("description");
            nom = intent.getStringExtra("nom");
            prenom = intent.getStringExtra("prenom");
            Log.d("tes recup form", "************"+ photopath + nom + description + prenom );
        }

        //evenements
        btnvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insertion dans la base de données

                Photographie unephoto = new Photographie(photopath,date);
                Plante uneplante = new Plante(nomPlante,spinnerEtat.getSelectedItem().toString(),"TEST", description);
                Lieu unlieu = new Lieu(spinnerLieu.getSelectedItem().toString(),spinnerSurface.getSelectedItem().toString(),spinnerIndividu.getSelectedItem().toString(),1,1,"TEST");

                controle = Controle.getInstance(FormActivity2.this);
                controle.ficheDao().insert(new Fiche(unephoto,uneplante,unlieu));
                controle.photoDao().insert(unephoto);
                controle.planteDao().insert(uneplante);
                controle.lieuDao().insert(unlieu);


            }
        });


    }

}
