package com.example.planteinvasives.roomDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.planteinvasives.geolocalisation.GpsTracker;
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
}
