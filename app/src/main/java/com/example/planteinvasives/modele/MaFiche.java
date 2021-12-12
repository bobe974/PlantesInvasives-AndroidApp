package com.example.planteinvasives.modele;

public class MaFiche {
    private int id;
    private String nomPlante;
    private String date;
    private String photoPath;

    public MaFiche(int id,String nomplante, String date, String photoPath){
        this.id = id;
        this.nomPlante = nomplante;
        this.date = date;
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public String getNomPlante() {
        return nomPlante;
    }

    public String getDate() {
        return date;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
