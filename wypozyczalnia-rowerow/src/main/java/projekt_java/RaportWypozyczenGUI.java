package projekt_java;

import data_model.io.PlikWypozyczenIO;
import data_model.model.Wypozyczenie;
import data_model.serwis.SerwisWypozyczen;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RaportWypozyczenGUI {
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
