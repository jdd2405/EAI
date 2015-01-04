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
    
    //Objekte die eine Ähnlichkeit der Stufe 1 aufweisen
    public ArrayList<Kunde> kundenAehnlichkeit1;
    public ArrayList<Konto> kontenAehnlichkeit1;
    
    //Objekte die eine Ähnlichkeit der Stufe 2 aufweisen
    public ArrayList<Kunde> kundenAehnlichkeit2;
    public ArrayList<Konto> kontenAehnlickheit2;
    
    
    Kunde eindeutigerKunde;
    int aehnlicherKunde;

    
    public Integrator() {
        
        ws = new WebServiceClient();
        
        kunden = new ArrayList<Kunde>();
        konten = new ArrayList<Konto>();
        kundenAehnlichkeit1 = new ArrayList<Kunde>();
        kontenAehnlichkeit1 = new ArrayList<Konto>();
        kundenAehnlichkeit2 = new ArrayList<Kunde>();
        kontenAehnlickheit2 = new ArrayList<Konto>();
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
            konto.setIban(ibanErstellung(kontonummer.value, 0));
            
            eindeutigerKunde = pruefeEindeutigkeit(kunde);
            if(eindeutigerKunde != null){
                konto.setKunde(eindeutigerKunde);
                konten.add(konto);
            }
            else {
                kunde.setKid(kunden.size()+1);
                kunden.add(kunde);
                konten.add(konto);
            }
            
            
            
        } 
    }
    
    
    
    public Kunde pruefeEindeutigkeit(Kunde kunde){
        Kunde returnKunde = null;
        
        ListIterator<Kunde> iterator = kunden.listIterator();
        while(iterator.hasNext()){
            Kunde temp = iterator.next();
            //Eindeutigkeit
            if(temp.vorname.equals(kunde.vorname)&&temp.nachname.equals(kunde.nachname)&&temp.adresse.equals(kunde.adresse)){                    
                returnKunde = temp;
            }
        }
        return returnKunde;
    }
    
    public int pruefeAehnlichkeit(Kunde kunde){
        
        ListIterator<Kunde> iterator = kunden.listIterator();
        while(iterator.hasNext()){
            Kunde tempKunde = iterator.next();
        
            //erste Aehnlichkeitsueberpruefung
            String [] trenneAdresse =kunde.getAdresse().split(", ");
            String [] trennePlzOrt = trenneAdresse[1].split("\\s+");
            if(tempKunde.vorname.equals(kunde.vorname)&&tempKunde.nachname.equals(kunde.nachname)&&tempKunde.adresse.contains(trennePlzOrt[0])){
               kundenAehnlichkeit1.add(tempKunde);
               ListIterator<Konto> iterator2 = konten.listIterator();
               while(iterator2.hasNext()){
                   Konto tempKonto = iterator2.next();
                   if(tempKonto.getKunde().equals(tempKunde)){
                       kontenAehnlichkeit1.add(tempKonto);
                   } 
               }
               return 1; 
            }
            //zweite Aehnlichkeitsueberpruefung
            //if(){
                
            //}
        }
        return 0;
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
            else if (name.length == 3 && name[0].equals("M.")){
                dbKunde.vorname = name[0] + " " + name[1];
                dbKunde.nachname = name[2]; 
            }
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
            //Status
            dbKonto.getKunde().setStatus("not defined status");
            //Kontostand
            dbKonto.setKontostand(dbKundeFromIterator.saldo);
            //Kontoart
            dbKonto.setKontoart("Kontokorrent");
            //IBAN-Nummer
            dbKonto.setIban(ibanErstellung(dbKundeFromIterator.kontonummer, dbKundeFromIterator.clearing));
            
            //Eindeutigkeit prüfen
            eindeutigerKunde = pruefeEindeutigkeit(dbKunde);
            if(eindeutigerKunde != null){
                dbKonto.setKunde(eindeutigerKunde);
                konten.add(dbKonto);
            }
            else {
                //Aehnlichkeit prüfen
                aehnlicherKunde = pruefeAehnlichkeit(dbKunde);
                if (aehnlicherKunde==0){
                    dbKunde.setKid(kunden.size()+1);
                    kunden.add(dbKunde);
                    konten.add(dbKonto);
                }
                else if(aehnlicherKunde==1){
                    kundenAehnlichkeit1.add(dbKunde);
                    kontenAehnlichkeit1.add(dbKonto);
                }
                else{
                    kundenAehnlichkeit2.add(dbKunde);
                    kontenAehnlickheit2.add(dbKonto);
                }
            }
        }
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

    
    
    
    
}
