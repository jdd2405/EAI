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
            ArrayList<DBDaten> DBdaten = db.readDataBase();
            integrator.DBDatenFormatieren(DBdaten);

            integrator.pruefeKonten();
            integrator.kundenIDverteilen();
            
            //Kontostand 2 Nachkommastellen
            DecimalFormat f = new DecimalFormat("#0.00");
            System.out.println("********************************************************************************************************************************************");
            System.out.println("Automatisch integrierte Objekte");
            System.out.println("********************************************************************************************************************************************");
            System.out.printf("%-10s %-20s %-20s %-50s %-15s %-10s%n", "Kunden-ID", "Vorname", "Nachname", "Adresse", "Ländercode", "Status");
            System.out.printf("%110s%n", "--------------------------------------------------------------------------------------------------------------------------------------------");
            
            ListIterator<Kunde> iterator = integrator.kunden.listIterator();
            while (iterator.hasNext()) {
                Kunde kunde = iterator.next();
                System.out.printf("%-10s %-20s %-20s %-50s %-15s %-10s%n", 
                        kunde.getKid(), 
                        kunde.getVorname(), 
                        kunde.getNachname(), 
                        kunde.getAdresse(), 
                        kunde.getLaendercode(),
                        kunde.getStatus());

                /*ListIterator<Konto> iterator2 = integrator.konten.listIterator();
                while (iterator2.hasNext()) {
                    Konto konto = iterator2.next();
                    if (konto.getKunde().kid == kunde.kid) {
                        System.out.println("*** Konto ***");
                        System.out.println(konto.getKunde().getKid());
                        System.out.println(konto.getIban());
                        System.out.println(f.format(konto.getKontostand()));
                        System.out.println(konto.getKontoart());
                        System.out.println("******************");
                    }
                }*/
            }
            System.out.println("");
            System.out.println("*******************************************************************************************************************************************");
            System.out.println("Bitte Überprüfen! Kunden die eine Ähnlichkeit aufweisen");
            System.out.println("*******************************************************************************************************************************************");
            
            System.out.printf("%-10s %-20s %-20s %-50s %-15s %-10s%n", "Kunden-ID", "Vorname", "Nachname", "Adresse", "Ländercode", "Status");
            System.out.printf("%110s%n", "-------------------------------------------------------------------------------------------------------------------------------------------");
            
            ListIterator<Kunde> iterator2 = integrator.kundenAehnlichkeit.listIterator();
            while (iterator2.hasNext()) {
                Kunde kunde = iterator2.next();
                String id = String.valueOf(kunde.getKid());
                if(kunde.getKid()==0){
                    id = "";
                }
                    
                System.out.printf("%-10s %-20s %-20s %-50s %-15s %-10s%n", 
                        id, 
                        kunde.getVorname(), 
                        kunde.getNachname(), 
                        kunde.getAdresse(), 
                        kunde.getLaendercode(),
                        kunde.getStatus());
            }
            
            
            System.out.println("");
            System.out.println("*******************************************************************************************************************************************");
            System.out.println("Automatisch integrierte Konten");
            System.out.println("*******************************************************************************************************************************************");
            
            System.out.printf("%-10s %-25s %20s %-5s %-20s%n", "Kunden-ID", "IBAN", "Kontostand (CHF)", "", "Kontoart");
            System.out.printf("%110s%n", "-------------------------------------------------------------------------------------------------------------------------------------------");
            
            ListIterator<Konto> iterator3 = integrator.konten.listIterator();
            while (iterator3.hasNext()) {
                Konto konto = iterator3.next();
                Kunde kunde = konto.getKunde();
                String id = String.valueOf(kunde.getKid());
                if(kunde.getKid()==0){
                    id = "";
                }
                    
                System.out.printf("%-10s %-25s %20.2f %-5s %-20s%n", 
                        id,
                        konto.getIban(),
                        konto.getKontostand(),
                        "",
                        konto.getKontoart());
            }
            
            
            System.out.println("");
            System.out.println("*******************************************************************************************************************************************");
            System.out.println("Fehlerhafte Konten (ungültige IBAN)");
            System.out.println("*******************************************************************************************************************************************");
            
            System.out.printf("%-10s %-25s %20s %-5s %-20s%n", "Kunden-ID", "IBAN", "Kontostand (CHF)", "", "Kontoart");
            System.out.printf("%110s%n", "-------------------------------------------------------------------------------------------------------------------------------------------");
            
            ListIterator<Konto> iterator4 = integrator.fehlerhafteKonten.listIterator();
            while (iterator4.hasNext()) {
                Konto konto = iterator4.next();
                Kunde kunde = konto.getKunde();
                String id = String.valueOf(kunde.getKid());
                if(kunde.getKid()==0){
                    id = "";
                }
                    
                System.out.printf("%-10s %-25s %20.2f %-5s %-20s%n", 
                        id,
                        konto.getIban(),
                        konto.getKontostand(),
                        "",
                        konto.getKontoart());
            }
            
            

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
