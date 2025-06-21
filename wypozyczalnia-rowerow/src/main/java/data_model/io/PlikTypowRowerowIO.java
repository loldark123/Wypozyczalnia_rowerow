package data_model.io;

import data_model.model.TypRoweru;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa obsługująca zapis i odczyt typów rowerów z/do pliku za pomocą serializacji.
 */
public class PlikTypowRowerowIO {

    /**
     * Zapisuje listę typów rowerów do wskazanego pliku.
     *
     * @param typy    lista typów do zapisania
     * @param sciezka ścieżka pliku
     * @throws IOException gdy wystąpi problem z zapisem
     */
    public void zapisz(List<TypRoweru> typy, String sciezka) throws IOException {
        File plik = new File(sciezka);
        File katalog = plik.getParentFile();
        if (katalog != null && !katalog.exists()) {
            katalog.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plik))) {
            oos.writeObject(typy);
        }
    }

    /**
     * Wczytuje listę typów rowerów z pliku.
     *
     * @param sciezka ścieżka pliku
     * @return lista wczytanych typów lub pusta lista, jeśli plik nie istnieje lub wystąpił błąd
     */
    @SuppressWarnings("unchecked")
    public List<TypRoweru> wczytaj(String sciezka) {
        File plik = new File(sciezka);
        if (!plik.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plik))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<TypRoweru>) obj;
            } else {
                System.err.println("Niepoprawny format pliku: oczekiwano listy TypRoweru.");
                return new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd podczas wczytywania typów rowerów: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
