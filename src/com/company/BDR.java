package com.company;

import java.util.ArrayList;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.util.stream.Collectors;


public class BDR {
    private String[] intitules;
    private ArrayList<Regle> regles;

    public BDR(String emplacement){
        try (
            FileReader descripteur = new FileReader(emplacement, StandardCharsets.UTF_8);
            BufferedReader buffer = new BufferedReader(descripteur);
        ) {
            // On ignore la ligne de déclaration des colonnes.
            // XXX: Modifier la structure des règles pour avoir plusieurs conclusions.
            intitules = buffer.readLine().split(",");
            
            regles = (ArrayList<Regle>) buffer.lines()
                .map(line -> new Regle(line.split(",")))
                .collect(Collectors.toCollection(ArrayList::new));

            System.out.println("BDR: Les règles ont correctement pu être récupérées.");
        } catch (IOException e) {
            System.out.println("BDR: Erreur lors de l'ouverture du fichier.");
        }
    }

    public ArrayList<Regle> getRegles() {
        return regles;
    }

    public Regle getRegle(int i) {
        return regles.get(i);
    }

    public String[] getIntitules() {
        return intitules;
    }

    public int getTaille() {
        return regles.size();
    }

    @Override
    public String toString() {
        return "BDR{" +
                "contenu=" + regles +
                ", taille=" + getTaille() +
                '}';
    }
}
