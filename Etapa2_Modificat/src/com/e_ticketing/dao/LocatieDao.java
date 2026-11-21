package com.e_ticketing.dao;

import com.e_ticketing.config.DbConnection;
import com.e_ticketing.model.Locatie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocatieDao {


    public void insertLoc(String nume, String adresa, int capacitate) {
        String sql = "INSERT INTO location (nume, adresa, capacitate) VALUES (?, ?, ?)";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nume);
            ps.setString(2, adresa);
            ps.setInt(3, capacitate);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea locatiei: " + e.getMessage());
        }
    }

    public List<Locatie> getAllLoc() {
        List<Locatie> locatii = new ArrayList<>();
        String sql = "SELECT * FROM location ORDER BY id";
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Locatie locatie = new Locatie(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("adresa"),
                        rs.getInt("capacitate")
                );
                locatii.add(locatie);
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea locatiei: " + e.getMessage());
        }
        return locatii;
    }


    public void updateLoc(int id, String numeNou, String adresaNoua, int capacitateNoua) {
        String sql = "UPDATE location SET nume = ?, adresa = ?, capacitate = ? WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeNou);
            ps.setString(2, adresaNoua);
            ps.setInt(3, capacitateNoua);
            ps.setInt(4, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("Nu a fost gasita nicio locatie cu id-ul: " + id);
            } else {
                System.out.println("Locatia a fost actualizata cu succes.");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea locatiei: " + e.getMessage());
        }
    }


    public void deleteLoc(int id) {
        String sql = "DELETE FROM location WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la stergerea locatiei: " + e.getMessage());
        }
    }
}
