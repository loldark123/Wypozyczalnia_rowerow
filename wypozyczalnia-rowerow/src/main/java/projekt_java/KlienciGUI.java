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
 * Graficzny interfejs użytkownika do kompleksowego zarządzania klientami w systemie wypożyczalni rowerów.
 * Klasa tworzy okno Swing umożliwiające pełne operacje CRUD (Create, Read, Update, Delete) na danych klientów
 * z zaawansowaną walidacją biznesową, kontrolą integralności danych oraz automatyczną synchronizacją
 * z systemem plików. Interfejs zapewnia bezpieczne zarządzanie klientami z uwzględnieniem aktywnych wypożyczeń.
 * 
 * <p>Główne funkcjonalności interfejsu:</p>
 * <ul>
 *   <li>Wyświetlanie listy wszystkich klientów z możliwością przeglądania szczegółów</li>
 *   <li>Dodawanie nowych klientów z walidacją formatu numeru dowodu</li>
 *   <li>Edycja danych istniejących klientów z zachowaniem integralności</li>
 *   <li>Bezpieczne usuwanie klientów z kontrolą aktywnych wypożyczeń</li>
 *   <li>Automatyczna synchronizacja zmian z plikami danych</li>
 *   <li>Walidacja formatu numeru dowodu osobistego (AAA123456)</li>
 *   <li>Obsługa błędów I/O z informatywnymi komunikatami</li>
 * </ul>
 * 
 * <p>Interfejs implementuje wzorzec MVC, gdzie widok (GUI) komunikuje się z warstwą biznesową
 * (serwisy) i warstwą persystencji (klasy IO) w sposób luźno powiązany.</p>
 * 
 * @see SerwisKlientow
 * @see PlikKlientowIO
 * @see Klient
 * @see SerwisWypozyczen
 * @see KonfiguracjaPlikow
 * @see JFrame
 * @see DefaultListModel
 * @since 1.0
 */
public class KlienciGUI {

    /**
 * Graficzny interfejs użytkownika do kompleksowego zarządzania klientami w systemie wypożyczalni rowerów.
 * Klasa tworzy okno Swing umożliwiające pełne operacje CRUD (Create, Read, Update, Delete) na danych klientów
 * z zaawansowaną walidacją biznesową, kontrolą integralności danych oraz automatyczną synchronizacją
 * z systemem plików. Interfejs zapewnia bezpieczne zarządzanie klientami z uwzględnieniem aktywnych wypożyczeń.
 * 
 * <p>Główne funkcjonalności interfejsu:</p>
 * <ul>
 *   <li>Wyświetlanie listy wszystkich klientów z możliwością przeglądania szczegółów</li>
 *   <li>Dodawanie nowych klientów z walidacją formatu numeru dowodu</li>
 *   <li>Edycja danych istniejących klientów z zachowaniem integralności</li>
 *   <li>Bezpieczne usuwanie klientów z kontrolą aktywnych wypożyczeń</li>
 *   <li>Automatyczna synchronizacja zmian z plikami danych</li>
 *   <li>Walidacja formatu numeru dowodu osobistego (AAA123456)</li>
 *   <li>Obsługa błędów I/O z informatywnymi komunikatami</li>
 * </ul>
 * 
 * <p>Interfejs implementuje wzorzec MVC, gdzie widok (GUI) komunikuje się z warstwą biznesową
 * (serwisy) i warstwą persystencji (klasy IO) w sposób luźno powiązany.</p>
 * 
 * @see SerwisKlientow
 * @see PlikKlientowIO
 * @see Klient
 * @see SerwisWypozyczen
 * @see KonfiguracjaPlikow
 * @see JFrame
 * @see DefaultListModel
 * @since 1.0
 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Tworzenie i konfiguracja głównego okna aplikacji
            JFrame frame = new JFrame("Lista klientów");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Używamy DISPOSE_ON_CLOSE, aby zamknąć tylko to okno
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);  // Wyśrodkowanie okna na ekranie

            // Panel główny z układem BorderLayout dla optymalnego rozmieszczenia komponentów
            JPanel panel = new JPanel(new BorderLayout());

            // Inicjalizacja warstwy persystencji i biznesowej
            PlikKlientowIO io = new PlikKlientowIO();
            SerwisKlientow serwis = new SerwisKlientow();

            // Wczytanie wszystkich klientów z pliku przy starcie aplikacji
            List<Klient> klienci = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_KLIENCI);
            klienci.forEach(serwis::dodajKlienta);  // Dodanie klientów do serwisu

            // Konfiguracja modelu listy i wypełnienie danymi
            DefaultListModel<Klient> listModel = new DefaultListModel<>();
            klienci.forEach(listModel::addElement);

            // Konfiguracja listy klientów z niestandardowym renderowaniem
            JList<Klient> lista = new JList<>(listModel);
            lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Tylko jeden element na raz

            // Niestandardowy renderer wyświetlający imię i nazwisko klienta
            lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel(value.getImie() + " " + value.getNazwisko());

                // Obsługa kolorów dla zaznaczonego elementu
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                }
                label.setOpaque(true); // Wymagane dla prawidłowego wyświetlania kolorów tła
                return label;
            });

            // Listener obsługujący kliknięcia na listę - wyświetlanie szczegółów klienta
            lista.addListSelectionListener(e -> {

                // Sprawdzenie czy to finalne zdarzenie selekcji (nie pośrednie)
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

            // Dodanie listy z możliwością przewijania do centrum panelu
            JScrollPane scrollPane = new JScrollPane(lista);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Panel przycisków funkcjonalnych na dole okna
            JPanel przyciski = new JPanel();
            JButton dodajBtn = new JButton("Dodaj nowego klienta");
            JButton edytujBtn = new JButton("Edytuj klienta");
            JButton usunBtn = new JButton("Usuń klienta");

            // Stylowanie przycisku usuwania dla lepszej widoczności
            usunBtn.setBackground(Color.RED);
            usunBtn.setForeground(Color.WHITE);

            // Dodanie przycisków do panelu
            przyciski.add(dodajBtn);
            przyciski.add(edytujBtn);
            przyciski.add(usunBtn);
            panel.add(przyciski, BorderLayout.SOUTH);

            // === OBSŁUGA DODAWANIA NOWEGO KLIENTA ===
            dodajBtn.addActionListener(e -> {

                // Tworzenie pól formularza dla nowego klienta
                JTextField imieField = new JTextField();
                JTextField nazwiskoField = new JTextField();
                JTextField dowodField = new JTextField();
                JTextField opisField = new JTextField();

                // Panel formularza z układem siatki 4x2
                JPanel addPanel = new JPanel(new GridLayout(4, 2));
                addPanel.add(new JLabel("Imię:")); addPanel.add(imieField);
                addPanel.add(new JLabel("Nazwisko:")); addPanel.add(nazwiskoField);
                addPanel.add(new JLabel("Numer dowodu:")); addPanel.add(dowodField);
                addPanel.add(new JLabel("Opis:")); addPanel.add(opisField);


                // Wyświetlenie dialogu z formularzem
                int result = JOptionPane.showConfirmDialog(frame, addPanel, "Nowy klient", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {

                        // Pobranie i oczyszczenie danych z formularza
                        String imie = imieField.getText().trim();
                        String nazwisko = nazwiskoField.getText().trim();
                        String dowod = dowodField.getText().trim();
                        String opis = opisField.getText().trim();

                        // Walidacja wymaganych pól
                        if (imie.isEmpty() || nazwisko.isEmpty() || dowod.isEmpty()) {
                            throw new IllegalArgumentException("Imię, nazwisko i numer dowodu są wymagane.");
                        }
                        if (!dowod.matches("[A-Z]{3}[0-9]{6}")) {
                            throw new IllegalArgumentException("Numer dowodu powinien mieć format: 3 wielkie litery i 6 cyfr (np. ABC123456)");
                        }

                        // Tworzenie nowego obiektu klienta z walidacją w konstruktorze
                        Klient nowy = new Klient(imie, nazwisko, dowod, opis);
                        // Dodanie klienta do serwisu (może rzucić wyjątek przy duplikacie)
                        serwis.dodajKlienta(nowy);

                        // Zapis zmian do pliku
                        io.zapisz(serwis.pobierzWszystkichKlientow(), KonfiguracjaPlikow.SCIEZKA_KLIENCI);
                        // Aktualizacja modelu listy w interfejsie
                        listModel.addElement(nowy);
                    } catch (Exception ex) {
                        // Obsługa wszystkich błędów walidacji i I/O
                        JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                    }
                }
            });

            // === OBSŁUGA EDYCJI ISTNIEJĄCEGO KLIENTA ===
            edytujBtn.addActionListener(e -> {
                Klient wybrany = lista.getSelectedValue();
                if (wybrany == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz klienta do edycji.");
                    return;
                }

                // Tworzenie pól formularza wypełnionych aktualnymi danymi klienta
                JTextField imieField = new JTextField(wybrany.getImie());
                JTextField nazwiskoField = new JTextField(wybrany.getNazwisko());
                JTextField dowodField = new JTextField(wybrany.getNumerDowodu());
                JTextField opisField = new JTextField(wybrany.getOpis());

                // Panel formularza edycji
                JPanel editPanel = new JPanel(new GridLayout(4, 2));
                editPanel.add(new JLabel("Imię:")); editPanel.add(imieField);
                editPanel.add(new JLabel("Nazwisko:")); editPanel.add(nazwiskoField);
                editPanel.add(new JLabel("Numer dowodu:")); editPanel.add(dowodField);
                editPanel.add(new JLabel("Opis:")); editPanel.add(opisField);

                // Wyświetlenie dialogu edycji
                int wynik = JOptionPane.showConfirmDialog(frame, editPanel, "Edytuj klienta", JOptionPane.OK_CANCEL_OPTION);
                if (wynik == JOptionPane.OK_OPTION) {
                    try {
                        // Pobranie i walidacja nowych danych
                        String imie = imieField.getText().trim();
                        String nazwisko = nazwiskoField.getText().trim();
                        String dowod = dowodField.getText().trim();
                        String opis = opisField.getText().trim();

                        // Walidacja wymaganych pól
                        if (imie.isEmpty() || nazwisko.isEmpty() || dowod.isEmpty()) {
                            throw new IllegalArgumentException("Imię, nazwisko i numer dowodu są wymagane.");
                        }
                        // Walidacja formatu numeru dowodu
                        if (!dowod.matches("[A-Z]{3}[0-9]{6}")) {
                            throw new IllegalArgumentException("Numer dowodu powinien mieć format: 3 wielkie litery i 6 cyfr (np. ABC123456)");
                        }
                        // Tworzenie zaktualizowanego obiektu klienta
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

            // === OBSŁUGA USUWANIA KLIENTA ===
            usunBtn.addActionListener(e -> {
                Klient wybrany = lista.getSelectedValue();
                if (wybrany == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz klienta do usunięcia.");
                    return;
                }

                // Sprawdzenie czy klient ma aktywne wypożyczenia (kontrola integralności)
                PlikWypozyczenIO wypIO = new PlikWypozyczenIO();
                SerwisWypozyczen serwisWyp = new SerwisWypozyczen();
                List<Wypozyczenie> wypList = wypIO.wczytaj(KonfiguracjaPlikow.SCIEZKA_WYPOZYCZENIA);
                wypList.forEach(serwisWyp::dodajWypozyczenie);

                // Sprawdzenie czy klient ma aktywne wypożyczenia
                boolean aktywne = serwisWyp.pobierzAktywneWypozyczenia().stream()
                        .anyMatch(w -> w.getKlient().equals(wybrany));
                if (aktywne) {
                    JOptionPane.showMessageDialog(frame, "Nie można usunąć klienta z aktywnymi wypożyczeniami.");
                    return;
                }

                // Potwierdzenie usunięcia przez użytkownika
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

            // Finalne ustawienie i wyświetlenie okna
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
