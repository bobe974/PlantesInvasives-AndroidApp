package com.example.planteinvasives.roomDataBase.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public  class Fiche {

    @PrimaryKey(autoGenerate = true)
    private int id_fiche;

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

    public Fiche(){

    }
    public int getId_fiche() {
        return id_fiche;
    }

    public void setId_fiche(int id_fiche) {
        this.id_fiche = id_fiche;
    }

}
