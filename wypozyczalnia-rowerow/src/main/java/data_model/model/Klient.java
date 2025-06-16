package projekt_java.model;

/**
 * Klasa reprezentująca osobę wypożyczającą rower.
 */
public class Klient {
    private String imie;
    private String nazwisko;
    private String numerDowodu;
    private String opis;

    public Klient(String imie, String nazwisko, String numerDowodu, String opis) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.numerDowodu = numerDowodu;
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
}