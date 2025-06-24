package data_model.io;

import data_model.model.Wypozyczenie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Obsługuje zapis i odczyt wypożyczeń do/z pliku przy użyciu serializacji.
 * Metoda wykorzystuje ObjectOutputStream do serializacji całej listy
 * jako pojedynczego obiektu. Automatycznie tworzy katalog nadrzędny
 * jeśli nie istnieje. W przypadku błędu I/O wypisuje komunikat na stderr
 * bez przerywania działania programu.
 */
public class PlikWypozyczenIO {

    /**
     * Zapisuje listę wypożyczeń do pliku binarnego.
     * Automatycznie tworzy strukturę katalogów i obsługuje błędy.
     * 
     * @param wypozyczenia Lista wypożyczeń do zapisania
     * @param sciezka Ścieżka do pliku docelowego
     */
    public void zapisz(List<Wypozyczenie> wypozyczenia, String sciezka) {
        File plik = new File(sciezka);
        plik.getParentFile().mkdirs(); // Upewnij się, że katalog istnieje

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plik))) {
            oos.writeObject(wypozyczenia);  // Serializacja listy wypożyczeń
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu wypożyczeń: " + e.getMessage());
        }
    }

    /**
     * Wczytuje listę wypożyczeń z pliku binarnego.
     * Obsługuje przypadki braku pliku i błędów deserializacji.
     * 
     * @param sciezka Ścieżka do pliku źródłowego
     * @return Lista wczytanych wypożyczeń lub pusta lista w przypadku błędu
     */
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
