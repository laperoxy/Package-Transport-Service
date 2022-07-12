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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author PetarPC
 */
public class lp180318_UserOperation implements UserOperations {

    private static final String PASSWORD_PATTERN
            = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[_!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private boolean checkPassword(String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password, int IdA) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        if (!Character.isUpperCase(firstName.charAt(0))) {
            return false;
        }

        if (!Character.isUpperCase(lastName.charAt(0))) {
            return false;
        }
        if (!checkPassword(password)) {
            return false;
        }

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query = "insert into Korisnik (KorIme, Ime, Prezime, Sifra, IdA) values(?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, userName);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, password);
            ps.setInt(5, IdA);
            int uspesno = ps.executeUpdate();
            if (uspesno > 0) {
                return true;
            }
        } catch (SQLException ex) {

        }
        return false;

    }

    @Override
    public boolean declareAdmin(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        if (userName == null) {
            return false;
        }

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int idK = -1;
        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idK = rs.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query1 = "select IdA\n"
                + "from Administrator\n"
                + "where IdA = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        String query = "insert into Administrator(IdA) values(?)";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idK);
            int ret = ps.executeUpdate();
            if (ret > 0) {
                return true;
            }
        } catch (SQLException ex) {

        }
        return false;
    }

    @Override
    public int getSentPackages(String... userNames) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBodyConnection conn = DB.getInstance().getConnection();
        Connection conn = DB.getInstance().getConnection();

        String query1 = "select IdK\n"
                + "from Korisnik\n"
                + "where KorIme = ?";

        int brKor = 0;

        try ( PreparedStatement stmt = conn.prepareStatement(query1)) {
            for (int i = 0; i < userNames.length; ++i) {
                stmt.setString(1, userNames[i]);
            }
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    brKor++;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (brKor == 0) {
            return -1;
        }
        
        int brPaketa=0;

        String query = "select IdI from Isporuka JOIN Korisnik on Isporuka.IdK = Korisnik.IdK"
                + " where Korisnik.KorIme = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < userNames.length; ++i) {
                stmt.setString(1, userNames[i]);
            }
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    brPaketa++;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return brPaketa;
    }

    @Override
    public int deleteUsers(String... userNames) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Connection conn = DB.getInstance().getConnection();

        int brKorisnika = 0;
        String query = "delete from Korisnik where Korisnik.KorIme = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < userNames.length; ++i) {
                ps.setString(1, userNames[i]);
                brKorisnika += ps.executeUpdate();
            }

        } catch (SQLException ex) {

        }

        return brKorisnika;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> korisnici = new ArrayList<String>();
        Connection conn = DB.getInstance().getConnection();
        try (
                 Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("select * from Korisnik")) {
            while (rs.next()) {
                korisnici.add(rs.getString("KorIme"));
            }
        } catch (SQLException ex) {

        }
        return korisnici;
    }

}
