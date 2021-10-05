package com.example.planteinvasives.roomDataBase.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.planteinvasives.roomDataBase.entity.Fiche;

import java.util.List;

@Dao
public interface FicheDao {
    @Insert
    void insert(Fiche fiche);

    @Delete
    void delete(Fiche fiche);

    @Update
    void update(Fiche fiche);

    @Query("SELECT * FROM fiche")
    List<Fiche> getAll();
}
