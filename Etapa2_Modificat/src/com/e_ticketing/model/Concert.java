package com.e_ticketing.model;
import java.time.LocalDate;

public class Concert extends Eveniment{
    private String artist;

    public Concert(int id, String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet, String artist) {
        super(id, nume, data, idLoc, totalBilete, pretBilet);
        this.artist = artist;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return super.toString() + " | Artist: " + artist;
    }
}
