package com.e_ticketing.dao;

import com.e_ticketing.config.DbConnection;
import com.e_ticketing.model.*;
import com.e_ticketing.dao.UserDao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DatabaseInitializer {

    public static void createTables() {
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement()) {

            // User table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS user (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nume VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    parola VARCHAR(100),
                    tip ENUM('CLIENT', 'ADMIN', 'ORGANIZATOR')
                )
            """);

            // Location table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS location (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nume VARCHAR(100),
                    adresa VARCHAR(255),
                    capacitate INT
                )
            """);

            // Event table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS event (
                 id INT AUTO_INCREMENT PRIMARY KEY,
                 nume VARCHAR(100),
                 data DATE,
                 locatie_id INT,
                 total_bilete INT,
                 pret_bilet DOUBLE,
                 tip ENUM('CONCERT', 'FESTIVAL', 'CLUB'),
                 artist VARCHAR(100),            -- specific Concert
                 genuri TEXT,                   -- specific Festival 
                 dj VARCHAR(100),               -- specific Club
                 varsta_minima INT,             -- specific Club
                 FOREIGN KEY (locatie_id) REFERENCES location(id)
                )
            """);

            // Ticket table
            stmt.executeUpdate("""
            CREATE TABLE ticket (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT,
                event_id INT,
                FOREIGN KEY (user_id) REFERENCES user(id),
                FOREIGN KEY (event_id) REFERENCES event(id)
            );
            """);

            System.out.println("Tabelele au fost create cu succes.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public void initializeDatabase() {
//        try (Connection con = DbConnection.getInstance()) {
//            UserDao ud = new UserDao();
//    ud.insertU("org", "org","org", Rol.ORGANIZATOR );
//    ud.insertU("cli", "cli","cli", Rol.CLIENT);
//    ud.insertU( "cris", "cris@cris.com", "cris", Rol.ADMIN);
//    ud.insertU( "cli2", "cli2","cli2", Rol.CLIENT);
//        LocatieDao ld = new LocatieDao();
//    ld.insertLoc("Arena Nationala", "Bucuresti", 50000);
//    ld.insertLoc("Muzeul de Arta", "Cluj-Napoca", 2000);
//    LocatieDao ld2 = new LocatieDao();
//    List<Locatie> locatii = ld2.getAllLoc();
//
//            FestivalDao fest = new FestivalDao();
//            ConcertDao conc = new ConcertDao();
//            ClubDao clb = new ClubDao();
//    conc.insertC("Concert A", LocalDate.of(2025, 5, 15), locatii.get(1).getId(), 5000, 50, "Artist X");
//    fest.insertF("Festival B", LocalDate.of(2025, 6, 20), locatii.get(0).getId(), 3000, 60, Arrays.asList("Rock", "Pop"));
//    clb.insertK("Club C", LocalDate.of(2025, 7, 10), locatii.get(0).getId(), 1000, 30, "DJ Z", 18);
//
//            System.out.println("Datele au fost inserate cu succes!");
//
//} catch (Exception e) {
//        System.out.println("Eroare la initializarea bazei de date: " + e.getMessage());
//        e.printStackTrace();
//        }
//                }



}
