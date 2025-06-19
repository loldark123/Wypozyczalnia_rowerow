package data_model.io;

import data_model.model.Klient;
import java.io.*;
import java.util.*;

public class PlikKlientowIO {

    public void zapisz(List<Klient> klienci, String sciezka) throws IOException {
    	
    	 // 🔧 Tworzymy folder nadrzędny jeśli nie istnieje (np. data/)
        new File(sciezka).getParentFile().mkdirs();
        
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(sciezka))) {
            dos.writeInt(klienci.size());
            for (Klient k : klienci) {
                dos.writeUTF(k.getImie());
                dos.writeUTF(k.getNazwisko());
                dos.writeUTF(k.getNumerDowodu());
                dos.writeUTF(k.getOpis());
            }
        }
    }

    public List<Klient> wczytaj(String sciezka) {
        List<Klient> klienci = new ArrayList<>();
        File plik = new File(sciezka);

        if (!plik.exists()) {
            return klienci; // plik nie istnieje → zwracamy pustą listę
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(plik))) {
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
