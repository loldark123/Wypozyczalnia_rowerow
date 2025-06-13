package projekt_java.model;

import java.time.LocalDateTime;

/**
 * Klasa reprezentująca wypożyczenie roweru.
 */
public class Wypozyczenie {
    private Rower rower;
    private Klient klient;
    private LocalDateTime dataOd;
    private LocalDateTime dataDo;
    private String status;
    private String uwagi;

    public Wypozyczenie(Rower rower, Klient klient, LocalDateTime dataOd, LocalDateTime dataDo, String status, String uwagi) {
        this.rower = rower;
        this.klient = klient;
        this.dataOd = dataOd;
        this.dataDo = dataDo;
        this.status = status;
        this.uwagi = uwagi;
    }

    public Rower getRower() {
        return rower;
    }

    public void setRower(Rower rower) {
        this.rower = rower;
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }

    public LocalDateTime getDataOd() {
        return dataOd;
    }

    public void setDataOd(LocalDateTime dataOd) {
        this.dataOd = dataOd;
    }

    public LocalDateTime getDataDo() {
        return dataDo;
    }

    public void setDataDo(LocalDateTime dataDo) {
        this.dataDo = dataDo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }

    @Override
    public String toString() {
        return rower + " wypożyczony przez " + klient;
    }
}
