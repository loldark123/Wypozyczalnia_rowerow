package projekt_java.serwis;

import projekt_java.model.TypRoweru;
import java.util.*;

/**
 * Serwis do zarządzania typami rowerów.
 */
public class SerwisTypRoweru {
    private final List<TypRoweru> typyRowerow = new ArrayList<>();

    // Utwórz nowy typ roweru
    public void dodajTypRoweru(TypRoweru typ) {
        typyRowerow.add(typ);
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
}
