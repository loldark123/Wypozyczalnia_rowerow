package data_model.serwis;

import data_model.model.Klient;

import java.util.*;
import java.util.stream.*;

/**
 * Serwis biznesowy do zarządzania klientami w systemie wypożyczalni.
 * Implementuje wzorzec Service Layer, enkapsulując logikę biznesową
 * związaną z operacjami CRUD na klientach.
 */
public class SerwisKlientow {

    // Przechowuje wszystkich klientów w pamięci
    private final List<Klient> klienci = new ArrayList<>();

    /**
     * Dodaje nowego klienta z walidacją unikalności numeru dowodu.
     * Zapobiega duplikatom poprzez sprawdzenie istniejących klientów.
     * 
     * @param klient Nowy klient do dodania
     * @throws IllegalArgumentException gdy klient z takim numerem dowodu już istnieje
     */
    public void dodajKlienta(Klient klient) {
        Optional<Klient> istnieje = znajdzKlientaPoDowodzie(klient.getNumerDowodu());
        if (istnieje.isPresent()) {
            throw new IllegalArgumentException("Klient z takim numerem dowodu już istnieje.");
        }
        klienci.add(klient);
    }

    /**
     * Pobiera listę wszystkich klientów zarejestrowanych w systemie.
     * Metoda zwraca kopię defensywną wewnętrznej listy klientów, co zapobiega
     * nieautoryzowanym modyfikacjom kolekcji z zewnątrz. Zmiany w zwróconej
     * liście nie wpływają na wewnętrzny stan serwisu.
     * 
     * @return nowa lista zawierająca kopie wszystkich klientów w systemie, 
     *         lub pusta lista, jeśli nie ma żadnych klientów.
     */
    public List<Klient> pobierzWszystkichKlientow() {
        return new ArrayList<>(klienci);
    }

    /**
 * Wyszukuje klientów w systemie na podstawie imienia i nazwiska.
 * Metoda filtruje klientów zwracając tylko tych, którzy pasują do podanych
 * kryteriów. Porównanie odbywa się bez uwzględniania wielkości liter
 * dla obu pól. Może zwrócić wielu klientów o tym samym imieniu i nazwisku.
 *
 * @param imie imię klienta do wyszukania (wielkość liter nie ma znaczenia)
 * @param nazwisko nazwisko klienta do wyszukania (wielkość liter nie ma znaczenia)
 * @return nowa lista zawierająca wszystkich klientów o podanym imieniu i nazwisku,
 *         lub pusta lista jeśli nie znaleziono pasujących klientów.
 * 
 * @see #znajdzKlientaPoDowodzie(String)
 * @see #pobierzWszystkichKlientow()
 * @see Klient#getImie()
 * @see Klient#getNazwisko()
 * @see Collectors#toList()
 */
    public List<Klient> znajdzKlientowPoImieniuINazwisku(String imie, String nazwisko) {
        return klienci.stream()
                .filter(k -> k.getImie().equalsIgnoreCase(imie) && k.getNazwisko().equalsIgnoreCase(nazwisko))
                .collect(Collectors.toList());
    }

    /**
     * Znajduje klienta po unikalnym numerze dowodu.
     * Metoda wykonuje wyszukiwanie bez uwzględniania wielkości liter,
     * co zapewnia elastyczność przy wprowadzaniu danych. Zwraca pierwszego
     * znalezionego klienta lub zwraca Optional dla bezpiecznej obsługi przypadku braku wyniku.
     * 
     * @param numerDowodu Numer dowodu do wyszukania w formacie AAA123456
     *                    (wielkość liter nie ma znaczenia)
     * @return {@code Optional<Klient>} zawierający znalezionego klienta,
     *         lub pusty {@code Optional} jeśli nie znaleziono o podanym numerze dowodu.
     */
    public Optional<Klient> znajdzKlientaPoDowodzie(String numerDowodu) {
        return klienci.stream()
                .filter(k -> k.getNumerDowodu().equalsIgnoreCase(numerDowodu))
                .findFirst();
    }

    /**
     * Aktualizuje dane istniejącego klienta na podstawie numeru dowodu.
     * Modyfikuje obiekt in-place zachowując referencje.
     * Jeśli klient o podanym numerze dowodu nie istnieje, nie wykonuje żadnych zmian.
     * 
     * @param numerDowodu Numer dowodu klienta do aktualizacji
     * @param nowyKlient Obiekt z nowymi danymi
     * @return {@code true} jeśli klient został znaleziony i zaktualizowany,
     *         {@code false} jeśli klient o podanym numerze dowodu nie istnieje
     */
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

    /**
     * Usuwa klienta z systemu na podstawie numeru dowodu.
     * Wykorzystuje removeIf dla funkcyjnego usuwania.
     * 
     * @param numerDowodu Numer dowodu klienta do usunięcia
     * @return {@code true} jeśli klient został znaleziony i usunięty,
     *         {@code false} jeśli klient o podanym numerze dowodu nie istnieje
     */
    public boolean usunKlienta(String numerDowodu) {
        return klienci.removeIf(k -> k.getNumerDowodu().equalsIgnoreCase(numerDowodu));
    }
    
    /**
     * Usuwa wszystkich klientów z systemu.
     * Metoda czyści całą wewnętrzną listę klientów, pozostawiając pusty
     * system bez żadnych zarejestrowanych użytkowników. Operacja jest
     * nieodwracalna - wszystkie dane klientów zostaną utracone.
     * Przydatne do resetowania danych testowych.
     */
    public void wyczysc() {
        klienci.clear();
    }
    
    
    
    
    
}
