package data_model.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa reprezentująca rower.
 */
public class Rower implements Serializable {
    private static final long serialVersionUID = 1L;

    private TypRoweru typ;
    private String marka;
    private String model;
    private int rozmiarKola;
    private String opis;
    private String numerSeryjny;

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

    public void setNumerSeryjny(String numerSeryjny) {
        if (numerSeryjny == null || numerSeryjny.trim().isEmpty()) {
            throw new IllegalArgumentException("Numer seryjny nie może być pusty.");
        }
        this.numerSeryjny = numerSeryjny.trim();
    }

    @Override
    public String toString() {
        return marka + " " + model + " (" + numerSeryjny + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rower)) return false;
        Rower other = (Rower) obj;
        return Objects.equals(numerSeryjny, other.numerSeryjny);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerSeryjny);
    }
}
