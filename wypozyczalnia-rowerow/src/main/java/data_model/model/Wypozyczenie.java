package data_model.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Random;

/**
 * Klasa reprezentująca wypożyczenie roweru przez klienta w systemie wypożyczalni.
 * Zawiera wszystkie informacje o transakcji wypożyczenia, w tym dane klienta, roweru,
 * daty wypożyczenia oraz aktualnego statusu.
 * Klasa jest serializowalna i może być używana do przechowywania danych w bazie danych.
 * 
 * @see Rower
 * @see Klient
 * @see StatusWypozyczenia
 */
public class Wypozyczenie implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;                  // Unikalny identyfikator wypożyczenia
    private Rower rower;                // Wypożyczony rower
    private Klient klient;              // Klient wypożyczający rower
    private LocalDate dataOd;           // Data rozpoczęcia wypożyczenia
    private LocalDate dataDo;           // Data zakończenia wypożyczenia
    private StatusWypozyczenia status;  // Aktualny status wypożyczenia (np. aktywne, zakończone, anulowane)
    private String uwagi;               // Dodatkowe uwagi dotyczące wypożyczenia

    /**
     * Konstruktor tworzący nowe wypożyczenie.
     * 
     * @param id Unikalny identyfikator
     * @param rower Wypożyczany rower
     * @param klient Klient wypożyczający
     * @param dataOd Data rozpoczęcia
     * @param dataDo Planowana data zwrotu
     * @param status aktualny status wypożyczenia (aktywne, zakończone, spóźnienie)
     * @param uwagi Dodatkowe uwagi
     * 
     * @see StatusWypozyczenia
     */
    public Wypozyczenie(String id, Rower rower, Klient klient, LocalDate dataOd, LocalDate dataDo,
                        StatusWypozyczenia status, String uwagi) {
        this.id = id;
        this.rower = rower;
        this.klient = klient;
        this.dataOd = dataOd;
        this.dataDo = dataDo;
        this.status = status;
        this.uwagi = uwagi;
    }

    /**
     * Generuje losowy 10-cyfrowy identyfikator wypożyczenia.
     * Metoda tworzy unikalny ID dla nowego wypożyczenia, składający się z 10 losowych cyfr.
     * 
     * @return String zawierający 10 losowych cyfr (0-9).
     */
    public static String generujLosoweId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10)); // losuje cyfrę 0-9
        }
        return sb.toString();
    }

    // Gettery i settery
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rower getRower() {
        return rower;
    }

    public void setRower(Rower rower) {
        this.rower = rower;
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }

    public LocalDate getDataOd() {
        return dataOd;
    }

    public void setDataOd(LocalDate dataOd) {
        this.dataOd = dataOd;
    }

    public LocalDate getDataDo() {
        return dataDo;
    }

    public void setDataDo(LocalDate dataDo) {
        this.dataDo = dataDo;
    }

    public StatusWypozyczenia getStatus() {
        return status;
    }

    public void setStatus(StatusWypozyczenia status) {
        this.status = status;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }

    /**
     * Metoda zwraca szczegółową reprezentację wypożyczenia z kluczowymi informacjami.
     * Zawiera informacje o ID, kliencie, rowerze, datach wypożyczenia oraz statusie.
     * 
     *  @return sformatowany łańcuch znaków w formacie:
     *         "ID: [id] | [imię] [nazwisko] wypożyczył: [marka] [model] od [dataOd] do [dataDo] [[status]]"
     *         np. "ID: 1122334455 | Jan Kowalski wypożyczył: Trek X-Caliber od 2025-01-20 do 2025-06-25 [AKTYWNE]"
     */
    @Override
    public String toString() {
        return "ID: " + id + " | " + klient.getImie() + " " + klient.getNazwisko() +
                " wypożyczył: " + rower.getMarka() + " " + rower.getModel() +
                " od " + dataOd + " do " + dataDo +
                " [" + status + "]";
    }
}
