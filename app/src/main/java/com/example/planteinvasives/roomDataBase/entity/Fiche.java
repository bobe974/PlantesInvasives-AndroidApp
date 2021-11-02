package com.example.planteinvasives.roomDataBase.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public  class Fiche  {

    @PrimaryKey(autoGenerate = true)
    private int id_fiche;

    private String nom_etablissement;

    @Ignore
    private Photographie photo;
    @Ignore
    private  Plante plante;

    @Ignore
    private Lieu lieu;

    /**
     * @param photo
     * @param plante
     */
    public Fiche(Photographie photo, Plante plante, Lieu lieu){

        this.photo = photo;
        this.plante = plante;
        this.lieu = lieu;
    }

    public Fiche(String nom_etablissement){
        this.nom_etablissement = nom_etablissement;
    }
    public int getId_fiche() {
        return id_fiche;
    }

    public Photographie getPhoto() {
        return photo;
    }

    public void setId_fiche(int id_fiche) {
        this.id_fiche = id_fiche;
    }
    public void setPhoto(Photographie photo) {
        this.photo = photo;
    }

    public Plante getPlante() {
        return plante;
    }

    public void setPlante(Plante plante) {
        this.plante = plante;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public String getNom_etablissement() {
        return nom_etablissement;
    }

    public void setNom_etablissement(String nom_etablissement) {
        this.nom_etablissement = nom_etablissement;
    }

}
