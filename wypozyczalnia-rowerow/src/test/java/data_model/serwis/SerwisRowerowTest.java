package data_model.serwis;

import data_model.model.Rower;
import data_model.model.TypRoweru;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class SerwisRowerowTest {

    private SerwisRowerow serwis;

    private TypRoweru gorski;
    private TypRoweru miejski;
    private TypRoweru szosowy;

    @BeforeEach
    void setUp() {
        serwis = new SerwisRowerow();
        gorski = new TypRoweru("Górski", "Rower do jazdy w górach");
        miejski = new TypRoweru("Miejski", "Rower miejski");
        szosowy = new TypRoweru("Szosowy", "Rower szosowy");
    }

    /**
     * Test dodania nowego roweru.
     */
    @Test
    void dodajRower_dodajePoprawnie() {
        Rower rower = new Rower(gorski, "Mountain", "XX666", 29, "Opis", "SN001");
        serwis.dodajRower(rower);

        List<Rower> rowery = serwis.pobierzWszystkieRowery();
        assertEquals(1, rowery.size(), "dodajRower_dodajePoprawnie: liczba rowerów jest poprawna");
        assertEquals("Mountain", rowery.get(0).getMarka(), "dodajRower_dodajePoprawnie: marka roweru jest poprawna");
    }

    /**
     * Test pobierania wszystkich rowerów.
     */
    @Test
    void pobierzWszystkieRowery_zwrociListe() {
        serwis.dodajRower(new Rower(miejski, "DrogiRower", "City", 28, "", "SN002"));
        serwis.dodajRower(new Rower(szosowy, "TaniRower", "Country", 28, "", "SN003"));

        List<Rower> rowery = serwis.pobierzWszystkieRowery();
        assertEquals(2, rowery.size(), "pobierzWszystkieRowery_zwrociListe: liczba rowerów jest poprawna");
    }

    /**
     * Test wyszukiwania roweru po marce i modelu.
     */
    @Test
    void znajdzRower_istnieje() {
        Rower rower = new Rower(gorski, "Składak", "Męski", 27, "", "SN004");
        serwis.dodajRower(rower);

        Optional<Rower> znaleziony = serwis.znajdzRower("składak", "męski");
        assertTrue(znaleziony.isPresent(), "znajdzRower_istnieje: rower znaleziony");
    }

    /**
     * Test usuwania roweru.
     */
    @Test
    void usunRower_poObiekcie() {
        Rower rower = new Rower(miejski, "Damka", "Girl", 28, "", "SN005");
        serwis.dodajRower(rower);

        boolean usunieto = serwis.usunRower(rower);
        assertTrue(usunieto, "usunRower_poObiekcie: usunięcie roweru powiodło się");
        assertTrue(serwis.pobierzWszystkieRowery().isEmpty(), "usunRower_poObiekcie: lista rowerów jest pusta");
    }

    /**
     * Test usuwania roweru przez markę i model.
     */
    @Test
    void usunRower_poMarceIModelu() {
        serwis.dodajRower(new Rower(miejski, "Góral", "Pro", 28, "", "SN006"));

        boolean usunieto = serwis.usunRower("góral", "pro");
        assertTrue(usunieto, "usunRower_poMarceIModelu: usunięcie roweru powiodło się");
        assertTrue(serwis.pobierzWszystkieRowery().isEmpty(), "usunRower_poMarceIModelu: lista rowerów jest pusta");
    }

    /**
     * Test filtrowania roweru po typie.
     */
    @Test
    void pobierzRoweryPoTypie() {
        serwis.dodajRower(new Rower(szosowy, "City", "Hardcore", 28, "", "SN007"));
        serwis.dodajRower(new Rower(gorski, "SmallCity", "Light", 29, "", "SN008"));

        List<Rower> szosowe = serwis.pobierzRoweryPoTypie(szosowy);
        assertEquals(1, szosowe.size(), "pobierzRoweryPoTypie: liczba rowerów szosowych jest poprawna");
        assertEquals("City", szosowe.get(0).getMarka(), "pobierzRoweryPoTypie: marka roweru jest poprawna");
    }

    /**
     * Test czyszczenia listy rowerów.
     */
    @Test
    void wyczysc_czysciListe() {
        serwis.dodajRower(new Rower(gorski, "Damka", "Classic", 27, "", "SN009"));
        serwis.wyczysc();

        assertTrue(serwis.pobierzWszystkieRowery().isEmpty(), "wyczysc_czysciListe: lista rowerów jest pusta po wyczyszczeniu");
    }
    
    /**
     * Test sprawdzania duplikatu numeru seryjnego
     */
    @Test
    void dodajRower_duplikatNumeruSeryjnego_zwracaFalse() {
        Rower rower1 = new Rower(gorski, "Rower1", "Model1", 27, "", "BA010");
        Rower rower2 = new Rower(miejski, "Rower2", "Model2", 28, "", "BA010"); // ten sam numer seryjny

        assertTrue(serwis.dodajRower(rower1));
        assertFalse(serwis.dodajRower(rower2), "Nie powinno dodawać roweru z duplikatem numeru seryjnego");
        assertEquals(1, serwis.pobierzWszystkieRowery().size());
    }

    /**
     * Test sprawdzania czy numer seryjny istnieje, zwraca true jeśli tak
     */
    @Test
    void czyNumerSeryjnyIstnieje_zwrociTrueDlaIstniejacegoNumeru() {
        serwis.dodajRower(new Rower(szosowy, "Rower3", "Model3", 29, "", "SN011"));
        assertTrue(serwis.czyNumerSeryjnyIstnieje("sn011"));
    }


    /**
     * Test sprawdzania czy numer seryjny istnieje, zwraca false jeśli nie
     */
    
    @Test
    void czyNumerSeryjnyIstnieje_zwrociFalseDlaBrakuNumeru() {
        assertFalse(serwis.czyNumerSeryjnyIstnieje("SN999"));
    }

    /**
     * Test sprawdzania czy rower nieistnieje
     */
    @Test
    void znajdzRower_nieIstnieje() {
        Optional<Rower> wynik = serwis.znajdzRower("NieMaTakiego", "Modelu");
        assertTrue(wynik.isEmpty(), "Rower nie powinien zostać znaleziony");
    }

    /**
     * Test sprawdzania aktualizacji roweru
     */
    @Test
    void aktualizujRower_istniejacyRower_zostajeZmieniony() {
        Rower stary = new Rower(miejski, "StaraMarka", "StaryModel", 26, "Opis", "BA012");
        serwis.dodajRower(stary);

        Rower nowy = new Rower(gorski, "NowaMarka", "NowyModel", 29, "Nowy Opis", "BA013");
        boolean wynik = serwis.aktualizujRower("StaraMarka", "StaryModel", nowy);

        assertTrue(wynik);
        Optional<Rower> zmieniony = serwis.znajdzRower("NowaMarka", "NowyModel");
        assertTrue(zmieniony.isPresent());
        assertEquals("BA013", zmieniony.get().getNumerSeryjny());
        assertEquals(gorski, zmieniony.get().getTyp());
    }


}
