package com.example.planteinvasives.roomDataBase.DAO;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;


import com.example.planteinvasives.roomDataBase.entity.Plante;
import com.example.planteinvasives.roomDataBase.entity.SpinnerData;

import java.util.List;

@Dao
public interface SpinnerDataDao {
    @RawQuery
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SpinnerData spinnerData);

    @Query("Delete FROM SpinnerData")
     void deleteAll();

    @Query("select * from spinnerdata")
    Cursor getAll();

    @Query("select * from spinnerdata")
    List<SpinnerData> getAllUser();


}
