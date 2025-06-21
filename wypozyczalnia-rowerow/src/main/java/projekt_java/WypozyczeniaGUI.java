package projekt_java;

import data_model.model.*;
import data_model.serwis.*;
import data_model.io.*;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WypozyczeniaGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nowe wypożyczenie");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(11, 2, 5, 5));

            // Serwisy
            SerwisKlientow serwisKlientow = new SerwisKlientow();
            SerwisRowerow serwisRowerow = new SerwisRowerow();
            SerwisWypozyczen serwisWypozyczen = new SerwisWypozyczen();
            PlikKlientowIO klientIO = new PlikKlientowIO();
            PlikRowerowIO rowerIO = new PlikRowerowIO();
            PlikWypozyczenIO wypozyczenieIO = new PlikWypozyczenIO();

            // Wczytywanie danych z plików
            for (Klient k : klientIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_KLIENCI)) serwisKlientow.dodajKlienta(k);
            for (Rower r : rowerIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_ROWERY)) serwisRowerow.dodajRower(r);
            for (Wypozyczenie w : wypozyczenieIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA)) serwisWypozyczen.dodajWypozyczenie(w);

            // Komponenty
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JTextField dzisiajField = new JTextField(LocalDate.now().format(formatter));
            JComboBox<Klient> klientCombo = new JComboBox<>(serwisKlientow.pobierzWszystkichKlientow().toArray(new Klient[0]));
            JTextField dataOdField = new JTextField(LocalDate.now().format(formatter));
            JTextField dataDoField = new JTextField(LocalDate.now().plusDays(2).format(formatter));
            JTextArea uwagiArea = new JTextArea();

            JButton sprawdzBtn = new JButton("Sprawdź dostępne rowery");
            JComboBox<Rower> rowerCombo = new JComboBox<>();
            JButton wypozyczBtn = new JButton("Wypożycz");
            JButton raportAktywneBtn = new JButton("Raport aktywnych");
            JButton raportSpoznioneBtn = new JButton("Raport spóźnionych");

            // Layout
            panel.add(new JLabel("Dzisiejsza data (rrrr-mm-dd):")); panel.add(dzisiajField);
            panel.add(new JLabel("Wybierz klienta:")); panel.add(klientCombo);
            panel.add(new JLabel("Data OD (rrrr-mm-dd):")); panel.add(dataOdField);
            panel.add(new JLabel("Data DO (rrrr-mm-dd):")); panel.add(dataDoField);
            panel.add(new JLabel("Uwagi:")); panel.add(new JScrollPane(uwagiArea));
            panel.add(sprawdzBtn); panel.add(rowerCombo);
            panel.add(new JLabel()); panel.add(wypozyczBtn);
            panel.add(raportAktywneBtn); panel.add(raportSpoznioneBtn);

            // Logika
            sprawdzBtn.addActionListener(e -> {
                try {
                    LocalDate dataOd = LocalDate.parse(dataOdField.getText().trim());
                    LocalDate dataDo = LocalDate.parse(dataDoField.getText().trim());
                    List<Rower> dostepne = serwisWypozyczen.pobierzDostepneRoweryWZakresie(dataOd, dataDo, serwisRowerow.pobierzWszystkieRowery());
                    rowerCombo.removeAllItems();
                    for (Rower r : dostepne) rowerCombo.addItem(r);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd formatu daty: " + ex.getMessage());
                }
            });

            wypozyczBtn.addActionListener(e -> {
                try {
                    Klient klient = (Klient) klientCombo.getSelectedItem();
                    Rower rower = (Rower) rowerCombo.getSelectedItem();
                    LocalDate dataOd = LocalDate.parse(dataOdField.getText().trim());
                    LocalDate dataDo = LocalDate.parse(dataDoField.getText().trim());
                    String uwagi = uwagiArea.getText().trim();

                    if (klient == null || rower == null) {
                        JOptionPane.showMessageDialog(frame, "Musisz wybrać klienta i rower.");
                        return;
                    }

                    Wypozyczenie wyp = new Wypozyczenie(rower, klient, dataOd, dataDo, StatusWypozyczenia.AKTYWNE, uwagi);
                    serwisWypozyczen.dodajWypozyczenie(wyp);
                    wypozyczenieIO.zapisz(serwisWypozyczen.pobierzWszystkieWypozyczenia(), KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA);
                    JOptionPane.showMessageDialog(frame, "Wypożyczono rower!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                }
            });

            raportAktywneBtn.addActionListener(e -> {
                try {
                    LocalDate symulowanaData = LocalDate.parse(dzisiajField.getText().trim());
                    List<Wypozyczenie> aktywne = serwisWypozyczen.pobierzAktywneWypozyczenia(symulowanaData);
                    StringBuilder raport = new StringBuilder("Aktywne wypożyczenia:\n\n");
                    for (Wypozyczenie w : aktywne) {
                        raport.append(w).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, raport.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd odczytu daty: " + ex.getMessage());
                }
            });

            raportSpoznioneBtn.addActionListener(e -> {
                try {
                    LocalDate symulowanaData = LocalDate.parse(dzisiajField.getText().trim());
                    List<Wypozyczenie> spoznione = serwisWypozyczen.pobierzWypozyczeniaSpoznione(symulowanaData);
                    StringBuilder raport = new StringBuilder("Spóźnione wypożyczenia:\n\n");
                    for (Wypozyczenie w : spoznione) {
                        raport.append(w).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, raport.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd odczytu daty: " + ex.getMessage());
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
