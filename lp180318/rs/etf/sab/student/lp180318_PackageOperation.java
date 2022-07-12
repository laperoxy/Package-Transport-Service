/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_PackageOperation implements PackageOperations {

    @Override
    public int insertPackage(int IdAOd, int IdADo, String userName, int tipPaketa, BigDecimal tezina) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (IdAOd == IdADo) {
            return -1;
        }

        if (tipPaketa < 0 || tipPaketa > 3) {
            return -1;
        }
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int IdK = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdK = rs.getInt(1);
                } else {
                    return -1;
                }
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_PackageOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_PackageOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "insert into Isporuka(AdresaOd, AdresaDo, StatusIsporuke, VremeKreiranja, VremePrihvatanja, AdresaTrenutna, IdK, Tezina, Tip, Cena) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, IdAOd);
            ps.setInt(2, IdADo);
            ps.setInt(3, 0);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            ps.setString(4, dtf.format(now));
            ps.setDate(5, null);
            ps.setInt(6, IdAOd);
            ps.setInt(7, IdK);
            ps.setBigDecimal(8, tezina);
            ps.setInt(9, tipPaketa);
            ps.setBigDecimal(10, BigDecimal.valueOf(0));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                //System.out.println("Kreiran novi paket sa id = " + rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;

    }

    @Override
    public boolean acceptAnOffer(int IdPon) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdI\n"
                + "from Isporuka\n"
                + "where IdI = ? and StatusIsporuke = 0";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdPon);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query1 = "select CenaIsporuke\n"
                + "from Ponuda\n"
                + "where IdPon = ?";

        double cenaIsporuke = 0;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdPon);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cenaIsporuke = rs.getBigDecimal("CenaIsporuke").doubleValue();
                } else {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query = "update Ponuda set Status = 1"
                + "where IdPon = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdPon);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "update Isporuka set StatusIsporuke = ?, VremePrihvatanja = ?, Cena = ?"
                + " where IdI = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, 1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            ps.setString(2, dtf.format(now));
            ps.setBigDecimal(3, BigDecimal.valueOf(cenaIsporuke));
            ps.setInt(4, IdPon);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(lp180318_AddressOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public boolean rejectAnOffer(int IdPon) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdI\n"
                + "from Isporuka\n"
                + "where IdI = ? and StatusIsporuke = 0";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setInt(1, IdPon);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query = "update Ponuda set Status = -1"
                + "where IdPon = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdPon);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "update Isporuka set StatusIsporuke = 4"
                + "where IdI = ?";

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdPon);
            int uspesno = ps.executeUpdate();
            if (uspesno != 0) {
                return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public List<Integer> getAllPackages() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<Integer> paketi = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Isporuka")) {
            while (rs.next()) {
                paketi.add(rs.getInt("IdI"));
            }
        } catch (SQLException ex) {

        }
        return paketi;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int tipPaketa) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<Integer> paketi = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();

        String query = "select IdI from Isporuka where Tip = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tipPaketa);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paketi.add(rs.getInt("IdI"));
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paketi;
    }

    @Override
    public List<Integer> getAllUndeliveredPackages() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<Integer> paketi = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Isporuka where StatusIsporuke = 1")) {
            while (rs.next()) {
                paketi.add(rs.getInt("IdI"));
            }
        } catch (SQLException ex) {

        }
        return paketi;
    }

    @Override
    public List<Integer> getAllUndeliveredPackagesFromCity(int IdG) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        List<Integer> paketi = new ArrayList<Integer>();
        Connection conn = DB.getInstance().getConnection();

        String query = "select IdI \n"
                + "from Isporuka JOIN Adresa on Isporuka.AdresaOd = Adresa.IdA\n"
                + "where Adresa.IdG = ? where StatusIsporuke = 1";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paketi.add(rs.getInt("IdI"));
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paketi;
    }

    @Override
    public List<Integer> getAllPackagesCurrentlyAtCity(int IdG) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        Connection conn = DB.getInstance().getConnection();

        int IdAMag = -1;

        String query = "select Magacin.IdA\n"
                + "from Magacin JOIN Adresa on Magacin.IdA = Adresa.IdA\n"
                + "where Adresa.IdG = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdAMag = rs.getInt(1);
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Integer> paketi = new ArrayList<Integer>();

        query = "select I.IdI "
                + "from Isporuka I JOIN Adresa A on I.AdresaTrenutna = A.IdA "
                + "JOIN Grad G on A.IdG = G.IdG "
                + "where G.IdG = ? and I.IdI not in (select IsporucujeSe.IdI from IsporucujeSe where I.IdI = IsporucujeSe.IdI "
                + "and (IsporucujeSe.StatusPaketa = 2 or IsporucujeSe.StatusPaketa = 3))";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paketi.add(rs.getInt("IdI"));
                }
                //if(paketi.size()>0)
                   // return paketi;
            } catch (SQLException ex) {
                Logger.getLogger(lp180318_PackageOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(lp180318_PackageOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paketi;

    }

    @Override
    public boolean deletePackage(int IdI) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query = "delete from Ponuda where IdPon = ? and (Status = 0 or Status = -1)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdI);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(lp180318_AddressOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "delete from Isporuka where IdI = ? and (StatusIsporuke = 0 or StatusIsporuke = 4) ";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdI);
            int uspesno = ps.executeUpdate();
            if (uspesno == 0) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(lp180318_AddressOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int IdI, BigDecimal tezina) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query = "update Isporuka set Tezina = ?\n"
                + "where IdI = ? and StatusIsporuke = 0";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, tezina);
            ps.setInt(2, IdI);
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
    public boolean changeType(int IdI, int tipPaketa) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query = "update Isporuka set Tip = ?\n"
                + "where IdI = ? and StatusIsporuke = 0";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tipPaketa);
            ps.setInt(2, IdI);
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
    public int getDeliveryStatus(int IdI) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query = "select * from Isporuka where IdI = ?";
        int ret = -1;
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdI);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ret = rs.getInt("StatusIsporuke");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int IdPon) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query = "select * from Ponuda where IdPon = ?";
        double ret = 0;
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdPon);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ret = rs.getBigDecimal("CenaIsporuke").doubleValue();
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.valueOf(ret);
    }

    @Override
    public int getCurrentLocationOfPackage(int IdI) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        String query = "select IdI from IsporucujeSe where IdI = ? and (IsporucujeSe.StatusPaketa = 2 or IsporucujeSe.StatusPaketa = 3)";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdI);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return -1;
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        query = "select Adresa.IdG \n"
                + "from Adresa join Isporuka on Adresa.IdA = Isporuka.AdresaTrenutna \n"
                + "where Isporuka.IdI = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdI);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("IdG");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;

    }

    @Override
    public Date getAcceptanceTime(int IdI) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        Connection conn = DB.getInstance().getConnection();

        String query = "select * from Isporuka where IdI = ?";
        Date ret = null;
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdI);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ret = rs.getDate("VremePrihvatanja");
                }
            } catch (SQLException ex) {
                //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(lp180318_AddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

}
