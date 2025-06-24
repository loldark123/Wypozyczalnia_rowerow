package util;

import konfiguracja.ThreadPoolConfig;
import java.util.concurrent.*;

import javax.swing.SwingWorker;

/**
 * Menedżer puli wątków dla systemu wypożyczalni rowerów.
 * Klasa zapewnia centralny punkt zarządzania wątkami w aplikacji, umożliwiając
 * wykonywanie operacji asynchronicznych bez blokowania interfejsu użytkownika.
 * Wykorzystuje wzorzec Singleton dla zapewnienia jednej instancji ExecutorService
 * w całej aplikacji.
 * 
 * @see ThreadPoolConfig
 * @see ExecutorService
 * @see Executors#newFixedThreadPool(int)
 * @see data_model.serwis.SerwisWypozyczen
 */
public class ThreadPoolManager {

    /**
     * Statyczna instancja ExecutorService z pulą wątków o stałym rozmiarze.
     * Liczba wątków jest konfigurowana przez {@link ThreadPoolConfig#LICZBA_WATKOW}.
     * Instancja jest tworzona przy pierwszym załadowaniu klasy i pozostaje
     * aktywna przez cały czas życia aplikacji.
     * 
     * @see ThreadPoolConfig#LICZBA_WATKOW
     * @see Executors#newFixedThreadPool(int)
     */
    private static final ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolConfig.LICZBA_WATKOW);

    /**
     * Zwraca instancję ExecutorService do wykonywania zadań asynchronicznych.
     * Metoda zapewnia dostęp do wspólnej puli wątków używanej w całej aplikacji.
     * ExecutorService może być używany do wykonywania zadań typu Callable lub Runnable
     * w tle bez blokowania głównego wątku interfejsu użytkownika.
     * 
     * @return statyczna instancja ExecutorService z pulą wątków
     */
    public static ExecutorService getExecutor() {
        return executor;
    }

     /**
     * Inicjuje uporządkowane zamknięcie ExecutorService.
     * Metoda pozwala na dokończenie aktualnie wykonywanych zadań, ale nie przyjmuje
     * nowych zadań do wykonania. Powinna być wywołana podczas zamykania aplikacji
     * aby zapewnić prawidłowe zwolnienie zasobów systemowych.
     */
    public static void shutdown() {
        executor.shutdown();
    }

    /**
     * Inicjuje natychmiastowe zamknięcie ExecutorService.
     * Metoda próbuje zatrzymać wszystkie aktualnie wykonywane zadania i nie przyjmuje
     * nowych zadań do wykonania. W przeciwieństwie do {@link #shutdown()}, ta metoda
     * nie czeka na naturalne zakończenie zadań, lecz próbuje je przerwać.
     */
    public static void shutdownNow() {
        executor.shutdownNow();
    }
}
