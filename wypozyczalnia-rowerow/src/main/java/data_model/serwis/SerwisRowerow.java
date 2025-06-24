package data_model.serwis;

import data_model.model.Rower;
import data_model.model.TypRoweru;

import java.util.*;
import java.util.stream.*;

/**
 * Serwis do zarządzania rowerami w systemie wypożyczalni rowerów.
 * Klasa zapewnia funkcjonalności CRUD (Create, Read, Update, Delete) dla rowerów,
 * w tym dodawanie, wyszukiwanie, aktualizację i usuwanie rowerów z systemu.
 * Każdy rower jest identyfikowany przez unikalny numer seryjny z automatyczną
 * walidacją duplikatów.
 * 
 * @see Rower
 * @see TypRoweru
 */
public class SerwisRowerow {

    /** Lista wszystkich rowerów zarejestrowanych w systemie wypożyczalni */
    private final List<Rower> rowery = new ArrayList<>();

    /**
     * Dodaje nowy rower do systemu z walidacją unikalności numeru seryjnego.
     * Metoda sprawdza czy rower o podanym numerze seryjnym już istnieje w systemie
     * i dodaje go tylko jeśli numer jest unikalny. Zapobiega to duplikowaniu
     * rowerów w systemie wypożyczalni.
     *
     * @param rower obiekt roweru do dodania do systemu
     * @return {@code true} jeśli rower został pomyślnie dodany,
     *         {@code false} jeśli rower o tym numerze seryjnym już istnieje
     * 
     * @see #czyNumerSeryjnyIstnieje(String)
     * @see Rower#getNumerSeryjny()
     */
    public boolean dodajRower(Rower rower) {
        if (czyNumerSeryjnyIstnieje(rower.getNumerSeryjny())) {
            return false; // Nie dodajemy duplikatu
        }
        rowery.add(rower);
        return true;
    }

    /**
     * Sprawdza, czy w systemie istnieje rower o podanym numerze seryjnym.
     * Metoda wykonuje szybkie sprawdzenie istnienia roweru na podstawie numeru seryjnego
     * bez konieczności pobierania całego obiektu. Porównanie odbywa się bez
     * uwzględniania wielkości liter.
     *
     * @param numerSeryjny numer seryjny roweru do sprawdzenia (wielkość liter nie ma znaczenia)
     * @return {@code true} jeśli rower o podanym numerze seryjnym istnieje w systemie,
     *         {@code false} jeśli nie znaleziono roweru o tym numerze
     * 
     * @see #dodajRower(Rower)
     * @see Rower#getNumerSeryjny()
     */
    public boolean czyNumerSeryjnyIstnieje(String numerSeryjny) {
        return rowery.stream()
                .anyMatch(r -> r.getNumerSeryjny().equalsIgnoreCase(numerSeryjny));
    }

    /**
     * Pobiera listę wszystkich rowerów zarejestrowanych w systemie.
     * Metoda zwraca kopię defensywną wewnętrznej listy rowerów, co zapobiega
     * nieautoryzowanym modyfikacjom kolekcji z zewnątrz. Zmiany w zwróconej
     * liście nie wpływają na wewnętrzny stan serwisu.
     *
     * @return nowa lista zawierająca kopie wszystkich rowerów w systemie,
     *         lub pusta lista jeśli nie ma zarejestrowanych rowerów
     * 
     * @see #dodajRower(Rower)
     * @see #pobierzRoweryPoTypie(TypRoweru)
     */
    public List<Rower> pobierzWszystkieRowery() {
        return new ArrayList<>(rowery);
    }

    /**
     * Wyszukuje rower w systemie na podstawie marki i modelu.
     * Metoda wykonuje wyszukiwanie bez uwzględniania wielkości liter
     * dla obu kryteriów. Zwraca pierwszy znaleziony rower pasujący
     * do podanych parametrów lub pusty Optional jeśli nie znaleziono dopasowania.
     *
     * @param marka marka roweru do wyszukania (wielkość liter nie ma znaczenia)
     * @param model model roweru do wyszukania (wielkość liter nie ma znaczenia)
     * @return {@code Optional<Rower>} zawierający znaleziony rower,
     *         lub pusty {@code Optional} jeśli rower o podanej marce i modelu nie istnieje
     * 
     * @see #aktualizujRower(String, String, Rower)
     * @see #usunRower(String, String)
     * @see Rower#getMarka()
     * @see Rower#getModel()
     * @see Optional#empty()
     */
    public Optional<Rower> znajdzRower(String marka, String model) {
        return rowery.stream()
                .filter(r -> r.getMarka().equalsIgnoreCase(marka) && r.getModel().equalsIgnoreCase(model))
                .findFirst();
    }

    /**
     * Aktualizuje dane istniejącego roweru w systemie na podstawie marki i modelu.
     * Metoda wyszukuje rower po marce i modelu (bez uwzględniania wielkości liter)
     * i aktualizuje wszystkie jego pola danymi z nowego obiektu roweru, włączając
     * numer seryjny. Jeśli rower o podanej marce i modelu nie istnieje, nie wykonuje żadnych zmian.
     *
     * @param marka marka roweru do zaktualizowania (wielkość liter nie ma znaczenia)
     * @param model model roweru do zaktualizowania (wielkość liter nie ma znaczenia)
     * @param nowyRower obiekt zawierający nowe dane roweru do zastosowania
     * @return {@code true} jeśli rower został znaleziony i zaktualizowany,
     *         {@code false} jeśli rower o podanej marce i modelu nie istnieje
     * 
     * @see #znajdzRower(String, String)
     * @see Rower#setTyp(TypRoweru)
     * @see Rower#setMarka(String)
     * @see Rower#setModel(String)
     * @see Rower#setRozmiarKola(int)
     * @see Rower#setOpis(String)
     * @see Rower#setNumerSeryjny(String)
     */
    public boolean aktualizujRower(String marka, String model, Rower nowyRower) {
        Optional<Rower> optRower = znajdzRower(marka, model);
        if (optRower.isPresent()) {
            Rower r = optRower.get();
            r.setTyp(nowyRower.getTyp());
            r.setMarka(nowyRower.getMarka());
            r.setModel(nowyRower.getModel());
            r.setRozmiarKola(nowyRower.getRozmiarKola());
            r.setOpis(nowyRower.getOpis());
            r.setNumerSeryjny(nowyRower.getNumerSeryjny()); // Można zmienić numer, ale w realnym przypadku warto to ograniczyć
            return true;
        }
        return false;
    }

    /**
     * Usuwa określony rower z systemu wypożyczalni.
     * Metoda wykorzystuje metodę equals() obiektu Rower do identyfikacji
     * roweru do usunięcia na podstawie numeru seryjnego. Usuwa pierwszą
     * znalezioną instancję roweru z listy.
     *
     * @param rower obiekt roweru do usunięcia z systemu
     * @return {@code true} jeśli rower został znaleziony i usunięty,
     *         {@code false} jeśli rower nie istnieje w systemie
     * 
     * @see #usunRower(String, String)
     * @see #dodajRower(Rower)
     * @see List#remove(Object)
     * @see Rower#equals(Object)
     */
    public boolean usunRower(Rower rower) {
        return rowery.remove(rower);
    }

    /**
     * Usuwa rower z systemu na podstawie marki i modelu.
     * Metoda wyszukuje i usuwa pierwszy rower o podanej marce i modelu
     * (bez uwzględniania wielkości liter). Jeśli w systemie znajduje się
     * rower pasujący do podanych kryteriów, zostaje on usunięty z listy.
     *
     * @param marka marka roweru do usunięcia (wielkość liter nie ma znaczenia)
     * @param model model roweru do usunięcia (wielkość liter nie ma znaczenia)
     * @return {@code true} jeśli rower został znaleziony i usunięty,
     *         {@code false} jeśli rower o podanej marce i modelu nie istnieje
     * 
     * @see #znajdzRower(String, String)
     * @see #usunRower(Rower)
     * @see List#removeIf(Predicate)
     * @see Rower#getMarka()
     * @see Rower#getModel()
     */
    public boolean usunRower(String marka, String model) {
        return rowery.removeIf(r -> r.getMarka().equalsIgnoreCase(marka) && r.getModel().equalsIgnoreCase(model));
    }

    /**
     * Pobiera listę wszystkich rowerów określonego typu z systemu.
     * Metoda filtruje rowery na podstawie typu i zwraca nową listę zawierającą
     * tylko te rowery, które pasują do podanego typu. Porównanie typów odbywa się
     * za pomocą metody equals() obiektu TypRoweru.
     *
     * @param typ typ roweru do wyszukania (np. górski, szosowy, miejski)
     * @return nowa lista zawierająca wszystkie rowery podanego typu,
     *         lub pusta lista jeśli nie znaleziono rowerów tego typu
     * 
     * @see #pobierzWszystkieRowery()
     * @see #znajdzRower(String, String)
     * @see TypRoweru#equals(Object)
     * @see Collectors#toList()
     */
    public List<Rower> pobierzRoweryPoTypie(TypRoweru typ) {
        return rowery.stream()
                .filter(r -> r.getTyp().equals(typ))
                .collect(Collectors.toList());
    }

    /**
     * Usuwa wszystkie rowery z systemu wypożyczalni.
     * Metoda czyści całą wewnętrzną listę rowerów, pozostawiając pusty
     * system bez żadnych dostępnych rowerów do wypożyczenia. Operacja jest
     * nieodwracalna - wszystkie dane rowerów zostaną utracone.
     *
     * @see #pobierzWszystkieRowery()
     * @see #usunRower(Rower)
     * @see #usunRower(String, String)
     * @see List#clear()
     */
    public void wyczysc() {
        rowery.clear();
    }
}
