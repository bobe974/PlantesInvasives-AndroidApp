package com.example.planteinvasives.Vue;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarMenuView;

public class MenuActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 100;
    private ImageView photo;
    private Button btncreateFiche, btnFiche, btnMap;
    private NavigationBarMenuView navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btncreateFiche = (Button) findViewById(R.id.btnNewFiche);
        btnFiche = (Button) findViewById(R.id.btnFiches);
        btnMap = (Button) findViewById(R.id.btnMap);

        photo = (ImageView) findViewById(R.id.photo) ;


        //EVENEMENTS
        btncreateFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //demarrer l'appareil photo
                TakePhoto();

                //redirige vers la formulaire

                //envoye la photo dans la fiche
            }
        });

        btnFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void TakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        catch (ActivityNotFoundException e) {
            // display error state to the user
            CharSequence msg = "impossible d'ouvrir l'appareil photo";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(MenuActivity.this,msg,duration);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }
}
