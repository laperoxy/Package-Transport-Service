/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_CityOperation implements CityOperations {

    @Override
    public int insertCity(String Naziv, String PostBr) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdG\n"
                + "from Grad\n"
                + "where  PostBr = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, PostBr);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "insert into Grad (Naziv, PostBr) values(?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, Naziv);
            ps.setString(2, PostBr);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
//                System.out.println("Kreiran novi grad " + rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_CityOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int deleteCity(String... strings) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        int brGradova = 0;
        String query = "delete from Grad where Grad.Naziv = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < strings.length; ++i) {
                ps.setString(1, strings[i]);
                int ret = ps.executeUpdate();
                if (ret != 0) {
                    brGradova += ret;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(lp180318_CityOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return brGradova;
    }

    @Override
    public boolean deleteCity(int i) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from Grad where Grad.IdG = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {

        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> gradovi = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Grad")) {
            while (rs.next()) {
                gradovi.add(rs.getInt("IdG"));
            }
        } catch (SQLException ex) {

        }
        return gradovi;
    }

}
