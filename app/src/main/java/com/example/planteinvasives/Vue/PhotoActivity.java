package com.example.planteinvasives.Vue;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.planteinvasives.BuildConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
/**
 *Classe pour qui contient les fonctions liée a la prise de photo
 * @author etienne baillif
 * @version 1.0
 */
public class PhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 100;
    private String currentPhotoPath;
    private  String timeStamp;
    private String ladate;
    private  GpsTracker gpsTracker;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //demarrer l'appareil photo
        dispatchTakePictureIntent();
    }

    /**
     * créer un fichier avec un nom unique dans le stockage interne de l'appareil
     * @return un objet de type File
     * @throws IOException
     */
    public File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ladate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /** Lance l’appareil photo existant
     *
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
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

            Log.d("CHEMIN PHOTO***********", currentPhotoPath);
            //envoie le chemin de la photo et lance l'activity formulaire
            Intent intent = new Intent(PhotoActivity.this,FormActivity.class);
            intent.putExtra("path",currentPhotoPath);
            intent.putExtra("date",ladate);
            intent.putExtra("update","0");

            //recupere les données gps
            getLocation();
            intent.putExtra("latitude",String.valueOf(latitude));
            intent.putExtra("longitude", String.valueOf(longitude));
            startActivity(intent);
        }else{

            Intent intent = new Intent(PhotoActivity.this,MenuActivity.class);
            startActivity(intent);
        }
    }

    /**
     * charge une image depuis un dossier et l'affecte dans un Imageview
     * @param path chemin du fichier
     */
    public static void loadImageFromStorage(String path, ImageView image)
    {
        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //taille de l'image
            //b = Bitmap.createScaledBitmap(b,64,64,false);
            image.setImageBitmap(b);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * recupere les donnees gps lors de la prise de photo
     */
    public void getLocation(){
        gpsTracker = new GpsTracker(PhotoActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.d("GETLOCATION", "latitude: "+ latitude + "longitude"+longitude);
        }else{
            gpsTracker.showSettingsAlert();

        }

    }

    /**
     * donne la position et l'orientation  de la photo
     * @param imagePath
     * @return
     */
    public static int getPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {
            ExifInterface exif  = null;
            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 90;
                    break;
                default:
                    rotate = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
}