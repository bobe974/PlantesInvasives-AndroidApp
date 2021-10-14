package com.example.planteinvasives.Vue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;

public class AdminActivity extends AppCompatActivity {

    private Controle controle;

    private EditText nomPlante1, nomPlante2,nomPlante3,nomPlante4,nomPlante5;
    private Button confirmer;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        controle = Controle.getInstance(this);
        nomPlante1 = findViewById(R.id.PNameSpinner);
        nomPlante2 = findViewById(R.id.PNameSpinner2);
        nomPlante3 = findViewById(R.id.PNameSpinner3);
        nomPlante4 = findViewById(R.id.PNameSpinner4);
        nomPlante5 = findViewById(R.id.PNameSpinner5);
        confirmer = findViewById(R.id.btnconfirme);

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
            }
        });


    }
}
