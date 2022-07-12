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
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_VehicleOperation implements VehicleOperations {

    @Override
    public boolean insertVehicle(String RegBroj, int tipGoriva, BigDecimal potrosnja, BigDecimal nosivost) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (tipGoriva < 0 || tipGoriva > 2) {
            return false;
        }
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdV\n"
                + "from Vozilo\n"
                + "where RB = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, RegBroj);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "insert into Vozilo (RB, TipGoriva, Potrosnja, Nosivost) values(?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, RegBroj);
            ps.setInt(2, tipGoriva);
            ps.setBigDecimal(3, potrosnja);
            ps.setBigDecimal(4, nosivost);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
//                System.out.println("Kreirano novo vozilo " + rs.getInt(1));
                rs.getInt(1);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... strings) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        int brVozila = 0;
        String query = "delete from Vozilo where Vozilo.RB = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < strings.length; ++i) {
                ps.setString(1, strings[i]);
                int ret = ps.executeUpdate();
                if (ret != 0) {
                    brVozila++;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return brVozila;
    }

    @Override
    public List<String> getAllVehichles() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<String> vozila = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Vozilo")) {
            ResultSetMetaData rsmd = rs.getMetaData();
//            System.out.println();
            while (rs.next()) {
                //for(int i=1; i<=rsmd.getColumnCount(); i++){    
                //if(rsmd.getColumnType(i)==java.sql.Types.INTEGER)
                //System.out.print(String.format("%-18d", rs.getString("RB")));
                vozila.add(rs.getString("RB"));
                //}
                //System.out.println();
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_VehicleOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vozila;
    }

    @Override
    public boolean changeFuelType(String RegBroj, int tipGoriva) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        if (tipGoriva < 0 || tipGoriva > 2) {
            return false;
        }

        Connection conn = DB.getInstance().getConnection();

        String query1 = "select Parkirano.IdV\n"
                + "from Parkirano JOIN Vozilo on Vozilo.IdV = Parkirano.IdV\n"
                + "where Vozilo.RB = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, RegBroj);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_UserOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_UserOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "update Vozilo set TipGoriva = ?\n"
                + "where RB = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tipGoriva);
            ps.setString(2, RegBroj);
            int uspesno = ps.executeUpdate();
            if (uspesno > 0) {
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    @Override
    public boolean changeConsumption(String RegBroj, BigDecimal potrosnja) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        Connection conn = DB.getInstance().getConnection();

        String query1 = "select Parkirano.IdV\n"
                + "from Parkirano JOIN Vozilo on Vozilo.IdV = Parkirano.IdV\n"
                + "where Vozilo.RB = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, RegBroj);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_UserOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_UserOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "update Vozilo set Potrosnja = ?\n"
                + "where RB = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, potrosnja);
            ps.setString(2, RegBroj);
            int uspesno = ps.executeUpdate();
            if (uspesno > 0) {
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public boolean changeCapacity(String RegBroj, BigDecimal nosivost) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        Connection conn = DB.getInstance().getConnection();

        String query1 = "select Parkirano.IdV\n"
                + "from Parkirano JOIN Vozilo on Vozilo.IdV = Parkirano.IdV\n"
                + "where Vozilo.RB = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, RegBroj);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_UserOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_UserOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "update Vozilo set Nosivost = ?\n"
                + "where RB = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, nosivost);
            ps.setString(2, RegBroj);
            int uspesno = ps.executeUpdate();
            if (uspesno > 0) {
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public boolean parkVehicle(String RegBroj, int IdM) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();
        
        String query1 = "select IdV\n"
                + "from Vozilo\n"
                + "where RB = ?";

        int IdV = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, RegBroj);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdV = rs.getInt(1);
                }else
                    return false;
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query2 = "select IdV\n"
                + "from TrenutnoVozi\n"
                + "where IdV = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdV);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        

        String query = "insert into Parkirano (IdM, IdV) values(?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, IdM);
            ps.setInt(2, IdV);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                //System.out.println("Vozilo " + IdV + " smestno u magacin " + IdM);
                //rs.getInt(1);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

}
