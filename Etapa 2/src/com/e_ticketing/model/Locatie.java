package com.e_ticketing.model;

public class Locatie {
    private int id;
    private String nume;
    private String adresa;
    private int capacitate;

    public Locatie(int id, String nume, String adresa, int capacitate) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.capacitate = capacitate;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getCapacitate() {
        return capacitate;
    }

    @Override
    public String toString() {
        return id + nume + " - " + adresa + " (capacitate: " + capacitate + ")";
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
