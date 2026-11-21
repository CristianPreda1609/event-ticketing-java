package com.e_ticketing.model;
import java.time.LocalDate;

public class Concert extends Eveniment{
    private String artist;

    public Concert(String nume, LocalDate data, Locatie locatie, int totalBilete, double pretBilet, String artist) {
        super(nume, data, locatie, totalBilete, pretBilet);
        this.artist = artist;
    }


//    public String getArtist() {
//        return artist;
//    }
//
//    public void setArtist(String artist) {
//        this.artist = artist;
//    }

    @Override
    public String toString() {
        return super.toString() + " | Artist: " + artist;
    }
}
