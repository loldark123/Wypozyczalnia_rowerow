package projekt_java;

import util.ThreadPoolManager;

import javax.swing.*;
import java.awt.*;


/**
 * Główny interfejs graficzny systemu wypożyczalni rowerów.
 * Klasa tworzy okno główne aplikacji z menu umożliwiającym dostęp do wszystkich
 * modułów systemu: zarządzania klientami, rowerami i wypożyczeniami.
 * Służy jako punkt wejścia do całego graficznego interfejsu użytkownika.
 * 
 * @see KlienciGUI
 * @see RoweryGUI
 * @see WypozyczeniaGUI
 */
public class MasterGUI {

    /**
     * Główna metoda uruchamiająca graficzny interfejs użytkownika systemu wypożyczalni rowerów.
     * Tworzy okno Swing z przyciskami umożliwiającymi dostęp do różnych modułów systemu.
     * Interfejs jest uruchamiany w wątku EDT (Event Dispatch Thread) zgodnie z najlepszymi praktykami Swing.
     *
     * @param args argumenty wiersza poleceń
     */ 
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Zamykanie ExecutorService...");
            ThreadPoolManager.shutdown();
        }));
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("System wypożyczalni rowerów");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

            JButton klienciBtn = new JButton("Klienci");
            JButton roweryBtn = new JButton("Rowery");
            JButton wypozyczeniaBtn = new JButton("Wypożyczenia");

            panel.add(klienciBtn);
            panel.add(roweryBtn);
            panel.add(wypozyczeniaBtn);

            klienciBtn.addActionListener(e -> KlienciGUI.main(null));
            roweryBtn.addActionListener(e -> RoweryGUI.main(null)); // To będzie nowa klasa
            wypozyczeniaBtn.addActionListener(e -> WypozyczeniaGUI.main(null)); // I to też

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}