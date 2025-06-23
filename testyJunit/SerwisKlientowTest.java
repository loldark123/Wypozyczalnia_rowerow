package data_model.serwis;

import data_model.model.Klient;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class SerwisKlientowTest {

    private SerwisKlientow serwis;

    @BeforeEach
    void init() {
        serwis = new SerwisKlientow();
    }

    @Test
    void dodanieKlienta_dzialaPoprawnie() {
        Klient klient = new Klient("Adaś", "Miałczyński", "ABC999123", "VIP");
        serwis.dodajKlienta(klient);

        List<Klient> klienci = serwis.pobierzWszystkichKlientow();
        assertEquals(1, klienci.size());
        assertEquals("Adaś", klienci.get(0).getImie());
    }

    @Test
    void dodanieKlienta_duplikat_throwsException() {
        Klient klient = new Klient("Cruella", "Demon", "XYZ789456", "Nowa");
        serwis.dodajKlienta(klient);

        Klient duplikat = new Klient("Napoleon", "Bonaparte", "xyz789456", "Ktoś inny");

        assertThrows(IllegalArgumentException.class, () -> serwis.dodajKlienta(duplikat));
    }

    @Test
    void pobierzWszystkichKlientow_zwracaListeKlientow() {
        serwis.dodajKlienta(new Klient("Juliusz", "Cezar", "AAA111222", ""));
        serwis.dodajKlienta(new Klient("Elvis", "Presley", "BBB333444", ""));

        List<Klient> klienci = serwis.pobierzWszystkichKlientow();
        assertEquals(2, klienci.size());
    }

    @Test
    void znajdzKlientowPoImieniuINazwisku_caseInsensitive() {
        serwis.dodajKlienta(new Klient("Włodzimierz", "Lenin", "CCC555666", ""));
        serwis.dodajKlienta(new Klient("włodzimierz", "lenin", "DDD777888", ""));

        List<Klient> znalezieni = serwis.znajdzKlientowPoImieniuINazwisku("WŁODZIMIERZ", "LENIN");
        assertEquals(2, znalezieni.size());
    }

    @Test
    void znajdzKlientaPoDowodzie_istnieje() {
        Klient klient = new Klient("Marek", "Aureliusz", "EEE999000", "");
        serwis.dodajKlienta(klient);

        Optional<Klient> wynik = serwis.znajdzKlientaPoDowodzie("eee999000");
        assertTrue(wynik.isPresent());
        assertEquals("Marek", wynik.get().getImie());
    }

    @Test
    void znajdzKlientaPoDowodzie_brak() {
        Optional<Klient> wynik = serwis.znajdzKlientaPoDowodzie("ZZZ123456");
        assertTrue(wynik.isEmpty());
    }

    @Test
    void aktualizujKlienta_poprawnieAktualizuje() {
        serwis.dodajKlienta(new Klient("Piotruś", "Pan", "FFF111111", "Opis"));

        Klient nowy = new Klient("Michał", "Wiśniewski", "FFF111111", "Zaktualizowany opis");
        boolean zaktualizowano = serwis.aktualizujKlienta("FFF111111", nowy);

        assertTrue(zaktualizowano);
        Optional<Klient> wynik = serwis.znajdzKlientaPoDowodzie("FFF111111");
        assertTrue(wynik.isPresent());
        assertEquals("Michał", wynik.get().getImie());
        assertEquals("Zaktualizowany opis", wynik.get().getOpis());
    }

    @Test
    void usunKlienta_istniejacyKlient() {
        Klient klient = new Klient("Abraham", "Lincoln", "GGG222333", "");
        serwis.dodajKlienta(klient);

        boolean usunieto = serwis.usunKlienta("ggg222333");
        assertTrue(usunieto);
        assertTrue(serwis.pobierzWszystkichKlientow().isEmpty());
    }

    @Test
    void usunKlienta_nieistniejacyKlient() {
        serwis.dodajKlienta(new Klient("Jacek", "Placek", "HHH444555", ""));
        boolean usunieto = serwis.usunKlienta("XYZ123456");

        assertFalse(usunieto);
        assertEquals(1, serwis.pobierzWszystkichKlientow().size());
    }

    @Test
    void wyczysc_usuwaWszystkichKlientow() {
        serwis.dodajKlienta(new Klient("A", "B", "III666777", ""));
        serwis.dodajKlienta(new Klient("C", "D", "JJJ888999", ""));
        serwis.wyczysc();

        assertTrue(serwis.pobierzWszystkichKlientow().isEmpty());
    }
}
