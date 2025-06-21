package data_model.model;

import java.io.Serializable;

/**
 * Klasa reprezentująca osobę wypożyczającą rower.
 */
public class Klient implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imie;
    private String nazwisko;
    private String numerDowodu;
    private String opis;

    public Klient(String imie, String nazwisko, String numerDowodu, String opis) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        setNumerDowodu(numerDowodu);  // ✅ kontrola przez setter z walidacją
        this.opis = opis;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNumerDowodu() {
        return numerDowodu;
    }

    public void setNumerDowodu(String numerDowodu) {
        if (numerDowodu == null || !numerDowodu.matches("[A-Z]{3}\\d{6}")) {
            throw new IllegalArgumentException("Niepoprawny numer dowodu (format: AAA123456).");
        }
        this.numerDowodu = numerDowodu;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Klient)) return false;
        Klient other = (Klient) obj;
        return numerDowodu != null && numerDowodu.equalsIgnoreCase(other.numerDowodu);
    }

    @Override
    public int hashCode() {
        return numerDowodu == null ? 0 : numerDowodu.toLowerCase().hashCode();
    }
}
