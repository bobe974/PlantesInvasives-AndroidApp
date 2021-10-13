package com.example.planteinvasives.Vue;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.example.planteinvasives.R;

import com.example.planteinvasives.roomDataBase.Controle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MenuActivity extends AppCompatActivity {


    private Button btncreateFiche, btnFiche, btnMap;
    private BottomNavigationView navbar;
    private BottomNavigationView.OnNavigationItemSelectedListener eventNav;
    Controle controle;

    /****************TEST******************/
    private GPSTracker pos;
    private double latittude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;

    /****************TEST******************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //creation base
        //controle = Controle.getInstance(this);

        /****************TEST******************/
        //verifPermission();
        // pos = new GPSTracker(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MenuActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission accepté
            getLocation();
        } else {
            //permission refusé
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        }


        btncreateFiche = (Button) findViewById(R.id.btnNewFiche);
        btnFiche = (Button) findViewById(R.id.btnFiches);
        btnMap = (Button) findViewById(R.id.btnMap);
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation_menu);

        //EVENEMENTS
        btncreateFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //demarrer l'appareil photo
                Intent intent = new Intent(MenuActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });

        btnFiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FicheActivity.class);
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        //Gestion de la navbar
        eventNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.MenuHome:

                        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.MenuNew:
                        Intent intent2 = new Intent(MenuActivity.this, PhotoActivity.class);
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


    /****************TEST******************/
    //get access to location permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public void getLocation() {
        Log.d("getlocation", "***********************************");


        fusedLocationClient.getLastLocation().addOnCompleteListener((new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //on recupere un objet Location dans un Task
                Log.d("onComplete", "********************************");
                Location location = task.getResult();
                if (location != null) {
                    Log.d("LOCATION NON NULL", "********************************");
                    try {
                        Geocoder geocoder = new Geocoder(MenuActivity.this, Locale.getDefault());
                        //list des addresses
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        //assigne aux variable locales
                        Log.d("ASSIGNE VARIABLE", "******************************");

                        Log.d("POSSSS", "latitude" + addresses.get(0).getLatitude());
                        latittude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
       

    }

    /****************TEST******************/
}
/**
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
 **/
/**
 * enregistrer l'image dans le dossier
 * /data/data/com.example.planteinvasives/app_data/photo
 * @param bitmapImage
 * @return
 */

/**
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
 **/
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
