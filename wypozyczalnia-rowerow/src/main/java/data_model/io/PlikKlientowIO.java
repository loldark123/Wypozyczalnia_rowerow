package data_model.io;

import data_model.model.Klient;
import java.io.*;
import java.util.*;

/**
 * Klasa odpowiedzialna za zapis i odczyt danych klientów do/z pliku binarnego.
 * Wykorzystuje DataInputStream/DataOutputStream dla efektywnego przechowywania danych.
 */
public class PlikKlientowIO {
    
    /**
     * Zapisuje listę klientów do pliku binarnego w formacie:
     * Format pliku: [int:rozmiar][UTF:imię][UTF:nazwisko][UTF:numerDowodu][UTF:opis]...
     * 
     * @param klienci Lista klientów do zapisania
     * @param sciezka Ścieżka do pliku docelowego
     * @throws IOException Gdy wystąpi błąd podczas zapisu lub tworzenia folderów
     */
    public void zapisz(List<Klient> klienci, String sciezka) throws IOException {
    	
    	// Tworzymy folder nadrzędny jeśli nie istnieje (np. data/)
        new File(sciezka).getParentFile().mkdirs();
        
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(sciezka))) {
            // Zapisujemy liczbę klientów jako pierwszy element pliku
            dos.writeInt(klienci.size());

            // Następnie dane każdego klienta jako UTF-8 stringi
            for (Klient k : klienci) {
                dos.writeUTF(k.getImie());
                dos.writeUTF(k.getNazwisko());
                dos.writeUTF(k.getNumerDowodu());
                dos.writeUTF(k.getOpis());
            }
        }
    }

    /**
     * Wczytuje listę klientów z pliku binarnego przez metodę zapisz().
     * Metoda odczytuje dane w formacie DataInputStream: najpierw rozmiar listy,
     * następnie dla każdego klienta: imię, nazwisko, numer dowodu i opis.
     * Jeśli plik nie istnieje, zwraca pustą listę bez rzucania wyjątku.
     * 
     * @param sciezka Ścieżka do pliku źródłowego
     * @return Lista wczytanych klientów lub pusta lista w przypadku błędu
     */
    public List<Klient> wczytaj(String sciezka) {
        List<Klient> klienci = new ArrayList<>();
        File plik = new File(sciezka);

        // Sprawdzamy czy plik istnieje
        if (!plik.exists()) {
            return klienci; // plik nie istnieje → zwracamy pustą listę
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(plik))) {
            
            // Wczytujemy liczbę klientów
            int n = dis.readInt();
            for (int i = 0; i < n; i++) {
                klienci.add(new Klient(
                    dis.readUTF(),
                    dis.readUTF(),
                    dis.readUTF(),
                    dis.readUTF()
                ));
            }
        } catch (IOException e) {
            System.err.println("Błąd przy wczytywaniu klientów: " + e.getMessage());
        }

        return klienci;
    }
}
