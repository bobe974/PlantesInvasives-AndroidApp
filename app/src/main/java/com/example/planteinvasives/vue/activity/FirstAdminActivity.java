package com.example.planteinvasives.vue.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;
import com.google.android.material.textfield.TextInputLayout;


public class FirstAdminActivity extends AppCompatActivity {

    private Controle controle;
    private TextInputLayout nomPlante1, nomPlante2,nomPlante3,nomPlante4,nomPlante5,nomEtablissemnt;
    private CheckBox ckEleve;
    private Button confirmer;
    private  int etatEleve;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_first_param);

        //page en plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        controle = Controle.getInstance(this);
        nomPlante1 = findViewById(R.id.FNameSpinner);
        nomPlante2 = findViewById(R.id.FNameSpinner1);
        nomPlante3 = findViewById(R.id.FNameSpinner2);
        nomPlante4 = findViewById(R.id.FNameSpinner3);
        nomPlante5 = findViewById(R.id.FNameSpinner4);
        nomEtablissemnt = findViewById(R.id.FnomEtablissement);
        ckEleve = findViewById(R.id.FcheckBoxEleve);
        confirmer = findViewById(R.id.Fbtnconfirme);


        //INSERTION
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

                Intent intent = new Intent(FirstAdminActivity.this, MenuActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "nom des espèces enregistrées", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
