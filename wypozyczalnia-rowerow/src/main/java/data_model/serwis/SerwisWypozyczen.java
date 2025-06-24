package data_model.serwis;

import data_model.model.Wypozyczenie;
import data_model.model.Rower;
import data_model.model.Klient;
import data_model.model.StatusWypozyczenia;
import data_model.model.TypRoweru;
import util.ThreadPoolManager;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


/**
 * Serwis do zarządzania wypożyczeniami w systemie wypożyczalni rowerów.
 * Klasa zapewnia funkcjonalności CRUD (Create, Read, Update, Delete) dla wypożyczeń,
 * w tym dodawanie, wyszukiwanie, aktualizację i usuwanie wypożyczeń z systemu.
 * Każde wypożyczenie łączy klienta z rowerem na określony okres czasu i ma
 * przypisany status (aktywne, zakończone, spóźnienie). Oferuje również
 * asynchroniczne operacje dla czasochłonnych zadań.
 * 
 * @see Wypozyczenie
 * @see Klient
 * @see Rower
 * @see StatusWypozyczenia
 * @see TypRoweru
 */
public class SerwisWypozyczen {

    /** Lista wszystkich wypożyczeń zarejestrowanych w systemie wypożyczalni */
    private final List<Wypozyczenie> wypozyczenia = new ArrayList<>();

    /**
     * Dodaje nowe wypożyczenie do systemu wypożyczalni rowerów.
     * Metoda dodaje wypożyczenie do listy aktywnych wypożyczeń w systemie.
     * Wypożyczenie łączy klienta z rowerem na określony okres czasu i zawiera
     * wszystkie niezbędne informacje o transakcji (daty, status, uwagi).
     *
     * @param wypozyczenie obiekt wypożyczenia do dodania do systemu
     * 
     * @see #znajdzWypozyczeniePoId(String)
     * @see #pobierzWszystkieWypozyczenia()
     * @see Wypozyczenie#generujLosoweId()
     * @see StatusWypozyczenia
     */
    public void dodajWypozyczenie(Wypozyczenie wypozyczenie) {
        wypozyczenia.add(wypozyczenie);
    }

    /**
     * Asynchronicznie pobiera listę rowerów dostępnych do wypożyczenia na określoną datę.
     * Metoda wykonuje operację w tle za pomocą ThreadPoolManager, co pozwala na
     * nieblokujące wykonanie czasochłonnych operacji filtrowania dużych list rowerów.
     *
     * @param wszystkieRowery lista wszystkich rowerów w systemie do sprawdzenia
     * @param dataSymulacji data, na którą sprawdzana jest dostępność rowerów
     * @return {@code Future<List<Rower>>} reprezentujący asynchroniczne zadanie
     *         zwracające listę dostępnych rowerów
     * 
     * @see #pobierzDostepneRowery(List, LocalDate)
     * @see ThreadPoolManager#getExecutor()
     * @see Future
     */
    public Future<List<Rower>> pobierzDostepneRoweryAsync(List<Rower> wszystkieRowery, LocalDate dataSymulacji) {
        return ThreadPoolManager.getExecutor().submit(() -> pobierzDostepneRowery(wszystkieRowery, dataSymulacji));
    }

    /**
     * Asynchronicznie pobiera listę rowerów dostępnych w określonym zakresie dat.
     * Metoda wykonuje operację w tle za pomocą ThreadPoolManager, co pozwala na
     * nieblokujące wykonanie czasochłonnych operacji filtrowania.
     *
     * @param dataOd data rozpoczęcia sprawdzanego okresu dostępności
     * @param dataDo data zakończenia sprawdzanego okresu dostępności
     * @param wszystkieRowery lista wszystkich rowerów w systemie do sprawdzenia
     * @return {@code Future<List<Rower>>} reprezentujący asynchroniczne zadanie
     *         zwracające listę dostępnych rowerów w zakresie dat
     * 
     * @see #pobierzDostepneRoweryWZakresie(LocalDate, LocalDate, List)
     * @see ThreadPoolManager#getExecutor()
     * @see Future
     */
    public Future<List<Rower>> pobierzDostepneRoweryWZakresieAsync(LocalDate dataOd, LocalDate dataDo, List<Rower> wszystkieRowery) {
        return ThreadPoolManager.getExecutor().submit(() -> pobierzDostepneRoweryWZakresie(dataOd, dataDo, wszystkieRowery));
    }

    /**
     * Asynchronicznie pobiera listę aktywnych wypożyczeń na bieżącą datę.
     * Metoda wykonuje operację w tle za pomocą ThreadPoolManager, co pozwala na
     * nieblokujące wykonanie operacji filtrowania wypożyczeń.
     *
     * @return {@code Future<List<Wypozyczenie>>} reprezentujący asynchroniczne zadanie
     *         zwracające listę aktywnych wypożyczeń
     * 
     * @see #pobierzAktywneWypozyczenia()
     * @see ThreadPoolManager#getExecutor()
     * @see Future
     */
    public Future<List<Wypozyczenie>> pobierzAktywneWypozyczeniaAsync() {
        return ThreadPoolManager.getExecutor().submit(() -> pobierzAktywneWypozyczenia());
    }

    /**
     * Asynchronicznie wyszukuje wszystkie wypożyczenia przypisane do określonego klienta.
     * Metoda wykonuje operację w tle za pomocą ThreadPoolManager, co pozwala na
     * nieblokujące wykonanie operacji filtrowania dużych list wypożyczeń.
     *
     * @param klient obiekt klienta, dla którego wyszukiwane są wypożyczenia
     * @return {@code Future<List<Wypozyczenie>>} reprezentujący asynchroniczne zadanie
     *         zwracające listę wypożyczeń klienta
     * 
     * @see #znajdzWypozyczeniaPoKliencie(Klient)
     * @see ThreadPoolManager#getExecutor()
     * @see Future
     */
    public Future<List<Wypozyczenie>> znajdzWypozyczeniaPoKliencieAsync(Klient klient) {
        return ThreadPoolManager.getExecutor().submit(() -> znajdzWypozyczeniaPoKliencie(klient));
    }

    /**
     * Pobiera listę wszystkich wypożyczeń zarejestrowanych w systemie.
     * Metoda zwraca kopię defensywną wewnętrznej listy wypożyczeń, co zapobiega
     * nieautoryzowanym modyfikacjom kolekcji z zewnątrz. Zmiany w zwróconej
     * liście nie wpływają na wewnętrzny stan serwisu.
     *
     * @return nowa lista zawierająca kopie wszystkich wypożyczeń w systemie,
     *         lub pusta lista jeśli nie ma zarejestrowanych wypożyczeń
     * 
     * @see #dodajWypozyczenie(Wypozyczenie)
     * @see #znajdzWypozyczeniePoId(String)
     * @see Wypozyczenie
     */
    public List<Wypozyczenie> pobierzWszystkieWypozyczenia() {
        return new ArrayList<>(wypozyczenia);
    }

    /**
     * Wyszukuje wszystkie wypożyczenia przypisane do określonego klienta.
     * Metoda filtruje wypożyczenia na podstawie obiektu klienta i zwraca nową listę
     * zawierającą tylko te wypożyczenia, które należą do podanego klienta.
     * Porównanie klientów odbywa się za pomocą metody equals() obiektu Klient.
     *
     * @param klient obiekt klienta, dla którego wyszukiwane są wypożyczenia
     * @return nowa lista zawierająca wszystkie wypożyczenia podanego klienta,
     *         lub pusta lista jeśli klient nie ma żadnych wypożyczeń
     * 
     * @see #pobierzWszystkieWypozyczenia()
     * @see #znajdzWypozyczeniePoId(String)
     * @see Klient#equals(Object)
     * @see Collectors#toList()
     */
    public List<Wypozyczenie> znajdzWypozyczeniaPoKliencie(Klient klient) {
        return wypozyczenia.stream()
                .filter(w -> w.getKlient().equals(klient))
                .collect(Collectors.toList());
    }

    /**
     * Wyszukuje wypożyczenie na podstawie kombinacji roweru i klienta.
     * Metoda filtruje wypożyczenia szukając pierwszego dopasowania, gdzie
     * zarówno rower jak i klient są równe podanym obiektom. Porównanie
     * odbywa się za pomocą metod equals() obiektów Rower i Klient.
     *
     * @param rower obiekt roweru do wyszukania w wypożyczeniach
     * @param klient obiekt klienta do wyszukania w wypożyczeniach
     * @return {@code Optional<Wypozyczenie>} zawierający znalezione wypożyczenie,
     *         lub pusty {@code Optional} jeśli nie znaleziono wypożyczenia
     *         dla podanej kombinacji roweru i klienta
     * 
     * @see #znajdzWypozyczeniaPoKliencie(Klient)
     * @see #znajdzWypozyczeniePoId(String)
     * @see Rower#equals(Object)
     * @see Klient#equals(Object)
     * @see Optional#empty()
     */
    public Optional<Wypozyczenie> znajdzWypozyczenie(Rower rower, Klient klient) {
        return wypozyczenia.stream()
                .filter(w -> w.getRower().equals(rower) && w.getKlient().equals(klient))
                .findFirst();
    }

    /**
     * Wyszukuje wypożyczenie w systemie na podstawie unikalnego identyfikatora.
     * Metoda wykonuje wyszukiwanie wypożyczenia po ID z uwzględnieniem wielkości liter
     * (porównanie dokładne). Zwraca pierwsze znalezione wypożyczenie pasujące
     * do podanego identyfikatora lub pusty Optional jeśli nie znaleziono dopasowania.
     *
     * @param id unikalny identyfikator wypożyczenia do wyszukania
     * @return {@code Optional<Wypozyczenie>} zawierający znalezione wypożyczenie,
     *         lub pusty {@code Optional} jeśli wypożyczenie o podanym ID nie istnieje
     * 
     * @see #znajdzWypozyczeniaPoKliencie(Klient)
     * @see #znajdzWypozyczenie(Rower, Klient)
     * @see Wypozyczenie#getId()
     * @see Wypozyczenie#generujLosoweId()
     * @see Optional#empty()
     */
    public Optional<Wypozyczenie> znajdzWypozyczeniePoId(String id) {
        return wypozyczenia.stream()
                .filter(w -> w.getId().equals(id))
                .findFirst();
    }

    /**
     * Sprawdza, czy w systemie istnieje wypożyczenie o podanym identyfikatorze.
     * Metoda wykonuje szybkie sprawdzenie istnienia wypożyczenia na podstawie ID
     * bez konieczności pobierania całego obiektu. Wykorzystuje mechanizm
     * short-circuit do optymalizacji wydajności.
     *
     * @param id unikalny identyfikator wypożyczenia do sprawdzenia
     * @return {@code true} jeśli wypożyczenie o podanym ID istnieje w systemie,
     *         {@code false} jeśli nie znaleziono wypożyczenia o tym identyfikatorze
     * 
     * @see #znajdzWypozyczeniePoId(String)
     * @see #dodajWypozyczenie(Wypozyczenie)
     * @see Wypozyczenie#getId()
     * @see Wypozyczenie#generujLosoweId()
     */
    public boolean czyIdIstnieje(String id) {
        return wypozyczenia.stream().anyMatch(w -> w.getId().equals(id));
    }

    /**
     * Aktualizuje dane istniejącego wypożyczenia w systemie na podstawie kombinacji roweru i klienta.
     * Metoda wyszukuje wypożyczenie po rowerze i kliencie (wykorzystując metody equals() obu obiektów)
     * i aktualizuje wybrane pola danymi z nowego obiektu wypożyczenia. ID wypożyczenia oraz
     * obiekty roweru i klienta pozostają niezmienione. Jeśli wypożyczenie dla podanej kombinacji
     * nie istnieje, nie wykonuje żadnych zmian.
     *
     * @param rower obiekt roweru używany do identyfikacji wypożyczenia (porównanie po numerze seryjnym)
     * @param klient obiekt klienta używany do identyfikacji wypożyczenia (porównanie po numerze dowodu)
     * @param noweWypozyczenie obiekt zawierający nowe dane wypożyczenia do zastosowania
     * @return {@code true} jeśli wypożyczenie zostało znalezione i zaktualizowane,
     *         {@code false} jeśli wypożyczenie dla podanej kombinacji roweru i klienta nie istnieje
     * 
     * @see #znajdzWypozyczenie(Rower, Klient)
     * @see Wypozyczenie#setDataOd(LocalDate)
     * @see Wypozyczenie#setDataDo(LocalDate)
     * @see Wypozyczenie#setStatus(StatusWypozyczenia)
     * @see Wypozyczenie#setUwagi(String)
     * @see Rower#equals(Object)
     * @see Klient#equals(Object)
     */
    public boolean aktualizujWypozyczenie(Rower rower, Klient klient, Wypozyczenie noweWypozyczenie) {
        Optional<Wypozyczenie> opt = znajdzWypozyczenie(rower, klient);
        if (opt.isPresent()) {
            Wypozyczenie w = opt.get();
            w.setDataOd(noweWypozyczenie.getDataOd());
            w.setDataDo(noweWypozyczenie.getDataDo());
            w.setStatus(noweWypozyczenie.getStatus());
            w.setUwagi(noweWypozyczenie.getUwagi());
            return true;
        }
        return false;
    }

    /**
     * Usuwa wypożyczenie z systemu na podstawie kombinacji roweru i klienta.
     * Metoda wyszukuje i usuwa wypożyczenie pasujące do podanych obiektów roweru
     * i klienta (bez uwzględniania wielkości liter dla numeru dowodu klienta).
     * Porównanie odbywa się za pomocą metod equals() obiektów Rower i Klient.
     *
     * @param rower obiekt roweru używany do identyfikacji wypożyczenia (porównanie po numerze seryjnym)
     * @param klient obiekt klienta używany do identyfikacji wypożyczenia (porównanie po numerze dowodu)
     * @return {@code true} jeśli wypożyczenie zostało znalezione i usunięte,
     *         {@code false} jeśli wypożyczenie dla podanej kombinacji roweru i klienta nie istnieje
     * 
     * @see #znajdzWypozyczenie(Rower, Klient)
     * @see #aktualizujWypozyczenie(Rower, Klient, Wypozyczenie)
     * @see List#removeIf(Predicate)
     * @see Rower#equals(Object)
     * @see Klient#equals(Object)
     */
    public boolean usunWypozyczenie(Rower rower, Klient klient) {
        return wypozyczenia.removeIf(w -> w.getRower().equals(rower) && w.getKlient().equals(klient));
    }

    /**
     * Pobiera listę aktywnych wypożyczeń na określoną datę symulacji.
     * Metoda filtruje wypożyczenia zwracając tylko te, które mają status AKTYWNE
     * i których data rozpoczęcia nie jest późniejsza niż podana data symulacji.
     * Służy do symulacji stanu wypożyczeń w konkretnym momencie czasowym.
     *
     * @param dataSymulacji data, na którą sprawdzane są aktywne wypożyczenia
     * @return nowa lista zawierająca wszystkie aktywne wypożyczenia na podaną datę,
     *         lub pusta lista jeśli nie ma aktywnych wypożyczeń
     * 
     * @see #pobierzWszystkieWypozyczenia()
     * @see #znajdzWypozyczeniaPoKliencie(Klient)
     * @see StatusWypozyczenia#AKTYWNE
     * @see LocalDate#isAfter(ChronoLocalDate)
     * @see Collectors#toList()
     */
    public List<Wypozyczenie> pobierzAktywneWypozyczenia(LocalDate dataSymulacji) {
        return wypozyczenia.stream()
                .filter(w -> w.getStatus() == StatusWypozyczenia.AKTYWNE && !w.getDataOd().isAfter(dataSymulacji))
                .collect(Collectors.toList());
    }

    /**
     * Pobiera listę aktywnych wypożyczeń na bieżącą datę systemową.
     * Metoda jest wygodnym skrótem do {@code pobierzAktywneWypozyczenia(LocalDate.now())},
     * zwracając aktywne wypożyczenia na dzisiejszą datę. Filtruje wypożyczenia
     * zwracając tylko te, które mają status AKTYWNE i już się rozpoczęły.
     *
     * @return nowa lista zawierająca wszystkie aktywne wypożyczenia na dzisiejszą datę,
     *         lub pusta lista jeśli nie ma aktywnych wypożyczeń
     * 
     * @see #pobierzAktywneWypozyczenia(LocalDate)
     * @see LocalDate#now()
     * @see StatusWypozyczenia#AKTYWNE
     */
    public List<Wypozyczenie> pobierzAktywneWypozyczenia() {
        return pobierzAktywneWypozyczenia(LocalDate.now());
    }

    /**
     * Pobiera listę spóźnionych wypożyczeń na określoną datę symulacji.
     * Metoda filtruje wypożyczenia zwracając tylko te, które mają status AKTYWNE
     * i których data zakończenia jest wcześniejsza niż podana data symulacji.
     * Służy do identyfikacji wypożyczeń, które przekroczyły planowany termin zwrotu.
     *
     * @param dataSymulacji data, na którą sprawdzane są spóźnione wypożyczenia
     * @return nowa lista zawierająca wszystkie spóźnione wypożyczenia na podaną datę,
     *         lub pusta lista jeśli nie ma spóźnionych wypożyczeń
     * 
     * @see #pobierzAktywneWypozyczenia(LocalDate)
     * @see #pobierzWszystkieWypozyczenia()
     * @see StatusWypozyczenia#AKTYWNE
     * @see StatusWypozyczenia#SPOZNIENIE
     * @see LocalDate#isBefore(ChronoLocalDate)
     * @see Collectors#toList()
     */
    public List<Wypozyczenie> pobierzWypozyczeniaSpoznione(LocalDate dataSymulacji) {
        return wypozyczenia.stream()
                .filter(w -> w.getStatus() == StatusWypozyczenia.AKTYWNE && w.getDataDo().isBefore(dataSymulacji))
                .collect(Collectors.toList());
    }

    /**
     * Pobiera listę rowerów dostępnych do wypożyczenia na określoną datę symulacji.
     * Metoda filtruje rowery wykluczając te, które są zajęte przez aktywne wypożyczenia
     * w podanej dacie. Rower jest uznawany za zajęty, jeśli istnieje wypożyczenie
     * o statusie innym niż ZAKONCZONE, które obejmuje datę symulacji w swoim okresie.
     *
     * @param wszystkieRowery lista wszystkich rowerów w systemie do sprawdzenia
     * @param dataSymulacji data, na którą sprawdzana jest dostępność rowerów
     * @return nowa lista zawierająca tylko rowery dostępne do wypożyczenia na podaną datę,
     *         lub pusta lista jeśli wszystkie rowery są zajęte
     * 
     * @see #pobierzAktywneWypozyczenia(LocalDate)
     * @see #pobierzWypozyczeniaSpoznione(LocalDate)
     * @see StatusWypozyczenia#ZAKONCZONE
     * @see Rower#equals(Object)
     * @see Collectors#toSet()
     * @see Collectors#toList()
     */
    public List<Rower> pobierzDostepneRowery(List<Rower> wszystkieRowery, LocalDate dataSymulacji) {
        Set<Rower> zajete = wypozyczenia.stream()
                .filter(w -> w.getStatus() != StatusWypozyczenia.ZAKONCZONE &&
                        !(dataSymulacji.isBefore(w.getDataOd()) || dataSymulacji.isAfter(w.getDataDo())))
                .map(Wypozyczenie::getRower)
                .collect(Collectors.toSet());

        return wszystkieRowery.stream()
                .filter(r -> !zajete.contains(r))
                .collect(Collectors.toList());
    }

    /**
     * Pobiera listę rowerów dostępnych do wypożyczenia w określonym zakresie dat.
     * Metoda filtruje rowery wykluczając te, które są zajęte przez aktywne wypożyczenia
     * w podanym przedziale czasowym. Rower jest uznawany za zajęty, jeśli istnieje
     * wypożyczenie o statusie innym niż ZAKONCZONE, którego okres nakłada się
     * z podanym zakresem dat.
     *
     * @param dataOd data rozpoczęcia sprawdzanego okresu dostępności
     * @param dataDo data zakończenia sprawdzanego okresu dostępności
     * @param wszystkieRowery lista wszystkich rowerów w systemie do sprawdzenia
     * @return nowa lista zawierająca tylko rowery dostępne w całym podanym zakresie dat,
     *         lub pusta lista jeśli wszystkie rowery są zajęte w tym okresie
     * 
     * @see #pobierzDostepneRowery(List, LocalDate)
     * @see #pobierzAktywneWypozyczenia(LocalDate)
     * @see StatusWypozyczenia#ZAKONCZONE
     * @see Rower#equals(Object)
     * @see Collectors#toSet()
     * @see Collectors#toList()
     */
    public List<Rower> pobierzDostepneRoweryWZakresie(LocalDate dataOd, LocalDate dataDo, List<Rower> wszystkieRowery) {
        Set<Rower> zajete = wypozyczenia.stream()
                .filter(w -> w.getStatus() != StatusWypozyczenia.ZAKONCZONE &&
                        !(dataDo.isBefore(w.getDataOd()) || dataOd.isAfter(w.getDataDo())))
                .map(Wypozyczenie::getRower)
                .collect(Collectors.toSet());

        return wszystkieRowery.stream()
                .filter(r -> !zajete.contains(r))
                .collect(Collectors.toList());
    }

    /**
     * Kończy wypożyczenie poprzez zmianę jego statusu na ZAKONCZONE.
     * Metoda aktualizuje status wypożyczenia, oznaczając je jako zakończone.
     * Po zakończeniu wypożyczenia rower staje się ponownie dostępny do wypożyczenia
     * przez innych klientów w systemie.
     *
     * @param wyp wypożyczenie do zakończenia
     * 
     * @see #pobierzAktywneWypozyczenia()
     * @see #pobierzDostepneRowery(List, LocalDate)
     * @see StatusWypozyczenia#ZAKONCZONE
     * @see Wypozyczenie#setStatus(StatusWypozyczenia)
     */
    public void zakonczWypozyczenie(Wypozyczenie wyp) {
        wyp.setStatus(StatusWypozyczenia.ZAKONCZONE);
    }

    /**
     * Kończy wypożyczenie dla określonej kombinacji roweru i klienta.
     * Metoda wyszukuje aktywne wypożyczenie na podstawie roweru i klienta,
     * a następnie zmienia jego status na ZAKONCZONE. Wypożyczenie może być
     * zakończone tylko jeśli ma status AKTYWNE. Po zakończeniu rower staje się
     * ponownie dostępny do wypożyczenia przez innych klientów.
     *
     * @param rower obiekt roweru używany do identyfikacji wypożyczenia (porównanie po numerze seryjnym)
     * @param klient obiekt klienta używany do identyfikacji wypożyczenia (porównanie po numerze dowodu)
     * @return {@code true} jeśli wypożyczenie zostało znalezione i zakończone,
     *         {@code false} jeśli wypożyczenie nie istnieje lub nie ma statusu AKTYWNE
     * 
     * @see #znajdzWypozyczenie(Rower, Klient)
     * @see #zakonczWypozyczenie(Wypozyczenie)
     * @see #pobierzAktywneWypozyczenia()
     * @see StatusWypozyczenia#AKTYWNE
     * @see StatusWypozyczenia#ZAKONCZONE
     * @see Rower#equals(Object)
     * @see Klient#equals(Object)
     */
    public boolean zakonczWypozyczenie(Rower rower, Klient klient) {
        Optional<Wypozyczenie> opt = znajdzWypozyczenie(rower, klient);
        if (opt.isPresent() && opt.get().getStatus() == StatusWypozyczenia.AKTYWNE) {
            opt.get().setStatus(StatusWypozyczenia.ZAKONCZONE);
            return true;
        }
        return false;
    }

    /**
     * Sprawdza, czy określony rower jest obecnie wypożyczony.
     * Metoda wykonuje szybkie sprawdzenie czy rower ma aktywne wypożyczenie
     * na podstawie numeru seryjnego i statusu AKTYWNE. Wykorzystuje mechanizm
     * short-circuit do optymalizacji wydajności.
     *
     * @param rower obiekt roweru do sprawdzenia (porównanie po numerze seryjnym)
     * @return {@code true} jeśli rower jest obecnie wypożyczony (ma status AKTYWNE),
     *         {@code false} jeśli rower jest dostępny do wypożyczenia
     * 
     * @see #pobierzDostepneRowery(List, LocalDate)
     * @see StatusWypozyczenia#AKTYWNE
     * @see Rower#equals(Object)
     */
    public boolean czyRowerJestWypozyczony(Rower rower) {
        return wypozyczenia.stream()
                .anyMatch(w -> w.getRower().equals(rower) && w.getStatus() == StatusWypozyczenia.AKTYWNE);
    }

    /**
     * Sprawdza, czy określony typ roweru jest używany przez jakikolwiek rower w systemie.
     * Metoda wykonuje sprawdzenie czy podany typ roweru jest przypisany do
     * któregokolwiek z rowerów w podanej liście. Przydatna do walidacji przed
     * usunięciem typu roweru z systemu.
     *
     * @param typ typ roweru do sprawdzenia
     * @param rowery lista rowerów do przeszukania
     * @return {@code true} jeśli typ jest używany przez co najmniej jeden rower,
     *         {@code false} jeśli typ nie jest używany przez żaden rower
     * 
     * @see TypRoweru#equals(Object)
     * @see Rower#getTyp()
     */
    public boolean czyTypJestWUzyciu(TypRoweru typ, List<Rower> rowery) {
        return rowery.stream().anyMatch(r -> r.getTyp().equals(typ));
    }

    /**
     * Waliduje poprawność zakresu dat wypożyczenia względem bieżącej daty.
     * Metoda sprawdza czy podane daty spełniają podstawowe reguły biznesowe:
     * data rozpoczęcia nie może być w przeszłości względem dzisiejszej daty
     * oraz data zakończenia nie może być wcześniejsza niż data rozpoczęcia.
     *
     * @param dzisiaj bieżąca data systemowa jako punkt odniesienia
     * @param od data rozpoczęcia wypożyczenia do walidacji
     * @param do_ data zakończenia wypożyczenia do walidacji
     * @return {@code true} jeśli zakres dat jest poprawny,
     *         {@code false} jeśli narusza reguły biznesowe
     * 
     * @see LocalDate#isBefore(ChronoLocalDate)
     * @see #dodajWypozyczenie(Wypozyczenie)
     */
    public boolean czyZakresDatJestPoprawny(LocalDate dzisiaj, LocalDate od, LocalDate do_) {
        return !od.isBefore(dzisiaj) && !do_.isBefore(od);
    }
}
