/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
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
import rs.etf.sab.operations.CourierOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_CourierOperation implements CourierOperations {

    @Override
    public boolean insertCourier(String userName, String vozackaDozvola) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int idK = -1;
        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idK = rs.getInt(1);
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
        }

        String query2 = "select IdKur\n"
                + "from Kurir \n"
                + "where IdKur = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query2)) {
            stmt.setInt(1, idK);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                
            }
        } catch (SQLException ex) {

        }

        String query = "insert into Kurir(IdKur, VozackaDozvola, BrIsporPaketa, Status, Profit) values(?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idK);
            ps.setString(2, vozackaDozvola);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
     
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int idK = -1;
        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idK = rs.getInt(1);
                }
            } catch (SQLException ex) {
             
            }
        } catch (SQLException ex) {
          
        }

        String query = "delete from Kurir where Kurir.IdKur = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idK);
            int ret = ps.executeUpdate();
            if (ret != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
           
        }

        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<String> kuriri = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query = "select Korisnik.KorIme \n"
                + "from Korisnik JOIN Kurir ON Korisnik.IdK = Kurir.IdKur \n"
                + "where Kurir.Status = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, status);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    kuriri.add(rs.getString(1));
                }
            } catch (SQLException ex) {
               
            }
        } catch (SQLException ex) {
           
        }
        return kuriri;
    }

    @Override
    public List<String> getAllCouriers() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<String> kuriri = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        String query = "select Korisnik.KorIme \n"
                + "from Korisnik JOIN Kurir ON Korisnik.IdK = Kurir.IdKur \n"
                + "ORDER BY Kurir.Profit DESC";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    kuriri.add(rs.getString(1));
                }
            } catch (SQLException ex) {
               
            }
        } catch (SQLException ex) {
         
        }
        return kuriri;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int BrIsporPaketa) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        if (BrIsporPaketa == -1) {
            String query = "select AVG(Profit)\n"
                    + "from Kurir\n";

            BigDecimal avgProfit;

            try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                try ( ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        avgProfit = rs.getBigDecimal(1);
                        return avgProfit;
                    }
                } catch (SQLException ex) {
              
                }
            } catch (SQLException ex) {
              
            }
        }

        String query = "select AVG(Profit)\n"
                + "from Kurir\n"
                + "where BrIsporPaketa=?";

        BigDecimal avgProfit;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, BrIsporPaketa);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    avgProfit = rs.getBigDecimal(1);
                    return avgProfit;
                }
            } catch (SQLException ex) {
               
            }
        } catch (SQLException ex) {
          
        }
        return null;
    }

}
