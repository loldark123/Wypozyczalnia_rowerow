package projekt_java.serwis;

import projekt_java.model.Wypozyczenie;
import projekt_java.model.Rower;
import projekt_java.model.Klient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

/**
 * Serwis do zarządzania wypożyczeniami rowerów.
 */
public class SerwisWypozyczen {
    private final List<Wypozyczenie> wypozyczenia = new ArrayList<>();

    // Dodaj wypożyczenie
    public void dodajWypozyczenie(Wypozyczenie wypozyczenie) {
        wypozyczenia.add(wypozyczenie);
    }

    // Pobierz wszystkie wypożyczenia
    public List<Wypozyczenie> pobierzWszystkieWypozyczenia() {
        return new ArrayList<>(wypozyczenia);
    }

    // Znajdź wszystkie wypożyczenia danego klienta
    public List<Wypozyczenie> znajdzWypozyczeniaPoKliencie(Klient klient) {
        return wypozyczenia.stream()
                .filter(w -> w.getKlient().equals(klient))
                .collect(Collectors.toList());
    }

    // Znajdź wypożyczenie po rowerze i kliencie
    public Optional<Wypozyczenie> znajdzWypozyczenie(Rower rower, Klient klient) {
        return wypozyczenia.stream()
                .filter(w -> w.getRower().equals(rower) && w.getKlient().equals(klient))
                .findFirst();
    }

    // Aktualizuj wypożyczenie
    public boolean aktualizujWypozyczenie(Rower rower, Klient klient, Wypozyczenie noweWypozyczenie) {
        Optional<Wypozyczenie> optWypozyczenie = znajdzWypozyczenie(rower, klient);
        if (optWypozyczenie.isPresent()) {
            Wypozyczenie w = optWypozyczenie.get();
            w.setDataOd(noweWypozyczenie.getDataOd());
            w.setDataDo(noweWypozyczenie.getDataDo());
            w.setStatus(noweWypozyczenie.getStatus());
            w.setUwagi(noweWypozyczenie.getUwagi());
            return true;
        }
        return false;
    }

    // Usuń wypożyczenie po rowerze i kliencie
    public boolean usunWypozyczenie(Rower rower, Klient klient) {
        return wypozyczenia.removeIf(w -> w.getRower().equals(rower) && w.getKlient().equals(klient));
    }

    // Pobierz wypożyczenia aktualnie aktywne
    public List<Wypozyczenie> pobierzAktywneWypozyczenia() {
        LocalDateTime teraz = LocalDateTime.now();
        return wypozyczenia.stream()
                .filter(w -> w.getDataDo().isAfter(teraz))
                .collect(Collectors.toList());
    }

    // Pobierz wypożyczenia spóźnione (dataDo przed teraz, status "nie zwrócono")
    public List<Wypozyczenie> pobierzWypozyczeniaSpóźnione() {
        LocalDateTime teraz = LocalDateTime.now();
        return wypozyczenia.stream()
                .filter(w -> w.getDataDo().isBefore(teraz) && !"zwrocono".equalsIgnoreCase(w.getStatus()))
                .collect(Collectors.toList());
    }

    // Pobierz dostępne rowery (te, które nie są wypożyczone)
    public List<Rower> pobierzDostepneRowery(List<Rower> wszystkieRowery) {
        List<Rower> wypozyczone = wypozyczenia.stream()
                .filter(w -> !"zwrocono".equalsIgnoreCase(w.getStatus()))
                .map(Wypozyczenie::getRower)
                .collect(Collectors.toList());

        return wszystkieRowery.stream()
                .filter(r -> !wypozyczone.contains(r))
                .collect(Collectors.toList());
    }
}
