package com.example.planteinvasives.Vue;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planteinvasives.R;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarMenuView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

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

            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //SavePhotoIntoFolder((BitmapDrawable) photo.getDrawable());

            //recupere le chemin de l'image
            String absolutePath = saveToInternalStorage(imageBitmap);

            //envoie le chemin de la photo et lance l'activity formulaire
            Intent intent = new Intent(MenuActivity.this,FormActivity.class);
            intent.putExtra("path",absolutePath);
            startActivity(intent);
        }
    }

    /**
     * enregistrer l'image dans le dossier
     * /data/data/com.example.planteinvasives/app_data/photo
     * @param bitmapImage
     * @return
     */
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("photo", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"plante.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(this,"image enregistré !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

/**
    public void SavePhotoIntoFolder(BitmapDrawable bitmapDrawable){

        Bitmap bitmap = bitmapDrawable.getBitmap();
        saveImageToGallery(bitmap);

    }**/

    /**
     * enregistre un bitmap dans un sous dossier /data/android/pictures/planteInvasives
     * @param image

    public void saveImageToGallery(Bitmap image){
        FileOutputStream fos;
        try {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_"+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"Image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+ File.separator +"PlantesInvasives");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                fos = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));
                Toast.makeText(this,"image enregistré !", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"image non enregistré \n"+ e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }**/

}
