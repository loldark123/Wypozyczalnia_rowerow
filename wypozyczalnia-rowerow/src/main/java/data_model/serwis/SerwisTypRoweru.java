package data_model.serwis;

import data_model.model.TypRoweru;
import java.util.*;

/**
 * Serwis do zarządzania typami rowerów.
 */
public class SerwisTypRoweru {
    private final List<TypRoweru> typyRowerow = new ArrayList<>();

    // Utwórz nowy typ roweru
    public boolean dodajTypRoweru(TypRoweru typ) {
        for (TypRoweru t : typyRowerow) {
            if (t.getNazwa().equalsIgnoreCase(typ.getNazwa())) {
                return false; // Już istnieje typ o tej nazwie
            }
        }
        typyRowerow.add(typ);
        return true;
    }

    // Odczytaj wszystkie typy rowerów
    public List<TypRoweru> pobierzWszystkieTypy() {
        return new ArrayList<>(typyRowerow);
    }

    // Znajdź typ roweru po nazwie
    public Optional<TypRoweru> znajdzTypPoNazwie(String nazwa) {
        return typyRowerow.stream()
                .filter(t -> t.getNazwa().equalsIgnoreCase(nazwa))
                .findFirst();
    }

    // Aktualizuj typ roweru
    public boolean aktualizujTypRoweru(String staraNazwa, TypRoweru nowyTyp) {
        Optional<TypRoweru> typOpt = znajdzTypPoNazwie(staraNazwa);
        if (typOpt.isPresent()) {
            TypRoweru typ = typOpt.get();
            typ.setNazwa(nowyTyp.getNazwa());
            typ.setOpis(nowyTyp.getOpis());
            return true;
        }
        return false;
    }

    // Usuń typ roweru po nazwie
    public boolean usunTypPoNazwie(String nazwa) {
        return typyRowerow.removeIf(t -> t.getNazwa().equalsIgnoreCase(nazwa));
    }

    // ✅ Nowa metoda: Usuń typ na podstawie obiektu
    public boolean usunTyp(TypRoweru typ) {
        return typyRowerow.remove(typ);
    }
}
