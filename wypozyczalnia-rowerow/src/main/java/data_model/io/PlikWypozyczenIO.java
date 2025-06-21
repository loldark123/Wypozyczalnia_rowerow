package data_model.io;

import data_model.model.Wypozyczenie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Obsługuje zapis i odczyt wypożyczeń do/z pliku przy użyciu serializacji.
 */
public class PlikWypozyczenIO {

    // Zapisuje listę wypożyczeń do pliku binarnego
    public void zapisz(List<Wypozyczenie> wypozyczenia, String sciezka) {
        File plik = new File(sciezka);
        plik.getParentFile().mkdirs(); // Upewnij się, że katalog istnieje

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plik))) {
            oos.writeObject(wypozyczenia);
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu wypożyczeń: " + e.getMessage());
        }
    }

    // Wczytuje listę wypożyczeń z pliku binarnego
    @SuppressWarnings("unchecked")
    public List<Wypozyczenie> wczytaj(String sciezka) {
        File plik = new File(sciezka);
        if (!plik.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plik))) {
            return (List<Wypozyczenie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd podczas wczytywania wypożyczeń: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
