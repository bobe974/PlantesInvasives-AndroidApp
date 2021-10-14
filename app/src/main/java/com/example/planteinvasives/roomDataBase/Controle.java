package com.example.planteinvasives.roomDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.planteinvasives.roomDataBase.DAO.FicheDao;
import com.example.planteinvasives.roomDataBase.DAO.LieuDao;
import com.example.planteinvasives.roomDataBase.DAO.PhotoDao;
import com.example.planteinvasives.roomDataBase.DAO.PlanteDao;
import com.example.planteinvasives.roomDataBase.DAO.SpinnerDataDao;
import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Lieu;
import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;


@Database(entities = {Fiche.class, Plante.class, Photographie.class, Lieu.class, SpinnerData.class}, version = 1, exportSchema = false)
public abstract class Controle extends RoomDatabase {

    private static  String  DB_name = "PlanteInvasives.sqlite";
    //singleton
    public static Controle INSTANCE;

    //méthodes abstraites DAO

    public abstract FicheDao ficheDao();
    public abstract PhotoDao photoDao();
    public abstract PlanteDao planteDao();
    public abstract LieuDao lieuDao();
    public abstract SpinnerDataDao spinnerDataDao();


    // --- INSTANCE ---
    public static synchronized Controle getInstance(Context context) {
        Log.d("TAG", "getInstance: *************************");
        if (INSTANCE == null) {
            synchronized (Controle.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Controle.class, DB_name).allowMainThreadQueries().build();
                    Log.d("TAG", "base créer: *************************");
                }
            }
        }
        return INSTANCE;
    }

    

}
