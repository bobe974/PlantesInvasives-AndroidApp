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

    @ColumnInfo(name = "nom_Appareil")
    private String nomApareil;

    @Ignore
    private Photographie photo;
    @Ignore
    private  Plante plante;

    /**
     * @param nomApareil
     * @param photo
     * @param plante
     */
    public Fiche(String nomApareil, Photographie photo, Plante plante){
        this.nomApareil = nomApareil;
        this.photo = photo;
        this.plante = plante;
    }

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
