package com.example.teakvolumetriccalculation.modelo;

public class User {

    private String imagenUrl;
    private float volumen, volumenAprox; // Cambiado a double para soportar decimales

    public User() {}

    // Constructor
    public User(String imagenUrl,float volumen, float volumenAprox) {
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

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }

    public double getVolumenAprox() {
        return volumenAprox;
    }

    public void setVolumenAprox(float volumenAprox) {
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
