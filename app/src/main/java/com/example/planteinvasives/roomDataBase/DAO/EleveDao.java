package com.example.planteinvasives.roomDataBase.DAO;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.planteinvasives.roomDataBase.entity.Eleve;
import com.example.planteinvasives.roomDataBase.entity.Fiche;

@Dao
public interface EleveDao {
    @RawQuery
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);
    @Insert
    void insert(Eleve eleve);

    @Delete
    void delete(Eleve eleve);

    @Update
    void update(Eleve eleve);

    @Query("select * from eleve where id_eleve = :id")
    Cursor getById(int id);


}
