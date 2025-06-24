package data_model.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa reprezentująca rower w systemie wypożyczalni.
 * Każdy rower ma unikalny numer seryjny służący jako identyfikator.
 */
public class Rower implements Serializable {
    private static final long serialVersionUID = 1L;

    // Charakterystyka roweru
    private TypRoweru typ;       // Referencja do typu roweru
    private String marka;      
    private String model;      
    private int rozmiarKola;     // Rozmiar w calach
    private String opis;         // Dodatkowe informacje
    private String numerSeryjny; // Unikalny identyfikator

    /**
     * Konstruktor tworzący nowy rower z walidacją numeru seryjnego.
     * Numer seryjny jest automatycznie oczyszczany z białych znaków
     * i służy jako unikalny identyfikator roweru w systemie.
     * 
     * @param typ Typ roweru (np. górski, szosowy)
     * @param marka Marka roweru
     * @param model Model roweru
     * @param rozmiarKola Rozmiar koła w calach
     * @param opis Dodatkowy opis roweru
     * @param numerSeryjny Unikalny numer seryjny roweru (nie może być pusty)
     * @throws IllegalArgumentException Jeśli numer seryjny jest {@code null} lub pusty po usunięciu białych znaków
     * 
     * @see #setNumerSeryjny(String)
     * @see #equals(Object)
     * @see #hashCode()
     */
    public Rower(TypRoweru typ, String marka, String model, int rozmiarKola, String opis, String numerSeryjny) {
        if (numerSeryjny == null || numerSeryjny.trim().isEmpty()) {
            throw new IllegalArgumentException("Numer seryjny nie może być pusty.");
        }
        this.typ = typ;
        this.marka = marka;
        this.model = model;
        this.rozmiarKola = rozmiarKola;
        this.opis = opis;
        this.numerSeryjny = numerSeryjny.trim();
    }

    // Gettery i settery 
    public TypRoweru getTyp() {
        return typ;
    }

    public void setTyp(TypRoweru typ) {
        this.typ = typ;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getRozmiarKola() {
        return rozmiarKola;
    }

    public void setRozmiarKola(int rozmiarKola) {
        this.rozmiarKola = rozmiarKola;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getNumerSeryjny() {
        return numerSeryjny;
    }

    /**
     * Setter z walidacją numeru seryjnego.
     * Numer seryjny nie może być pusty ani zawierać tylko białych znaków.
     * Automatycznie usuwa białe znaki z początku i końca numeru.
     * 
     * @param numerSeryjny Numer seryjny do ustawienia
     * @throws IllegalArgumentException Jeśli numer seryjny jest pusty
     * 
     * @see #getNumerSeryjny()
     * @see #equals(Object)
     */
    public void setNumerSeryjny(String numerSeryjny) {
        if (numerSeryjny == null || numerSeryjny.trim().isEmpty()) {
            throw new IllegalArgumentException("Numer seryjny nie może być pusty.");
        }
        this.numerSeryjny = numerSeryjny.trim();
    }

    /**
     * Metoda zwracająca pełne informacje o rowerze.
     * @return String z informacjami o rowerze
     */
    @Override
    public String toString() {
        return marka + " " + model + " (" + numerSeryjny + ")";
    }

    /**
     * Sprawdza równość dwóch obiektów klasy Rower na podstawie numeru seryjnego.
     * Dwa rowery są uznawane za równe, jeśli mają identyczny numer seryjny.
     * Numer seryjny służy jako unikalny identyfikator roweru w systemie
     * wypożyczalni.
     * 
     * @param obj obiekt do porównania z bieżącym obiektem Rower
     * @return {@code true} jeśli obiekty są równe (mają ten sam numer seryjny),
     *         {@code false} w przeciwnym przypadku
     * 
     * @see Object#equals(Object)
     * @see #hashCode()
     * @see Objects#equals(Object, Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rower)) return false;
        Rower other = (Rower) obj;
        return Objects.equals(numerSeryjny, other.numerSeryjny);
    }

    /**
     * Zwraca hashCode obiektu na podstawie numeru seryjnego.
     * Kod hash jest generowany przy użyciu metody Objects.hash(), która
     * zapewnia spójność z metodą equals() bazującą na numerze seryjnym.
     *  
     * @return kod hash obiektu, lub 0 jeśli numer seryjny jest {@code null}
     */
    @Override
    public int hashCode() {
        return Objects.hash(numerSeryjny);
    }
}
