package com.example.planteinvasives.roomDataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id_user;

    public String nom;
    public String prenom;
    public String classe;
    public String nom_appareil;
}
