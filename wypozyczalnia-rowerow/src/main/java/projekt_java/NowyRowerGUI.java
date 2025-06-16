package projekt_java;

import data_model.model.Rower;
import data_model.model.TypRoweru;
import data_model.serwis.SerwisTypRoweru;
import data_model.serwis.SerwisRowerow;

import javax.swing.*;
import java.awt.*;


public class NowyRowerGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dodawanie roweru");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

            // Serwisy
            SerwisRowerow serwisRowerow = new SerwisRowerow();
            SerwisTypRoweru serwisTypow = new SerwisTypRoweru();

     

            // Komponenty
            JComboBox<TypRoweru> typCombo = new JComboBox<>(serwisTypow.pobierzWszystkieTypy().toArray(new TypRoweru[0]));
            JTextField markaField = new JTextField();
            JTextField modelField = new JTextField();
            JTextField rozmiarKolaField = new JTextField();
            JTextField opisField = new JTextField();
            JButton dodajButton = new JButton("Dodaj");

            // Układ
            panel.add(new JLabel("Typ roweru:"));
            panel.add(typCombo);
            panel.add(new JLabel("Marka:"));
            panel.add(markaField);
            panel.add(new JLabel("Model:"));
            panel.add(modelField);
            panel.add(new JLabel("Rozmiar koła (w calach):"));
            panel.add(rozmiarKolaField);
            panel.add(new JLabel("Opis:"));
            panel.add(opisField);
            panel.add(new JLabel(""));
            panel.add(dodajButton);

            // Obsługa kliknięcia
            dodajButton.addActionListener(e -> {
                try {
                    String marka = markaField.getText().trim();
                    String model = modelField.getText().trim();
                    String rozmiarText = rozmiarKolaField.getText().trim();
                    String opis = opisField.getText().trim();
                    TypRoweru typ = (TypRoweru) typCombo.getSelectedItem();

                    // Walidacja
                    if (marka.isEmpty() || model.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Marka i model są wymagane.");
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

                    int rozmiarKola = Integer.parseInt(rozmiarText);
                    Rower rower = new Rower(typ, marka, model, rozmiarKola, opis);

                    serwisRowerow.dodajRower(rower);

                    JOptionPane.showMessageDialog(frame, "Dodano rower: " + rower);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}

