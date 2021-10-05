package com.example.planteinvasives.roomDataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Fiche.class,
        parentColumns = "id_fiche",
        childColumns = "id_photo",
        onDelete = ForeignKey.CASCADE))

public class Photographie {
    @PrimaryKey(autoGenerate = true)
    public int id_photo;

    @ColumnInfo(name ="chemin_fichier" )
    private String chemin;
    @ColumnInfo(name = "Date")
    private String date;
    @ColumnInfo(name ="Latitude")
    private int latitude;
    @ColumnInfo(name ="Longitude")
    private int longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public Photographie( String chemin, String date, int longitude, int latitude){

        this.chemin = chemin;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

        public String getChemin() {
            return chemin;
        }

        public void setChemin(String chemin) {
            this.chemin = chemin;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getLatitude() {
            return latitude;
        }

        public void setLatitude(int latitude) {
            this.latitude = latitude;
        }

        public int getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }
}
