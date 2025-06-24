package data_model.serwis;
import data_model.model.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


class SerwisWypozyczenTest {

    private SerwisWypozyczen serwis;
    private Klient klient1;
    private Rower rower1;
    private Wypozyczenie wypozyczenie1;

    /**
     * Inicjalizacja wspólnych danych testowych przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        serwis = new SerwisWypozyczen();
        klient1 = new Klient("Robin", "Hood", "ABC123456", "Opis klienta");

        TypRoweru typMiejski = new TypRoweru("Miejski", "Rower miejski do jazdy po mieście");
        rower1 = new Rower(typMiejski, "Składak", "City", 28, "Opis roweru", "NUM001");

        wypozyczenie1 = new Wypozyczenie("w1", rower1, klient1,
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(3),
                StatusWypozyczenia.AKTYWNE, "Brak uwag");
    }

    /**
     * Test dodawania wypożyczenia do serwisu.
     */
    @Test
    void dodajWypozyczenie_dodajePoprawnie() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        List<Wypozyczenie> lista = serwis.pobierzWszystkieWypozyczenia();
        assertEquals(1, lista.size(), "Powinien być dokładnie jeden element w liście wypożyczeń");
        assertEquals(wypozyczenie1, lista.get(0), "Dodane wypożyczenie powinno znajdować się na liście");
    }

    /**
     * Test pobierania listy wszystkich wypożyczeń.
     */
    @Test
    void pobierzWszystkieWypozyczenia_zwrociListe() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        List<Wypozyczenie> lista = serwis.pobierzWszystkieWypozyczenia();
        assertFalse(lista.isEmpty(), "Lista wypożyczeń nie powinna być pusta po dodaniu wypożyczenia");
    }

    /**
     * Test znajdowania wypożyczeń po kliencie.
     */
    @Test
    void znajdzWypozyczeniaPoKliencie_znajduje() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        List<Wypozyczenie> wynik = serwis.znajdzWypozyczeniaPoKliencie(klient1);
        assertEquals(1, wynik.size(), "Powinna być jedna wypożyczona pozycja dla tego klienta");
        assertEquals(klient1, wynik.get(0).getKlient(), "Klient powinien się zgadzać");
    }

    /**
     * Test znajdowania wypożyczenia po rowerze i kliencie.
     */
    @Test
    void znajdzWypozyczenie_znajduje() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        Optional<Wypozyczenie> wynik = serwis.znajdzWypozyczenie(rower1, klient1);
        assertTrue(wynik.isPresent(), "Wypożyczenie powinno zostać znalezione");
        assertEquals(wypozyczenie1, wynik.get(), "Zwrócone wypożyczenie powinno się zgadzać");
    }

    /**
     * Test znajdowania wypożyczenia po ID.
     */
    @Test
    void znajdzWypozyczeniePoId_znajduje() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        Optional<Wypozyczenie> wynik = serwis.znajdzWypozyczeniePoId("w1");
        assertTrue(wynik.isPresent(), "Wypożyczenie powinno zostać znalezione po ID");
        assertEquals(wypozyczenie1, wynik.get(), "Znalezione wypożyczenie powinno się zgadzać z oczekiwanym");
    }

    /**
     * Test sprawdzający istnienie ID wypożyczenia w systemie.
     */
    @Test
    void czyIdIstnieje_test() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        assertTrue(serwis.czyIdIstnieje("w1"), "ID 'w1' powinno istnieć");
        assertFalse(serwis.czyIdIstnieje("nieistnieje"), "Nieistniejące ID nie powinno być znalezione");
    }

    /**
     * Test usuwania wypożyczenia po rowerze i kliencie.
     */
    @Test
    void usunWypozyczenie_usuwa() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        boolean wynik = serwis.usunWypozyczenie(rower1, klient1);
        assertTrue(wynik, "Wypożyczenie powinno być poprawnie usunięte");
        assertTrue(serwis.pobierzWszystkieWypozyczenia().isEmpty(), "Lista wypożyczeń powinna być pusta");
    }

    /**
     * Test pobierania wypożyczeń, które są już przeterminowane.
     */
    @Test
    void pobierzWypozyczeniaSpoznione_test() {
        Wypozyczenie spoznione = new Wypozyczenie("w2", rower1, klient1,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(1),
                StatusWypozyczenia.AKTYWNE, "");

        serwis.dodajWypozyczenie(wypozyczenie1);
        serwis.dodajWypozyczenie(spoznione);

        List<Wypozyczenie> spoznioneLista = serwis.pobierzWypozyczeniaSpoznione(LocalDate.now());
        assertEquals(1, spoznioneLista.size(), "Powinna być dokładnie jedna przeterminowana pozycja");
        assertEquals(spoznione, spoznioneLista.get(0), "Znalezione wypożyczenie powinno być tym przetermine");
    }

    /**
     * Test pobierania rowerów dostępnych w danym dniu.
     */
    @Test
    void pobierzDostepneRowery_test() {
        TypRoweru typGorski = new TypRoweru("Górski", "Rower do jazdy terenowej");
        Rower rower2 = new Rower(typGorski, "Skladak", "Tradycyjny", 29, "Inny rower", "NUM002");

        serwis.dodajWypozyczenie(wypozyczenie1);
        List<Rower> wszystkieRowery = List.of(rower1, rower2);

        List<Rower> dostepne = serwis.pobierzDostepneRowery(wszystkieRowery, LocalDate.now());
        assertEquals(1, dostepne.size(), "Powinien być jeden dostępny rower");
        assertTrue(dostepne.contains(rower2), "Dostępny rower powinien być rowerem, który nie został wypożyczony");
    }

    /**
     * Test sprawdzający, pobieranie tylko rowerów dostępnych w danym przedziale dat.
     */
    @Test
    void pobierzDostepneRoweryWZakresie_test() {
        TypRoweru typ = new TypRoweru("Szosa", "Rower szosowy");
        Rower rower2 = new Rower(typ, "Model", "Marka", 28, "", "NUM003");
        serwis.dodajWypozyczenie(wypozyczenie1);

        List<Rower> wszystkie = List.of(rower1, rower2);
        LocalDate od = LocalDate.now();
        LocalDate do_ = LocalDate.now().plusDays(5);

        List<Rower> wynik = serwis.pobierzDostepneRoweryWZakresie(od, do_, wszystkie);
        assertEquals(1, wynik.size());
        assertTrue(wynik.contains(rower2));
    }

    /**
     * Test sprawdzający, czy metoda wykrywa, że rower jest aktualnie wypożyczony.
     */
    @Test
    void czyRowerJestWypozyczony_test() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        assertTrue(serwis.czyRowerJestWypozyczony(rower1));
    }

    /**
     * Test sprawdzający wykrywanie użycia typu roweru.
     */
    @Test
    void czyTypJestWUzyciu_test() {
        TypRoweru typ = rower1.getTyp();
        assertTrue(serwis.czyTypJestWUzyciu(typ, List.of(rower1)));
        assertFalse(serwis.czyTypJestWUzyciu(typ, List.of()));
    }

    /**
     * Test sprawdzający poprawność przedziału dat wypożyczenia.
     */
    @Test
    void czyZakresDatJestPoprawny_test() {
        LocalDate dzisiaj = LocalDate.now();
        assertTrue(serwis.czyZakresDatJestPoprawny(dzisiaj, dzisiaj.plusDays(1), dzisiaj.plusDays(3)));
        assertFalse(serwis.czyZakresDatJestPoprawny(dzisiaj, dzisiaj.minusDays(1), dzisiaj.plusDays(3)));
        assertFalse(serwis.czyZakresDatJestPoprawny(dzisiaj, dzisiaj.plusDays(2), dzisiaj));
    }

    /**
     * Test zakończenia wypożyczenia przez przekazanie obiektu wypożyczenia.
     */
    @Test
    void zakonczWypozyczenie_poObiekcie() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        serwis.zakonczWypozyczenie(wypozyczenie1);
        assertEquals(StatusWypozyczenia.ZAKONCZONE, wypozyczenie1.getStatus());
    }

    /**
     * Test zakończenia wypożyczenia przez podanie roweru i klienta.
     */
    @Test
    void zakonczWypozyczenie_poRowerzeIKliencie() {
        serwis.dodajWypozyczenie(wypozyczenie1);
        boolean wynik = serwis.zakonczWypozyczenie(rower1, klient1);
        assertTrue(wynik);
        assertEquals(StatusWypozyczenia.ZAKONCZONE, wypozyczenie1.getStatus());
    }

    /**
     * Test metody zwracającej aktywne wypożyczenia.
     */
    @Test
    void pobierzAktywneWypozyczeniaAsync_test() throws Exception {
        serwis.dodajWypozyczenie(wypozyczenie1);
        List<Wypozyczenie> wynik = serwis.pobierzAktywneWypozyczeniaAsync().get();
        assertEquals(1, wynik.size());
        assertEquals(StatusWypozyczenia.AKTYWNE, wynik.get(0).getStatus());
    }

}
