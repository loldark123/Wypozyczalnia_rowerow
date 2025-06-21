package projekt_java;

import data_model.model.TypRoweru;
import data_model.serwis.SerwisTypRoweru;
import data_model.io.PlikTypowRowerowIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TypyRowerowGUI {
    public static void otworz(JFrame owner, SerwisTypRoweru serwis, JComboBox<TypRoweru> typCombo, PlikTypowRowerowIO io) {
        JDialog dialog = new JDialog(owner, "Typy rowerów", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(owner);

        DefaultListModel<TypRoweru> model = new DefaultListModel<>();
        for (TypRoweru t : serwis.pobierzWszystkieTypy()) {
            model.addElement(t);
        }

        JList<TypRoweru> lista = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(lista);

        JTextField nazwaField = new JTextField();
        JTextField opisField = new JTextField();
        JButton dodaj = new JButton("Dodaj typ");
        JButton usun = new JButton("Usuń zaznaczony");

        dodaj.addActionListener(e -> {
            String nazwa = nazwaField.getText().trim();
            String opis = opisField.getText().trim();
            if (nazwa.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nazwa nie może być pusta.");
                return;
            }
            TypRoweru nowy = new TypRoweru(nazwa, opis);
            if (serwis.pobierzWszystkieTypy().contains(nowy)) {
                JOptionPane.showMessageDialog(dialog, "Taki typ już istnieje.");
                return;
            }
            serwis.dodajTypRoweru(nowy);
            model.addElement(nowy);
            try {
                io.zapisz(serwis.pobierzWszystkieTypy(), konfiguracja.KonfiguracjaPlikow.SCIEZKA_TYPY);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Błąd zapisu: " + ex.getMessage());
            }
            if (typCombo != null) {
                typCombo.removeAllItems();
                for (TypRoweru t : serwis.pobierzWszystkieTypy()) typCombo.addItem(t);
            }
        });

        usun.addActionListener((ActionEvent e) -> {
            TypRoweru zaznaczony = lista.getSelectedValue();
            if (zaznaczony == null) {
                JOptionPane.showMessageDialog(dialog, "Zaznacz typ do usunięcia.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(dialog, "Na pewno usunąć typ: " + zaznaczony.getNazwa() + "?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                serwis.usunTyp(zaznaczony);
                model.removeElement(zaznaczony);
                try {
                    io.zapisz(serwis.pobierzWszystkieTypy(), konfiguracja.KonfiguracjaPlikow.SCIEZKA_TYPY);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Błąd zapisu: " + ex.getMessage());
                }
                if (typCombo != null) {
                    typCombo.removeAllItems();
                    for (TypRoweru t : serwis.pobierzWszystkieTypy()) typCombo.addItem(t);
                }
            }
        });

        JPanel dolnyPanel = new JPanel();
        dolnyPanel.setLayout(new BoxLayout(dolnyPanel, BoxLayout.Y_AXIS));

        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.add(new JLabel("Nazwa:"));
        formPanel.add(nazwaField);
        formPanel.add(new JLabel("Opis:"));
        formPanel.add(opisField);

        JPanel przyciskiPanel = new JPanel();
        przyciskiPanel.add(dodaj);
        przyciskiPanel.add(usun);

        dolnyPanel.add(formPanel);
        dolnyPanel.add(przyciskiPanel);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(dolnyPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
