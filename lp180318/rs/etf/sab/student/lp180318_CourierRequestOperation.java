/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author PetarPC
 */
public class lp180318_CourierRequestOperation implements CourierRequestOperation {

    private lp180318_CourierOperation co;

    lp180318_CourierRequestOperation() {
        co = new lp180318_CourierOperation();
    }

    @Override
    public boolean insertCourierRequest(String userName, String vozackaDozvola) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();
        
        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int IdZ = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdZ = rs.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
        }

        String query2 = "select VozackaDozvola\n"
                + "from Zahtev \n"
                + "where VozackaDozvola = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query2)) {
            stmt.setString(1, vozackaDozvola);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
        }
        
        query2 = "select VozackaDozvola\n"
                + "from Kurir \n"
                + "where VozackaDozvola = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query2)) {
            stmt.setString(1, vozackaDozvola);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
        }
        
        String query3 = "select IdZ\n"
                + "from Kurir \n"
                + "where IdZ = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query3)) {
            stmt.setInt(1, IdZ);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
        }

        String query = "insert into Zahtev (IdZ, VozackaDozvola) values(?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdZ);
            ps.setString(2, vozackaDozvola);
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
    public boolean deleteCourierRequest(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        if (userName == null) {
            return false;
        }
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int IdZ = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdZ = rs.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {

        }

        String query = "delete from Zahtev where Zahtev.IdZ = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, IdZ);
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
    public boolean changeDriverLicenceNumberInCourierRequest(String userName, String vozackaDozvola) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int IdZ = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdZ = rs.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query2 = "select VozackaDozvola\n"
                + "from Kurir \n"
                + "where VozackaDozvola = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query2)) {
            stmt.setString(1, vozackaDozvola);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                
            }
        } catch (SQLException ex) {
            
        }

        String query = "update Zahtev set VozackaDozvola = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, vozackaDozvola);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    @Override
    public List<String> getAllCourierRequests() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<String> zahtevi = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();

        String query = "select Korisnik.KorIme \n"
                + "from Korisnik JOIN Zahtev ON Korisnik.IdK = Zahtev.IdZ";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    zahtevi.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return zahtevi;
    }

    @Override
    public boolean grantRequest(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (userName == null) {
            return false;
        }
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int IdZ = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdZ = rs.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                
            }
        } catch (SQLException ex) {
           
        }

        String query2 = "select VozackaDozvola\n"
                + "from Zahtev\n"
                + "where IdZ = ?";

        String vozackaDozvola;

        try ( PreparedStatement stmt = conn.prepareStatement(query2)) {
            stmt.setInt(1, IdZ);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vozackaDozvola = rs.getString("VozackaDozvola");
                    if(co.insertCourier(userName, vozackaDozvola))
                        return this.deleteCourierRequest(userName);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }
        return false;
    }

}
