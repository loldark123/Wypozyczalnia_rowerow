package konfiguracja;

/**
 * Klasa zawierająca stałe konfiguracyjne dla ścieżek plików w systemie wypożyczalni rowerów.
 * Centralizuje wszystkie ścieżki do plików danych używanych przez system do persystencji
 * obiektów. Wszystkie stałe są publiczne, statyczne i finalne, co zapewnia globalny
 * dostęp do konfiguracji bez możliwości modyfikacji[7].
 * 
 * @see data_model.io.PlikRowerowIO
 * @see data_model.io.PlikKlientowIO
 * @see data_model.io.PlikWypozyczenIO
 * @see data_model.io.PlikTypowRowerowIO
 */
public class KonfiguracjaPlikow {

    /** Ścieżka do pliku z danymi rowerów (serializacja obiektów) */
    public static final String SCIEZKA_ROWERY = "dane/rowery.ser";

    /** Ścieżka do pliku z danymi klientów (format binarny DataOutputStream) */
    public static final String SCIEZKA_KLIENCI = "dane/klienci.ser";

    /** Ścieżka do pliku z danymi wypożyczeń (serializacja obiektów) */
    public static final String SCIEZKA_WYPOZYCZENIA = "dane/wypozyczenia.ser";

    /** Ścieżka do pliku z danymi typów rowerów (serializacja obiektów) */
    public static final String SCIEZKA_TYPY = "dane/typy.ser";
}