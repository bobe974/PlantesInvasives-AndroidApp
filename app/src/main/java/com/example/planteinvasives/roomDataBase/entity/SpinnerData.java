package com.example.planteinvasives.roomDataBase.entity;

import androidx.room.Entity;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

/** Table contenant les 5 champs des noms des especes
 * @author etienne baillif
 * @version 1.0
 */
@Entity
public class SpinnerData {


    @PrimaryKey()

    private int id_label;

    private String nomPlante;

    public SpinnerData(int id_label, String nomPlante){
        this.nomPlante = nomPlante;
        this.id_label = id_label;
    }


    public String getNomPlante() {
        return nomPlante;
    }

    public void setNomPlante(String nomPlante) {
        this.nomPlante = nomPlante;
    }

    public int getId_label() {
        return id_label;
    }

    public void setId_label(int id_label) {
        this.id_label = id_label;
    }
}
