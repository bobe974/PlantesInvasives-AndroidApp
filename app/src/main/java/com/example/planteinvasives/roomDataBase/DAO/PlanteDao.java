package com.example.planteinvasives.roomDataBase.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.planteinvasives.roomDataBase.entity.Photographie;
import com.example.planteinvasives.roomDataBase.entity.Plante;

@Dao
public interface PlanteDao {
    @Insert
    void insert(Plante plante);

    @Delete
    void delete(Plante plante);

    @Update
    void update(Plante plante);

}
