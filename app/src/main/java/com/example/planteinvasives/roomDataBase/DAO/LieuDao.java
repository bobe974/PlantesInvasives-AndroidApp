package com.example.planteinvasives.roomDataBase.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.planteinvasives.roomDataBase.entity.Lieu;

@Dao
public interface LieuDao {
    @Insert
    void insert(Lieu lieu);

    @Delete
    void delete(Lieu lieu);

    @Update
    void update(Lieu lieu);

    @Query("UPDATE Lieu set type= :type,surface=:surface, nb_individu =:nbIndividu,remarques= :remarques " +
            "WHERE  id_lieu = :id")

    void updateLieu(int id, String type, String surface, String nbIndividu, String remarques);
}
