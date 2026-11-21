package com.e_ticketing.dao;

import com.e_ticketing.config.DbConnection;
import com.e_ticketing.model.Bilet;
import com.e_ticketing.model.Eveniment;
import com.e_ticketing.model.User;
import com.e_ticketing.model.Rol;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BiletDao {

    // Metoda privata pentru gasirea user id dupa nume
    private int getUserIdByNume(String nume) {
        String sql = "SELECT id FROM user WHERE nume = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nume);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la gasirea userului: " + e.getMessage());
        }
        return -1;
    }

    // Metoda privata pentru gasirea event id dupa nume
    private int getEventIdByNume(String nume) {
        String sql = "SELECT id FROM event WHERE nume = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nume);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la gasirea evenimentului: " + e.getMessage());
        }
        return -1;
    }

    // Inserare bilet nou
    public void insertB(Bilet bilet) {
        String sql = "INSERT INTO ticket (user_id, event_id, nr_loc) VALUES (?, ?, ?)";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int userId = getUserIdByNume(bilet.getUser().getNume());
            int eventId = getEventIdByNume(bilet.getEveniment().getNume());

            if (userId == -1 || eventId == -1) {
                System.out.println("User sau Eveniment nu există în baza de date.");
                return;
            }

            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setInt(3, 0); // pune nr_loc corect dacă îl ai

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea biletului: " + e.getMessage());
        }
    }

    // Actualizare bilet după ID
    public void update(int biletId, Bilet bilet) {
        String sql = "UPDATE ticket SET user_id = ?, event_id = ?, nr_loc = ? WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int userId = getUserIdByNume(bilet.getUser().getNume());
            int eventId = getEventIdByNume(bilet.getEveniment().getNume());

            if (userId == -1 || eventId == -1) {
                System.out.println("User sau Eveniment nu există în baza de date.");
                return;
            }

            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setInt(3, 0); // pune nr_loc corect dacă îl ai
            ps.setInt(4, biletId);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea biletului: " + e.getMessage());
        }
    }

    // Ștergere bilet după ID
    public void delete(int biletId) {
        String sql = "DELETE FROM ticket WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, biletId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la stergerea biletului: " + e.getMessage());
        }
    }

    public List<Bilet> getAll() {
        List<Bilet> bilete = new ArrayList<>();
        String sql = "SELECT t.id AS bilet_id, " +
                "u.id AS user_id, u.nume AS user_nume, u.email AS user_email, u.parolaHash AS user_parola, u.rol AS user_rol, " +
                "e.id AS event_id, e.nume AS event_nume, e.data, e.pret_bilet, e.locatie_id " +
                "FROM ticket t " +
                "JOIN user u ON t.user_id = u.id " +
                "JOIN event e ON t.event_id = e.id";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int biletId = rs.getInt("bilet_id");

                int userId = rs.getInt("user_id");
                String userNume = rs.getString("user_nume");
                String userEmail = rs.getString("user_email");
                String userParolaHash = rs.getString("user_parola");
                Rol userRol = Rol.valueOf(rs.getString("user_rol"));

                User user = new User(userId, userNume, userEmail, userParolaHash, userRol, true);

                int eventId = rs.getInt("event_id");
                String eventNume = rs.getString("event_nume");
                LocalDate eventData = rs.getDate("data").toLocalDate();
                double pretBilet = rs.getDouble("pret_bilet");
                int locatieId = rs.getInt("locatie_id");

                Eveniment eveniment = new Eveniment(eventId, eventNume, eventData, locatieId, 0, pretBilet);

                Bilet bilet = new Bilet(biletId, eveniment, user);
                bilete.add(bilet);
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea biletelor: " + e.getMessage());
        }
        return bilete;
    }


}
