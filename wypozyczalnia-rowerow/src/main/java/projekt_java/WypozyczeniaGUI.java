package projekt_java;

import data_model.model.*;
import data_model.serwis.*;
import data_model.io.*;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * Główny interfejs graficzny do zarządzania wypożyczeniami w systemie wypożyczalni rowerów.
 * Klasa tworzy kompleksowe okno umożliwiające tworzenie nowych wypożyczeń, sprawdzanie
 * dostępności rowerów, generowanie raportów oraz zarządzanie zwrotami. Interfejs integruje
 * wszystkie główne funkcjonalności systemu wypożyczalni w jednym miejscu.
 * 
 * @see SerwisWypozyczen
 * @see SerwisKlientow
 * @see SerwisRowerow
 * @see ZarzadzanieZwrotamiGUI
 * @see Wypozyczenie
 */
public class WypozyczeniaGUI {

    /**
     * Główna metoda uruchamiająca interfejs zarządzania wypożyczeniami.
     * Tworzy okno Swing z formularzem wypożyczenia, przyciskami funkcjonalnymi
     * oraz implementuje wszystkie operacje związane z wypożyczeniami rowerów.
     * Wykorzystuje asynchroniczne operacje dla lepszej responsywności interfejsu.
     * 
     * @param args Argumenty linii poleceń
     * 
     * @see SwingUtilities#invokeLater(Runnable)
     * @see JFrame
     * @see GridLayout
     * @see SwingWorker
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nowe wypożyczenie");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(750, 550);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));

            // Serwisy i IO
            SerwisKlientow serwisKlientow = new SerwisKlientow();
            SerwisRowerow serwisRowerow = new SerwisRowerow();
            SerwisWypozyczen serwisWypozyczen = new SerwisWypozyczen();
            PlikKlientowIO klientIO = new PlikKlientowIO();
            PlikRowerowIO rowerIO = new PlikRowerowIO();
            PlikWypozyczenIO wypozyczenieIO = new PlikWypozyczenIO();

            // Wczytywanie danych
            klientIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_KLIENCI).forEach(serwisKlientow::dodajKlienta);
            rowerIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_ROWERY).forEach(serwisRowerow::dodajRower);
            wypozyczenieIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA).forEach(serwisWypozyczen::dodajWypozyczenie);

            // Pola formularza
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JTextField dzisiajField = new JTextField(LocalDate.now().format(formatter));
            JComboBox<Klient> klientCombo = new JComboBox<>(serwisKlientow.pobierzWszystkichKlientow().toArray(new Klient[0]));
            JTextField dataOdField = new JTextField(LocalDate.now().format(formatter));
            JTextField dataDoField = new JTextField(LocalDate.now().plusDays(2).format(formatter));
            JTextArea uwagiArea = new JTextArea();

            // Przycisk i combo do roweru
            JButton sprawdzBtn = new JButton("Sprawdź dostępne rowery");
            JComboBox<Rower> rowerCombo = new JComboBox<>();

            // Przycisk wypożyczenia i zarządzania
            JButton wypozyczBtn = new JButton("Wypożycz");
            JButton pokazZwrotyBtn = new JButton("Zarządzaj wypożyczeniami");
            JButton historiaZwrotowBtn = new JButton("Historia zwrotów");

            // Raporty
            JButton raportAktywneBtn = new JButton("Raport aktywnych");
            JButton raportSpoznioneBtn = new JButton("Raport spóźnionych");

            // Dodawanie elementów do panelu
            panel.add(new JLabel("Dzisiejsza data (rrrr-mm-dd):")); panel.add(dzisiajField);
            panel.add(new JLabel("Wybierz klienta:")); panel.add(klientCombo);
            panel.add(new JLabel("Data OD (rrrr-mm-dd):")); panel.add(dataOdField);
            panel.add(new JLabel("Data DO (rrrr-mm-dd):")); panel.add(dataDoField);
            panel.add(new JLabel("Uwagi:")); panel.add(new JScrollPane(uwagiArea));
            panel.add(sprawdzBtn); panel.add(rowerCombo);
            panel.add(wypozyczBtn); panel.add(pokazZwrotyBtn);
            panel.add(raportAktywneBtn); panel.add(raportSpoznioneBtn);
            panel.add(historiaZwrotowBtn); panel.add(new JLabel(""));

            // Sprawdź dostępność rowerów
            sprawdzBtn.addActionListener(e -> {
                try {
                    LocalDate dataOd = LocalDate.parse(dataOdField.getText().trim());
                    LocalDate dataDo = LocalDate.parse(dataDoField.getText().trim());

                    new SwingWorker<List<Rower>, Void>() {
                        @Override
                        protected List<Rower> doInBackground() throws Exception {
                            return serwisWypozyczen.pobierzDostepneRoweryWZakresieAsync(
                                    dataOd, dataDo, serwisRowerow.pobierzWszystkieRowery()).get();
                        }

                        @Override
                        protected void done() {
                            try {
                                List<Rower> dostepne = get();
                                rowerCombo.removeAllItems();
                                dostepne.forEach(rowerCombo::addItem);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                            }
                        }
                    }.execute();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd formatu daty: " + ex.getMessage());
                }
            });

            // Wypożycz rower
            wypozyczBtn.addActionListener(e -> {
                try {
                    Klient klient = (Klient) klientCombo.getSelectedItem();
                    Rower rower = (Rower) rowerCombo.getSelectedItem();
                    LocalDate dataOd = LocalDate.parse(dataOdField.getText().trim());
                    LocalDate dataDo = LocalDate.parse(dataDoField.getText().trim());
                    LocalDate dzisiaj = LocalDate.parse(dzisiajField.getText().trim());
                    String uwagi = uwagiArea.getText().trim();

                    if (klient == null || rower == null) {
                        JOptionPane.showMessageDialog(frame, "Musisz wybrać klienta i rower.");
                        return;
                    }

                    if (!serwisWypozyczen.czyZakresDatJestPoprawny(dzisiaj, dataOd, dataDo)) {
                        JOptionPane.showMessageDialog(frame, "Zakres dat wypożyczenia jest niepoprawny.");
                        return;
                    }

                    String noweId;
                    Random random = new Random();
                    do {
                        StringBuilder sb = new StringBuilder(10);
                        for (int i = 0; i < 10; i++) sb.append(random.nextInt(10));
                        noweId = sb.toString();
                    } while (serwisWypozyczen.czyIdIstnieje(noweId));

                    Wypozyczenie wyp = new Wypozyczenie(noweId, rower, klient, dataOd, dataDo, StatusWypozyczenia.AKTYWNE, uwagi);
                    serwisWypozyczen.dodajWypozyczenie(wyp);
                    wypozyczenieIO.zapisz(serwisWypozyczen.pobierzWszystkieWypozyczenia(), KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA);
                    JOptionPane.showMessageDialog(frame, "Wypożyczono rower!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                }
            });

            // Raport aktywnych
            raportAktywneBtn.addActionListener(e -> {
                try {
                    LocalDate symulowanaData = LocalDate.parse(dzisiajField.getText().trim());
                    new SwingWorker<List<Wypozyczenie>, Void>() {
                        @Override
                        protected List<Wypozyczenie> doInBackground() throws Exception {
                            return serwisWypozyczen.pobierzAktywneWypozyczenia(symulowanaData);
                        }

                        @Override
                        protected void done() {
                            try {
                                List<Wypozyczenie> aktywne = get();
                                if (aktywne.isEmpty()) {
                                    JOptionPane.showMessageDialog(frame, "Brak aktualnych wypożyczeń.");
                                    return;
                                }

                                StringBuilder raport = new StringBuilder("Aktywne wypożyczenia:\n\n");
                                aktywne.forEach(w -> raport.append(w).append("\n"));
                                JOptionPane.showMessageDialog(frame, raport.toString());
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Błąd raportu: " + ex.getMessage());
                            }
                        }
                    }.execute();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd daty: " + ex.getMessage());
                }
            });

            // Raport spóźnionych
            raportSpoznioneBtn.addActionListener(e -> {
                try {
                    LocalDate symulowanaData = LocalDate.parse(dzisiajField.getText().trim());
                    List<Wypozyczenie> spoznione = serwisWypozyczen.pobierzWypozyczeniaSpoznione(symulowanaData);

                    if (spoznione.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Brak spóźnionych wypożyczeń.");
                        return;
                    }

                    StringBuilder raport = new StringBuilder("Spóźnione wypożyczenia:\n\n");
                    for (Wypozyczenie w : spoznione) {
                        raport.append(w).append("\n");
                    }
                    JOptionPane.showMessageDialog(frame, raport.toString());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd generowania raportu: " + ex.getMessage());
                }
            });

            // Zarządzanie wypożyczeniami
            pokazZwrotyBtn.addActionListener(e ->
                    ZarzadzanieZwrotamiGUI.uruchom(frame, serwisWypozyczen, wypozyczenieIO)
            );

            // Historia zwrotów – ZMIENIONA
            historiaZwrotowBtn.addActionListener(e -> {
                try {
                    List<String> linie = Files.readAllLines(Paths.get("dane/zwroty.txt"));
                    if (linie.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Brak historii zwrotów.");
                        return;
                    }

                    JTextArea textArea = new JTextArea(String.join("\n", linie));
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(800, 400));

                    JOptionPane.showMessageDialog(frame, scrollPane, "Historia zwrotów", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Błąd odczytu pliku zwrotów: " + ex.getMessage());
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
