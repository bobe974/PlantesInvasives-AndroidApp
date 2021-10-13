package com.example.planteinvasives.roomDataBase.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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
}
