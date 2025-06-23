package data_model.io;

import data_model.model.Wypozyczenie;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Obsługuje zapis zwróconych wypożyczeń do pliku.
 */
public class PlikZwrotowIO {
    private static final String SCIEZKA_ZWROTY = "dane/zwroty.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
