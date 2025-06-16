package projekt_java.model;

/**
 * Klasa reprezentująca rower.
 */
public class Rower {
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
        return typ; //czy tu poda nazwe typu?
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
        StringBuilder sb = new StringBuilder();
        sb.append(marka);
        sb.append(" ");
        sb.append(model);
        return sb.toString();
    }
}