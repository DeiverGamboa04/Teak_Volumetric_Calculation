package com.example.teakvolumetriccalculation.modelo;

public class User {

    String imageUrl;
    String diametro;
    String alturacomercial;
    String factorforma;
    String constante;
    String volumen;
    String volumenAprox;

    public User() {}

    // Constructor
    public User(String imageUrl,String volumen, String volumenAprox, String diametro, String alturacomercial, String factorforma, String constante) {
        this.imageUrl = imageUrl;
        this.diametro = diametro;
        this.alturacomercial = alturacomercial;
        this.factorforma = factorforma;
        this.constante = constante;
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

    public String getDiametro() {
        return diametro;
    }

    public void setDiametro(String diametro) {
        this.diametro = diametro;
    }

    public String getAlturacomercial() {
        return alturacomercial;
    }

    public void setAlturacomercial(String alturacomercial) {
        this.alturacomercial = alturacomercial;
    }

    public String getFactorforma() {
        return factorforma;
    }

    public void setFactorforma(String factorforma) {
        this.factorforma = factorforma;
    }

    public String getConstante() {
        return constante;
    }

    public void setConstante(String constante) {
        this.constante = constante;
    }
}
