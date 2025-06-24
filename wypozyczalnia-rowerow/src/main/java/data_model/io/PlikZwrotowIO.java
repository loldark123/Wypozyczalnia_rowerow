package data_model.io;

import data_model.model.Wypozyczenie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Klasa odpowiedzialna za zapis informacji o zwrotach wypożyczeń do pliku tekstowego.
 * Tworzy log zwrotów w formacie czytelnym dla człowieka, zapisując szczegóły
 * każdego zwróconego wypożyczenia w osobnej linii pliku tekstowego.
 * Służy do prowadzenia historii zwrotów w systemie wypożyczalni rowerów.
 * 
 * @see Wypozyczenie
 * @see DateTimeFormatter
 * @see BufferedWriter
 * @see FileWriter
 */
public class PlikZwrotowIO {
    private static final String SCIEZKA_ZWROTY = "dane/zwroty.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Zapisuje informacje o zwrocie wypożyczenia do pliku tekstowego.
     * Metoda dodaje nową linię do pliku z szczegółami zwróconego wypożyczenia,
     * w tym datą zwrotu, okresem wypożyczenia, danymi roweru i klienta.
     * Plik jest otwierany w trybie dopisywania (append mode).
     *
     * @param wypozyczenie wypożyczenie, którego zwrot ma być zapisany do loga
     * 
     * @see BufferedWriter#BufferedWriter(Writer)
     * @see FileWriter#FileWriter(String, boolean)
     * @see String#format(String, Object...)
     * @see LocalDate#now()
     * @see DateTimeFormatter#format(TemporalAccessor)
     */
    public void zapiszZwrot(Wypozyczenie wypozyczenie) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCIEZKA_ZWROTY, true))) {
            String linia = String.format(
                "Oddany %s Okres %s %s  | Rower: %s | Klient: %s | Uwagi: %s",
                LocalDate.now().format(FORMATTER),
                wypozyczenie.getDataOd().format(FORMATTER),
                wypozyczenie.getDataDo().format(FORMATTER),
                wypozyczenie.getRower().getNumerSeryjny(),
                wypozyczenie.getKlient().getImie() + " " + wypozyczenie.getKlient().getNazwisko(),
                wypozyczenie.getUwagi()
            );
            writer.write(linia);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // można zastąpić loggerem
        }
    }
}
