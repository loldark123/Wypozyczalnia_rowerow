package data_model.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Klasa reprezentująca wypożyczenie roweru.
 */
public class Wypozyczenie implements Serializable {
    private static final long serialVersionUID = 1L;

    private Rower rower;
    private Klient klient;
    private LocalDate dataOd;
    private LocalDate dataDo;
    private StatusWypozyczenia status;
    private String uwagi;

    public Wypozyczenie(Rower rower, Klient klient, LocalDate dataOd, LocalDate dataDo,
                        StatusWypozyczenia status, String uwagi) {
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

    public LocalDate getDataOd() {
        return dataOd;
    }

    public void setDataOd(LocalDate dataOd) {
        this.dataOd = dataOd;
    }

    public LocalDate getDataDo() {
        return dataDo;
    }

    public void setDataDo(LocalDate dataDo) {
        this.dataDo = dataDo;
    }

    public StatusWypozyczenia getStatus() {
        return status;
    }

    public void setStatus(StatusWypozyczenia status) {
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
        return klient.getImie() + " " + klient.getNazwisko() +
                " wypożyczył: " + rower.getMarka() + " " + rower.getModel() +
                " od " + dataOd + " do " + dataDo +
                " [" + status + "]";
    }
}
