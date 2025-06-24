package projekt_java;

/**
 * Główna klasa aplikacji systemu wypożyczalni rowerów.
 * Zawiera metodę main() służącą jako punkt wejścia do aplikacji.
 * Klasa odpowiada za uruchomienie głównego interfejsu graficznego
 * systemu wypożyczalni rowerów poprzez delegację do MasterGUI.
 */
public class App {

    /**
     * Główna metoda aplikacji - punkt wejścia do systemu wypożyczalni rowerów.
     * Metoda deleguje uruchomienie aplikacji do głównego interfejsu graficznego
     * MasterGUI, który zapewnia dostęp do wszystkich modułów systemu:
     * zarządzania klientami, rowerami i wypożyczeniami.
     * 
     * @param args argumenty wiersza poleceń
     */ 
    public static void main(String[] args) {
        MasterGUI.main(args); // <-- To wszystko!
    }
}