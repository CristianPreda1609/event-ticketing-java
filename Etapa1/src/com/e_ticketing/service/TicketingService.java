package com.e_ticketing.service;

import com.e_ticketing.model.*;

import java.time.LocalDate;
import java.util.*;

public class TicketingService {
    private List<Eveniment> evenimente = new ArrayList<>();
    private Map<Client, List<Bilet>> bileteClienti = new HashMap<>();
    private Set<Client> clienti = new HashSet<>();
    private List<Locatie> locatii = new ArrayList<>();

    // ===============================
    // ==== CREARE OBIECTE
    // ===============================

    public Client creeazaClient(String nume, String email) {
        Client client = new Client(nume, email);
        clienti.add(client);
        return client;
    }

    public Locatie creeazaLocatie(String nume, String oras, int capacitate) {
        Locatie locatie = new Locatie(nume, oras, capacitate);
        locatii.add(locatie);
        return locatie;
    }

    public Concert creeazaConcert(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, String artist) {
        Concert concert = new Concert(nume, data, locatie, totalBilete, pretBilet, artist);
        evenimente.add(concert);
        return concert;
    }

    public Festival creeazaFestival(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, List<String> genuri) {
        Festival festival = new Festival(nume, data, locatie, totalBilete, pretBilet, genuri);
        evenimente.add(festival);
        return festival;
    }

    public Club creeazaClub(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, String dj, int varstaMinima) {
        Club club = new Club(nume, data, locatie, totalBilete, pretBilet, dj, varstaMinima);
        evenimente.add(club);
        return club;
    }

    // ===============================
    // ==== FUNCȚIONALITAȚI DE SISTEM
    // ===============================

    public void afiseazaEvenimente() {
        evenimente.sort(Comparator.comparing(Eveniment::getData));
        System.out.println("\n Lista Evenimentelor:");
        for (Eveniment e : evenimente) {
            System.out.println(" - " + e);
        }
    }

    public boolean cumparaBilet(Client client, Eveniment eveniment) {
        if (eveniment.vindeBilet()) {
            Bilet bilet = new Bilet(eveniment, client);
            bileteClienti.putIfAbsent(client, new ArrayList<>());
            bileteClienti.get(client).add(bilet);
            System.out.println(" Bilet cumparat cu succes pentru " + client.getNume());
            return true;
        } else {
            System.out.println(" Nu mai sunt bilete disponibile pentru acest eveniment.");
            return false;
        }
    }

    public void afiseazaBileteClient(Client client) {
        List<Bilet> bilete = bileteClienti.getOrDefault(client, new ArrayList<>());
        if (bilete.isEmpty()) {
            System.out.println(client.getNume() + " nu are bilete.");
        } else {
            System.out.println("\n Bilete pentru " + client.getNume() + ":");
            for (Bilet b : bilete) {
                System.out.println("  - " + b);
            }
        }
    }

    public void afiseazaClienti() {
        System.out.println("\n Clienti inregistrati:");
        for (Client c : clienti) {
            System.out.println(" - " + c);
        }
    }

    public void afiseazaEvenimenteDinOras(String oras) {
        System.out.println("\n Evenimente in " + oras + ":");
        for (Eveniment e : evenimente) {
            if (e.getLocatie().getAdresa().equalsIgnoreCase(oras)) {
                System.out.println(" - " + e);
            }
        }
    }

    public void afiseazaLocatii() {
        System.out.println("\n Locatii disponibile:");
        for (Locatie l : locatii) {
            System.out.println(" - " + l);
        }
    }

    // ===============================
    // ==== GETTERI/SETTERI UTILI
    // ===============================

//    public String getEmail(Client client) {
//        return client.getEmail();
//    }
//
//    public int getCapacitate(Locatie locatie) {
//        return locatie.getCapacitate();
//    }
//
//    public String getArtist(Concert concert) {
//        return concert.getArtist();
//    }
//
//    public void setArtist(Concert concert, String artist) {
//        concert.setArtist(artist);
//    }
//
//    public String getDj(Club club) {
//        return club.getDj();
//    }
//
//    public void setDj(Club club, String dj) {
//        club.setDj(dj);
//    }
//
//    public int getVarstaMinima(Club club) {
//        return club.getVarstaMinima();
//    }
//
//    public void setVarstaMinima(Club club, int varsta) {
//        club.setVarstaMinima(varsta);
//    }
//
//    public List<String> getGenuri(Festival festival) {
//        return festival.getGenuri();
//    }
//
//    public void setGenuri(Festival festival, List<String> genuri) {
//        festival.setGenuri(genuri);
//    }
//
//    public Eveniment getEveniment(Bilet bilet) {
//        return bilet.getEveniment();
//    }
//
//    public Client getClient(Bilet bilet) {
//        return bilet.getClient();
//    }
    public List<Locatie> getLocatii() {
        return locatii;
    }

    public double getPret(Eveniment eveniment) {
        return eveniment.getPretBilet();
    }

    public Set<Client> getClienti() {
        return clienti;
    }

    public List<Eveniment> getEvenimente() {
        return evenimente;
    }
}
