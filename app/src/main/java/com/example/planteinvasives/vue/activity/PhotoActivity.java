package com.example.planteinvasives.vue.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.example.planteinvasives.BuildConfig;
import com.example.planteinvasives.geolocalisation.GpsTracker;
import com.example.planteinvasives.roomDataBase.Controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private GpsTracker gpsTracker;
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
            Intent intent = new Intent(PhotoActivity.this, FormActivity.class);
            intent.putExtra("path",currentPhotoPath);
            intent.putExtra("date",ladate);
            intent.putExtra("update","0");

            //recupere les données gps
            getLocation();
            intent.putExtra("latitude",String.valueOf(latitude));
            intent.putExtra("longitude", String.valueOf(longitude));
            startActivity(intent);
        }else{

            Intent intent = new Intent(PhotoActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }

    /**
     * charge une image depuis un dossier et l'affecte dans un Imageview
     * @param path chemin du fichier
     */
    public static Bitmap loadImageFromStorage(String path, Activity activity)
    {
        System.out.println("DANS LA METHODE");
        Bitmap b = null;
        File f=new File(path);
        //b = BitmapFactory.decodeStream(new FileInputStream(f));
        //taille de l'image
        b = rectifyImage(activity.getApplicationContext(),f);
        b = changeSizeBitmap(b,0.4f,activity);

        //image.setImageBitmap(b);
        return b;


    }

    /**
     * recupere les donnees gps lors de la prise de photo
     */
    public void getLocation(){
        gpsTracker = Controle.getGpsTracker();
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.d("GETLOCATION", "latitude: "+ latitude + "longitude"+longitude);
        }else{
            gpsTracker.showSettingsAlert();

        }

    }


    /**
     * change la taille d'une image -> decoupage carré et centré
     * @param proportion
     * @return
     */
    private static Bitmap changeSizeBitmap(Bitmap bitmap, float proportion, Activity activity){

        //metrique
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //taille de l'écran et affecter la proportion
        float screenHeight = metrics.heightPixels*proportion;
        float screenWidth = metrics.widthPixels*proportion;
        //récupérer le plus petit coté de l'écran
        float screen = Math.min(screenHeight,screenHeight);

        //taille de l'image
        float bimapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();
        //calcul ratio entre taille image et ecran
        float ratioHeight = screen/ bimapHeight;
        float ratioWidth = screen / bitmapWidth;
        //récupérer le plus grand ratio
        float ratio = Math.max(ratioHeight,ratioWidth);
        //redimentionner l'image avec le ratio
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmapWidth*ratio),(int)(bimapHeight*ratio),true);
        //couper l'image en carré
        int x = (int)Math.max(0,(bitmap.getWidth() - screen)/2);
        int y = (int)Math.max(0,(bitmap.getHeight()-screen)/2);
        bitmap = Bitmap.createBitmap(bitmap,x,y,(int)(screen),(int)(screen));
        return bitmap;

    }

    /**
     * positionne l'image dans la bonne position
     * @param context
     * @param imageFile
     * @return
     */
    public static Bitmap rectifyImage(Context context,File imageFile){
        Bitmap originalBitmap= BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        try{
            Uri uri=Uri.fromFile(imageFile);
            InputStream input = context.getContentResolver().openInputStream(uri);
            ExifInterface ei;

            if (Build.VERSION.SDK_INT > 23)
                ei = new ExifInterface(input);
            else
                ei = new ExifInterface(uri.getPath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(originalBitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(originalBitmap, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(originalBitmap, 270);
                default:
                    return originalBitmap;
            }
        }catch (Exception e){
            return originalBitmap;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    /************PLUS UTILSE*************/
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