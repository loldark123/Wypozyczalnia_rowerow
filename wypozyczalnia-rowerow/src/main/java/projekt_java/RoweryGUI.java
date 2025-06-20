// GUI do zarządzania rowerami (lista, dodawanie, usuwanie, zapis/odczyt z pliku) z walidacją duplikatów

package projekt_java;

import data_model.model.Rower;
import data_model.model.TypRoweru;
import data_model.serwis.SerwisRowerow;
import data_model.serwis.SerwisTypRoweru;
import data_model.io.PlikRowerowIO;
import konfiguracja.KonfiguracjaPlikow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RoweryGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Zarządzanie rowerami");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            SerwisRowerow serwis = new SerwisRowerow();
            SerwisTypRoweru serwisTypow = new SerwisTypRoweru();
            PlikRowerowIO io = new PlikRowerowIO();

            // Wczytaj rowery z pliku
            List<Rower> listaZRozszerzenia = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_ROWERY);
            for (Rower r : listaZRozszerzenia) {
                serwis.dodajRower(r);
            }

            // Przykładowe typy (jeśli nie masz wczytywania typów z pliku)
            if (serwisTypow.pobierzWszystkieTypy().isEmpty()) {
                serwisTypow.dodajTypRoweru(new TypRoweru("Górski", "Do jazdy w terenie"));
                serwisTypow.dodajTypRoweru(new TypRoweru("Miejski", "Do jazdy po mieście"));
            }

            DefaultListModel<Rower> model = new DefaultListModel<>();
            for (Rower r : listaZRozszerzenia) model.addElement(r);

            JList<Rower> lista = new JList<>(model);
            lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                String txt = String.format("[%s] %s %s (%d\")\nOpis: %s",
                        value.getTyp().getNazwa(),
                        value.getMarka(),
                        value.getModel(),
                        value.getRozmiarKola(),
                        value.getOpis());
                JLabel label = new JLabel("<html>" + txt.replace("\n", "<br>") + "</html>");
                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                }
                label.setOpaque(true);
                return label;
            });

            JScrollPane scrollPane = new JScrollPane(lista);
            panel.add(scrollPane, BorderLayout.CENTER);

            JPanel przyciski = new JPanel();
            JButton dodaj = new JButton("Dodaj rower");
            JButton usun = new JButton("Usuń rower");
            JButton odswiez = new JButton("Odśwież");
            przyciski.add(dodaj);
            przyciski.add(usun);
            przyciski.add(odswiez);
            panel.add(przyciski, BorderLayout.SOUTH);

            dodaj.addActionListener(e -> {
                JTextField marka = new JTextField();
                JTextField modelR = new JTextField();
                JTextField rozmiar = new JTextField();
                JTextField opis = new JTextField();
                JComboBox<TypRoweru> combo = new JComboBox<>(serwisTypow.pobierzWszystkieTypy().toArray(new TypRoweru[0]));

                JPanel input = new JPanel(new GridLayout(5, 2));
                input.add(new JLabel("Marka:")); input.add(marka);
                input.add(new JLabel("Model:")); input.add(modelR);
                input.add(new JLabel("Rozmiar koła:")); input.add(rozmiar);
                input.add(new JLabel("Opis:")); input.add(opis);
                input.add(new JLabel("Typ:")); input.add(combo);

                int result = JOptionPane.showConfirmDialog(frame, input, "Nowy rower", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int rRozmiar = Integer.parseInt(rozmiar.getText().trim());
                        Rower nowy = new Rower((TypRoweru) combo.getSelectedItem(), marka.getText(), modelR.getText(), rRozmiar, opis.getText());

                        if (serwis.pobierzWszystkieRowery().contains(nowy)) {
                            throw new IllegalArgumentException("Taki rower już istnieje w systemie.");
                        }

                        serwis.dodajRower(nowy);
                        io.zapisz(serwis.pobierzWszystkieRowery(), KonfiguracjaPlikow.SCIEZKA_ROWERY);
                        model.addElement(nowy);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage());
                    }
                }
            });

            usun.addActionListener(e -> {
                Rower r = lista.getSelectedValue();
                if (r == null) {
                    JOptionPane.showMessageDialog(frame, "Wybierz rower do usunięcia.");
                    return;
                }
                int conf = JOptionPane.showConfirmDialog(frame, "Usunąć rower: " + r + "?", "Potwierdź", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    boolean ok = serwis.usunRower(r);
                    if (ok) {
                        try {
                            io.zapisz(serwis.pobierzWszystkieRowery(), KonfiguracjaPlikow.SCIEZKA_ROWERY);
                            model.removeElement(r);
                            JOptionPane.showMessageDialog(frame, "Usunięto rower.");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "Błąd zapisu pliku: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Nie udało się usunąć roweru.");
                    }
                }
            });

            odswiez.addActionListener(e -> {
                List<Rower> nowaLista = io.wczytaj(KonfiguracjaPlikow.SCIEZKA_ROWERY);
                serwis.wyczysc();
                model.clear();
                for (Rower r : nowaLista) {
                    serwis.dodajRower(r);
                    model.addElement(r);
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
