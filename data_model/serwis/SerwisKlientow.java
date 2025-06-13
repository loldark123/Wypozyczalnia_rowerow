package projekt_java.serwis;

import projekt_java.model.Klient;

import java.util.*;
import java.util.stream.*;

/**
 * Serwis do zarządzania klientami.
 */
public class SerwisKlientow {
    private final List<Klient> klienci = new ArrayList<>();

    // Dodaj klienta
    public void dodajKlienta(Klient klient) {
        klienci.add(klient);
    }

    // Pobierz wszystkich klientów
    public List<Klient> pobierzWszystkichKlientow() {
        return new ArrayList<>(klienci);
    }

    // Znajdź klientów po imieniu i nazwisku
    public List<Klient> znajdzKlientowPoImieniuINazwisku(String imie, String nazwisko) {
        return klienci.stream()
                .filter(k -> k.getImie().equalsIgnoreCase(imie) && k.getNazwisko().equalsIgnoreCase(nazwisko))
                .collect(Collectors.toList());
    }

    // Znajdź klienta po numerze dowodu
    public Optional<Klient> znajdzKlientaPoDowodzie(String numerDowodu) {
        return klienci.stream()
                .filter(k -> k.getNumerDowodu().equalsIgnoreCase(numerDowodu))
                .findFirst();
    }

    // Aktualizuj klienta
    public boolean aktualizujKlienta(String numerDowodu, Klient nowyKlient) {
        Optional<Klient> optKlient = znajdzKlientaPoDowodzie(numerDowodu);
        if (optKlient.isPresent()) {
            Klient k = optKlient.get();
            k.setImie(nowyKlient.getImie());
            k.setNazwisko(nowyKlient.getNazwisko());
            k.setNumerDowodu(nowyKlient.getNumerDowodu());
            k.setOpis(nowyKlient.getOpis());
            return true;
        }
        return false;
    }

    // Usuń klienta po numerze dowodu
    public boolean usunKlienta(String numerDowodu) {
        return klienci.removeIf(k -> k.getNumerDowodu().equalsIgnoreCase(numerDowodu));
    }
}
