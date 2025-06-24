package konfiguracja;

/**
 * Klasa zawierająca stałe konfiguracyjne dla wątku puli w systemie wypożyczalni rowerów.
 * Centralizuje konfigurację liczby wątków używanych do obsługi równoległych zadań.
 * Wszystkie stałe są publiczne, statyczne i finalne, co zapewnia globalny
 * dostęp do konfiguracji bez możliwości modyfikacji.
 * 
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public class ThreadPoolConfig {

    /**
     * Liczba wątków w puli do wykonywania operacji asynchronicznych.
     * Wartość określa maksymalną liczbę jednocześnie wykonywanych zadań
     * w tle, takich jak filtrowanie dostępnych rowerów czy wyszukiwanie
     * wypożyczeń.
     */
    public static final int LICZBA_WATKOW = 4;
}
