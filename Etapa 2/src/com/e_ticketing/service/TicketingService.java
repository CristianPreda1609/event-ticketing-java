package com.e_ticketing.service;

import com.e_ticketing.model.*;
import com.e_ticketing.dao.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class TicketingService {
    private List<Eveniment> evenimente = new ArrayList<>();
    private Map<User, List<Bilet>> bileteClienti = new HashMap<>();
    private Set<User> utilizator = new HashSet<>();
    private List<Locatie> locatii = new ArrayList<>();
    private String parolahash;

    // ===============================
    // ==== UTILE
    // ===============================


    public void incarcaDateDinBazaDeDate() {
        utilizator.clear();
        evenimente.clear();
        locatii.clear();
        bileteClienti.clear();

        UserDao userDao = new UserDao();
        ConcertDao concertDao = new ConcertDao();
        FestivalDao festivalDao = new FestivalDao();
        ClubDao clubDao = new ClubDao();
        LocatieDao locationDao = new LocatieDao();
        BiletDao ticketDao = new BiletDao();

        Set<User> usersFromDb = userDao.getAll();
        utilizator.addAll(usersFromDb);

        List<Locatie> locatiiFromDb = locationDao.getAllLoc();
        locatii.addAll(locatiiFromDb);

        List<Concert> concerte = concertDao.getAllC();
        List<Festival> festivaluri = festivalDao.getAllF();
        List<Club> cluburi = clubDao.getAllK();

        evenimente.addAll(concerte);
        evenimente.addAll(festivaluri);
        evenimente.addAll(cluburi);

        List<Bilet> bileteFromDb = ticketDao.getAll();
        for (Bilet bilet : bileteFromDb) {
            User user = bilet.getUser();
            bileteClienti.computeIfAbsent(user, k -> new ArrayList<>()).add(bilet);
        }
    }



    private String incarcaHashDinFisier() {
        try {
            return new String(Files.readAllBytes(Paths.get("src/com/e_ticketing/resources/pass.txt"))).trim();
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut citi parola administratorului!", e);
        }
    }
    public static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public User logareClient() {
        while(true){
            System.out.println("\n### Meniu de logare ###");
            System.out.println("1. Logare");
            System.out.println("2. Exit");
            System.out.print("Introduceți optiunea: ");
            Scanner scanner = new Scanner(System.in);
            int optiune = scanner.nextInt();
            scanner.nextLine();
            switch (optiune) {
                case 1:
                    System.out.print("Nume : ");
                    String nume = scanner.nextLine();
                    System.out.print("Parola : ");
                    String parola = scanner.nextLine();

                    for (User u : utilizator) {
                        if(u.getNume().equals(nume)){
                            if(u.verificaParola(parola)){
                                System.out.println("Bun venit " + u.getNume() + "!");
                                return u;
                            }
                        }

//                        if (u.getNume().equals(nume) && u.verificaParola(parola)) {
//                            System.out.println("Bun venit " + u.getNume() + "!");
//                            return u;
//                        }
                    }
                    break;


                case 2:
                    return null;
            }

            System.out.println("Nu exista niciun client cu acest nume!");
        }

    }
    // ===============================
    // ==== CREARE OBIECTE
    // ===============================

    public void creeazaClient(String nume, String email, String parola) {
        this.parolahash = incarcaHashDinFisier();
        UserDao userDao = new UserDao();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n### Meniu de sign-up ###");
            System.out.println("1. Client");
            System.out.println("2. Organizator");
            System.out.println("3. Admin");
            System.out.println("4. Exit");
            System.out.print("Introduceți rolul: ");

            int rol = Integer.parseInt(scanner.nextLine());
            if (rol == 4) break;

            if (rol == 3) {
                System.out.print("Introduceți parola admin: ");
                String parolaAdmin = scanner.nextLine();
                if (md5Hash(parolaAdmin).equals(parolahash)) {
                    userDao.insertU(nume, email, parola, Rol.ADMIN);
                    System.out.println("Admin inregistrat cu succes!");
                    utilizator.clear();
                    Set<User> usersFromDb = userDao.getAll();
                    utilizator.addAll(usersFromDb);
                } else {
                    System.out.println("Parola introdusa nu corespunde!");
                }
                break;
            }

            if (rol == 2) {
                userDao.insertU(nume, email, parola, Rol.ORGANIZATOR);
                System.out.println("Organizator inregistrat cu succes!");
                utilizator.clear();
                Set<User> usersFromDb = userDao.getAll();
                utilizator.addAll(usersFromDb);
                break;

            }

            if (rol == 1) {
                userDao.insertU(nume, email, parola, Rol.CLIENT);
                System.out.println("Client inregistrat cu succes!");
                utilizator.clear();
                Set<User> usersFromDb = userDao.getAll();
                utilizator.addAll(usersFromDb);
                break;
            }
        }
    }

    private void refreshEvenimente() {
        evenimente.clear();

        ConcertDao concertDao = new ConcertDao();
        FestivalDao festivalDao = new FestivalDao();
        ClubDao clubDao = new ClubDao();

        List<Concert> concerte = concertDao.getAllC();
        List<Festival> festivaluri = festivalDao.getAllF();
        List<Club> cluburi = clubDao.getAllK();

        evenimente.addAll(concerte);
        evenimente.addAll(festivaluri);
        evenimente.addAll(cluburi);
    }
    public Locatie creeazaLocatie(String nume, String oras, int capacitate) {
        LocatieDao locatieDao = new LocatieDao();

        locatieDao.insertLoc(nume, oras, capacitate);

        locatii = locatieDao.getAllLoc();

        return locatii.stream()
                .max(Comparator.comparingInt(Locatie::getId))
                .orElse(null);
    }

    public void stergeLocatie(int id) {
        LocatieDao locatieDao = new LocatieDao();
        locatieDao.deleteLoc(id);

        locatii = locatieDao.getAllLoc();
    }

    public void actualizeazaLocatie(int id, String numeNou, String adresaNoua, int capacitateNoua) {
        LocatieDao locatieDao = new LocatieDao();
        locatieDao.updateLoc(id, numeNou, adresaNoua, capacitateNoua);

        locatii = locatieDao.getAllLoc();
    }


    public void creeazaConcert(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, String artist) {
        ConcertDao concertDao = new ConcertDao();
        concertDao.insertC(nume, data, locatie.getId(), totalBilete, pretBilet, artist);
        refreshEvenimente();
    }

    public void creeazaFestival(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, List<String> genuri) {
        FestivalDao festivalDao = new FestivalDao();
        festivalDao.insertF(nume, data, locatie.getId(), totalBilete, pretBilet, genuri);
        refreshEvenimente();
    }

    public void creeazaClub(String nume, LocalDate data, Locatie locatie, int totalBilete, int pretBilet, String dj, int varstaMinima) {
        ClubDao clubDao = new ClubDao();
        clubDao.insertK(nume, data, locatie.getId(), totalBilete, pretBilet, dj, varstaMinima);
        refreshEvenimente();
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

    public boolean cumparaBilet(User client, Eveniment eveniment) {
        if (eveniment.vindeBilet()) {
            BiletDao biletDao = new BiletDao();
            Bilet biletTemp = new Bilet(0, eveniment, client);
            int idBilet = biletDao.insertB(biletTemp);
            if (idBilet != -1) {
                Bilet bilet = new Bilet(idBilet, eveniment, client);
                bileteClienti.putIfAbsent(client, new ArrayList<>());
                bileteClienti.get(client).add(bilet);
                System.out.println("Bilet cumpărat cu succes pentru " + client.getNume());
                return true;
            } else {
                System.out.println("Eroare la cumpărarea biletului.");
                return false;
            }
        } else {
            System.out.println("Nu mai sunt bilete disponibile pentru acest eveniment.");
            return false;
        }
    }


    public void afiseazaBileteClient(User client) {
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
        for (User c : utilizator) {
            if(c.getRol() == Rol.CLIENT || c.getRol() == Rol.ORGANIZATOR || c.getRol() == Rol.ADMIN) {
                System.out.println(" - " + c);
            }
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

    public Set<User> getClienti() {
        return utilizator;
    }

    public List<Eveniment> getEvenimente() {
        return evenimente;
    }
}
