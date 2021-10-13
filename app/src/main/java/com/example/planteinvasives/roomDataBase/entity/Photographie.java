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

    @ColumnInfo(name = "DatePhoto")
    private String date;




    public Photographie( String chemin, String date){

        this.chemin = chemin;
        this.date = date;
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

        public int getId_photo() { return id_photo;}

        public void setId_photo(int id_photo) { this.id_photo = id_photo; }


}
