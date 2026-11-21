package com.e_ticketing.model;


public class Bilet {
    private Eveniment eveniment;
    private Client client;
    private double pret;

    public Bilet(Eveniment eveniment, Client client) {
        this.eveniment = eveniment;
        this.client = client;
        this.pret = eveniment.getPretBilet();
    }

//    public Eveniment getEveniment() {
//        return eveniment;
//    }
//
//    public Client getClient() {
//        return client;
//    }
//
//    public double getPret() {
//        return pret;
//    }

    @Override
    public String toString() {
        return "Bilet pentru " + eveniment.getNume() + " - "+" - " + pret + " RON | Client: " + client.getNume();
    }
}
