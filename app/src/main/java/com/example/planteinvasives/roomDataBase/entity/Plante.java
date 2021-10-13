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

    @ColumnInfo(name = "état" )
    public String etat;

    @ColumnInfo(name = "stade" )
    public String stade;

    @ColumnInfo(name = "description" )
    public String description;

    public Plante(String nom, String etat, String stade, String description){

        this.nom = nom;
        this.etat = etat;
        this.stade = stade;
        this.description = description;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getStade() {
        return stade;
    }

    public void setStade(String stade) {
        this.stade = stade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
