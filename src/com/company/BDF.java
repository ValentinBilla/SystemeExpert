package com.company;

import java.util.ArrayList;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import java.io.FileWriter;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class BDF implements ListModel<String> {
    private ArrayList<String> faits;
    // private ArrayList<Float> coefs;

    public BDF(String emplacement){
        charger(emplacement);
    }
    
    public void charger(String emplacement) {
        try (
            FileReader descripteur = new FileReader(emplacement, StandardCharsets.UTF_8);
            BufferedReader buffer = new BufferedReader(descripteur);
        ) {
            // On collecte toute les lignes du fichier, en filtrant au préalable celles qui sont vides.
            faits = buffer.lines()
                .filter(line -> line != null && !line.isEmpty() && !line.isBlank())
                .collect(Collectors.toCollection(ArrayList::new));

            notifyListeners();
            System.out.println("BDF: " + getTaille() + " faits ont put être extraits du document.");
        } catch (IOException e) {
            System.out.println("BDF: Erreur lors de la lecture du fichier.");
        }
    }

    public void sauvegarder(String emplacement) {
        try (FileWriter fw = new FileWriter(emplacement, StandardCharsets.UTF_8)) {
            for (String fait: faits)
                fw.write(fait + '\n');
        }
        catch(IOException  e) {
            System.out.println("BDF: Erreur lors de la sauvegarde des faits.");
        }
    }

    public boolean contient(String fait) {
        return faits.contains(fait);
    }

    @Override
    public String toString() {
        return "BDF{" +
                "faits=" + faits +
                ", taille=" + getTaille() +
                '}';
    }

    public ArrayList<String> getFaits() {
        return faits;
    }

    public String getFait(int i) {
        return faits.get(i);
    }

    public void addFait(String fait) {
        System.out.println(fait);
        // Si un fait est déjà dans la BDF on ne l'ajoute pas.
        if (contient(fait))
           return;

        faits.add(fait);
        notifyListeners();
    }

    public void delFait(String fait) {
        faits.remove(fait);
        notifyListeners();
    }

    public void reset() {
        faits.clear();
        notifyListeners();
    }

    public int getTaille() {
        return faits.size();
    }

    // IMPLEMENTATION OF ListModel
    
    public String getElementAt(int index) {
        return faits.get(index);
    }

    public int getSize() {
        return faits.size();
    }

    ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>();

    public void removeListDataListener(javax.swing.event.ListDataListener l) {
        listeners.remove(l);
    }

    public void addListDataListener(javax.swing.event.ListDataListener l) {
        listeners.add(l);
    }

    private void notifyListeners() {
        ListDataEvent le = new ListDataEvent(
            this, ListDataEvent.CONTENTS_CHANGED,
            0, getSize()
        );
        for (ListDataListener listener: listeners) {
            listener.contentsChanged(le);
        }
    }
}