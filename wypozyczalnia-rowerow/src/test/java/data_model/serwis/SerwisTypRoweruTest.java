package data_model.serwis;

import data_model.model.TypRoweru;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class SerwisTypRoweruTest {

    private SerwisTypRoweru serwis;

    @BeforeEach
    void setUp() {
        serwis = new SerwisTypRoweru();
    }

    /**
     * Test na dodawanie nowego typu roweru.
     */
    @Test
    void dodajTypRoweru_dodajePoprawnie() {
        TypRoweru typ = new TypRoweru("Górski", "Rower terenowy");
        serwis.dodajTypRoweru(typ);

        List<TypRoweru> typy = serwis.pobierzWszystkieTypy();
        assertEquals(1, typy.size(), "dodajTypRoweru_dodajePoprawnie: liczba typów jest poprawna");
        assertEquals("Górski", typy.get(0).getNazwa(), "dodajTypRoweru_dodajePoprawnie: nazwa typu jest poprawna");
    }

    /**
     * Test pobierania wszystkich typów rowerów.
     */
    @Test
    void pobierzWszystkieTypy_zwrociListe() {
        serwis.dodajTypRoweru(new TypRoweru("Miejski", "Rower do miasta"));
        serwis.dodajTypRoweru(new TypRoweru("Szosowy", "Rower szosowy"));

        List<TypRoweru> typy = serwis.pobierzWszystkieTypy();
        assertEquals(2, typy.size(), "pobierzWszystkieTypy_zwrociListe: liczba typów jest poprawna");
    }

    /**
     * Test na wyszukiwanie typu roweru po nazwie.
     */
    @Test
    void znajdzTypPoNazwie_istnieje() {
        TypRoweru typ = new TypRoweru("Damka", "Typ mieszany");
        serwis.dodajTypRoweru(typ);

        Optional<TypRoweru> wynik = serwis.znajdzTypPoNazwie("damka");
        assertTrue(wynik.isPresent(), "znajdzTypPoNazwie_istnieje: typ znaleziony");
        assertEquals("Damka", wynik.get().getNazwa(), "znajdzTypPoNazwie_istnieje: nazwa typu jest poprawna");
    }

    /**
     * Test na wyszukiwanie nieistniejącego typu roweru.
     */
    @Test
    void znajdzTypPoNazwie_nieIstnieje() {
        Optional<TypRoweru> wynik = serwis.znajdzTypPoNazwie("NieIstnieje");
        assertTrue(wynik.isEmpty(), "znajdzTypPoNazwie_nieIstnieje: typ nie znaleziony");
    }

    /**
     * Test na usuwanie typu roweru po nazwie.
     */
    @Test
    void usunTypPoNazwie_usuwa() {
        TypRoweru typ = new TypRoweru("Szosowy", "Rower szosowy");
        serwis.dodajTypRoweru(typ);

        boolean usunieto = serwis.usunTypPoNazwie("szosowy");
        assertTrue(usunieto, "usunTypPoNazwie_usuwa: usunięcie typu powiodło się");
        assertTrue(serwis.pobierzWszystkieTypy().isEmpty(), "usunTypPoNazwie_usuwa: lista typów jest pusta");
    }

    /**
     * Test na usuwanie typu roweru po obiekcie.
     */
    @Test
    void usunTyp_poObiekcie() {
        TypRoweru typ = new TypRoweru("Miejski", "Rower miejski");
        serwis.dodajTypRoweru(typ);

        boolean usunieto = serwis.usunTyp(typ);
        assertTrue(usunieto, "usunTyp_poObiekcie: usunięcie typu powiodło się");
        assertTrue(serwis.pobierzWszystkieTypy().isEmpty(), "usunTyp_poObiekcie: lista typów jest pusta");
    }
}
