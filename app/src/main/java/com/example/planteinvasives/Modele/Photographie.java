package com.example.planteinvasives.Modele;

public class Photographie {
    private int id;
    private String chemin;
    private String date;
    private int latitude;
    private int longitude;

    public Photographie(String chemin, String date, int longitude, int latitude){
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
