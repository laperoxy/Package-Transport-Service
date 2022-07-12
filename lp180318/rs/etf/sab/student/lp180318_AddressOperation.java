/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.AddressOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_AddressOperation implements AddressOperations {

    @Override
    public int insertAddress(String ulica, int broj, int idG, int xCord, int yCord) {

        if (ulica == null) {
            return -1;
        }
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdG\n"
                + "from Grad\n"
                + "where  IdG = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, idG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String query = "insert into Adresa (Ulica, Broj, IdG, X, Y) values(?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ulica);
            ps.setInt(2, broj);
            ps.setInt(3, idG);
            ps.setInt(4, xCord);
            ps.setInt(5, yCord);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public int deleteAddresses(String ulica, int broj) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from Adresa where Ulica = ? and Broj = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, ulica);
            ps.setInt(2, broj);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean deleteAdress(int IdA) {
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from Adresa where IdA = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdA);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public int deleteAllAddressesFromCity(int IdG) {
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdG\n"
                + "from Grad\n"
                + "where  IdG = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String query = "delete from Adresa where IdG = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdG);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_AddressOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public List<Integer> getAllAddresses() {
        List<Integer> adrese = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Adresa")) {
            while (rs.next()) {
                adrese.add(rs.getInt("IdA"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return adrese;
    }

    @Override
    public List<Integer> getAllAddressesFromCity(int IdG) {
        List<Integer> adrese = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdG\n"
                + "from Grad\n"
                + "where  IdG = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String query = "select IdA from Adresa where IdG = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    adrese.add(rs.getInt("IdA"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return adrese;
    }

}
