package com.example.teakvolumetriccalculation.modelo;

public class User {

    String imageUrl;
    String volumen;
    String volumenAprox;

    public User() {}

    // Constructor
    public User(String imageUrl,String volumen, String volumenAprox) {
        this.imageUrl = imageUrl;
        this.volumen = volumen;
        this.volumenAprox = volumenAprox;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getVolumenAprox() {
        return volumenAprox;
    }

    public void setVolumenAprox(String volumenAprox) {
        this.volumenAprox = volumenAprox;
    }

    /*String imagenUrl;
    long volumen, volumenAprox;

    // Constructor
    public User(String imagenUrl, long volumen, long volumenAprox) {
        this.imagenUrl = imagenUrl;
        this.volumen = volumen;
        this.volumenAprox = volumenAprox;
    }

    // Getters y setters
    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public long getVolumen() {
        return volumen;
    }

    public void setVolumen(long volumen) {
        this.volumen = volumen;
    }

    public long getVolumenAprox() {
        return volumenAprox;
    }

    public void setVolumenAprox(long volumenAprox) {
        this.volumenAprox = volumenAprox;
    }*/

}
