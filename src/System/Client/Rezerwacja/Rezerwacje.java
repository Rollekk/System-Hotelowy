package System.Client.Rezerwacja;

import java.io.Serializable;

public class Rezerwacje implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nazwisko;
    private String adres;
    private String od;
    private String dod;
    private String pokoj;
    private String numer;
    private String cena;

    public Rezerwacje(String nazwisko, String adres, String od, String dod, String pokoj,String numer, String cena) {
        this.nazwisko = nazwisko;
        this.adres = adres;
        this.od = od;
        this.dod = dod;
        this.pokoj = pokoj;
        this.numer = numer;
        this.cena = cena;

    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getPokoj() {
        return pokoj;
    }

    public void setPokoj(String pokoj) {
        this.pokoj = pokoj;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

}
