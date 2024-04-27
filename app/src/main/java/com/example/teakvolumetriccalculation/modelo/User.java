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
}
