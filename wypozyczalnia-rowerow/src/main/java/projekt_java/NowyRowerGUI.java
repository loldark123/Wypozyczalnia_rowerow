package projekt_java;

import data_model.model.Rower;
import data_model.model.TypRoweru;
import data_model.serwis.SerwisRowerow;
import data_model.serwis.SerwisTypRoweru;
import konfiguracja.KonfiguracjaPlikow;
import data_model.io.PlikRowerowIO;
import data_model.io.PlikTypowRowerowIO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class NowyRowerGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dodawanie roweru");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));

            // Serwisy
            SerwisRowerow serwisRowerow = new SerwisRowerow();
            SerwisTypRoweru serwisTypow = new SerwisTypRoweru();
            PlikRowerowIO io = new PlikRowerowIO();
            PlikTypowRowerowIO typIO = new PlikTypowRowerowIO();

            List<Rower> wczytane = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_ROWERY);
            for (Rower r : wczytane) serwisRowerow.dodajRower(r);

            List<TypRoweru> typy = typIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_TYPY);
            for (TypRoweru t : typy) serwisTypow.dodajTypRoweru(t);

            // Komponenty
            JComboBox<TypRoweru> typCombo = new JComboBox<>(serwisTypow.pobierzWszystkieTypy().toArray(new TypRoweru[0]));
            JTextField markaField = new JTextField();
            JTextField modelField = new JTextField();
            JTextField rozmiarKolaField = new JTextField();
            JTextField opisField = new JTextField();
            JTextField numerSeryjnyField = new JTextField();
            JButton dodajButton = new JButton("Dodaj");
            JButton zarzadzajTypamiBtn = new JButton("Zarządzaj typami");

            // Układ
            panel.add(new JLabel("Typ roweru:")); panel.add(typCombo);
            panel.add(new JLabel("Marka:")); panel.add(markaField);
            panel.add(new JLabel("Model:")); panel.add(modelField);
            panel.add(new JLabel("Rozmiar koła (w calach):")); panel.add(rozmiarKolaField);
            panel.add(new JLabel("Opis:")); panel.add(opisField);
            panel.add(new JLabel("Numer seryjny:")); panel.add(numerSeryjnyField);
            panel.add(zarzadzajTypamiBtn); panel.add(dodajButton);

            // Obsługa dodawania roweru
            dodajButton.addActionListener(e -> {
                try {
                    String marka = markaField.getText().trim();
                    String model = modelField.getText().trim();
                    String rozmiarText = rozmiarKolaField.getText().trim();
                    String opis = opisField.getText().trim();
                    String nrSeryjny = numerSeryjnyField.getText().trim();
                    TypRoweru typ = (TypRoweru) typCombo.getSelectedItem();

                    if (typ == null || marka.isEmpty() || model.isEmpty() || nrSeryjny.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Typ, marka, model i numer seryjny są wymagane.");
                        return;
                    }

                    if (!rozmiarText.matches("\\d+")) {
                        JOptionPane.showMessageDialog(frame, "Rozmiar koła musi być liczbą całkowitą.");
                        return;
                    }

                    if (opis.length() > 100) {
                        JOptionPane.showMessageDialog(frame, "Opis nie może mieć więcej niż 100 znaków.");
                        return;
                    }

                    for (Rower r : serwisRowerow.pobierzWszystkieRowery()) {
                        if (r.getNumerSeryjny().equalsIgnoreCase(nrSeryjny)) {
                            JOptionPane.showMessageDialog(frame, "Rower o tym numerze seryjnym już istnieje.");
                            return;
                        }
                    }

                    int rozmiarKola = Integer.parseInt(rozmiarText);
                    Rower rower = new Rower(typ, marka, model, rozmiarKola, opis, nrSeryjny);

                    serwisRowerow.dodajRower(rower);
                    io.zapisz(serwisRowerow.pobierzWszystkieRowery(), KonfiguracjaPlikow.SCIEZKA_ROWERY);

                    JOptionPane.showMessageDialog(frame, "Dodano rower: " + rower);
                    frame.dispose();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd zapisu: " + ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                }
            });

            // Obsługa zarządzania typami
            zarzadzajTypamiBtn.addActionListener(e -> TypyRowerowGUI.otworz(frame, serwisTypow, typCombo, typIO));

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
