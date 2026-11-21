package com.e_ticketing.dao;

import com.e_ticketing.model.Festival;
import com.e_ticketing.model.Locatie;
import com.e_ticketing.config.DbConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FestivalDao {

    public void insertF(String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet, List<String> genuri) {
        String sql = """
        INSERT INTO event (nume, data, total_bilete, pret_bilet, tip, genuri, locatie_id)
        VALUES (?, ?, ?, ?, 'FESTIVAL', ?, ?)
    """;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nume);
            ps.setDate(2, java.sql.Date.valueOf(data));
            ps.setInt(3, totalBilete);
            ps.setDouble(4, pretBilet);

            String genuriStr = String.join(",", genuri);
            ps.setString(5, genuriStr);

            ps.setInt(6, idLoc);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Festival inserat cu succes.");
            } else {
                System.out.println("Nu s-a inserat niciun festival.");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea festivalului: " + e.getMessage());
        }
    }


    public void deleteF(int id) {
        String sql = "DELETE FROM event WHERE id = ? AND tip = 'FESTIVAL'";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea festivalului: " + e.getMessage());
        }
    }

    public void updateF(int idF, String nume, LocalDate data, int idLoc, int totalBilete, double pretBilet, List<String> genuri) {
        String updateSql = """
        UPDATE event
        SET nume = ?, data = ?, locatie_id = ?, total_bilete = ?, pret_bilet = ?, genuri = ?
        WHERE id = ? AND tip = 'FESTIVAL'
    """;

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(updateSql)) {

            ps.setString(1, nume);
            ps.setDate(2, java.sql.Date.valueOf(data));
            ps.setInt(3, idLoc);
            ps.setInt(4, totalBilete);
            ps.setDouble(5, pretBilet);
            ps.setString(6, String.join(",", genuri));
            ps.setInt(7, idF);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Festival actualizat cu succes.");
            } else {
                System.out.println("Nu s-a găsit niciun festival cu id-ul specificat.");
            }

        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea festivalului: " + e.getMessage());
        }
    }
    public List<Festival> getAllF() {
        List<Festival> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.nume, e.data, e.total_bilete, e.pret_bilet, e.locatie_id, f.genuri, " +
                "l.nume AS locatie_nume, l.adresa, l.nr_locuri " +
                "FROM event e " +
                "JOIN festival f ON e.id = f.id " +
                "JOIN locatie l ON e.locatie_id = l.id";

        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int locatieId = rs.getInt("locatie_id");
                String genuriStr = rs.getString("genuri");
                List<String> genuri = Arrays.asList(genuriStr.split(",\\s*"));

                Festival festival = new Festival(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getDate("data").toLocalDate(),
                        locatieId,
                        rs.getInt("total_bilete"),
                        rs.getDouble("pret_bilet"),
                        genuri
                );

                Locatie locatie = new Locatie(
                        locatieId,
                        rs.getString("locatie_nume"),
                        rs.getString("adresa"),
                        rs.getInt("nr_locuri")
                );

                festival.setLocatie(locatie);
                lista.add(festival);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la getAll Festival: " + e.getMessage());
        }

        return lista;
    }





}
