package com.example.planteinvasives.roomDataBase.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public  class Fiche {

    @PrimaryKey(autoGenerate = true)
    private int id_fiche;

    @ColumnInfo(name = "nom_Appareil")
    public String nomApareil;

    //@Embedded public Photographie photo;
    //@Embedded public Plante plante;

    public Fiche(String nomApareil){
        this.nomApareil = nomApareil;
    }

    public int getId_fiche() {
        return id_fiche;
    }

    public void setId_fiche(int id_fiche) {
        this.id_fiche = id_fiche;
    }

    public String getNomApareil() {
        return nomApareil;
    }

    public void setNomApareil(String nomApareil) {
        this.nomApareil = nomApareil;
    }
}
