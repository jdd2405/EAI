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
public class Konto {
    
    public Kunde kunde;
    public String iban;
    public double kontostand;
    public String kontoart;

    public Konto(Kunde kunde) {
        this.kunde = kunde;
    }
    
    
    
}
