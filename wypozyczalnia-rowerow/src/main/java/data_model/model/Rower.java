package data_model.model;

import java.io.Serializable;

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

    public Rower(TypRoweru typ, String marka, String model, int rozmiarKola, String opis) {
        this.typ = typ;
        this.marka = marka;
        this.model = model;
        this.rozmiarKola = rozmiarKola;
        this.opis = opis;
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

    @Override
    public String toString() {
        return marka + " " + model;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rower)) return false;
        Rower other = (Rower) obj;
        return marka.equalsIgnoreCase(other.marka)
            && model.equalsIgnoreCase(other.model)
            && typ.getNazwa().equalsIgnoreCase(other.typ.getNazwa());
    }

    @Override
    public int hashCode() {
        return (marka + model + typ.getNazwa()).toLowerCase().hashCode();
    }
}