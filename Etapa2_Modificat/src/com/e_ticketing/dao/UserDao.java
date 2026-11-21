package com.e_ticketing.dao;

import com.e_ticketing.config.DbConnection;
import com.e_ticketing.model.Rol;
import com.e_ticketing.model.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDao {

    public void insertU(String nume, String email, String parola, Rol rol) {
        String sql = "INSERT INTO user (nume, email, parola, tip) VALUES (?, ?, ?, ?)";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nume);
            ps.setString(2, email);
            ps.setString(3, User.hashParola(parola));
            ps.setString(4, rol.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la inserarea user-ului: " + e.getMessage());
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM user WHERE email = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Eroare la verificarea existenței emailului: " + e.getMessage());
        }
        return false;
    }

    public void updateU(int id, String nume, String email, String parola, Rol rol) {
        String sql = "UPDATE user SET nume = ?, parola = ?, tip = ?, email = ? WHERE id = ? ";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,nume);
            ps.setString(2, parola);
            ps.setString(3, rol.name());
            ps.setString(4, email);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea user-ului: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea user-ului: " + e.getMessage());
        }
    }


    public Set<User> getAll() {
        Set<User> utilizatori = new HashSet<>();
        String sql = "SELECT * FROM user ORDER BY id ASC;";
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nume = rs.getString("nume");
                String email = rs.getString("email");
                String parola = rs.getString("parola");
                Rol rol = Rol.valueOf(rs.getString("tip"));

                utilizatori.add(new User(id, nume, email, parola, rol));
            }

        } catch (SQLException e) {
            System.out.println("Eroare la extragerea userilor: " + e.getMessage());
        }
        return utilizatori;
    }


}

    private void writeAudit(String operatie) {
        try (FileWriter fw = new FileWriter("audit.csv", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String linie = operatie + "," + java.time.LocalDateTime.now();
            bw.write(linie);
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Eroare la scrierea in audit.csv: " + e.getMessage());
        }
    }

    public Set<User> getAllUsers() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * FROM user";
        try (Connection con = DbConnection.getInstance();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User(
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("parola"),
                        Rol.valueOf(rs.getString("tip")),
                        true
                );
                users.add(u);
            }
            writeAudit("READ_USER");
        } catch (SQLException e) {
            System.out.println("Eroare la citirea utilizatorilor: " + e.getMessage());
        }
        return users;
    }

    public void updateUser(String email, String numeNou, String parolaNoua, Rol rolNou) {
        String sql = "UPDATE user SET nume = ?, parola = ?, tip = ? WHERE email = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeNou);
            ps.setString(2, parolaNoua);
            ps.setString(3, rolNou.toString());
            ps.setString(4, email);
            ps.executeUpdate();
            writeAudit("UPDATE_USER");
        } catch (SQLException e) {
            System.out.println("Eroare la actualizarea utilizatorului: " + e.getMessage());
        }
    }

    public void deleteUser(String email) {
        String sql = "DELETE FROM user WHERE email = ?";
        try (Connection con = DbConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
            writeAudit("DELETE_USER");
        } catch (SQLException e) {
            System.out.println("Eroare la stergerea utilizatorului: " + e.getMessage());
        }
    }

}