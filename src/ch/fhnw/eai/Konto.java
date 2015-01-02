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

    public Konto(Kunde kunde, String iban, double kontostand, String kontoart) {
        this.kunde = kunde;
        this.iban = iban;
        this.kontostand = kontostand;
        this.kontoart = kontoart;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getKontostand() {
        return kontostand;
    }

    public void setKontostand(double kontostand) {
        this.kontostand = kontostand;
    }

    public String getKontoart() {
        return kontoart;
    }

    public void setKontoart(String kontoart) {
        this.kontoart = kontoart;
    }
    
    
    
    
}
