/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw.eai;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonas
 */
public class Main {
    

    public static void main(String[] args) {
        try {
            // Macht den ganzen Vergleichs-Kram
            Integrator integrator = new Integrator();
            integrator.extrahiereKontokorrente();
            integrator.extrahiereSparkonten();
            
            DBAccess db = new DBAccess();
            ArrayList <DBDaten> DBdaten =db.readDataBase();
            integrator.DBDatenFormatieren(DBdaten);
            
            //Kontostand 2 Nachkommastellen
            DecimalFormat f = new DecimalFormat("#0.00");
            System.out.println("********************");
            System.out.println("Objekte");
            System.out.println("********************");
            ListIterator<Konto> iterator = integrator.konten.listIterator();
            while(iterator.hasNext()){
                Konto temp = iterator.next();
                System.out.println(temp.getKunde().getKid());
                System.out.println(temp.getKunde().getVorname());
                System.out.println(temp.getKunde().getNachname());
                System.out.println(temp.getKunde().getAdresse());
                System.out.println(temp.getKunde().getLaendercode());
                System.out.println(temp.getKunde().getStatus());
                System.out.println("*** Konto ***");
                System.out.println(temp.getKunde().getKid());
                System.out.println(temp.getIban());
                System.out.println(f.format(temp.getKontostand()));
                System.out.println(temp.getKontoart());
                System.out.println("******************");
            }
            
            System.out.println("");
            System.out.println("Konten die eine Ähnlichkeit aufweisen");
            System.out.println("");
            
            ListIterator<Konto> iterator2 = integrator.kontenAehnlichkeit.listIterator();
            while(iterator2.hasNext()){
                Konto temp = iterator2.next();
                System.out.println(temp.getKunde().getKid());
                System.out.println(temp.getKunde().getVorname());
                System.out.println(temp.getKunde().getNachname());
                System.out.println(temp.getKunde().getAdresse());
                System.out.println(temp.getKunde().getLaendercode());
                System.out.println(temp.getKunde().getStatus());
                System.out.println("***");
                System.out.println(temp.getKunde().getKid());
                System.out.println(temp.getIban());
                System.out.println(f.format(temp.getKontostand()));
                System.out.println(temp.getKontoart());
                System.out.println("******************");
            }

          
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void holeKontoKorrent(java.lang.String queryVorname, java.lang.String queryNachname, javax.xml.ws.Holder<java.lang.String> vorname, javax.xml.ws.Holder<java.lang.String> nachname, javax.xml.ws.Holder<java.lang.String> adresse, javax.xml.ws.Holder<java.lang.String> land, javax.xml.ws.Holder<java.lang.Integer> ranking, javax.xml.ws.Holder<java.lang.String> ibanKontonummer, javax.xml.ws.Holder<java.lang.Float> kontostand, javax.xml.ws.Holder<java.lang.String> bic) {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        port.holeKontoKorrent(queryVorname, queryNachname, vorname, nachname, adresse, land, ranking, ibanKontonummer, kontostand, bic);
    }

    private static void holeSparkonto(java.lang.String queryVorname, java.lang.String queryNachname, javax.xml.ws.Holder<java.lang.String> vorname, javax.xml.ws.Holder<java.lang.String> nachname, javax.xml.ws.Holder<java.lang.String> strasse, javax.xml.ws.Holder<java.lang.String> plzOrt, javax.xml.ws.Holder<java.lang.Float> zinsen, javax.xml.ws.Holder<java.lang.Long> kontonummer, javax.xml.ws.Holder<java.lang.Long> kontostand) {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        port.holeSparkonto(queryVorname, queryNachname, vorname, nachname, strasse, plzOrt, zinsen, kontonummer, kontostand);
    }

    private static java.util.List<java.lang.String> listeKontokorrentNachname() {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        return port.listeKontokorrentNachname();
    }

    private static java.util.List<java.lang.String> listeSparkontoNachname() {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        return port.listeSparkontoNachname();
    }

    private static void printKontokorrent() {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        port.printKontokorrent();
    }

    private static void printSparkonto() {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        port.printSparkonto();
    }

}
