/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.StockroomOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_StockroomOperation implements StockroomOperations {

    @Override
    public int insertStockroom(int idA) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdA\n"
                + "from Adresa\n"
                + "where IdA = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, idA);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query2 = "select IdG\n"
                + "from Adresa\n"
                + "where IdA = ?";

        int idG = -1;
        try ( PreparedStatement stmt = conn.prepareStatement(query2)) {
            stmt.setInt(1, idA);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idG = rs.getInt(1);
                } else {
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_StockroomOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_StockroomOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query3 = "select Grad.IdG\n"
                + "from Grad JOIN Adresa ON Grad.IdG = Adresa.IdG\n"
                + "JOIN Magacin on Adresa.IdA=Magacin.IdA\n"
                + "where Adresa.IdG = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query3)) {
            stmt.setInt(1, idG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
        }

        String query = "insert into Magacin (IdA) values(?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idA);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
        }
        return -1;
    }

    @Override
    public boolean deleteStockroom(int IdM) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdI\n"
                + "from Isporuka JOIN Adresa ON Isporuka.AdresaTrenutna = Adresa.IdA\n"
                + "JOIN Magacin on Adresa.IdA = Magacin.IdA"
                + "where IdM = ? and (Isporuka.Status != 0 and Isporuka.Status != 4)";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdM);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query = "delete from Magacin where IdM = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdM);
            int uspesno = ps.executeUpdate();
            if (uspesno > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_AddressOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public int deleteStockroomFromCity(int IdG) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdM"
                + " from Magacin join Adresa on Magacin.IdA = Adresa.IdA "
                + " where Adresa.IdG = ?";

        int IdM = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdM = rs.getInt("IdM");
                } else {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query1 = "select IdI\n"
                + "from Isporuka JOIN Adresa ON Isporuka.AdresaTrenutna = Adresa.IdA\n"
                + "JOIN Magacin on Adresa.IdA = Magacin.IdA"
                + "where IdM = ? and (Isporuka.Status != 0 and Isporuka.Status != 4)";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdM);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query = "delete from Magacin where IdM = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdM);
            int uspesno = ps.executeUpdate();
            if (uspesno > 0) {
                return IdM;
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_AddressOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    @Override
    public List<Integer> getAllStockrooms() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<Integer> magacini = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Magacin")) {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                magacini.add(rs.getInt("IdM"));
            }
        } catch (SQLException ex) {
        }
        return magacini;
    }

}
