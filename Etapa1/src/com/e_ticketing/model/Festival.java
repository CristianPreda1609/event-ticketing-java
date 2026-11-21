package com.e_ticketing.model;

import java.time.LocalDate;
import java.util.List;

public class Festival extends Eveniment {
    private List<String> genuri;

    public Festival(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, List<String> genuri) {
        super(nume, data, locatie, totalBilete,pretBilet);
        this.genuri = genuri;
    }

//    public List<String> getGenuri() {
//        return genuri;
//    }
//
//    public void setGenuri(List<String> genuri) {
//        this.genuri = genuri;
//    }

    @Override
    public String toString() {
        return super.toString() + " | Genuri: " + String.join(", ", genuri);
    }
}
