package com.e_ticketing.service;

import com.e_ticketing.model.*;

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

    public void incarcaClienti() {
        utilizator.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("src/com/e_ticketing/resources/Clienti.txt"))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                String[] parts = linie.split(",");
                if (parts.length == 4) {
                    User client = new User(parts[0], parts[1], parts[2], parts[3].equals("CLIENT") ? Rol.CLIENT : parts[3].equals("ORGANIZATOR") ? Rol.ORGANIZATOR : Rol.ADMIN, true);
                    utilizator.add(client);
                }
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului: " + e.getMessage());
        }
    }
    public void salveazaTotiiClientii() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/com/e_ticketing/resources/Clienti.txt"))) {

        }
        catch (IOException e) {
            System.out.println("Eroare la golirea fisierului: " + e.getMessage());
        }

        for (User u : utilizator) {
            u.salveazaInFisier("src/com/e_ticketing/resources/Clienti.txt");
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
        while(true){
            System.out.println("\n### Meniu de sign-up ###");
            System.out.println("1. Client");
            System.out.println("2. Organizator");
            System.out.println("3. Admin");
            System.out.println("4. Exit");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduceți rolul: ");
            int rol = Integer.parseInt(scanner.nextLine());
            if(rol == 4)
                break;
            if(rol == 3){
                System.out.print("Introduceți parola admin: ");
                String parolaAdmin = scanner.nextLine();
                if(md5Hash(parolaAdmin).equals(parolahash)){
                    User admin = new User(nume, email, parola, Rol.ADMIN);
                    utilizator.add(admin);
                    System.out.println("Admin inregistrat cu succes!");
                }
                else
                    System.out.println("Parola introdusa nu corespunde!");
                break;
            }
            if(rol == 2){
                User organizator = new User(nume, email, parola, Rol.ORGANIZATOR);
                utilizator.add(organizator);
                System.out.println("Organizator inregistrat cu succes!");
                break;
            }
            if(rol == 1){
                User client = new User(nume, email, parola, Rol.CLIENT);
                utilizator.add(client);
                System.out.println("Client inregistrat cu succes!");
                break;
            }
        }
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

    public boolean cumparaBilet(User client, Eveniment eveniment) {
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
