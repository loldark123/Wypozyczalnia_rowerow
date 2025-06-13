package projekt_java.model;

/**
 * Klasa reprezentująca typ roweru.
 */
public class TypRoweru {
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
