package data_model.serwis;

import data_model.model.Wypozyczenie;
import data_model.model.Rower;
import data_model.model.Klient;
import data_model.model.StatusWypozyczenia;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SerwisWypozyczen {
    private final List<Wypozyczenie> wypozyczenia = new ArrayList<>();

    public void dodajWypozyczenie(Wypozyczenie wypozyczenie) {
    	
    	
    	
        wypozyczenia.add(wypozyczenie);
    }

    public List<Wypozyczenie> pobierzWszystkieWypozyczenia() {
        return new ArrayList<>(wypozyczenia);
    }

    public List<Wypozyczenie> znajdzWypozyczeniaPoKliencie(Klient klient) {
        return wypozyczenia.stream()
                .filter(w -> w.getKlient().equals(klient))
                .collect(Collectors.toList());
    }

    public Optional<Wypozyczenie> znajdzWypozyczenie(Rower rower, Klient klient) {
        return wypozyczenia.stream()
                .filter(w -> w.getRower().equals(rower) && w.getKlient().equals(klient))
                .findFirst();
    }

    public Optional<Wypozyczenie> znajdzWypozyczeniePoId(String id) {
        return wypozyczenia.stream()
                .filter(w -> w.getId().equals(id))
                .findFirst();
    }

    public boolean czyIdIstnieje(String id) {
        return wypozyczenia.stream().anyMatch(w -> w.getId().equals(id));
    }

    public boolean aktualizujWypozyczenie(Rower rower, Klient klient, Wypozyczenie noweWypozyczenie) {
        Optional<Wypozyczenie> opt = znajdzWypozyczenie(rower, klient);
        if (opt.isPresent()) {
            Wypozyczenie w = opt.get();
            w.setDataOd(noweWypozyczenie.getDataOd());
            w.setDataDo(noweWypozyczenie.getDataDo());
            w.setStatus(noweWypozyczenie.getStatus());
            w.setUwagi(noweWypozyczenie.getUwagi());
            return true;
        }
        return false;
    }

    public boolean usunWypozyczenie(Rower rower, Klient klient) {
        return wypozyczenia.removeIf(w -> w.getRower().equals(rower) && w.getKlient().equals(klient));
    }

    public List<Wypozyczenie> pobierzAktywneWypozyczenia(LocalDate dataSymulacji) {
        return wypozyczenia.stream()
                .filter(w -> w.getStatus() == StatusWypozyczenia.AKTYWNE && !w.getDataOd().isAfter(dataSymulacji))
                .collect(Collectors.toList());
    }

    // NOWA METODA
    public List<Wypozyczenie> pobierzAktywneWypozyczenia() {
        return pobierzAktywneWypozyczenia(LocalDate.now());
    }

    public List<Wypozyczenie> pobierzWypozyczeniaSpoznione(LocalDate dataSymulacji) {
        return wypozyczenia.stream()
                .filter(w -> w.getStatus() == StatusWypozyczenia.AKTYWNE && w.getDataDo().isBefore(dataSymulacji))
                .collect(Collectors.toList());
    }

    public List<Rower> pobierzDostepneRowery(List<Rower> wszystkieRowery, LocalDate dataSymulacji) {
        Set<Rower> zajete = wypozyczenia.stream()
                .filter(w -> w.getStatus() != StatusWypozyczenia.ZAKONCZONE &&
                        !(dataSymulacji.isBefore(w.getDataOd()) || dataSymulacji.isAfter(w.getDataDo())))
                .map(Wypozyczenie::getRower)
                .collect(Collectors.toSet());

        return wszystkieRowery.stream()
                .filter(r -> !zajete.contains(r))
                .collect(Collectors.toList());
    }

    public List<Rower> pobierzDostepneRoweryWZakresie(LocalDate dataOd, LocalDate dataDo, List<Rower> wszystkieRowery) {
        Set<Rower> zajete = wypozyczenia.stream()
                .filter(w -> w.getStatus() != StatusWypozyczenia.ZAKONCZONE &&
                        !(dataDo.isBefore(w.getDataOd()) || dataOd.isAfter(w.getDataDo())))
                .map(Wypozyczenie::getRower)
                .collect(Collectors.toSet());

        return wszystkieRowery.stream()
                .filter(r -> !zajete.contains(r))
                .collect(Collectors.toList());
    }

    public void zakonczWypozyczenie(Wypozyczenie wyp) {
        wyp.setStatus(StatusWypozyczenia.ZAKONCZONE);
    }

    public boolean zakonczWypozyczenie(Rower rower, Klient klient) {
        Optional<Wypozyczenie> opt = znajdzWypozyczenie(rower, klient);
        if (opt.isPresent() && opt.get().getStatus() == StatusWypozyczenia.AKTYWNE) {
            opt.get().setStatus(StatusWypozyczenia.ZAKONCZONE);
            return true;
        }
        return false;
    }
}
