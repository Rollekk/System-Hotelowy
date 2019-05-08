package System.Client.PokojeZajete;


import java.io.Serializable;


public class PokojZ implements Serializable {

    private static final long serialVersionUID = 1L;
    private String numerpokoju;
    private String nazwisko;
    private String od;
    private String dod;
    private String liczbamiejsc;
    private String cena;

    public PokojZ(String numerpokoju, String nazwisko, String od, String dod, String liczbamiejsc, String cena) {
        this.numerpokoju = numerpokoju;
        this.nazwisko = nazwisko;
        this.od = od;
        this.dod = dod;
        this.liczbamiejsc = liczbamiejsc;
        this.cena = cena;
    }

    public String getNumerpokoju() {
        return numerpokoju;
    }

    public String getOd() {
        return od;
    }

    public String getDod() {
        return dod;
    }

    public String getLiczbamiejsc() {
        return liczbamiejsc;
    }

    public String getCena() {
        return cena;
    }

    public void setNumerpokoju(String value) {
        numerpokoju = value;
    }

    public void setOd(String value) {
        od=value;
    }

    public void setDod(String value) {
        dod=value;
    }

    public void setLiczbamiejsc(String value) {
        liczbamiejsc=value;
    }

    public void setCena(String value) {
        cena= value;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
}
