package data_model.model;

import java.io.Serializable;

/**
 * Klasa reprezentująca osobę wypożyczającą rower.
 * Implementuje Serializable dla możliwości serializacji obiektów.
 */
public class Klient implements Serializable {
    private static final long serialVersionUID = 1L;

    // Podstawowe dane osobowe klienta
    private String imie;
    private String nazwisko;
    private String numerDowodu; // Unikalny identyfikator klienta
    private String opis;        // Dodatkowe informacje o kliencie

    /**
     * Konstruktor tworzący nowego klienta z walidacją numeru dowodu.
     * @param imie Imię klienta
     * @param nazwisko Nazwisko klienta
     * @param numerDowodu Numer dowodu w formacie AAA123456
     * @param opis Dodatkowy opis klienta
     * 
     * @see #setNumerDowodu(String)
     * @see #equals(Object)
     * @see #hashCode()
     */
    public Klient(String imie, String nazwisko, String numerDowodu, String opis) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        setNumerDowodu(numerDowodu);  // kontrola przez setter z walidacją
        this.opis = opis;
    }

    // Gettery i settery
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

    /**
     * Setter z walidacją formatu numeru dowodu.
     * Numer dowodu musi być w formacie AAA123456, gdzie AAA to trzy wielkie litery, a 123456 to sześć cyfr.
     * 
     * @param numerDowodu numer dowodu osobistego w formacie AAA123456
     *                    (trzy wielkie litery + sześć cyfr)
     * @throws IllegalArgumentException jeśli numer dowodu jest {@code null}
     *         lub nie odpowiada formatowi AAA123456
     * 
     * @see #getNumerDowodu()
     * @see #equals(Object)
     * @see #hashCode()
     */
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

    /**
     * Metoda zwracająca pełne imię i nazwisko klienta.
     * @return łańcuch znaków zawierający imię i nazwisko klienta oddzielone spacją,
     *         lub {@code "null null"} jeśli oba pola są {@code null}
     */
    @Override
    public String toString() {
        return imie + " " + nazwisko;
    }

    /**
     * Sprawdza równość dwóch obiektów klasy Klient na podstawie numeru dowodu osobistego.
     * Dwa obiekty Klient są uznawane za równe, jeśli mają identyczny numer dowodu
     * (porównanie bez uwzględniania wielkości liter).
     * 
     * @param obj obiekt do porównania z bieżącym obiektem Klient
     * @return {@code true} jeśli obiekty są równe (mają ten sam numer dowodu),
     *         {@code false} w przeciwnym przypadku
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Klient)) return false;
        Klient other = (Klient) obj;
        return numerDowodu != null && numerDowodu.equalsIgnoreCase(other.numerDowodu);
    }

    /**
     * Metoda zwracająca hashCode obiektu na podstawie numeru dowodu.
     * Kod hash jest obliczany z numeru dowodu przekonwertowanego na małe litery,
     * co zapewnia spójność z metodą equals(), która porównuje numery dowodów
     * bez uwzględniania wielkości liter.
     * 
     * @return hashCode obiektu, lub 0 jeśli numer dowodu jest {@code null}
     * 
     * @see #equals(Object)
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return numerDowodu == null ? 0 : numerDowodu.toLowerCase().hashCode();
    }
}
