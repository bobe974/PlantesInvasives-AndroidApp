package com.example.planteinvasives.Modele;

public class Fiche {
    private  int id;
    private Photographie photo;
    private Plante plante;


    public Fiche(Photographie photo, Plante plante){
        this.photo = photo;
        this.plante = plante;
    }

}
