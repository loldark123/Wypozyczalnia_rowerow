package projekt_java;

import data_model.model.Klient;
import data_model.io.PlikKlientowIO;
import data_model.serwis.SerwisKlientow;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class KlienciGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lista klientów");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());

            // Wczytaj dane z pliku
            PlikKlientowIO io = new PlikKlientowIO();
            
            SerwisKlientow serwis = new SerwisKlientow();
        
            
            List<Klient> klienci = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_KLIENCI);
            
            for (Klient k : klienci) {
                serwis.dodajKlienta(k);
            }
            

            // Model listy klientów (zamiast String, trzymamy obiekty Klient)
            DefaultListModel<Klient> listModel = new DefaultListModel<>();
            for (Klient k : klienci) {
                listModel.addElement(k);
            }

            // Lista klientów
            JList<Klient> lista = new JList<>(listModel);

            // Pokazuj tylko imię i nazwisko w liście
            lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel(value.getImie() + " " + value.getNazwisko());
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                }
                label.setOpaque(true);
                return label;
            });

            // Wyświetl szczegóły klienta po kliknięciu
            lista.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    Klient wybrany = lista.getSelectedValue();
                    if (wybrany != null) {
                        JOptionPane.showMessageDialog(frame,
                                "Imię: " + wybrany.getImie() + "\n" +
                                "Nazwisko: " + wybrany.getNazwisko() + "\n" +
                                "Numer dowodu: " + wybrany.getNumerDowodu() + "\n" +
                                "Opis: " + wybrany.getOpis(),
                                "Szczegóły klienta",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(lista);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Panel z przyciskami
            JPanel przyciski = new JPanel();

            JButton dodajBtn = new JButton("Dodaj nowego klienta");
            przyciski.add(dodajBtn);

            JButton odswiezBtn = new JButton("Odśwież listę");
            przyciski.add(odswiezBtn);
            
            JButton usunBtn = new JButton("Usuń klienta");
            przyciski.add(usunBtn);
            usunBtn.setBackground(Color.RED);
            usunBtn.setForeground(Color.WHITE);

            panel.add(przyciski, BorderLayout.SOUTH);

            // Działanie: otwórz NowyKlientGUI
            dodajBtn.addActionListener(e -> {
                NowyKlientGUI.main(null);
            });

            // Działanie: odśwież listę po ponownym wczytaniu pliku
            odswiezBtn.addActionListener(e -> {
                List<Klient> nowaLista = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_KLIENCI);
                listModel.clear();
                for (Klient k : nowaLista) {
                    listModel.addElement(k);
                }
                
               
            });
            
            usunBtn.addActionListener(e -> {
                Klient wybrany = lista.getSelectedValue();
                if (wybrany == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz klienta do usunięcia.");
                    return;
                }

                int potwierdzenie = JOptionPane.showConfirmDialog(
                        frame,
                        "Czy na pewno chcesz usunąć klienta: " + wybrany.getImie() + " " + wybrany.getNazwisko() + "?",
                        "Potwierdzenie",
                        JOptionPane.YES_NO_OPTION
                );

                if (potwierdzenie == JOptionPane.YES_OPTION) {
                    // Usuń klienta
                    boolean usunieto = serwis.usunKlienta(wybrany.getNumerDowodu());

                    if (usunieto) {
                        try {
                            io.zapisz(serwis.pobierzWszystkichKlientow(), KonfiguracjaPlikow.SCIEZKA_KLIENCI);
                            listModel.removeElement(wybrany);
                            JOptionPane.showMessageDialog(frame, "Klient został usunięty.");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "Błąd przy zapisie do pliku: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Nie udało się usunąć klienta.");
                    }
                }
            });


            frame.add(panel);
            frame.setVisible(true);
        });
    }
}