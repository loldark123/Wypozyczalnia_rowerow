package data_model.serwis;

import data_model.model.TypRoweru;
import java.util.*;

/**
 * Serwis do zarządzania typami rowerów w systemie wypożyczalni rowerów.
 * Klasa zapewnia funkcjonalności CRUD (Create, Read, Update, Delete) dla typów rowerów,
 * w tym dodawanie, wyszukiwanie, aktualizację i usuwanie typów z systemu.
 * Typy rowerów służą do kategoryzacji rowerów (np. górski, szosowy, miejski).
 * Każdy typ jest identyfikowany przez unikalną nazwę z automatyczną walidacją duplikatów.
 * 
 * @see TypRoweru
 */
public class SerwisTypRoweru {

    /** Lista wszystkich typów rowerów zarejestrowanych w systemie wypożyczalni */
    private final List<TypRoweru> typyRowerow = new ArrayList<>();

    /**
     * Dodaje nowy typ roweru do systemu z walidacją unikalności nazwy.
     * Metoda sprawdza czy typ o podanej nazwie już istnieje w systemie
     * i dodaje go tylko jeśli nazwa jest unikalna. Zapobiega to duplikowaniu
     * typów rowerów w systemie wypożyczalni.
     *
     * @param typ obiekt typu roweru do dodania do systemu
     * @return {@code true} jeśli typ został pomyślnie dodany,
     *         {@code false} jeśli typ o tej nazwie już istnieje
     * 
     * @see #znajdzTypPoNazwie(String)
     * @see TypRoweru#getNazwa()
     */
    public boolean dodajTypRoweru(TypRoweru typ) {
        for (TypRoweru t : typyRowerow) {
            if (t.getNazwa().equalsIgnoreCase(typ.getNazwa())) {
                return false; // Już istnieje typ o tej nazwie
            }
        }
        typyRowerow.add(typ);
        return true;
    }

    /**
     * Pobiera listę wszystkich typów rowerów zarejestrowanych w systemie.
     * Metoda zwraca kopię defensywną wewnętrznej listy typów rowerów, co zapobiega
     * nieautoryzowanym modyfikacjom kolekcji z zewnątrz. Zmiany w zwróconej
     * liście nie wpływają na wewnętrzny stan serwisu.
     *
     * @return nowa lista zawierająca kopie wszystkich typów rowerów w systemie,
     *         lub pusta lista jeśli nie ma zarejestrowanych typów
     * 
     * @see #dodajTypRoweru(TypRoweru)
     * @see #znajdzTypPoNazwie(String)
     * @see TypRoweru
     */
    public List<TypRoweru> pobierzWszystkieTypy() {
        return new ArrayList<>(typyRowerow);
    }

    /**
     * Wyszukuje typ roweru w systemie na podstawie nazwy.
     * Metoda wykonuje wyszukiwanie bez uwzględniania wielkości liter,
     * co zapewnia elastyczność przy wprowadzaniu danych. Zwraca pierwszy
     * znaleziony typ roweru pasujący do podanej nazwy lub pusty Optional
     * jeśli nie znaleziono dopasowania.
     *
     * @param nazwa nazwa typu roweru do wyszukania (wielkość liter nie ma znaczenia)
     * @return {@code Optional<TypRoweru>} zawierający znaleziony typ roweru,
     *         lub pusty {@code Optional} jeśli typ o podanej nazwie nie istnieje
     * 
     * @see #dodajTypRoweru(TypRoweru)
     * @see #aktualizujTypRoweru(String, TypRoweru)
     * @see TypRoweru#getNazwa()
     * @see Optional#empty()
     */
    public Optional<TypRoweru> znajdzTypPoNazwie(String nazwa) {
        return typyRowerow.stream()
                .filter(t -> t.getNazwa().equalsIgnoreCase(nazwa))
                .findFirst();
    }

    /**
     * Aktualizuje dane istniejącego typu roweru w systemie na podstawie nazwy.
     * Metoda wyszukuje typ roweru po nazwie (bez uwzględniania wielkości liter)
     * i aktualizuje wszystkie jego pola danymi z nowego obiektu typu roweru.
     * Jeśli typ roweru o podanej nazwie nie istnieje, nie wykonuje żadnych zmian.
     *
     * @param staraNazwa nazwa typu roweru do zaktualizowania (wielkość liter nie ma znaczenia)
     * @param nowyTyp obiekt zawierający nowe dane typu roweru do zastosowania
     * @return {@code true} jeśli typ roweru został znaleziony i zaktualizowany,
     *         {@code false} jeśli typ roweru o podanej nazwie nie istnieje
     * 
     * @see #znajdzTypPoNazwie(String)
     * @see TypRoweru#setNazwa(String)
     * @see TypRoweru#setOpis(String)
     */
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

    /**
     * Usuwa typ roweru z systemu na podstawie nazwy.
     * Metoda wyszukuje i usuwa typ roweru o podanej nazwie (bez uwzględniania
     * wielkości liter). Jeśli w systemie znajduje się typ roweru pasujący
     * do podanej nazwy, zostaje on usunięty z listy.
     *
     * @param nazwa nazwa typu roweru do usunięcia (wielkość liter nie ma znaczenia)
     * @return {@code true} jeśli typ roweru został znaleziony i usunięty,
     *         {@code false} jeśli typ roweru o podanej nazwie nie istnieje
     * 
     * @see #znajdzTypPoNazwie(String)
     * @see #usunTyp(TypRoweru)
     * @see List#removeIf(Predicate)
     * @see TypRoweru#getNazwa()
     */
    public boolean usunTypPoNazwie(String nazwa) {
        return typyRowerow.removeIf(t -> t.getNazwa().equalsIgnoreCase(nazwa));
    }

    /**
     * Usuwa określony typ roweru z systemu wypożyczalni.
     * Metoda wykorzystuje metodę equals() obiektu TypRoweru do identyfikacji
     * typu do usunięcia. Usuwa pierwszą znalezioną instancję typu z listy.
     * Usunięcie typu może wpłynąć na istniejące obiekty Rower, które używają
     * tego typu do kategoryzacji.
     *
     * @param typ obiekt typu roweru do usunięcia z systemu
     * @return {@code true} jeśli typ roweru został znaleziony i usunięty,
     *         {@code false} jeśli typ nie istnieje w systemie
     * 
     * @see #dodajTypRoweru(TypRoweru)
     * @see #usunTypPoNazwie(String)
     * @see List#remove(Object)
     * @see TypRoweru#equals(Object)
     */
    public boolean usunTyp(TypRoweru typ) {
        return typyRowerow.remove(typ);
    }
}
