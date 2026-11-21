package com.e_ticketing.dao;

import com.e_ticketing.config.DbConnection;
import com.e_ticketing.model.Concert;
import com.e_ticketing.model.Locatie;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConcertDao {

    public void insertC(String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet, String artist) {
        String sql = "INSERT INTO event (nume, data, total_bilete, pret_bilet, tip, artist, locatie_id) VALUES (?, ?, ?, ?, 'CONCERT', ?,?)";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {



            ps.setString(1, nume);
            ps.setDate(2, java.sql.Date.valueOf(data));
            ps.setInt(3, totalBilete);
            ps.setDouble(4, pretBilet);
            ps.setString(5, artist);
            ps.setInt(6, idLoc);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea concertului: " + e.getMessage());
        }
        AuditService.log("INSERT");
    }

    public List<Concert> getAllC() {
        List<Concert> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.nume, e.data, e.total_bilete, e.pret_bilet, e.locatie_id, e.artist, " +
                "l.nume AS locatie_nume, l.adresa, l.capacitate " +
                "FROM event e " +
                "JOIN location l ON e.locatie_id = l.id " +
                "WHERE e.tip = 'CONCERT'";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int locatieId = rs.getInt("locatie_id");

                Locatie locatie = new Locatie(
                        locatieId,
                        rs.getString("locatie_nume"),
                        rs.getString("adresa"),
                        rs.getInt("capacitate")
                );

                Concert concert = new Concert(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getDate("data").toLocalDate(),
                        locatieId,
                        rs.getInt("total_bilete"),
                        rs.getDouble("pret_bilet"),
                        rs.getString("artist")
                );

                concert.setLocatie(locatie);
                lista.add(concert);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la getAll Concert: " + e.getMessage());
        }

        AuditService.log("READ");
        return lista;
    }





    public void updateC(int idC, String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet, String artist) {
        String sql = "UPDATE event SET nume = ?, data = ?, locatie_id = ?, total_bilete = ?, pret_bilet = ?, artist = ? WHERE id = ? AND tip = 'CONCERT'";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nume);
            ps.setDate(2, Date.valueOf(data));
            ps.setInt(3, idLoc);
            ps.setInt(4, totalBilete);
            ps.setDouble(5, pretBilet);
            ps.setString(6, artist);
            ps.setInt(7, idC);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea concertului: " + e.getMessage());
        }
        AuditService.log("UPDATE");
    }


    public void deleteC(int id) {
        String sql = "DELETE FROM event WHERE id = ? AND tip = 'CONCERT'";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea concertului: " + e.getMessage());
        }
        AuditService.log("DELETE");
    }


}
