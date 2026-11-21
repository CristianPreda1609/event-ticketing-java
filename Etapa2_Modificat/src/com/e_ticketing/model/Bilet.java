package com.e_ticketing.model;


public class Bilet {
    private int id;
    private Eveniment eveniment;
    private User user;
    private double pret;

    public Bilet(int id, Eveniment eveniment, User user) {
        this.id = id;
        this.eveniment = eveniment;
        this.user = user;
        this.pret = eveniment.getPretBilet();
    }

    public Eveniment getEveniment() {
        return eveniment;
    }

    public User getUser() {
        return user;
    }

    public double getPret() {
        return pret;
    }

    @Override
    public String toString() {
        return "Bilet pentru " + eveniment.getNume() + " - "+" - " + pret + " RON | Client: " + user.getNume();
    }
}
