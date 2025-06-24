package data_model.model;

import java.io.Serializable;

/**
 * Klasa reprezentująca typ roweru w systemie wypożyczalni.
 * Definiuje kategorię roweru (np. górski, szosowy, miejski) wraz z opisem charakterystycznych cech danego typu.
 * 
 * @see Rower
 * @see Wypozyczenie
 */
public class TypRoweru implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nazwa; // Nazwa typu roweru (np. "Górski", "Szosowy")
    private String opis; // Szczegółowy opis typu roweru

    /**
     * Konstruktor tworzący nowy typ roweru z określoną nazwą i opisem.
     * Konstruktor inicjalizuje wszystkie podstawowe właściwości typu roweru używane do identyfikacji i opisu.
     *  
     * @param nazwa Nazwa typu roweru
     * @param opis Opis charakterystyki tego typu
     */
    public TypRoweru(String nazwa, String opis) {
        this.nazwa = nazwa;
        this.opis = opis;
    }

    // Gettery i settery
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

    /**
     * Metoda zwracająca szczegółowy opis typu roweru.
     * @return nazwa typu roweru
     */
    @Override
    public String toString() {
        return nazwa;
    }
}