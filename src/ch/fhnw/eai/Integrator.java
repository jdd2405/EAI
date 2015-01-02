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
    
    
    
}
