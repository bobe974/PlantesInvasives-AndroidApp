package com.example.planteinvasives.roomDataBase;

import android.content.Context;
import android.database.Cursor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.planteinvasives.geolocalisation.GpsTracker;
import com.example.planteinvasives.modele.MaFiche;
import com.example.planteinvasives.roomDataBase.DAO.EleveDao;
import com.example.planteinvasives.roomDataBase.DAO.FicheDao;
import com.example.planteinvasives.roomDataBase.DAO.LieuDao;
import com.example.planteinvasives.roomDataBase.DAO.PhotoDao;
import com.example.planteinvasives.roomDataBase.DAO.PlanteDao;
import com.example.planteinvasives.roomDataBase.DAO.SpinnerDataDao;
import com.example.planteinvasives.roomDataBase.entity.Eleve;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;

import java.util.ArrayList;
import java.util.List;

/** Classe du controlleur de la base de donénes
 * @author etienne baillif
 * @version 1.0
 */
@Database(entities = {Fiche.class, Plante.class, Photographie.class, Lieu.class, Eleve.class, SpinnerData.class}, version = 1, exportSchema = false)
public abstract class Controle extends RoomDatabase {

    private static  String  DB_name = "PlanteInvasives.sqlite";
    //singleton
    private static Controle INSTANCE;
    private  static GpsTracker gpsTracker;
    private static Context context;



    //méthodes abstraites DAO

    public abstract FicheDao ficheDao();
    public abstract PhotoDao photoDao();
    public abstract PlanteDao planteDao();
    public abstract LieuDao lieuDao();
    public abstract SpinnerDataDao spinnerDataDao();
    public abstract EleveDao eleveDao();


    // --- INSTANCE --- sinlgeton

    /**
     * singleton qui permet d'avoir une instance unique du controlleur
     * @param lecontext
     * @return un Controleur
     */
    public static synchronized Controle getInstance(Context lecontext) {
        if (INSTANCE == null) {
            synchronized (Controle.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(lecontext.getApplicationContext(),
                            Controle.class, DB_name).allowMainThreadQueries().build();
                    gpsTracker = new GpsTracker(lecontext);
                    context = lecontext;
                    System.out.println("***** nouvelle instance: l/"+gpsTracker.getLatitude()+"lon/"+gpsTracker.getLongitude());

                }
            }
        }
        return INSTANCE;
    }

    public static GpsTracker getGpsTracker() {
        return gpsTracker;
    }

    public static void InstancieGps(){
        gpsTracker = new GpsTracker(context);
        System.out.println("update"+gpsTracker.getLatitude()+gpsTracker.getLongitude());
    }

    /**
     * retourne toutes les fiches sous forme de liste
     * @return
     */
    public List<Fiche> getAllFiche(){
        Cursor cursor = ficheDao().getAll();
        List<Fiche> lesfiches;
        lesfiches = new ArrayList<>();
        Photographie photographie;
        Plante plante;
        Lieu lieu;
        Fiche fiche;

        //parcours du cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            photographie = new Photographie(cursor.getString(3),cursor.getString(4));
            plante = new Plante(cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            lieu = new Lieu(cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getDouble(14),cursor.getDouble(15),cursor.getString(16));

            fiche = new Fiche(photographie,plante,lieu);
            fiche.setId_fiche(cursor.getInt(0));
            lesfiches.add(fiche);

            cursor.moveToNext();
        }
        cursor.close();
        return lesfiches;
    }
}
