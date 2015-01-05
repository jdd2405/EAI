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
    
    //Objekte die eine Ähnlichkeit aufweisen
    public ArrayList<Kunde> kundenAehnlichkeit;

    public ArrayList<Konto> fehlerhafteKonten;
    
    Kunde eindeutigerKunde;
    Konto aehnlichesKonto1;

    
    public Integrator() {
        
        ws = new WebServiceClient();
        
        kunden = new ArrayList<Kunde>();
        konten = new ArrayList<Konto>();
        kundenAehnlichkeit = new ArrayList<Kunde>();

        fehlerhafteKonten = new ArrayList();
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
            
                
            Kunde kunde = new Kunde(vorname.value, nachname.value);
            kunde.setAdresse(adresse.value);
            // Quick and Dirty!
            if(kunde.adresse.equals("Kastelgasse 31, 8001 ZH")){
                kunde.setAdresse("Kastelgasse 31, 8001 Zürich");
            }
            
            // Ranking Mapping
            if(ranking.value<3){
                kunde.setStatus("Gold");
            }
            else if(ranking.value==3){
                kunde.setStatus("Silber");
            }
            else {
                kunde.setStatus("Bronze");
            }
            
            Konto konto = new Konto(kunde);
            konto.setKontostand(kontostand.value);
            konto.setIban(ibanKontonummer.value);
            konto.setKontoart("Kontokorrent");
            
            
            //Eindeutigkeit prüfen
            eindeutigerKunde = pruefeEindeutigkeit(kunde, kunden);
            if(eindeutigerKunde != null){
                konto.setKunde(eindeutigerKunde);
                konten.add(konto);
            }
            else if(pruefeEindeutigkeit(kunde, kundenAehnlichkeit) != null){
                konto.setKunde(pruefeEindeutigkeit(kunde, kundenAehnlichkeit));
                konten.add(konto);
            }
            //Aehnlichkeit prüfen
            else {
                aehnlichesKonto1 = pruefeAehnlichkeit(kunde);
                if (aehnlichesKonto1==null){
                        kunden.add(kunde);
                        konten.add(konto);
                }
                    
                else{
                    kundenAehnlichkeit.add(kunde);
                    konten.add(konto);
                    if(!kundenAehnlichkeit.contains(aehnlichesKonto1.getKunde())){
                        kundenAehnlichkeit.add(aehnlichesKonto1.getKunde());
                    }
                    if(kunden.contains(aehnlichesKonto1.getKunde())){
                        kunden.remove(aehnlichesKonto1.getKunde());
                    }                    
                }
            }

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
            
           
            Kunde kunde = new Kunde(vorname.value, nachname.value);
            kunde.setAdresse(strasse.value+", "+plzOrt.value);
            // Quick and Dirty!
            if(kunde.adresse.equals("Riggenbachstrasse, , 4600 Olten")){
                kunde.setAdresse("Riggenbachstrasse, 4600 Olten");
            }
            Konto konto = new Konto(kunde);
            konto.setKontostand(kontostand.value);
            konto.setIban(ibanErstellung(kontonummer.value, 0));
            konto.setKontoart("Sparkonto");
            
            //Eindeutigkeit prüfen
            eindeutigerKunde = pruefeEindeutigkeit(kunde, kunden);
            if(eindeutigerKunde != null){
                konto.setKunde(eindeutigerKunde);
                konten.add(konto);
            }
            else if(pruefeEindeutigkeit(kunde, kundenAehnlichkeit) != null){
                konto.setKunde(pruefeEindeutigkeit(kunde, kundenAehnlichkeit));
                konten.add(konto);
            }
            //Aehnlichkeit prüfen
            else {
                aehnlichesKonto1 = pruefeAehnlichkeit(kunde);
                if (aehnlichesKonto1==null){
                        kunden.add(kunde);
                        konten.add(konto);
                }
                    
                else{
                    kundenAehnlichkeit.add(kunde);
                    konten.add(konto);
                    if(!kundenAehnlichkeit.contains(aehnlichesKonto1.getKunde())){
                        kundenAehnlichkeit.add(aehnlichesKonto1.getKunde());
                    }
                    if(kunden.contains(aehnlichesKonto1.getKunde())){
                        kunden.remove(aehnlichesKonto1.getKunde());
                    }                    
                }
            }

        } 
    }
    
    
    
    
    
    public void DBDatenFormatieren(ArrayList <DBDaten> DBdaten){
        ListIterator<DBDaten> iterator1 = DBdaten.listIterator();

        //Firma aus arraylist löschen
        while(iterator1.hasNext()){
            if (iterator1.next().kundenart.equals("Firma")){
                iterator1.remove();
            }
        }
        
        ListIterator<DBDaten> iterator2 = DBdaten.listIterator();
        while (iterator2.hasNext()){
            DBDaten dbKundeFromIterator = iterator2.next();
            
            //name und vorname trennen
            String kundenname = dbKundeFromIterator.kundenname;
            String [] name = kundenname.split("\\s+");
            
            //Gross- und Kleinschriebung bereinigen
            for (int i =0; i < name.length; i++){
                name[i]=firstLetterCaps(name[i]);
            }
                        
            Kunde dbKunde = new Kunde();
            Konto dbKonto = new Konto(dbKunde);
            if (name.length == 2){
                dbKunde.vorname = name[0];
                dbKunde.nachname = name[1];
            }
            else if (name.length == 3 && name[0].equals("Dr.")){
                dbKunde.vorname = name[1];
                dbKunde.nachname = name[2];
            }
            /*else if (name.length == 3 && name[0].equals("M.")){
                dbKunde.vorname = name[0] + " " + name[1];
                dbKunde.nachname = name[2]; 
            }*/
            else if (name.length == 3){
                dbKunde.vorname = name[0];
                dbKunde.nachname = name[1]+ " " + name[2]; 
            }
            
            //adresse generieren
            dbKunde.adresse = dbKundeFromIterator.strassenname +", " + dbKundeFromIterator.plz +" " + dbKundeFromIterator.stadt;
            
            //laendercode
            if(dbKundeFromIterator.land.equals("Schweiz") || dbKundeFromIterator.land.equals("Switzerland")){
                dbKonto.getKunde().setLaendercode("CH");
            }
            else if(dbKundeFromIterator.land.equals("Deutschland") || dbKundeFromIterator.land.equals("Germany")){
                dbKunde.laendercode = "DE";
            }
            else if(dbKundeFromIterator.land.equals("The Netherlands")){
                dbKunde.laendercode = "NL";
            }
            //Kontostand
            dbKonto.setKontostand(dbKundeFromIterator.saldo);
            //Kontoart
            dbKonto.setKontoart("Kontokorrent");
            //IBAN-Nummer
            dbKonto.setIban(ibanErstellung(dbKundeFromIterator.kontonummer, dbKundeFromIterator.clearing));
            
            //Eindeutigkeit prüfen
            eindeutigerKunde = pruefeEindeutigkeit(dbKunde, kunden);
            if(eindeutigerKunde != null){
                dbKonto.setKunde(eindeutigerKunde);
                konten.add(dbKonto);
            }
            else if(pruefeEindeutigkeit(dbKunde, kundenAehnlichkeit) != null){
                dbKonto.setKunde(pruefeEindeutigkeit(dbKunde, kundenAehnlichkeit));
                konten.add(dbKonto);
            }
            
            //Aehnlichkeit prüfen
            //Aehnlichkeit prüfen
            else {
                aehnlichesKonto1 = pruefeAehnlichkeit(dbKunde);
                if (aehnlichesKonto1==null){
                        kunden.add(dbKunde);
                        konten.add(dbKonto);
                }
                    
                else{
                    kundenAehnlichkeit.add(dbKunde);
                    konten.add(dbKonto);
                    if(!kundenAehnlichkeit.contains(aehnlichesKonto1.getKunde())){
                        kundenAehnlichkeit.add(aehnlichesKonto1.getKunde());
                    }
                    if(kunden.contains(aehnlichesKonto1.getKunde())){
                        kunden.remove(aehnlichesKonto1.getKunde());
                    }                    
                }
            }
        }
    }
    
    public Kunde pruefeEindeutigkeit(Kunde kunde, ArrayList<Kunde> durchsuchungsliste){
        Kunde returnKunde = null;
        
        ListIterator<Kunde> iterator = durchsuchungsliste.listIterator();
        while(iterator.hasNext()){
            Kunde temp = iterator.next();
            //Eindeutigkeit
            if(temp.vorname.equals(kunde.vorname)&&temp.nachname.equals(kunde.nachname)&&temp.adresse.equals(kunde.adresse)){                    
                returnKunde = temp;
            }
        }
        return returnKunde;
    }
    
    
    
    
    
    public Konto pruefeAehnlichkeit(Kunde kunde){
        ListIterator<Konto> iterator = konten.listIterator();
        while(iterator.hasNext()){
            Konto tempKonto = iterator.next();
        
            //erste Aehnlichkeitsueberpruefung
            String [] trenneAdresse =kunde.getAdresse().split(", ");
            String [] trennePlzOrt = trenneAdresse[1].split("\\s+");
            String nachnameOhneUmlaut = kunde.nachname.replace("ü", "ue");
            String nachnameMitUmlaut = kunde.nachname.replace("ue", "ü");
            if(tempKonto.getKunde().vorname.equals(kunde.vorname)&&(tempKonto.getKunde().nachname.equals(nachnameOhneUmlaut)||tempKonto.getKunde().nachname.equals(nachnameMitUmlaut))&&tempKonto.getKunde().adresse.contains(trennePlzOrt[0])){
               
               return tempKonto; 
            }
            if(tempKonto.getKunde().adresse.equals(kunde.adresse)&&((kunde.vorname + kunde.nachname).contains(tempKonto.getKunde().vorname) ||(kunde.vorname + kunde.nachname).contains(tempKonto.getKunde().nachname))){
               
               return tempKonto;
               
            }
        }
        return null;
    }
    
    
    
    //Methode für die Gross- und Kleinschriebung
    static public String firstLetterCaps ( String data ){
        if (data.equals("van")||data.equals("von")){
            return data.toLowerCase();
        }
        else{
            String firstLetter = data.substring(0,1).toUpperCase();
            String restLetters = data.substring(1).toLowerCase();
            return firstLetter + restLetters;
        }    
    }
    
    //Methode für die Erstellung einer IBAN-Nummer
    static public String ibanErstellung(long kontonummerLONG, int clearingINT){
        String ibannummer;
        String pruefzifferInText;
        int chInZahlen = 1217;
        String kontonummer = String.valueOf(kontonummerLONG);
        String clearing = String.valueOf(clearingINT);
        //Kontonummer auf 12 Stellen verlängern wenn nötig
        while (kontonummer.length()<12){
            kontonummer ="0" +kontonummer;
        }
        //clearing definieren oder bereinigen
        if (clearingINT == 0){
            clearing = "00240";
        }
        else{
            while (clearing.length()<5){
                clearing = "0" + clearing;
            }
        }
        //pruefziffer muss 2stellig sein
        long pruefziffer = (Integer.parseInt(clearing) + Long.parseLong(kontonummer) +  chInZahlen + 00)%97;
        if (pruefziffer <10){
            pruefzifferInText = "0"+pruefziffer;
        }
        else{
            pruefzifferInText = String.valueOf(pruefziffer);
        }
        //zusammensetzung zum Endresultat
        ibannummer = "CH" + pruefzifferInText + clearing + kontonummer;
        return ibannummer;
    }
    
    public void pruefeKonten(){
        ListIterator<Konto> iterator = konten.listIterator();
        while(iterator.hasNext()){
            Konto konto = iterator.next();
            if(konto.getIban().length()>21){
                fehlerhafteKonten.add(konto);
                iterator.remove();
            }
        }
    }

    public void kundenIDverteilen(){
        for(int i = 0; i<kunden.size();i++){
            kunden.get(i).setKid(i+1);
        }
    }
    
    
    
}
