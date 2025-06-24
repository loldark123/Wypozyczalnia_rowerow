package projekt_java;

import data_model.io.PlikKlientowIO;
import data_model.io.PlikWypozyczenIO;
import data_model.model.Klient;
import data_model.model.Wypozyczenie;
import data_model.serwis.SerwisKlientow;
import data_model.serwis.SerwisWypozyczen;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Graficzny interfejs użytkownika do zarządzania klientami w systemie wypożyczalni rowerów.
 * Klasa tworzy okno Swing wyświetlające listę klientów z możliwością ich przeglądania
 * i zarządzania. Dane klientów są wczytywane z pliku przy uruchomieniu aplikacji.
 * 
 * @see SerwisKlientow
 * @see PlikKlientowIO
 * @see Klient
 * @see KonfiguracjaPlikow
 */
public class KlienciGUI {

    /**
     * Główna metoda uruchamiająca kompleksowy interfejs zarządzania klientami.
     * Tworzy okno z listą klientów, panelem szczegółów oraz przyciskami funkcjonalnymi
     * umożliwiającymi pełne zarządzanie danymi klientów z walidacją biznesową
     * i automatyczną synchronizacją z systemem plików.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Lista klientów");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());

            PlikKlientowIO io = new PlikKlientowIO();
            SerwisKlientow serwis = new SerwisKlientow();

            List<Klient> klienci = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_KLIENCI);
            klienci.forEach(serwis::dodajKlienta);

            DefaultListModel<Klient> listModel = new DefaultListModel<>();
            klienci.forEach(listModel::addElement);

            JList<Klient> lista = new JList<>(listModel);
            lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel(value.getImie() + " " + value.getNazwisko());
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                }
                label.setOpaque(true);
                return label;
            });

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

            JPanel przyciski = new JPanel();
            JButton dodajBtn = new JButton("Dodaj nowego klienta");
            JButton edytujBtn = new JButton("Edytuj klienta");
            JButton usunBtn = new JButton("Usuń klienta");
            usunBtn.setBackground(Color.RED);
            usunBtn.setForeground(Color.WHITE);

            przyciski.add(dodajBtn);
            przyciski.add(edytujBtn);
            przyciski.add(usunBtn);
            panel.add(przyciski, BorderLayout.SOUTH);

            dodajBtn.addActionListener(e -> {
                JTextField imieField = new JTextField();
                JTextField nazwiskoField = new JTextField();
                JTextField dowodField = new JTextField();
                JTextField opisField = new JTextField();

                JPanel addPanel = new JPanel(new GridLayout(4, 2));
                addPanel.add(new JLabel("Imię:")); addPanel.add(imieField);
                addPanel.add(new JLabel("Nazwisko:")); addPanel.add(nazwiskoField);
                addPanel.add(new JLabel("Numer dowodu:")); addPanel.add(dowodField);
                addPanel.add(new JLabel("Opis:")); addPanel.add(opisField);

                int result = JOptionPane.showConfirmDialog(frame, addPanel, "Nowy klient", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String imie = imieField.getText().trim();
                        String nazwisko = nazwiskoField.getText().trim();
                        String dowod = dowodField.getText().trim();
                        String opis = opisField.getText().trim();

                        if (imie.isEmpty() || nazwisko.isEmpty() || dowod.isEmpty()) {
                            throw new IllegalArgumentException("Imię, nazwisko i numer dowodu są wymagane.");
                        }
                        if (!dowod.matches("[A-Z]{3}[0-9]{6}")) {
                            throw new IllegalArgumentException("Numer dowodu powinien mieć format: 3 wielkie litery i 6 cyfr (np. ABC123456)");
                        }

                        Klient nowy = new Klient(imie, nazwisko, dowod, opis);
                        serwis.dodajKlienta(nowy);
                        io.zapisz(serwis.pobierzWszystkichKlientow(), KonfiguracjaPlikow.SCIEZKA_KLIENCI);
                        listModel.addElement(nowy);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                    }
                }
            });

            edytujBtn.addActionListener(e -> {
                Klient wybrany = lista.getSelectedValue();
                if (wybrany == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz klienta do edycji.");
                    return;
                }

                JTextField imieField = new JTextField(wybrany.getImie());
                JTextField nazwiskoField = new JTextField(wybrany.getNazwisko());
                JTextField dowodField = new JTextField(wybrany.getNumerDowodu());
                JTextField opisField = new JTextField(wybrany.getOpis());

                JPanel editPanel = new JPanel(new GridLayout(4, 2));
                editPanel.add(new JLabel("Imię:")); editPanel.add(imieField);
                editPanel.add(new JLabel("Nazwisko:")); editPanel.add(nazwiskoField);
                editPanel.add(new JLabel("Numer dowodu:")); editPanel.add(dowodField);
                editPanel.add(new JLabel("Opis:")); editPanel.add(opisField);

                int wynik = JOptionPane.showConfirmDialog(frame, editPanel, "Edytuj klienta", JOptionPane.OK_CANCEL_OPTION);
                if (wynik == JOptionPane.OK_OPTION) {
                    try {
                        String imie = imieField.getText().trim();
                        String nazwisko = nazwiskoField.getText().trim();
                        String dowod = dowodField.getText().trim();
                        String opis = opisField.getText().trim();

                        if (imie.isEmpty() || nazwisko.isEmpty() || dowod.isEmpty()) {
                            throw new IllegalArgumentException("Imię, nazwisko i numer dowodu są wymagane.");
                        }
                        if (!dowod.matches("[A-Z]{3}[0-9]{6}")) {
                            throw new IllegalArgumentException("Numer dowodu powinien mieć format: 3 wielkie litery i 6 cyfr (np. ABC123456)");
                        }

                        Klient nowy = new Klient(imie, nazwisko, dowod, opis);
                        boolean ok = serwis.aktualizujKlienta(wybrany.getNumerDowodu(), nowy);
                        if (ok) {
                            io.zapisz(serwis.pobierzWszystkichKlientow(), KonfiguracjaPlikow.SCIEZKA_KLIENCI);
                            listModel.setElementAt(nowy, lista.getSelectedIndex());
                            JOptionPane.showMessageDialog(frame, "Zaktualizowano dane klienta.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                    }
                }
            });

            usunBtn.addActionListener(e -> {
                Klient wybrany = lista.getSelectedValue();
                if (wybrany == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz klienta do usunięcia.");
                    return;
                }

                PlikWypozyczenIO wypIO = new PlikWypozyczenIO();
                SerwisWypozyczen serwisWyp = new SerwisWypozyczen();
                List<Wypozyczenie> wypList = wypIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA);
                wypList.forEach(serwisWyp::dodajWypozyczenie);

                boolean aktywne = serwisWyp.pobierzAktywneWypozyczenia().stream()
                        .anyMatch(w -> w.getKlient().equals(wybrany));
                if (aktywne) {
                    JOptionPane.showMessageDialog(frame, "Nie można usunąć klienta z aktywnymi wypożyczeniami.");
                    return;
                }

                int potwierdzenie = JOptionPane.showConfirmDialog(
                        frame,
                        "Czy na pewno chcesz usunąć klienta: " + wybrany.getImie() + " " + wybrany.getNazwisko() + "?",
                        "Potwierdzenie",
                        JOptionPane.YES_NO_OPTION
                );

                if (potwierdzenie == JOptionPane.YES_OPTION) {
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
