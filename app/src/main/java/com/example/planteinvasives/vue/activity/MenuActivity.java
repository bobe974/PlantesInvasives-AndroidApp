package com.example.planteinvasives.vue.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.planteinvasives.BuildConfig;
import com.example.planteinvasives.R;
import com.example.planteinvasives.geolocalisation.GpsTracker;
import com.example.planteinvasives.map.MapActivity;
import com.example.planteinvasives.roomDataBase.Controle;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *Page principale de l'application
 * @author etienne baillif
 * @version 1.0
 */
public class MenuActivity extends AppCompatActivity {

    private CardView btncreateFiche, btnFiche, btnMap, btnParam;
    private Button btndump;
    private ExtendedFloatingActionButton btnInfo, btnUpdate;
    private TextView textlongitude, textlatitude, textAccueil;
    GpsTracker gpsTracker;
    Controle controle;
    int versionCode = BuildConfig.VERSION_CODE;
    int etatApp = 0;
    String versionName = BuildConfig.VERSION_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //page en plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //lance la page de premiere utilisation si la base de données est vide
        controle = Controle.getInstance(this);

        List<SpinnerData> users;
        users = controle.spinnerDataDao().getAllUser();
        if (!(users.size()>0)) {
            startActivity(new Intent(this, InfoActivity.class));
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        //verifie si la localisation fonctionne et est activé
        gpsTracker = new GpsTracker(MenuActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
        }else {
            gpsTracker.showSettingsAlert();
        }

        btncreateFiche =  findViewById(R.id.btnNewFiche);
        btnFiche =  findViewById(R.id.btnFiches);
        btnMap =  findViewById(R.id.btnMap);
        btnParam = findViewById(R.id.btnparam);
        btnUpdate = findViewById(R.id.btnactualiser);
        btnInfo = findViewById(R.id.btnAPropos);
        textlatitude = findViewById(R.id.labellattitude);
        textlongitude = findViewById(R.id.labellongitude);
        textAccueil= findViewById(R.id.msgAccueil);

        //affichage lattitude longitude
        String newLine = System.getProperty("line.separator");

        textlatitude.setText("lattitude: " + Controle.getGpsTracker().getLatitude());
        textlongitude.setText("longitude: "+ Controle.getGpsTracker().getLongitude());
        textAccueil.setText("Application de signalement des espèces exotiques envahissantes " +
                "ou potentiellement envahissantes" +newLine+ newLine+"Ver: " +versionName);


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
                Intent intent = new Intent(MenuActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        btnParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("****longlat="+Controle.getGpsTracker().getLongitude() +Controle.getGpsTracker().getLatitude());
                //protégé par un mot de passe
                String mdp = "root";

                // on recupere la vue de la fenetre contextuelle
                LayoutInflater li = LayoutInflater.from(MenuActivity.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MenuActivity.this);

                // on ajoute la fenetre a  alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText pssword = (EditText) promptsView
                        .findViewById(R.id.passwordField);

                // ajout de la boite de dialogue
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        System.out.println("mdp: "+ pssword.getText().toString());
                                        if(pssword.getText().toString().equals(mdp)){
                                            Intent intent3 = new Intent(MenuActivity.this, AdminActivity.class);
                                            startActivity(intent3);
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Mot de passe Invalide", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // affichage
                alertDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepos();
                System.out.println("update pos");
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AutorsActivity.class);
                startActivity(intent);
            }
        });

    }


    public void updatepos(){
        Controle.InstancieGps();
        //mise a jour affichage
        textlatitude.setText("lattitude: " + Controle.getGpsTracker().getLatitude());
        textlongitude.setText("longitude: "+ Controle.getGpsTracker().getLongitude());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatepos();
        System.out.println("***********DUMP**************");
        dumpDatabase();
        //
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        etatApp = sp.getInt("etatDel", 0);
        System.out.println(etatApp);
        if (etatApp == 1){
            System.out.println("coché");
            cleanApp();
        }else{
            removeConfFile();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //lance la page de premiere utilisation si la base de données est vide
        controle = Controle.getInstance(this);

        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        etatApp = sp.getInt("etatDel", 0);
        System.out.println(etatApp);
        if (etatApp == 1){
            System.out.println("coché");
            cleanApp();
        }else{
          removeConfFile();
        }

        List<SpinnerData> users;
        users = controle.spinnerDataDao().getAllUser();
        if (!(users.size()>0)) {
            startActivity(new Intent(this, InfoActivity.class));
        }


    }

    /**
     * sauvegarde un dump de la bdd au format .sql  -> android/data/files/pictures/DBsaves
     */
    public void dumpDatabase(){
        //recupere les dernieres données
        controle.checkpointDB();

        File dbfile = getDatabasePath("PlanteInvasives.sqlite");
        //Toast.makeText(getApplicationContext(), "nom"+ dbfile.getName(), Toast.LENGTH_SHORT).show();
        File sdir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"DBsaves");
        String sfpath = sdir.getPath() + File.separator + dbfile.getName();
        //String sfpath = sdir.getPath() + File.separator + dbfile.getName() + String.valueOf(System.currentTimeMillis());
        if (!sdir.exists()) {
            sdir.mkdirs();
        }
        File savefile = new File(sfpath);
        try {
            savefile.createNewFile();
            int buffersize = 8 * 1024;
            byte[] buffer = new byte[buffersize];
            int bytes_read = buffersize;
            OutputStream savedb = new FileOutputStream(sfpath);
            InputStream indb = new FileInputStream(dbfile);
            while ((bytes_read = indb.read(buffer,0,buffersize)) > 0) {
                savedb.write(buffer,0,bytes_read);
            }
            savedb.flush();
            indb.close();
            savedb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * le nom de la méthode parle d'elle meme
     */
    public void exportToCsv(String nomTable){
        SQLiteDatabase sqldb = controle.getSQLiteDB(getApplicationContext());
        Cursor c = null;

        try {
            c = sqldb.rawQuery("select * from "+nomTable, null);
            int rowcount = 0;
            int colcount = 0;
            File sDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/DBsaves");
            String filename = nomTable+ ".csv";
            //créer un répertoire
            //File sdir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/DBsaves"),"CSV");
            // nom du fichier
            File saveFile = new File(sDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();

                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {

                        //séparateur
                        bw.write(c.getColumnName(i) + ";");

                    } else {

                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();

                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ";");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                Toast.makeText(getApplicationContext(),"exportation complete",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            if (sqldb.isOpen()) {
                sqldb.close();
            }
        } finally {

        }
    }

    /**
     * vérifie si le fichier delete existe puis supprime les données de l'application
     */
    public void cleanApp(){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                "Android/data/com.example.planteinvasives/files/1.225_172303_8349967972327207504");
        try {
            System.out.println(file.getAbsolutePath());
            //si le fichier existe et ck supression coché alors on reset l'application
            if(file.exists()&& etatApp == 1){
                System.out.println("supprime external");
                //supprime la base
                controle.clearAllTables();

                //supprimer le fichier xml sharepreferences
                SharedPreferences settings = getSharedPreferences("your_prefs", Context.MODE_PRIVATE);
                settings.edit().clear().commit();

                //supprimer les fichiers
                File dirName = new File(Environment.getExternalStorageDirectory() + File.separator +
                        "Android/data/com.example.planteinvasives");
                FileUtils.deleteDirectory(dirName);
            }
            else {
                System.out.println("fichier delete non existe, aucune modification");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * supprime le fichier qui permet de reset l'application
     */
    public void removeConfFile(){
        //supprime le fichier del au démarrage si ckdel n'est pas coché
        File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                "Android/data/com.example.planteinvasives/files/1.225_172303_8349967972327207504");
        if(file.exists()){
            System.out.println("suppression du fichier delete");
            file.delete();
            System.out.println("*****************SUPRESSION DE "+file.getName()+"************************");
        }
    }
}