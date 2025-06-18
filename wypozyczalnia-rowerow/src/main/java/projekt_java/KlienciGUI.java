package projekt_java;

import data_model.model.Klient;
import data_model.io.PlikKlientowIO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class KlienciGUI {

    private static final String PLIK_KLIENCI = "klienci.dat";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lista klientów");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());

            // Wczytaj dane
            PlikKlientowIO io = new PlikKlientowIO();
            List<Klient> klienci = io.wczytaj(PLIK_KLIENCI);

            // Przeglądanie - JList lub JTable
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (Klient k : klienci) {
                listModel.addElement(k.toString());  // np. "Jan Kowalski"
            }

            JList<String> lista = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(lista);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Przyciski
            JPanel przyciski = new JPanel();

            JButton dodajBtn = new JButton("Dodaj nowego klienta");
            przyciski.add(dodajBtn);

            JButton odswiezBtn = new JButton("Odśwież listę");
            przyciski.add(odswiezBtn);

            panel.add(przyciski, BorderLayout.SOUTH);

            // Działanie: otwórz NowyKlientGUI
            dodajBtn.addActionListener(e -> {
                NowyKlientGUI.main(null);
            });

            // Działanie: odczytaj dane na nowo
            odswiezBtn.addActionListener(e -> {
                List<Klient> nowaLista = io.wczytaj(PLIK_KLIENCI);
                listModel.clear();
                for (Klient k : nowaLista) {
                    listModel.addElement(k.toString());
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
