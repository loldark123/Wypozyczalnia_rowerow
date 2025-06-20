package data_model.model;

import java.io.Serializable;

/**
 * Klasa reprezentująca typ roweru.
 */
public class TypRoweru implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nazwa;
    private String opis;

    public TypRoweru(String nazwa, String opis) {
        this.nazwa = nazwa;
        this.opis = opis;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return nazwa;
    }
}