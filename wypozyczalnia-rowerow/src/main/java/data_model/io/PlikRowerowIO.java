package data_model.io;

import data_model.model.Rower;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Obsługuje zapis i odczyt danych rowerów z pliku.
 * Wykorzystuje ObjectInputStream/ObjectOutputStream dla prostego zapisu obiektów.
 */
public class PlikRowerowIO {

    /**
     * Zapisuje listę rowerów do pliku binarnego.
     * Używa ObjectOutputStream do serializacji obiektów Rower.
     *
     * @param rowery Lista rowerów do zapisania w pliku
     * @param sciezka Ścieżka do pliku docelowego
     * @throws IOException Gdy wystąpi błąd podczas zapisu lub tworzenia folderów
     * 
     */
    public void zapisz(List<Rower> rowery, String sciezka) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(sciezka))) {
            oos.writeObject(rowery);
        }
    }

    /**
     * Wczytuje listę rowerów z pliku binarnego.
     * Używa ObjectInputStream do deserializacji obiektów Rower.
     *
     * @param sciezka Ścieżka do pliku źródłowego
     * @return Lista wczytanych rowerów lub pusta lista w przypadku błędu/braku pliku
     */
    public List<Rower> wczytaj(String sciezka) {
        File plik = new File(sciezka);
        
        // Sprawdzamy czy plik istnieje
        if (!plik.exists()) {
            return new ArrayList<>();
        }

        // Deserializacja całej listy rowerów
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plik))) {
            return (List<Rower>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>(); // W przypadku błędu zwracamy pustą listę
        }
    }
}