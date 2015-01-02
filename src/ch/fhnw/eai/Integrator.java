/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw.eai;

import java.util.ArrayList;
import java.util.ListIterator;
import javax.xml.ws.Holder;


/**
 *
 * @author Jonas
 */
public class Integrator {
    
    public WebServiceClient ws;
    
    // Nur geprüfte Objekte kommen in die Liste
    public ArrayList<Kunde> kunden;
    public ArrayList<Konto> konten;

    
    public Integrator() {
        
        ws = new WebServiceClient();
        
        kunden = new ArrayList<Kunde>();
        konten = new ArrayList<Konto>();        
    }
    
    public void extrahiereKontokorrente(){
        String queryVorname;
        String queryNachname;
        Holder<String> vorname = new Holder();
        Holder<String> nachname = new Holder();
        Holder<String> adresse = new Holder();
        Holder<String> land = new Holder();
        Holder<Integer> ranking = new Holder();
        Holder<String> ibanKontonummer = new Holder();
        Holder<Float> kontostand = new Holder();
        Holder<String> bic = new Holder();
        
        ListIterator<String> iterator = ws.listeKontokorrentNachname().listIterator();
        while (iterator.hasNext()){
            queryNachname = iterator.next();
            queryVorname = null;
            ws.holeKontoKorrent(queryVorname, queryNachname, vorname, nachname, adresse, land, ranking, ibanKontonummer, kontostand, bic);
            System.out.println("Resultat für '"+queryNachname+"' : "+vorname.value+", "+nachname.value+", "+adresse.value+", "+land.value+", "+ranking.value+", "+ibanKontonummer.value+", "+kontostand.value+", "+bic.value);
        } 
    }
    
    public void extrahiereSparkonten(){
        String queryVorname;
        String queryNachname;
        Holder<String> vorname = new Holder();
        Holder<String> nachname = new Holder();
        Holder<String> strasse = new Holder();
        Holder<String> plzOrt = new Holder();
        Holder<Float> zinsen = new Holder();
        Holder<Long> kontonummer = new Holder();
        Holder<Long> kontostand = new Holder();
        
        ListIterator<String> iterator = ws.listeSparkontoNachname().listIterator();
        while (iterator.hasNext()){
            queryNachname = iterator.next();
            queryVorname = null;
            ws.holeSparkonto(queryVorname, queryNachname, vorname, nachname, strasse, plzOrt, zinsen, kontonummer, kontostand);
            System.out.println("Resultat für '"+queryNachname+"' : "+vorname.value+", "+nachname.value+", "+strasse.value+", "+plzOrt.value+", "+zinsen.value+", "+kontonummer.value+", "+kontostand.value);
            
            Kunde kunde = new Kunde(vorname.value, nachname.value);
            kunde.setAdresse(strasse.value+", "+plzOrt.value);
            Konto konto = new Konto(kunde);
            konto.setKontostand(kontostand.value);
            konto.setIban(""+kontonummer.value);
            if(pruefeEindeutigkeit(kunde)){
                // To Do: Vergleichen
                konten.add(konto);
            }
            else {
                kunden.add(kunde);
                konten.add(konto);
            }
            
            
            
        } 
    }
    
    
    
    public boolean pruefeEindeutigkeit(Kunde kunde){
        boolean istEindeutig = false;
        ListIterator<Kunde> iterator = kunden.listIterator();
        while(iterator.hasNext()){
            Kunde temp = iterator.next();
            if(temp.vorname.equals(kunde.vorname)&&temp.nachname.equals(kunde.nachname)){
                istEindeutig = true;
            }
        }
        
        return istEindeutig;
    }
    
    
    
}
