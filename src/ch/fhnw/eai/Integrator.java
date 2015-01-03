/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw.eai;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 *
 * @author Jonas
 */
public class Integrator {
    
    public ArrayList<Kunde> kunden;
    public ArrayList<Konto> konten;

    public Integrator() {
        
        kunden = new ArrayList<Kunde>();
        konten = new ArrayList<Konto>();
        
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
    
    public ArrayList<Kunde> DBNamenTrennen(ArrayList <DBDaten> DBdaten){
        ArrayList <Kunde> DBkunde = new ArrayList <Kunde>();
        ArrayList <Konto> DBkonto = new ArrayList <Konto>();
        ListIterator<DBDaten> iterator1 = DBdaten.listIterator();
        
        
        //Firma aus arraylist löschen
        while(iterator1.hasNext()){
            if (iterator1.next().kundenart.equals("Firma")){
                iterator1.remove();
            }
        }
        
        ListIterator<DBDaten> iterator2 = DBdaten.listIterator();
        while (iterator2.hasNext()){
            //name und vorname trennen
            String kundenname = iterator2.next().kundenname;
            String [] name = kundenname.split("\\s+");
            Kunde dbKunde = new Kunde();
            if (name.length == 2){
                dbKunde.vorname = name[0];
                dbKunde.nachname = name[1];
            }
            else if (name.length == 3 && name[0].equals("Dr.")){
                dbKunde.vorname = name[1];
                dbKunde.nachname = name[2];
            }
            else if (name.length == 3){
                dbKunde.vorname = name[0];
                dbKunde.nachname = name[1]+ " " + name[2]; 
            }
            
            DBkunde.add(dbKunde);
        }
        return DBkunde; 
    }
    
    
}
