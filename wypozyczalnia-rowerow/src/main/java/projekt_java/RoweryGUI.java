package projekt_java;

import data_model.model.Rower;
import data_model.model.TypRoweru;
import data_model.serwis.SerwisRowerow;
import data_model.serwis.SerwisTypRoweru;
import data_model.serwis.SerwisWypozyczen;
import data_model.io.PlikRowerowIO;
import data_model.io.PlikTypowRowerowIO;
import data_model.io.PlikWypozyczenIO;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RoweryGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Zarządzanie rowerami");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            SerwisRowerow serwis = new SerwisRowerow();
            SerwisTypRoweru serwisTypow = new SerwisTypRoweru();
            SerwisWypozyczen serwisWypozyczen = new SerwisWypozyczen();

            PlikRowerowIO io = new PlikRowerowIO();
            PlikTypowRowerowIO typyIO = new PlikTypowRowerowIO();
            PlikWypozyczenIO wypozyczeniaIO = new PlikWypozyczenIO();

            // Wczytaj dane
            List<Rower> listaZRozszerzenia = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_ROWERY);
            for (Rower r : listaZRozszerzenia) serwis.dodajRower(r);

            List<TypRoweru> typy = typyIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_TYPY);
            for (TypRoweru t : typy) serwisTypow.dodajTypRoweru(t);

            List<data_model.model.Wypozyczenie> wypozyczenia = wypozyczeniaIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA);
            for (data_model.model.Wypozyczenie w : wypozyczenia) serwisWypozyczen.dodajWypozyczenie(w);

            DefaultListModel<Rower> model = new DefaultListModel<>();
            for (Rower r : listaZRozszerzenia) model.addElement(r);

            JList<Rower> lista = new JList<>(model);
            lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                String txt = String.format("[%s] %s %s (%d\")\nOpis: %s\nSN: %s",
                        value.getTyp() != null ? value.getTyp().getNazwa() : "Brak typu",
                        value.getMarka(),
                        value.getModel(),
                        value.getRozmiarKola(),
                        value.getOpis(),
                        value.getNumerSeryjny());
                JLabel label = new JLabel("<html>" + txt.replace("\n", "<br>") + "</html>");
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                }
                label.setOpaque(true);
                return label;
            });

            JScrollPane scrollPane = new JScrollPane(lista);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel przyciski = new JPanel();
            JButton dodaj = new JButton("Dodaj rower");
            JButton usun = new JButton("Usuń rower");
            JButton typyButton = new JButton("Typy rowerów");

            przyciski.add(dodaj);
            przyciski.add(usun);
            przyciski.add(typyButton);
            panel.add(przyciski, BorderLayout.SOUTH);

            dodaj.addActionListener(e -> {
                if (serwisTypow.pobierzWszystkieTypy().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Brak dostępnych typów rowerów. Najpierw dodaj typ.");
                    return;
                }

                JTextField marka = new JTextField();
                JTextField modelR = new JTextField();
                JTextField rozmiar = new JTextField();
                JTextField opis = new JTextField();
                JTextField numerSeryjny = new JTextField();
                JComboBox<TypRoweru> combo = new JComboBox<>(serwisTypow.pobierzWszystkieTypy().toArray(new TypRoweru[0]));

                JPanel input = new JPanel(new GridLayout(6, 2));
                input.add(new JLabel("Marka:")); input.add(marka);
                input.add(new JLabel("Model:")); input.add(modelR);
                input.add(new JLabel("Rozmiar koła:")); input.add(rozmiar);
                input.add(new JLabel("Opis:")); input.add(opis);
                input.add(new JLabel("Numer seryjny:")); input.add(numerSeryjny);
                input.add(new JLabel("Typ:")); input.add(combo);

                int result = JOptionPane.showConfirmDialog(frame, input, "Nowy rower", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int rRozmiar = Integer.parseInt(rozmiar.getText().trim());
                        String nrS = numerSeryjny.getText().trim();
                        TypRoweru wybranyTyp = (TypRoweru) combo.getSelectedItem();

                        if (wybranyTyp == null) {
                            throw new IllegalArgumentException("Musisz wybrać typ roweru.");
                        }

                        if (nrS.isEmpty() || nrS.length() < 5) {
                            throw new IllegalArgumentException("Numer seryjny nie może być pusty ani krótszy niż 5 znaków.");
                        }
                        if (!nrS.matches("[A-Za-z0-9\\-]+")) {
                            throw new IllegalArgumentException("Numer seryjny może zawierać tylko litery, cyfry i myślniki.");
                        }

                        Rower nowy = new Rower(
                                wybranyTyp,
                                marka.getText().trim(),
                                modelR.getText().trim(),
                                rRozmiar,
                                opis.getText().trim(),
                                nrS
                        );

                        if (serwis.pobierzWszystkieRowery().contains(nowy)) {
                            throw new IllegalArgumentException("Rower o tym numerze seryjnym już istnieje.");
                        }

                        serwis.dodajRower(nowy);
                        io.zapisz(serwis.pobierzWszystkieRowery(), KonfiguracjaPlikow.SCIEZKA_ROWERY);
                        model.addElement(nowy);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                    }
                }
            });

            usun.addActionListener(e -> {
                Rower r = lista.getSelectedValue();
                if (r == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz rower do usunięcia.");
                    return;
                }

                boolean aktywne = serwisWypozyczen.czyRowerJestWypozyczony(r);
                if (aktywne) {
                    JOptionPane.showMessageDialog(frame, "Nie można usunąć roweru, który jest aktualnie wypożyczony.");
                    return;
                }

                int conf = JOptionPane.showConfirmDialog(frame, "Usunąć rower: " + r + "?", "Potwierdź", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    boolean ok = serwis.usunRower(r);
                    if (ok) {
                        try {
                            io.zapisz(serwis.pobierzWszystkieRowery(), KonfiguracjaPlikow.SCIEZKA_ROWERY);
                            model.removeElement(r);
                            JOptionPane.showMessageDialog(frame, "Usunięto rower.");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "Błąd zapisu pliku: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Nie udało się usunąć roweru.");
                    }
                }
            });

            typyButton.addActionListener(e -> {
                TypyRowerowGUI.otworz(frame, serwisTypow, null, typyIO, serwis.pobierzWszystkieRowery(), serwisWypozyczen);
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
