package projekt_java;

import konfiguracja.KonfiguracjaPlikow;

import data_model.model.TypRoweru;

import javax.swing.*;
import java.awt.*;

public class NowyTypRoweruGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dodawanie typu roweru");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 180);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(3, 2));

            JTextField nazwaField = new JTextField();
            JTextField opisField = new JTextField();
            JButton dodajButton = new JButton("Dodaj");

            panel.add(new JLabel("Nazwa typu:"));
            panel.add(nazwaField);
            panel.add(new JLabel("Opis:"));
            panel.add(opisField);
            panel.add(dodajButton);

            dodajButton.addActionListener(e -> {
                String nazwa = nazwaField.getText().trim();
                String opis = opisField.getText().trim();

                if (nazwa.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nazwa nie może być pusta.");
                    return;
                }

                if (opis.length() > 100) {
                    JOptionPane.showMessageDialog(frame, "Opis nie może przekraczać 100 znaków.");
                    return;
                }
                try {
                TypRoweru typ = new TypRoweru(nazwa, opis);
                JOptionPane.showMessageDialog(frame, "Dodano typ roweru: " + typ);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
