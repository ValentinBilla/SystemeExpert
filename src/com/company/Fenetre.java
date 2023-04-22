
package com.company;

import java.util.Vector;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Fenetre extends JFrame {
    private static final String FICHIER_SELECTION = "selection.txt";
    private static final String FICHIER_FAITS = "faits.txt";
    private static final String FICHIER_DEDUITS = "deduits.txt";
    private static final String FICHIER_BDR = "regles.csv";

    private static final String FICHIER_LOGO = "logo_ecm.png";

    private BDF selection = new BDF(FICHIER_SELECTION);
    private BDF deduits = new BDF(FICHIER_FAITS);
    private BDR bdr = new BDR(FICHIER_BDR);
    private MI moteur = new MI(deduits, bdr);

    private JComboBox<String> comboBoxFaits;

    public Fenetre(String titre) {
        super(titre);

        setSize(800, 800);
        setLayout(null);

        initAccessoires();
        initSelectionFaits();
        initInterfaceMI();
        initTableauRegles();
        setVisible(true);
    }

    public void initAccessoires() {
        ImageIcon icone = new ImageIcon(FICHIER_LOGO);
        icone.setDescription("Logo de Centrale Marseille");
        JLabel image = new JLabel(icone, JLabel.CENTER);
        image.setBounds(10, 10, icone.getIconWidth(), icone.getIconHeight());
        getContentPane().add(image);

        JButton quit = new JButton("Quitter");
        quit.setBounds(690, 20, 75, 30);
        getContentPane().add(quit);
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private String getSelected() {
        return comboBoxFaits.getItemAt(comboBoxFaits.getSelectedIndex());
    }

    public void initSelectionFaits() {
        JLabel label = new JLabel("Selection des faits", SwingConstants.CENTER);
        label.setBounds(20, 70, 300, 20);
        getContentPane().add(label);

        JTextField saisie = new JTextField(60);
        saisie.setBounds(20, 100, 300, 20);
        getContentPane().add(saisie);
        saisie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JTextField textField = (JTextField) event.getSource();
                comboBoxFaits.setSelectedItem(textField.getText());
            }
        });

        DefaultComboBoxModel model = new DefaultComboBoxModel(selection.getFaits().toArray());
        comboBoxFaits = new JComboBox<String>(model);        
        comboBoxFaits.setBounds(20, 130, 300, 20);
        getContentPane().add(comboBoxFaits);
        
        JButton ajouter = new JButton("Ajouter le fait");
        ajouter.setBounds(20, 160, 145, 30);
        getContentPane().add(ajouter);
        ajouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selection.addFait(getSelected());
                System.out.println(selection);
            }
        });

        JButton supprimer = new JButton("Supprimer le fait");
        supprimer.setBounds(175, 160, 145, 30);
        getContentPane().add(supprimer);
        supprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selection.delFait(getSelected());
                System.out.println(selection);
            }
        });
        
        selection.charger(FICHIER_FAITS);
        JList<String> list = new JList<String>(selection);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jScrollPaneSelection = new JScrollPane(list);
        jScrollPaneSelection.setBounds(20, 200, 300, 160);
        getContentPane().add(jScrollPaneSelection);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent le) {
                // On ne fait rien si l'utilisateur est en cours de sélèction.
                if (le.getValueIsAdjusting())
                    return;

                JList<String> liste = (JList<String>) le.getSource();
                String selected = liste.getSelectedValue();
                comboBoxFaits.setSelectedItem(selected);
            }
        });
    }

    
    public void initInterfaceMI() {
        JLabel labelMI = new JLabel("Moteur d'inférences", SwingConstants.CENTER);
        labelMI.setBounds(350, 70, 420, 20);
        getContentPane().add(labelMI);

        JButton chainageAvant = new JButton("Chainage avant");
        chainageAvant.setBounds(350, 100, 205, 30);        
        getContentPane().add(chainageAvant);
        chainageAvant.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selection.sauvegarder(FICHIER_FAITS);
                deduits.charger(FICHIER_FAITS);
                moteur.ChainageAvant();
            }
        });

        JButton chainageArriere = new JButton("Chainage Arriere");
        chainageArriere.setBounds(560, 100, 205, 30);
        getContentPane().add(chainageArriere);
        chainageArriere.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selection.sauvegarder(FICHIER_FAITS);
                deduits.charger(FICHIER_FAITS);
                // moteur.chainageArriere();
            }
        });

        JLabel labelDeduits = new JLabel("Faits déduits", SwingConstants.CENTER);
        labelDeduits.setBounds(350, 140, 420, 20);
        getContentPane().add(labelDeduits);

        deduits.charger(FICHIER_DEDUITS);
        JList<String> list = new JList<String>(deduits);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane jScrollPaneDeduit = new JScrollPane(list);
        jScrollPaneDeduit.setBounds(350, 170, 420, 190);
        getContentPane().add(jScrollPaneDeduit);
    }


    public void initTableauRegles() {
        JLabel labelRegles = new JLabel("Base de Règles", SwingConstants.CENTER);
        labelRegles.setBounds(20, 370, 750, 20);
        getContentPane().add(labelRegles);

        Vector<String> colonnes = new Vector<String>();
        Vector<Vector<String>> donnees = new Vector<Vector<String>>();
        
        for (String intitule: bdr.getIntitules()) {
            colonnes.add(intitule);
        }

        for (Regle regle: bdr.getRegles()) {
            // On converti le schéma directement en vecteur pour ensuite l'ajouter aux données.
            Vector<String> ligne = new Vector<String>(Arrays.asList(regle.getSchema()));
            donnees.add(ligne);
        }

        JTable tableRegles = new JTable(donnees, colonnes);
        tableRegles.setFillsViewportHeight(true);
        JScrollPane jScrollPaneRegles = new JScrollPane(tableRegles);
        jScrollPaneRegles.setBounds(20, 400, 750, 350);
        getContentPane().add(jScrollPaneRegles);
    }
}

