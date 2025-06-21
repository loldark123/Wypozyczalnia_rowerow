package data_model.serwis;

import data_model.model.Rower;
import data_model.model.TypRoweru;

import java.util.*;
import java.util.stream.*;

/**
 * Serwis do zarządzania rowerami.
 */
public class SerwisRowerow {
    private final List<Rower> rowery = new ArrayList<>();

    // Dodaj nowy rower
    public void dodajRower(Rower rower) {
        rowery.add(rower);
    }

    // Pobierz wszystkie rowery
    public List<Rower> pobierzWszystkieRowery() {
        return new ArrayList<>(rowery);
    }

    // Znajdź rower po marce i modelu
    public Optional<Rower> znajdzRower(String marka, String model) {
        return rowery.stream()
                .filter(r -> r.getMarka().equalsIgnoreCase(marka) && r.getModel().equalsIgnoreCase(model))
                .findFirst();
    }

    // Aktualizuj rower
    public boolean aktualizujRower(String marka, String model, Rower nowyRower) {
        Optional<Rower> optRower = znajdzRower(marka, model);
        if (optRower.isPresent()) {
            Rower r = optRower.get();
            r.setTyp(nowyRower.getTyp());
            r.setMarka(nowyRower.getMarka());
            r.setModel(nowyRower.getModel());
            r.setRozmiarKola(nowyRower.getRozmiarKola());
            r.setOpis(nowyRower.getOpis());
            return true;
        }
        return false;
    }

    // Usuń rower po obiekcie
    public boolean usunRower(Rower rower) {
        return rowery.remove(rower);
    }

    // Usuń rower po marce i modelu
    public boolean usunRower(String marka, String model) {
        return rowery.removeIf(r -> r.getMarka().equalsIgnoreCase(marka) && r.getModel().equalsIgnoreCase(model));
    }

    // Pobierz rowery po typie
    public List<Rower> pobierzRoweryPoTypie(TypRoweru typ) {
        return rowery.stream()
                .filter(r -> r.getTyp().equals(typ))
                .collect(Collectors.toList());
    }

    // Wyczyść wszystkie dane
    public void wyczysc() {
        rowery.clear();
    }
}