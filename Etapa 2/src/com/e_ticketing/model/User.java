package com.e_ticketing.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;

public class User {
    private int id;
    private String nume;
    private String email;
    private String parolaHash;
    private Rol rol;

    public User(){

    }
    public User(int id, String nume, String email, String parola, Rol rol) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.parolaHash = hashParola(parola);
        this.rol = rol;
    }
    public User(int id, String nume, String email, String parola, Rol rol, boolean Hash) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.parolaHash = parola;
        this.rol = rol;
    }


    public static String hashParola(String parola) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(parola.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean verificaParola(String parola) {
        return parolaHash.equals(hashParola(parola));
    }

    public String getEmail() { return email; }
    public String getNume() { return nume; }
    public Rol getRol() { return rol; }
    @Override
    public String toString() {
        return nume + " (" + email + ")";
    }
    public void salveazaInFisier(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            String linie = nume + "," + email + "," + parolaHash + "," + rol.toString();
            bw.write(linie);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Eroare la salvarea user-ului: " + e.getMessage());
        }
    }
    public String getParola(){
        return parolaHash;
    }
    public void setParola(String parola){
        this.parolaHash = hashParola(parola);
    }
    public void setRol(Rol rol){
        this.rol = rol;
    }
    public void setNume(String nume){
        this.nume = nume;
    }
    public void setId(int idn){
        this.id = idn;
    }
    public int getId(){
        return this.id;
    }
}
