package com.e_ticketing.model;

import java.time.LocalDate;

public class Eveniment {
    private String nume;
    private LocalDate data;
    private Locatie locatie;
    private int totalBilete;
    private int bileteVandute;
    private double pretBilet;

    public Eveniment(String nume, LocalDate data, Locatie locatie, int totalBilete, double pretBilet) {
        this.nume = nume;
        this.data = data;
        this.locatie = locatie;
        this.totalBilete = totalBilete;
        this.bileteVandute = 0;
        this.pretBilet = pretBilet;
    }


    public String getNume() {
        return nume;
    }
    public double getPretBilet() {
        return pretBilet;
    }

    public LocalDate getData() {
        return data;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public int getBileteDisponibile() {
        return totalBilete - bileteVandute;
    }

    public boolean vindeBilet() {
        if (getBileteDisponibile() > 0) {
            bileteVandute++;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nume + " - " + data + " la " + locatie.getNume() +
                " | Bilete disponibile: " + getBileteDisponibile() +
                " | Pret bilet: " + pretBilet + " RON";

    }
}
