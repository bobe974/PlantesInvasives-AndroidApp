package com.example.planteinvasives.roomDataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Fiche.class,
        parentColumns = "id_fiche",
        childColumns = "id_plante",
        onDelete = ForeignKey.CASCADE))

public class Plante {

    @PrimaryKey(autoGenerate = true)
    public int id_plante;

    @ColumnInfo(name = "Nom" )
    public String nom;

    public Plante(String nom){
        this.nom = nom;
    }

    public int getId_plante() {
        return id_plante;
    }

    public void setId_plante(int id_plante) {
        this.id_plante = id_plante;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
