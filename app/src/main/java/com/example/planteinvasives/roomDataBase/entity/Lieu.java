package com.example.planteinvasives.roomDataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Fiche.class,
        parentColumns = "id_fiche",
        childColumns = "id_lieu",
        onDelete = ForeignKey.CASCADE))

public class Lieu {
    @PrimaryKey(autoGenerate = true)
    private int id_lieu;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "surface")
    private String surface;

    @ColumnInfo(name = "nb_individu")
    private String nbIndividu;

    @ColumnInfo(name = "latitude")
    private int latittude;

    @ColumnInfo(name = "longitude")
    private int longitude;

    @ColumnInfo(name = "remarques")
    private String remarques;

    public Lieu(String type, String surface, String nbIndividu, int latittude, int longitude, String remarques){
        this.type = type;
        this.surface = surface;
        this.nbIndividu = nbIndividu;
        this.latittude = latittude;
        this.longitude= longitude;
        this.remarques = remarques;
    }

    public int getId_lieu() {
        return id_lieu;
    }

    public void setId_lieu(int id_lieu) {
        this.id_lieu = id_lieu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String  surface) {
        this.surface = surface;
    }

    public String  getNbIndividu() {
        return nbIndividu;
    }

    public void setNbIndividu(String  nbIndividu) {
        this.nbIndividu = nbIndividu;
    }

    public int getLatittude() {
        return latittude;
    }

    public void setLatittude(int latittude) {
        this.latittude = latittude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
