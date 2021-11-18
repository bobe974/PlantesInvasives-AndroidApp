package com.example.planteinvasives.roomDataBase.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Classe correspondant a la table Eleve de la base de données
 * @author etienne baillif
 * @version 1.0
 */
@Entity
public class Eleve {
    @PrimaryKey
    private int id_eleve;

    private String nom;
    private String prenom;

    public Eleve(int id_eleve, String nom, String prenom){
        this.id_eleve = id_eleve;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId_eleve() {
        return id_eleve;
    }

    public void setId_eleve(int id_eleve) {
        this.id_eleve = id_eleve;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
