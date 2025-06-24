package projekt_java;

import data_model.io.PlikTypowRowerowIO;
import data_model.model.Rower;
import data_model.model.TypRoweru;
import data_model.serwis.SerwisTypRoweru;
import data_model.serwis.SerwisWypozyczen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Graficzny interfejs użytkownika do zarządzania typami rowerów w systemie wypożyczalni.
 * Klasa tworzy modalne okno dialogowe umożliwiające pełne zarządzanie typami rowerów:
 * przeglądanie, dodawanie, edycję i usuwanie typów z walidacją biznesową.
 * 
 * @see SerwisTypRoweru
 * @see PlikTypowRowerowIO
 * @see TypRoweru
 * @see SerwisWypozyczen
 */
public class TypyRowerowGUI {

    /**
     * Otwiera modalne okno dialogowe do zarządzania typami rowerów.
     * Tworzy interfejs z listą istniejących typów, formularzem do dodawania nowych typów.
     * Wszystkie zmiany są automatycznie zapisywane do pliku i synchronizowane
     * 
     * @param parent okno nadrzędne, względem którego centrowane jest okno dialogowe;
     *               może być {@code null} dla centrowania względem ekranu
     * @param serwis serwis do obsługi operacji CRUD na typach rowerów;
     *               nie może być {@code null}
     * @param comboBox opcjonalny ComboBox do automatycznej aktualizacji po zmianach;
     *                 może być {@code null} jeśli aktualizacja nie jest wymagana
     * @param io obiekt do obsługi operacji I/O na plikach typów rowerów;
     *           nie może być {@code null}
     * @param rowery lista wszystkich rowerów w systemie używana do walidacji
     *               czy typ może być usunięty; nie może być {@code null}
     * @param wypozyczeniaSerwis serwis wypożyczeń używany do sprawdzania
     *                          czy typ jest w użyciu; może być {@code null}
     *                          (wtedy pomijana jest walidacja użycia)
     * 
     * @throws IllegalArgumentException jeśli którykolwiek z wymaganych parametrów jest {@code null}
     * 
     * @see JDialog#setModal(boolean)
     * @see JDialog#setLocationRelativeTo(Component)
     * @see DefaultListModel
     * @see ListSelectionModel#SINGLE_SELECTION
     * @see SerwisTypRoweru#dodajTypRoweru(TypRoweru)
     * @see SerwisTypRoweru#usunTyp(TypRoweru)
     * @see SerwisTypRoweru#aktualizujTypRoweru(String, TypRoweru)
     * @see SerwisWypozyczen#czyTypJestWUzyciu(TypRoweru, List)
     * @see PlikTypowRowerowIO#zapisz(List, String)
     */
    public static void otworz(JFrame parent, SerwisTypRoweru serwis, JComboBox<TypRoweru> comboBox, PlikTypowRowerowIO io, List<Rower> rowery, SerwisWypozyczen wypozyczeniaSerwis) {
        JDialog dialog = new JDialog(parent, "Typy rowerów", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parent);

        DefaultListModel<TypRoweru> model = new DefaultListModel<>();
        serwis.pobierzWszystkieTypy().forEach(model::addElement);

        JList<TypRoweru> lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel("<html><b>" + value.getNazwa() + "</b> – <i>" + value.getOpis() + "</i></html>");
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            label.setOpaque(true);
            return label;
        });

        JScrollPane scrollPane = new JScrollPane(lista);

        JTextField nazwaField = new JTextField();
        JTextField opisField = new JTextField();
        JButton dodajButton = new JButton("Dodaj typ");
        JButton usunButton = new JButton("Usuń zaznaczony");
        JButton edytujButton = new JButton("Edytuj zaznaczony");

        JPanel dolPanel = new JPanel(new GridLayout(4, 2));
        dolPanel.add(new JLabel("Nazwa:"));
        dolPanel.add(nazwaField);
        dolPanel.add(new JLabel("Opis:"));
        dolPanel.add(opisField);
        dolPanel.add(dodajButton);
        dolPanel.add(usunButton);
        dolPanel.add(edytujButton);

        dodajButton.addActionListener(e -> {
            String nazwa = nazwaField.getText().trim();
            String opis = opisField.getText().trim();

            if (nazwa.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nazwa nie może być pusta.");
                return;
            }

            if (serwis.znajdzTypPoNazwie(nazwa).isPresent()) {
                JOptionPane.showMessageDialog(dialog, "Typ o tej nazwie już istnieje.");
                return;
            }

            TypRoweru nowy = new TypRoweru(nazwa, opis);
            serwis.dodajTypRoweru(nowy);
            model.addElement(nowy);
            if (comboBox != null) comboBox.addItem(nowy);

            try {
                io.zapisz(serwis.pobierzWszystkieTypy(), konfiguracja.KonfiguracjaPlikow.SCIEZKA_TYPY);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(dialog, "Błąd zapisu pliku: " + ex.getMessage());
            }
        });

        usunButton.addActionListener(e -> {
            TypRoweru zaznaczony = lista.getSelectedValue();
            if (zaznaczony == null) {
                JOptionPane.showMessageDialog(dialog, "Wybierz typ do usunięcia.");
                return;
            }

            // Pobierz aktualny typ z serwisu (nie obiekt z listy GUI!)
            TypRoweru typZRozpoznania = serwis.znajdzTypPoNazwie(zaznaczony.getNazwa()).orElse(null);
            if (typZRozpoznania == null) {
                JOptionPane.showMessageDialog(dialog, "Typ nie istnieje w systemie.");
                return;
            }

            if (wypozyczeniaSerwis != null && wypozyczeniaSerwis.czyTypJestWUzyciu(typZRozpoznania, rowery)) {
                JOptionPane.showMessageDialog(dialog, "Nie można usunąć typu, który jest używany przez rowery.");
                return;
            }

            int conf = JOptionPane.showConfirmDialog(dialog, "Na pewno usunąć typ: " + zaznaczony.getNazwa() + "?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                boolean ok = serwis.usunTyp(typZRozpoznania);
                if (ok) {
                    model.removeElement(zaznaczony);
                    if (comboBox != null) comboBox.removeItem(zaznaczony);
                    try {
                        io.zapisz(serwis.pobierzWszystkieTypy(), konfiguracja.KonfiguracjaPlikow.SCIEZKA_TYPY);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dialog, "Błąd zapisu pliku: " + ex.getMessage());
                    }
                }
            }
        });

        edytujButton.addActionListener(e -> {
            TypRoweru zaznaczony = lista.getSelectedValue();
            if (zaznaczony == null) {
                JOptionPane.showMessageDialog(dialog, "Wybierz typ do edycji.");
                return;
            }

            String nowaNazwa = nazwaField.getText().trim();
            String nowyOpis = opisField.getText().trim();

            if (nowaNazwa.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nazwa nie może być pusta.");
                return;
            }

            if (!zaznaczony.getNazwa().equalsIgnoreCase(nowaNazwa) &&
                    serwis.znajdzTypPoNazwie(nowaNazwa).isPresent()) {
                JOptionPane.showMessageDialog(dialog, "Typ o tej nazwie już istnieje.");
                return;
            }

            TypRoweru nowyTyp = new TypRoweru(nowaNazwa, nowyOpis);
            boolean ok = serwis.aktualizujTypRoweru(zaznaczony.getNazwa(), nowyTyp);
            if (ok) {
                model.setElementAt(nowyTyp, lista.getSelectedIndex());
                if (comboBox != null) {
                    comboBox.removeItem(zaznaczony);
                    comboBox.addItem(nowyTyp);
                }
                try {
                    io.zapisz(serwis.pobierzWszystkieTypy(), konfiguracja.KonfiguracjaPlikow.SCIEZKA_TYPY);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Błąd zapisu pliku: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Nie udało się zaktualizować typu.");
            }
        });

        lista.addListSelectionListener(e -> {
            TypRoweru zaznaczony = lista.getSelectedValue();
            if (zaznaczony != null) {
                nazwaField.setText(zaznaczony.getNazwa());
                opisField.setText(zaznaczony.getOpis());
            }
        });

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(dolPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
