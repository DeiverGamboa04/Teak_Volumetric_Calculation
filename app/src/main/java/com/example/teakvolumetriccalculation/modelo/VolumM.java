package com.example.teakvolumetriccalculation.modelo;

public class VolumM {

    String alturaComercial;
    String constante;
    String diametro;
    String factorForma;
    String volumen;
    String volumenAproximado;

    public VolumM() {}

    public VolumM(String diametro, String alturaComercial, String factorForma, String constante, String volumen, String volumenAproximado) {
        this.diametro = diametro;
        this.alturaComercial = alturaComercial;
        this.factorForma = factorForma;
        this.constante = constante;
        this.volumen = volumen;
        this.volumenAproximado = volumenAproximado;
    }

    public String getAlturaComercial() {
        return alturaComercial;
    }

    public void setAlturaComercial(String alturaComercial) {
        this.alturaComercial = alturaComercial;
    }

    public String getConstante() {
        return constante;
    }

    public void setConstante(String constante) {
        this.constante = constante;
    }

    public String getDiametro() {
        return diametro;
    }

    public void setDiametro(String diametro) {
        this.diametro = diametro;
    }

    public String getFactorForma() {
        return factorForma;
    }

    public void setFactorForma(String factorForma) {
        this.factorForma = factorForma;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getVolumenAproximado() {
        return volumenAproximado;
    }

    public void setVolumenAproximado(String volumenAproximado) {
        this.volumenAproximado = volumenAproximado;
    }

    /*double diametro, factorForma, constante, volumen, volumenAproximado;
    long alturaComercial;

    public VolumM() {}

    public VolumM(double diametro, long alturaComercial, double factorForma, double constante, double volumen, double volumenAproximado) {
        this.diametro = diametro;
        this.alturaComercial = alturaComercial;
        this.factorForma = factorForma;
        this.constante = constante;
        this.volumen = volumen;
        this.volumenAproximado = volumenAproximado;
    }

    public double getDiametro() {
        return diametro;
    }

    public void setDiametro(double diametro) {
        this.diametro = diametro;
    }

    public long getAlturaComercial() {
        return alturaComercial;
    }

    public void setAlturaComercial(long alturaComercial) {
        this.alturaComercial = alturaComercial;
    }

    public double getFactorForma() {
        return factorForma;
    }

    public void setFactorForma(double factorForma) {
        this.factorForma = factorForma;
    }

    public double getConstante() {
        return constante;
    }

    public void setConstante(double constante) {
        this.constante = constante;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public double getVolumenAproximado() {
        return volumenAproximado;
    }

    public void setVolumenAproximado(double volumenAproximado) {
        this.volumenAproximado = volumenAproximado;
    }*/

    /*int AlturaComercial;

    double Diametro, FactorForma, Constante, Volumen, VolumenAproximado;

    // Constructor vacío necesario para Firebase
    public VolumM() {}

    // Constructor con parámetros
    public VolumM(double diametro, int alturaComercial, double factorForma, double constante, double volumen, double volumenAproximado) {
        Diametro = diametro;
        AlturaComercial = alturaComercial;
        FactorForma = factorForma;
        Constante = constante;
        Volumen = volumen;
        VolumenAproximado = volumenAproximado;
    }

    public int getAlturaComercial() {
        return AlturaComercial;
    }

    public void setAlturaComercial(int alturaComercial) {
        AlturaComercial = alturaComercial;
    }

    public double getDiametro() {
        return Diametro;
    }

    public void setDiametro(double diametro) {
        Diametro = diametro;
    }

    public double getFactorForma() {
        return FactorForma;
    }

    public void setFactorForma(double factorForma) {
        FactorForma = factorForma;
    }

    public double getConstante() {
        return Constante;
    }

    public void setConstante(double constante) {
        Constante = constante;
    }

    public double getVolumen() {
        return Volumen;
    }

    public void setVolumen(double volumen) {
        Volumen = volumen;
    }

    public double getVolumenAproximado() {
        return VolumenAproximado;
    }

    public void setVolumenAproximado(double volumenAproximado) {
        VolumenAproximado = volumenAproximado;
    }*/
}
