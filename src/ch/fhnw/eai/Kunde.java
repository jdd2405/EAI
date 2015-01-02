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
public class Kunde {
    
    public int kid;
    public String vorname;
    public String nachname;
    public String adresse;
    public char laendercode;
    public String status;

    public Kunde(){
        
    }
    
    public Kunde(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Kunde(int kid, String vorname, String nachname, String adresse, char laendercode, String status) {
        this.kid = kid;
        this.vorname = vorname;
        this.nachname = nachname;
        this.adresse = adresse;
        this.laendercode = laendercode;
        this.status = status;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public char getLaendercode() {
        return laendercode;
    }

    public void setLaendercode(char laendercode) {
        this.laendercode = laendercode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
    
    
}
