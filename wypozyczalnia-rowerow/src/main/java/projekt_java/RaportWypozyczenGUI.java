package projekt_java;

import data_model.io.PlikWypozyczenIO;
import data_model.model.Wypozyczenie;
import data_model.serwis.SerwisWypozyczen;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Graficzny interfejs użytkownika do wyświetlania raportów wypożyczeń w systemie wypożyczalni rowerów.
 * Klasa tworzy okno z raportem tekstowym pokazującym aktywne wypożyczenia i opóźnione zwroty
 * na określoną datę symulacji. Raport jest wyświetlany w czytelnym formacie tabelarycznym
 * z użyciem czcionki monospaced.
 * 
 * @see SerwisWypozyczen
 * @see Wypozyczenie
 * @see SerwisWypozyczen#pobierzAktywneWypozyczenia(LocalDate)
 * @see SerwisWypozyczen#pobierzWypozyczeniaSpoznione(LocalDate)
 */
public class RaportWypozyczenGUI {

    /**
     * Otwiera okno z raportem wypożyczeń na określoną datę symulacji.
     * Metoda tworzy okno zawierające raport tekstowy z dwoma sekcjami:
     * aktywne wypożyczenia i opóźnione zwroty. Dane są pobierane z serwisu
     * wypożyczeń i formatowane w czytelny sposób.
     *
     * @param parent okno nadrzędne, względem którego centrowane jest okno raportu
     * @param dataSymulacji data, na którą generowany jest raport wypożyczeń
     * @param serwis serwis wypożyczeń dostarczający dane do raportu
     * 
     * @see JFrame#setLocationRelativeTo(Component)
     * @see JFrame#DISPOSE_ON_CLOSE
     * @see JTextArea#setEditable(boolean)
     * @see Font#Font(String, int, int)
     * @see JScrollPane
     * @see #formatWypozyczenie(Wypozyczenie)
     */
    public static void otworz(JFrame parent, LocalDate dataSymulacji, SerwisWypozyczen serwis) {
        JFrame frame = new JFrame("Raport wypożyczeń - " + dataSymulacji);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(parent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea raportArea = new JTextArea();
        raportArea.setEditable(false);
        raportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();

        sb.append("=== AKTYWNE WYPOŻYCZENIA ===\n");
        List<Wypozyczenie> aktywne = serwis.pobierzAktywneWypozyczenia(dataSymulacji);
        if (aktywne.isEmpty()) {
            sb.append("Brak aktywnych wypożyczeń.\n");
        } else {
            for (Wypozyczenie w : aktywne) {
                sb.append(formatWypozyczenie(w)).append("\n");
            }
        }

        sb.append("\n=== OPÓŹNIONE ZWROTY ===\n");
        List<Wypozyczenie> spoznione = serwis.pobierzWypozyczeniaSpoznione(dataSymulacji);
        if (spoznione.isEmpty()) {
            sb.append("Brak opóźnionych wypożyczeń.\n");
        } else {
            for (Wypozyczenie w : spoznione) {
                sb.append(formatWypozyczenie(w)).append("\n");
            }
        }

        raportArea.setText(sb.toString());
        JScrollPane scrollPane = new JScrollPane(raportArea);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    private static String formatWypozyczenie(Wypozyczenie w) {
        return String.format(
            "Rower: %-15s | Klient: %-20s | OD: %s | DO: %s | Uwagi: %s",
            w.getRower().getModel(),
            w.getKlient().getImie() + " " + w.getKlient().getNazwisko(),
            w.getDataOd(),
            w.getDataDo(),
            w.getUwagi()
        );
    }
}
