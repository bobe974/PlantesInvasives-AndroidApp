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



    @Ignore
    private Photographie photo;
    @Ignore
    private  Plante plante;

    public Photographie getPhoto() {
        return photo;
    }

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

    public Fiche(){

    }
    public int getId_fiche() {
        return id_fiche;
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


}
