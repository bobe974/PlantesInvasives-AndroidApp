package com.example.planteinvasives.roomDataBase.DAO;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.planteinvasives.roomDataBase.entity.Fiche;
import com.example.planteinvasives.roomDataBase.entity.Photographie;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface PhotoDao {
    @Insert
    void insert(Photographie photo);

    @Delete
    void delete(Photographie photo);

    @Update
    void update(Photographie photo);

}
