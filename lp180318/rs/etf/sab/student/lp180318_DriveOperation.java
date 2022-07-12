/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.DriveOperation;

class Isporuka {

    private int IdI;
    private double tezina;
    private int IdADo;
    private int IdATr;

    public double euklid;

    public int getIdI() {
        return IdI;
    }

    public void setIdI(int IdI) {
        this.IdI = IdI;
    }

    public double getTezina() {
        return tezina;
    }

    public void setTezina(double tezina) {
        this.tezina = tezina;
    }

    public int getIdADo() {
        return IdADo;
    }

    public void setIdADo(int IdADo) {
        this.IdADo = IdADo;
    }

    public int getIdATr() {
        return IdATr;
    }

    public void setIdATr(int IdATr) {
        this.IdATr = IdATr;
    }

    public void sracunajEuklid() {
        int Xtr = 0;
        int Ytr = 0;

        int Xdo = 0;
        int Ydo = 0;

        Connection conn = DB.getInstance().getConnection();
        String query = "select X, Y \n"
                + "from Adresa \n"
                + "where IdA = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdATr);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Xtr = rs.getInt(1);
                    Ytr = rs.getInt(2);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdADo);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Xdo = rs.getInt(1);
                    Ydo = rs.getInt(2);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        euklid = Math.sqrt(Math.pow(Xdo - Xtr, 2) + Math.pow(Ydo - Ytr, 2));

    }

}

/**
 *
 * @author PetarPC
 */
public class lp180318_DriveOperation implements DriveOperation {

    public double euklidZaVozaca(int IdA, int IdB) {
        int Xtr = 0;
        int Ytr = 0;

        int Xdo = 0;
        int Ydo = 0;

        Connection conn = DB.getInstance().getConnection();
        String query = "select X, Y \n"
                + "from Adresa \n"
                + "where IdA = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdA);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Xtr = rs.getInt(1);
                    Ytr = rs.getInt(2);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdB);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Xdo = rs.getInt(1);
                    Ydo = rs.getInt(2);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        return Math.sqrt(Math.pow(Xdo - Xtr, 2) + Math.pow(Ydo - Ytr, 2));

    }

    public static int compare(double a, double b) {
        return a < b ? -1
                : a > b ? 1
                        : 0;
    }

    @Override
    public boolean planingDrive(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        System.out.println("PlaningDrive");

        Connection conn = DB.getInstance().getConnection();
        String query = "select IdKur \n"
                + "from Kurir JOIN Korisnik on Kurir.IdKur = Korisnik.IdK"
                + " where KorIme = ?";

        int IdKur = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdKur = rs.getInt("IdKur");
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "select IdG \n"
                + "from Adresa JOIN Korisnik on Adresa.IdA = Korisnik.IdA "
                + " where IdK = ?";

        int IdG = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdG = rs.getInt("IdG");
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "select Vozilo.IdV, Vozilo.Nosivost, Magacin.IdA \n"
                + "from Vozilo JOIN Parkirano on Vozilo.IdV = Parkirano.IdV \n"
                + "JOIN Magacin on Parkirano.IdM = Magacin.IdM \n"
                + "JOIN Adresa on Magacin.IdA = Adresa.IdA \n"
                + "where Adresa.IdG = ?";

        int IdV = -1;
        double nosivost = 0;
        int IdAMagacina = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdV = rs.getInt(1);
                    nosivost = rs.getBigDecimal(2).doubleValue();
                    IdAMagacina = rs.getInt(3);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        List<Isporuka> paketiZaPreuzimanje = new ArrayList<Isporuka>();

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN Adresa on Isporuka.AdresaOd = Adresa.IdA \n"
                + "where Adresa.IdG = ? and Adresa.IdA != ? and StatusIsporuke = 1 and Isporuka.IdI not in (select IdI from IsporucujeSe)\n"
                + "ORDER BY Isporuka.VremePrihvatanja";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            stmt.setInt(2, IdAMagacina);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Isporuka nova = new Isporuka();
                    nova.setIdI(rs.getInt(1));
                    nova.setIdADo(rs.getInt(2));
                    nova.setIdATr(rs.getInt(3));
                    nova.setTezina(rs.getBigDecimal(4).doubleValue());
                    paketiZaPreuzimanje.add(nova);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        List<Isporuka> paketiIzMagacina = new ArrayList<Isporuka>();

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN Adresa on Isporuka.AdresaTrenutna = Adresa.IdA \n"
                + "where Adresa.IdA = ? and Isporuka.StatusIsporuke = 2 and Isporuka.IdI not in (select IdI from IsporucujeSe)\n"
                + "ORDER BY Isporuka.VremePrihvatanja";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdAMagacina);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Isporuka nova = new Isporuka();
                    nova.setIdI(rs.getInt(1));
                    nova.setIdADo(rs.getInt(2));
                    nova.setIdATr(rs.getInt(3));
                    nova.setTezina(rs.getBigDecimal(4).doubleValue());
                    paketiIzMagacina.add(nova);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        List<Isporuka> paketiZaPrevozenjeGrad = new ArrayList<Isporuka>();
        List<Isporuka> paketiZaPrevozenjeMagacin = new ArrayList<Isporuka>();
        double trTezina = 0;

        for (int i = 0; i < paketiZaPreuzimanje.size(); ++i) {
            if (trTezina + paketiZaPreuzimanje.get(i).getTezina() < nosivost) {
                trTezina += paketiZaPreuzimanje.get(i).getTezina();
                paketiZaPrevozenjeGrad.add(paketiZaPreuzimanje.get(i));
            }
        }

        for (int i = 0; i < paketiIzMagacina.size(); ++i) {
            if (trTezina + paketiIzMagacina.get(i).getTezina() < nosivost) {
                trTezina += paketiIzMagacina.get(i).getTezina();
                paketiZaPrevozenjeMagacin.add(paketiIzMagacina.get(i));
            }
        }

        if (paketiZaPrevozenjeGrad.size() == 0 && paketiZaPrevozenjeMagacin.size() == 0) {
            return false;
        }

        query = "delete from Parkirano where IdV = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdV);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "insert into TrenutnoVozi (IdV, IdKur, TrAdr, PredjeniPut, Kilaza) values(?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdV);
            ps.setInt(2, IdKur);
            ps.setInt(3, IdAMagacina);
            ps.setBigDecimal(4, BigDecimal.valueOf(0));
            ps.setBigDecimal(5, BigDecimal.valueOf(0));
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "update Kurir set Status = 1 where IdKur = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, IdKur);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "insert into IsporucujeSe(IdI, IdV, IdKur, StatusPaketa) values(?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < paketiZaPrevozenjeGrad.size(); ++i) {
                ps.setInt(1, paketiZaPrevozenjeGrad.get(i).getIdI());
                ps.setInt(2, IdV);
                ps.setInt(3, IdKur);
                ps.setInt(4, 0);
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "insert into IsporucujeSe(IdI, IdV, IdKur, StatusPaketa) values(?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < paketiIzMagacina.size(); ++i) {
                ps.setInt(1, paketiIzMagacina.get(i).getIdI());
                ps.setInt(2, IdV);
                ps.setInt(3, IdKur);
                ps.setInt(4, 1);
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }

    @Override
    public int nextStop(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        System.out.println("nextStop");

        Connection conn = DB.getInstance().getConnection();
        String query = "select IdKur, BrIsporPaketa \n"
                + "from Kurir JOIN Korisnik on Kurir.IdKur = Korisnik.IdK"
                + " where KorIme = ?";

        int IdKur = -1;
        int BrPaketa = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdKur = rs.getInt("IdKur");
                    BrPaketa = rs.getInt("BrIsporPaketa");
                } else {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select IdV, TrAdr, PredjeniPut, Kilaza \n"
                + "from TrenutnoVozi \n"
                + "where IdKur = ?";

        int IdV = -1;
        int trAdr = -1;
        double put = 0;
        double kilaza = 0;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdV = rs.getInt("IdV");
                    trAdr = rs.getInt("TrAdr");
                    put = rs.getBigDecimal("PredjeniPut").doubleValue();
                    kilaza = rs.getBigDecimal("Kilaza").doubleValue();
                } else {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        int IdGTr = -1;

        query = "select IdG \n"
                + "from Adresa \n"
                + "where IdA = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, trAdr);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdGTr = rs.getInt("IdG");
                } else {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select IdG \n"
                + "from Adresa JOIN Korisnik on Adresa.IdA = Korisnik.IdA "
                + " where IdK = ?";

        int IdG = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdG = rs.getInt("IdG");
                } else {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select Magacin.IdA, Magacin.IdM \n"
                + "from Magacin JOIN Adresa on Magacin.IdA = Adresa.IdA \n"
                + "where Adresa.IdG = ?";

        int IdAMagacina = -1;
        int IdM = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdG);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdAMagacina = rs.getInt(1);
                    IdM = rs.getInt(2);
                } else {
                    return -1;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        List<Isporuka> paketiZaPrevozenje = new ArrayList<Isporuka>();

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 2";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Isporuka nova = new Isporuka();
                    nova.setIdI(rs.getInt(1));
                    nova.setIdADo(rs.getInt(2));
                    nova.setIdATr(rs.getInt(3));
                    nova.setTezina(rs.getBigDecimal(4).doubleValue());
                    paketiZaPrevozenje.add(nova);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        List<Isporuka> paketiZaMagacin = new ArrayList<Isporuka>();

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 3";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Isporuka nova = new Isporuka();
                    nova.setIdI(rs.getInt(1));
                    nova.setIdADo(rs.getInt(2));
                    nova.setIdATr(rs.getInt(3));
                    nova.setTezina(rs.getBigDecimal(4).doubleValue());
                    paketiZaMagacin.add(nova);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 0 \n"
                + "ORDER BY Isporuka.VremePrihvatanja";

        Isporuka nova = new Isporuka();
        boolean idaljeSkuplja = false;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idaljeSkuplja = true;
                    nova.setIdI(rs.getInt(1));
                    nova.setIdADo(rs.getInt(2));
                    nova.setIdATr(rs.getInt(3));
                    nova.setTezina(rs.getBigDecimal(4).doubleValue());
                } else {
                    nova = null;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 1 \n"
                + "ORDER BY Isporuka.VremePrihvatanja";

        List<Isporuka> novaMagacin = new ArrayList<Isporuka>();

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Isporuka novaMag = new Isporuka();
                    idaljeSkuplja = true;
                    novaMag.setIdI(rs.getInt(1));
                    novaMag.setIdADo(rs.getInt(2));
                    novaMag.setIdATr(rs.getInt(3));
                    novaMag.setTezina(rs.getBigDecimal(4).doubleValue());
                    novaMagacin.add(novaMag);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 5 \n"
                + "ORDER BY Isporuka.VremePrihvatanja";

        Isporuka zaMagacin = new Isporuka();
        boolean imaPaketaZaMagacin = false;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    imaPaketaZaMagacin = true;
                    zaMagacin.setIdI(rs.getInt(1));
                    zaMagacin.setIdADo(rs.getInt(2));
                    zaMagacin.setIdATr(rs.getInt(3));
                    zaMagacin.setTezina(rs.getBigDecimal(4).doubleValue());
                } else {
                    zaMagacin = null;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 6 \n"
                + "ORDER BY Isporuka.VremePrihvatanja";

        List<Isporuka> novaMagacinIzMag = new ArrayList<Isporuka>();

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Isporuka zaMag = new Isporuka();
                    imaPaketaZaMagacin = true;
                    zaMag.setIdI(rs.getInt(1));
                    zaMag.setIdADo(rs.getInt(2));
                    zaMag.setIdATr(rs.getInt(3));
                    zaMag.setTezina(rs.getBigDecimal(4).doubleValue());
                    novaMagacinIzMag.add(zaMag);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        if (imaPaketaZaMagacin) {

            if (zaMagacin == null && novaMagacinIzMag.size() > 0) {
                int IdAMagN = novaMagacinIzMag.get(0).getIdATr();
                double euklid = euklidZaVozaca(trAdr, IdAMagN);

                put += euklid;

                for (int i = 0; i < novaMagacinIzMag.size(); ++i) {
                    kilaza += novaMagacinIzMag.get(i).getTezina();
                }

                

                query = "update TrenutnoVozi set PredjeniPut = ?, TrAdr = ?, Kilaza = ? where IdKur = ? and IdV = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setBigDecimal(1, BigDecimal.valueOf(put));
                    ps.setInt(2, IdAMagN);
                    ps.setBigDecimal(3, BigDecimal.valueOf(kilaza));
                    ps.setInt(4, IdKur);
                    ps.setInt(5, IdV);
                    ps.executeUpdate();

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set StatusIsporuke = 2 where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < novaMagacinIzMag.size(); ++i) {
                        ps.setInt(1, novaMagacinIzMag.get(i).getIdI());
                        ps.executeUpdate();
                    }

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update IsporucujeSe set StatusPaketa = 3 where IdI = ? and IdKur = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < novaMagacinIzMag.size(); ++i) {
                        ps.setInt(1, novaMagacinIzMag.get(i).getIdI());
                        ps.setInt(2, IdKur);
                        ps.executeUpdate();
                    }

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
                        ps.setInt(1, IdAMagN);
                        ps.setInt(2, paketiZaPrevozenje.get(i).getIdI());
                        ps.executeUpdate();
                    }
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < paketiZaMagacin.size(); ++i) {
                        ps.setInt(1, IdAMagN);
                        ps.setInt(2, paketiZaMagacin.get(i).getIdI());
                        ps.executeUpdate();
                    }
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }
                return -2;
            }

            double euklid = euklidZaVozaca(trAdr, zaMagacin.getIdATr());

            put += euklid;

            kilaza += zaMagacin.getTezina();

            query = "update TrenutnoVozi set PredjeniPut = ?, TrAdr = ?, Kilaza = ? where IdKur = ? and IdV = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setBigDecimal(1, BigDecimal.valueOf(put));
                ps.setInt(2, zaMagacin.getIdATr());
                ps.setBigDecimal(3, BigDecimal.valueOf(kilaza));
                ps.setInt(4, IdKur);
                ps.setInt(5, IdV);
                ps.executeUpdate();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Isporuka set StatusIsporuke = 2 where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, zaMagacin.getIdI());
                ps.executeUpdate();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update IsporucujeSe set StatusPaketa = 3 where IdI = ? and IdKur = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, zaMagacin.getIdI());
                ps.setInt(2, IdKur);
                ps.executeUpdate();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
                    ps.setInt(1, zaMagacin.getIdATr());
                    ps.setInt(2, paketiZaPrevozenje.get(i).getIdI());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < paketiZaMagacin.size(); ++i) {
                    ps.setInt(1, zaMagacin.getIdATr());
                    ps.setInt(2, paketiZaMagacin.get(i).getIdI());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }
            return -2;
        }

        if (idaljeSkuplja) {

            if (nova == null && novaMagacin.size() > 0) {
                double euklid = euklidZaVozaca(trAdr, IdAMagacina);

                put += euklid;

                for (int i = 0; i < novaMagacin.size(); ++i) {
                    kilaza += novaMagacin.get(i).getTezina();
                }

                query = "update TrenutnoVozi set PredjeniPut = ?, TrAdr = ?, Kilaza = ? where IdKur = ? and IdV = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setBigDecimal(1, BigDecimal.valueOf(put));
                    ps.setInt(2, IdAMagacina);
                    ps.setBigDecimal(3, BigDecimal.valueOf(kilaza));
                    ps.setInt(4, IdKur);
                    ps.setInt(5, IdV);
                    ps.executeUpdate();

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set StatusIsporuke = 2 where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < novaMagacin.size(); ++i) {
                        ps.setInt(1, novaMagacin.get(i).getIdI());
                        ps.executeUpdate();
                    }

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update IsporucujeSe set StatusPaketa = 2 where IdI = ? and IdKur = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < novaMagacin.size(); ++i) {
                        ps.setInt(1, novaMagacin.get(i).getIdI());
                        ps.setInt(2, IdKur);
                        ps.executeUpdate();
                    }

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
                        ps.setInt(1, IdAMagacina);
                        ps.setInt(2, paketiZaPrevozenje.get(i).getIdI());
                        ps.executeUpdate();
                    }
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                return -2;
            }

            double euklid = euklidZaVozaca(trAdr, nova.getIdATr());

            put += euklid;

            kilaza += nova.getTezina();

            query = "update TrenutnoVozi set PredjeniPut = ?, TrAdr = ?, Kilaza = ? where IdKur = ? and IdV = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setBigDecimal(1, BigDecimal.valueOf(put));
                ps.setInt(2, nova.getIdATr());
                ps.setBigDecimal(3, BigDecimal.valueOf(kilaza));
                ps.setInt(4, IdKur);
                ps.setInt(5, IdV);
                ps.executeUpdate();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Isporuka set StatusIsporuke = 2 where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, nova.getIdI());
                ps.executeUpdate();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update IsporucujeSe set StatusPaketa = 2 where IdI = ? and IdKur = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, nova.getIdI());
                ps.setInt(2, IdKur);
                ps.executeUpdate();

            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
                    ps.setInt(1, nova.getIdATr());
                    ps.setInt(2, paketiZaPrevozenje.get(i).getIdI());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < paketiZaMagacin.size(); ++i) {
                    ps.setInt(1, nova.getIdATr());
                    ps.setInt(2, paketiZaMagacin.get(i).getIdI());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            return -2;

        } else {

            if (paketiZaPrevozenje.size() > 0) {
                for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
                    paketiZaPrevozenje.get(i).sracunajEuklid();
                }

                paketiZaPrevozenje.sort((e1, e2)
                        -> compare(e1.euklid, e2.euklid)
                );

//                for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
//                    System.out.println(paketiZaPrevozenje.get(i).euklid);
//                }
                Isporuka sledeca = paketiZaPrevozenje.get(0);

                kilaza -= sledeca.getTezina();

                int IdGSled = -1;

                query = "select IdG \n"
                        + "from Adresa \n"
                        + "where IdA = ?";

                try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, sledeca.getIdADo());
                    try ( ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            IdGSled = rs.getInt("IdG");
                        } else {
                            return -1;
                        }
                    } catch (SQLException ex) {

                    }
                } catch (SQLException ex) {

                }

                double euklid = euklidZaVozaca(trAdr, sledeca.getIdADo());

                put += euklid;

                query = "update TrenutnoVozi set PredjeniPut = ?, TrAdr = ?, Kilaza = ? where IdKur = ? and IdV = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setBigDecimal(1, BigDecimal.valueOf(put));
                    ps.setInt(2, sledeca.getIdADo());
                    ps.setBigDecimal(3, BigDecimal.valueOf(kilaza));
                    ps.setInt(4, IdKur);
                    ps.setInt(5, IdV);
                    ps.executeUpdate();

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update IsporucujeSe set StatusPaketa = 4 where IdI = ? and IdKur = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, sledeca.getIdI());
                    ps.setInt(2, IdKur);
                    ps.executeUpdate();

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set StatusIsporuke = 3 where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, sledeca.getIdI());
                    ps.executeUpdate();

                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < paketiZaPrevozenje.size(); ++i) {
                        ps.setInt(1, sledeca.getIdADo());
                        ps.setInt(2, paketiZaPrevozenje.get(i).getIdI());
                        ps.executeUpdate();
                    }
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    for (int i = 0; i < paketiZaMagacin.size(); ++i) {
                        ps.setInt(1, sledeca.getIdADo());
                        ps.setInt(2, paketiZaMagacin.get(i).getIdI());
                        ps.executeUpdate();
                    }
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                query = "update Kurir set BrIsporPaketa = ? where IdKur = ?";
                try ( PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, BrPaketa + 1);
                    ps.setInt(2, IdKur);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                }

                ///////PAKETI IZ DRUGOG GRADA
                if (IdGTr != IdGSled) {

                    double nosivost = 0;

                    query = "select Nosivost \n"
                            + "from Vozilo \n"
                            + "where IdV = ?";

                    try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, IdV);
                        try ( ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                nosivost = rs.getBigDecimal(1).doubleValue();
                            }
                        } catch (SQLException ex) {
                        }
                    } catch (SQLException ex) {
                    }

                    int IdAMagacinaSled = -1;

                    query = "select Magacin.IdA \n"
                            + "from Magacin JOIN Adresa on Magacin.IdA = Adresa.IdA \n"
                            + "where Adresa.IdG = ?";

                    try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, IdGSled);
                        try ( ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                IdAMagacinaSled = rs.getInt(1);
                            }
                        } catch (SQLException ex) {
                        }
                    } catch (SQLException ex) {
                    }

                    List<Isporuka> paketiZaPreuzimanje = new ArrayList<Isporuka>();

                    query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                            + "from Isporuka JOIN Adresa on Isporuka.AdresaOd = Adresa.IdA \n"
                            + "where Adresa.IdG = ? and Adresa.IdA != ? and StatusIsporuke = 1 and Isporuka.IdI not in (select IdI from IsporucujeSe)\n"
                            + "ORDER BY Isporuka.VremePrihvatanja";

                    try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, IdGSled);
                        stmt.setInt(2, IdAMagacinaSled);
                        try ( ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                Isporuka isporuka = new Isporuka();
                                isporuka.setIdI(rs.getInt(1));
                                isporuka.setIdADo(rs.getInt(2));
                                isporuka.setIdATr(rs.getInt(3));
                                isporuka.setTezina(rs.getBigDecimal(4).doubleValue());
                                paketiZaPreuzimanje.add(isporuka);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    List<Isporuka> paketiIzMagacina = new ArrayList<Isporuka>();

                    query = "select Isporuka.IdI, Isporuka.AdresaDo, Isporuka.AdresaTrenutna, Isporuka.Tezina \n"
                            + "from Isporuka JOIN Adresa on Isporuka.AdresaTrenutna = Adresa.IdA \n"
                            + "where Adresa.IdA = ? and Isporuka.StatusIsporuke = 2 and Isporuka.IdI not in (select IdI from IsporucujeSe)\n"
                            + "ORDER BY Isporuka.VremePrihvatanja";

                    try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, IdAMagacinaSled);
                        try ( ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                Isporuka isporuka = new Isporuka();
                                isporuka.setIdI(rs.getInt(1));
                                isporuka.setIdADo(rs.getInt(2));
                                isporuka.setIdATr(rs.getInt(3));
                                isporuka.setTezina(rs.getBigDecimal(4).doubleValue());
                                paketiIzMagacina.add(isporuka);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    List<Isporuka> paketiZaPrevozenjeGrad = new ArrayList<Isporuka>();
                    List<Isporuka> paketiZaPrevozenjeMagacin = new ArrayList<Isporuka>();
                    double trTezina = 0;

                    for (int i = 0; i < paketiZaPreuzimanje.size(); ++i) {
                        if (kilaza + paketiZaPreuzimanje.get(i).getTezina() < nosivost) {
                            kilaza += paketiZaPreuzimanje.get(i).getTezina();
                            paketiZaPrevozenjeGrad.add(paketiZaPreuzimanje.get(i));
                        }
                    }

                    for (int i = 0; i < paketiIzMagacina.size(); ++i) {
                        if (kilaza + paketiIzMagacina.get(i).getTezina() < nosivost) {
                            kilaza += paketiIzMagacina.get(i).getTezina();
                            paketiZaPrevozenjeMagacin.add(paketiIzMagacina.get(i));
                        }
                    }

                    query = "insert into IsporucujeSe(IdI, IdV, IdKur, StatusPaketa) values(?, ?, ?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(query)) {
                        for (int i = 0; i < paketiZaPrevozenjeGrad.size(); ++i) {
                            ps.setInt(1, paketiZaPrevozenjeGrad.get(i).getIdI());
                            ps.setInt(2, IdV);
                            ps.setInt(3, IdKur);
                            ps.setInt(4, 5);
                            ps.executeUpdate();
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    query = "insert into IsporucujeSe(IdI, IdV, IdKur, StatusPaketa) values(?, ?, ?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(query)) {
                        for (int i = 0; i < paketiZaPrevozenjeMagacin.size(); ++i) {
                            ps.setInt(1, paketiZaPrevozenjeMagacin.get(i).getIdI());
                            ps.setInt(2, IdV);
                            ps.setInt(3, IdKur);
                            ps.setInt(4, 6);
                            ps.executeUpdate();
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
                //////////////////////////

                return sledeca.getIdI();

            }

            double euklid = euklidZaVozaca(trAdr, IdAMagacina);

            put += euklid;

//            query = "update TrenutnoVozi set PredjeniPut = ?, TrAdr = ? where IdKur = ? and IdV = ?";
//            try ( PreparedStatement ps = conn.prepareStatement(query)) {
//                ps.setBigDecimal(1, BigDecimal.valueOf(put));
//                ps.setInt(2, IdAMagacina);
//                ps.setInt(3, IdKur);
//                ps.setInt(4, IdV);
//                ps.executeUpdate();
//
//            } catch (SQLException ex) {
//                //ex.printStackTrace();
//            }
            int tipGoriva = -1;
            double potrosnja = -1;

            query = "select TipGoriva, Potrosnja \n"
                    + "from Vozilo \n"
                    + "where IdV = ?";

            try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, IdV);
                try ( ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        tipGoriva = rs.getInt("TipGoriva");
                        potrosnja = rs.getBigDecimal("Potrosnja").doubleValue();
                    }
                } catch (SQLException ex) {

                }
            } catch (SQLException ex) {

            }

            int cenaGoriva = 0;

            switch (tipGoriva) {
                case 0:
                    cenaGoriva = 15;
                    break;
                case 1:
                    cenaGoriva = 32;
                    break;
                case 2:
                    cenaGoriva = 36;
                    break;
                default:
                    break;
            }

            query = "select Isporuka.Cena \n"
                    + "from Isporuka JOIN IsporucujeSe on Isporuka.IdI = IsporucujeSe.IdI \n"
                    + "where IsporucujeSe.IdKur = ? and IsporucujeSe.StatusPaketa = 4";

            double UkCenaIsporucenihPaketa = 0;

            try ( PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, IdKur);
                try ( ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        UkCenaIsporucenihPaketa += rs.getBigDecimal("Cena").doubleValue();
                    }
                } catch (SQLException ex) {

                }
            } catch (SQLException ex) {

            }

            double profit = UkCenaIsporucenihPaketa - (cenaGoriva * potrosnja * put);

            query = "update Isporuka set AdresaTrenutna = ? where IdI = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                for (int i = 0; i < paketiZaMagacin.size(); ++i) {
                    ps.setInt(1, IdAMagacina);
                    ps.setInt(2, paketiZaMagacin.get(i).getIdI());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                //ex.printStackTrace();
            }

            query = "update Kurir set Status = ?, Profit = Profit + ? where IdKur = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, 0);
                ps.setBigDecimal(2, BigDecimal.valueOf(profit));
                ps.setInt(3, IdKur);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            query = "insert into Vozena(IdKur, IdV) values (?, ?)";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, IdKur);
                ps.setInt(2, IdV);
                ps.executeUpdate();
            } catch (SQLException ex) {

            }

            query = "delete from IsporucujeSe where IdKur = ? and IdV = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, IdKur);
                ps.setInt(2, IdV);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            query = "delete from TrenutnoVozi where IdKur = ? and IdV = ?";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, IdKur);
                ps.setInt(2, IdV);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            query = "Insert into Parkirano (IdV, IdM) values (?, ?)";
            try ( PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, IdV);
                ps.setInt(2, IdM);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return -1;

        }

        //return 0;
    }

    @Override
    public List<Integer> getPackagesInVehicle(String userName) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        int IdKur = -1;
        Connection conn = DB.getInstance().getConnection();
        String query = "select IdKur \n"
                + "from Korisnik JOIN Kurir on Korisnik.IdK = Kurir.IdKur \n"
                + "where Korisnik.KorIme = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userName);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdKur = rs.getInt("IdKur");
                } else {
                    return null;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        query = "select IdV \n"
                + "from TrenutnoVozi \n"
                + "where IdKur = ?";

        int IdV = -1;

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    IdV = rs.getInt("IdV");
                } else {
                    return null;
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        List<Integer> paketi = new ArrayList<Integer>();

        query = "select IsporucujeSe.IdI \n"
                + "from IsporucujeSe \n"
                + "where IsporucujeSe.IdKur = ? and IsporucujeSe.IdV = ? and (IsporucujeSe.StatusPaketa = 2 or IsporucujeSe.StatusPaketa = 3)";

        try ( PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, IdKur);
            stmt.setInt(2, IdV);
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paketi.add(rs.getInt(1));
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {

        }

        return paketi;

    }

}
