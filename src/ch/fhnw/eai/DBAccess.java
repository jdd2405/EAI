/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw.eai;

/**
 *
 * @author Jonas
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBAccess {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public ArrayList<DBDaten> DBdaten = new ArrayList<DBDaten>();

    public ArrayList <DBDaten> readDataBase() throws Exception {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://194.126.200.49/realnetc_eai";
        String user = "realnetc_eai";
        String password = "KuA#5Ki!3JE&9i";

        try {

            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT * FROM Account");
            rs = pst.executeQuery();

            while (rs.next()) {
                DBDaten dbKunde = new DBDaten();
                dbKunde.kundenID = rs.getInt(1);
                dbKunde.kundenname = rs.getString(2);
                dbKunde.strassenname = rs.getString(3);
                dbKunde.plz = rs.getString(4);
                dbKunde.stadt = rs.getString(5);
                dbKunde.land = rs.getString(6);
                dbKunde.kundenart = rs.getString(7);
                dbKunde.kontonummer = rs.getLong(8);
                dbKunde.saldo = rs.getDouble(9);
                dbKunde.clearing = rs.getInt(10);
               
                DBdaten.add(dbKunde);
            }
            return DBdaten;
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DBAccess.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBAccess.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
