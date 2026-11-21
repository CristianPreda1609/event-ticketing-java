package com.e_ticketing.model;

import java.time.LocalDate;

public class Eveniment {
    private int id;
    private int idLoc;
    private String nume;
    private LocalDate data;
    private int totalBilete;
    private int bileteVandute;
    private double pretBilet;
    private Locatie locatie;

    public Eveniment(int id,String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet) {
        this.id = id;
        this.nume = nume;
        this.data = data;
        this.idLoc = idLoc;
        this.totalBilete = totalBilete;
        this.bileteVandute = 0;
        this.pretBilet = pretBilet;
    }

    public Locatie getLocatie() {
        return locatie;
    }
    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
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

public int getIdLoc() {
        return idLoc;
}

    public int getBileteDisponibile() {
        return totalBilete - bileteVandute;
    }
    public int getTotalBilete(){
        return totalBilete;
    }

    public boolean vindeBilet() {
        if (getBileteDisponibile() > 0) {
            bileteVandute++;
            return true;
        }
        return false;
    }
    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return nume + " - " + data + " la " + locatie.getNume() +
                " | Bilete disponibile: " + getBileteDisponibile() +
                " | Pret bilet: " + pretBilet + " RON";

    }
}
