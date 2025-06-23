package projekt_java;

import data_model.model.*;
import data_model.serwis.SerwisWypozyczen;
import data_model.io.PlikWypozyczenIO;
import data_model.io.PlikZwrotowIO;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ZarzadzanieZwrotamiGUI {
    public static void uruchom(JFrame parent, SerwisWypozyczen serwisWypozyczen, PlikWypozyczenIO wypozyczenieIO) {
        JDialog dialog = new JDialog(parent, "Zarządzanie wypożyczeniami", true);
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Szukaj");
        JButton zwrocBtn = new JButton("Zwróć zaznaczone");
        DefaultListModel<Wypozyczenie> model = new DefaultListModel<>();
        JList<Wypozyczenie> lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        topPanel.add(new JLabel("Szukaj (ID lub klient):"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);

        bottomPanel.add(zwrocBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Ustawienie daty z dzisiajField z GUI głównego
        final LocalDate dzisiaj = LocalDate.now();

        // Załaduj aktywne wypożyczenia
        List<Wypozyczenie> aktywne = serwisWypozyczen.pobierzAktywneWypozyczenia(dzisiaj);
        model.clear();
        aktywne.forEach(model::addElement);

        // Przycisk wyszukiwania
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            List<Wypozyczenie> wyniki = serwisWypozyczen.pobierzAktywneWypozyczenia(dzisiaj).stream()
                    .filter(w -> w.getId().toLowerCase().contains(query)
                            || (w.getKlient().getImie() + " " + w.getKlient().getNazwisko()).toLowerCase().contains(query))
                    .collect(Collectors.toList());
            model.clear();
            wyniki.forEach(model::addElement);
        });

        // Przycisk zwrotu
        zwrocBtn.addActionListener(e -> {
            List<Wypozyczenie> zaznaczone = lista.getSelectedValuesList();

            if (zaznaczone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nie zaznaczono żadnych wypożyczeń.");
                return;
            }

            PlikZwrotowIO zwrotIO = new PlikZwrotowIO();

            for (Wypozyczenie w : zaznaczone) {
                String opis = JOptionPane.showInputDialog(dialog,
                        "Podaj opis zwrotu dla wypożyczenia:\n" + w);

                if (opis == null) {
                    // użytkownik anulował – pomiń
                    continue;
                }

                w.setStatus(StatusWypozyczenia.ZAKONCZONE);
                w.setUwagi(opis);
                zwrotIO.zapiszZwrot(w);
            }

            wypozyczenieIO.zapisz(
                    serwisWypozyczen.pobierzWszystkieWypozyczenia(),
                    KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA
            );

            JOptionPane.showMessageDialog(dialog, "Zwrócono " + zaznaczone.size() + " wypożyczeń.");
            model.clear();
            serwisWypozyczen.pobierzAktywneWypozyczenia(dzisiaj).forEach(model::addElement);
        });

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
}
