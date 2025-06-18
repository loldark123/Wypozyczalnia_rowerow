package projekt_java;

import javax.swing.*;

import java.awt.*;
import data_model.io.PlikKlientowIO;
import java.io.IOException;
import data_model.model.Klient;
import data_model.serwis.SerwisKlientow;

public class NowyKlientGUI {
    public static void main(String[] args) {
    	
    	
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dodawanie klienta");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(5, 2));

            JTextField imieField = new JTextField();
            JTextField nazwiskoField = new JTextField();
            JTextField dowodField = new JTextField();
            JTextField opisField = new JTextField();
            JButton dodajButton = new JButton("Dodaj");

            panel.add(new JLabel("Imię:"));
            panel.add(imieField);
            panel.add(new JLabel("Nazwisko:"));
            panel.add(nazwiskoField);
            panel.add(new JLabel("Numer dowodu:"));
            panel.add(dowodField);
            panel.add(new JLabel("Opis:"));
            panel.add(opisField);
            panel.add(dodajButton);

            //  Serwis do zarządzania klientami
           SerwisKlientow serwis = new SerwisKlientow();
                PlikKlientowIO io = new PlikKlientowIO();
                for (Klient k : io.wczytaj("klienci.dat")) {
                    serwis.dodajKlienta(k);
                }

            dodajButton.addActionListener(e -> {
                String imie = imieField.getText().trim();
                String nazwisko = nazwiskoField.getText().trim();
                String dowod = dowodField.getText().trim();
                String opis = opisField.getText().trim();

                // Walidacje GUI
                if (imie.isEmpty() || nazwisko.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Imię i nazwisko nie mogą być puste.");
                    return;
                }

                if (!imie.matches("[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\- ]+") ||
                    !nazwisko.matches("[A-ZĄĆĘŁŃÓŚŹŻa-ząćęłńóśźż\\- ]+")) {
                    JOptionPane.showMessageDialog(frame, "Imię i nazwisko zawierają niedozwolone znaki.");
                    return;
                }

                if (!dowod.matches("[A-Z]{3}\\d{6}")) {
                    JOptionPane.showMessageDialog(frame, "Niepoprawny numer dowodu (format: AAA123456). Gdzie \"AAA\" to trzy duze litery a reszta znakow to numery od "
                    		+ "0 do 9");
                    return;
                }

                if (opis.length() > 100) {
                    JOptionPane.showMessageDialog(frame, "Opis nie może przekraczać 100 znaków.");
                    return;
                }

                // Tworzenie i dodawanie klienta , ewnetualnie wyrzucanie wyjatku w razie duplikacji
                try {
                            // 🧱 Tworzenie obiektu Klient na podstawie danych z formularza
                            Klient k = new Klient(imie, nazwisko, dowod, opis);

                            // ✅ Próba dodania klienta do serwisu
                            serwis.dodajKlienta(k);

                            // 💾 Zapis całej listy klientów do pliku
                            try {
                                
                                io.zapisz(serwis.pobierzWszystkichKlientow(), "klienci.dat"); // ⬅️ poprawna metoda
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(frame, "Błąd zapisu do pliku: " + ex.getMessage());
                                return;
                            }

                            // ✅ Potwierdzenie dla użytkownika
                            JOptionPane.showMessageDialog(frame, "Dodano klienta: " + k);

                } catch (IllegalArgumentException ex) {
                    // ❌ Obsługa błędów biznesowych (np. duplikat dowodu)
                    JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
