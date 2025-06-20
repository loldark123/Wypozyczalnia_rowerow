package data_model.io;

import data_model.model.Rower;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Obsługuje zapis i odczyt danych rowerów z pliku.
 */
public class PlikRowerowIO {

    // Zapis listy rowerów do pliku
    public void zapisz(List<Rower> rowery, String sciezka) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(sciezka))) {
            oos.writeObject(rowery);
        }
    }

    // Wczytanie listy rowerów z pliku
    public List<Rower> wczytaj(String sciezka) {
        File plik = new File(sciezka);
        if (!plik.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plik))) {
            return (List<Rower>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}