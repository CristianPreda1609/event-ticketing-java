package com.e_ticketing.dao;

import com.e_ticketing.config.DbConnection;
import com.e_ticketing.model.Club;
import com.e_ticketing.model.Locatie;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClubDao {


    public void insertK(String nume, LocalDate data, int idLoc, int totalBilete, int pretBilet, String dj, int varstaMinima) {
        String sql = """
        INSERT INTO event (nume, data, locatie_id, total_bilete, pret_bilet, tip, dj, varsta_minima)
        VALUES (?, ?, ?, ?, ?, 'CLUB', ?, ?)
        """;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nume);
            ps.setDate(2, Date.valueOf(data));
            ps.setInt(3, idLoc);
            ps.setInt(4, totalBilete);
            ps.setInt(5, pretBilet);
            ps.setString(6, dj);
            ps.setInt(7, varstaMinima);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la inserarea clubului: " + e.getMessage());
        }
    }


    public void updateK(int id, String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet, String dj, int varstaMinima) {
        String sql = """
        UPDATE event
        SET nume = ?, data = ?, locatie_id = ?, total_bilete = ?, pret_bilet = ?, dj = ?, varsta_minima = ?
        WHERE id = ? AND tip = 'CLUB'
        """;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nume);
            ps.setDate(2, Date.valueOf(data));
            ps.setInt(3, idLoc);
            ps.setInt(4, totalBilete);
            ps.setDouble(5, pretBilet);
            ps.setString(6, dj);
            ps.setInt(7, varstaMinima);
            ps.setInt(8, id);  // WHERE id = ?

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea clubului: " + e.getMessage());
        }
    }



    public void deleteK(int id) {
        String sql = "DELETE FROM event WHERE id = ? AND tip = 'CLUB'";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea clubului: " + e.getMessage());
        }
    }
    public List<Club> getAllK() {
        List<Club> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.nume, e.data, e.total_bilete, e.pret_bilet, e.locatie_id, c.dj, c.varsta_minima, " +
                "l.nume AS locatie_nume, l.adresa, l.nr_locuri " +
                "FROM event e " +
                "JOIN club c ON e.id = c.id " +
                "JOIN locatie l ON e.locatie_id = l.id";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int locatieId = rs.getInt("locatie_id");

                Club club = new Club(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getDate("data").toLocalDate(),
                        locatieId,
                        rs.getInt("total_bilete"),
                        rs.getDouble("pret_bilet"),
                        rs.getString("dj"),
                        rs.getInt("varsta_minima")
                );

                Locatie locatie = new Locatie(
                        locatieId,
                        rs.getString("locatie_nume"),
                        rs.getString("adresa"),
                        rs.getInt("nr_locuri")
                );

                club.setLocatie(locatie);
                lista.add(club);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la getAll Club: " + e.getMessage());
        }

        return lista;
    }



}
