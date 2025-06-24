package data_model.model;

/**
 * Enum reprezentujący status wypożyczenia roweru w systemie wypożyczalni.
 * Określa aktualny stan procesu wypożyczenia od momentu rozpoczęcia
 * do zakończenia lub wystąpienia problemów.
 * 
 * @see Wypozyczenie
 */
public enum StatusWypozyczenia {

    /** Wypożyczenie jest aktywne - rower został wypożyczony i nie został jeszcze zwrócony */
    AKTYWNE,

    /** Wypożyczenie zostało zakończone - rower został zwrócony w terminie */
    ZAKONCZONE,

    /** Wypożyczenie z opóźnieniem - rower nie został zwrócony w wyznaczonym terminie */
    SPOZNIENIE
}
