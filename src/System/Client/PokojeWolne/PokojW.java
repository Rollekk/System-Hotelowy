package System.Client.PokojeWolne;


import java.io.Serializable;


public class PokojW implements Serializable {

    private static final long serialVersionUID = 1L;
    private String opis;
    private String numerpokoju;
    private String liczbamiejsc;
    private String cena;
    private String od;
    private String dod;


    public PokojW(String numerpokoju, String liczbamiejsc,String cena, String od, String dod, String opis) {

        this.numerpokoju = numerpokoju;
        this.liczbamiejsc = liczbamiejsc;
        this.cena = cena;
        this.od = od;
        this.dod = dod;
        this.opis = opis;
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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

}
