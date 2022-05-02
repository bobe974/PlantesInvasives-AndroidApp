package com.example.planteinvasives.roomDataBase.DAO;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.planteinvasives.roomDataBase.entity.Fiche;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FicheDao {

    @RawQuery
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);

    @Insert
    void insert(Fiche fiche);

    @Delete
    void delete(Fiche fiche);

    @Update
    void update(Fiche fiche);

    @Query("   SELECT * FROM Fiche \n" +
            "  INNER JOIN Photographie \n" +
            "  ON Fiche.id_fiche = Photographie.id_photo\n" +
            "  INNER JOIN Plante \n" +
            "  ON Fiche.id_fiche = Plante.id_plante\n" +
            "  INNER JOIN Lieu \n" +
            "  ON Fiche.id_fiche = Lieu.id_lieu ORDER BY Fiche.id_fiche DESC")
    Cursor getAll();

    @Query("   SELECT * FROM Fiche \n" +
            "  INNER JOIN Photographie \n" +
            "  ON Fiche.id_fiche = Photographie.id_photo\n" +
            "  INNER JOIN Plante \n" +
            "  ON Fiche.id_fiche = Plante.id_plante\n" +
            "  INNER JOIN Lieu \n" +
            "  ON Fiche.id_fiche = Lieu.id_lieu WHERE Fiche.id_fiche = :id")
     Cursor getById(int id);

    @Query(" select id_fiche from Fiche order by id_fiche DESC limit 1")
    Cursor getLastId();

}



