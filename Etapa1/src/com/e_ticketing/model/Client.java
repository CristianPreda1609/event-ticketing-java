package com.e_ticketing.model;

public class Client {
    private String nume;
    private String email;

    public Client(String nume, String email) {
        this.nume = nume;
        this.email = email;
    }

    public String getNume() {
        return nume;
    }

//    public String getEmail() {
//        return email;
//    }

    @Override
    public String toString() {
        return nume + " (" + email + ")";
    }
}
