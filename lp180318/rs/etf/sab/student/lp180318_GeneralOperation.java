/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_GeneralOperation implements GeneralOperations {

    @Override
    public void eraseAll() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();
        String query = "delete from IsporucujeSe;"
                + "delete from Zahtev;"
                + "delete from Ponuda;"
                + "delete from TrenutnoVozi;"
                + "delete from Vozena;"
                + "delete from Parkirano;"
                + "delete from Magacin;"
                + "delete from Vozilo;"
                + "delete from Isporuka;"
                + "delete from Korisnik;"
                + "delete from Adresa;"
                + "delete from Grad;";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_CityOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
