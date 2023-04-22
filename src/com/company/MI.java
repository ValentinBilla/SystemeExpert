package com.company;

public class MI {
    private BDF bdf;
    private BDR bdr;

    public MI(BDF bdf, BDR bdr) {
        this.bdf = bdf;
        this.bdr = bdr;
    }

    public boolean RegleDeclenchee(Regle regle) {
        if(bdf.contient(regle.getConclusion()))
            return true;

        for (String condition: regle.getConditions()) {
            // Il n'y a pas de condition à vérifier (la case est vide).
            if (condition.isEmpty())
                continue;
            // Si une des conditions n'est pas satisfaites, la règle ne marche pas.
            if (!bdf.contient(condition))
                return false;
        }
        return true;
    }
    public void ChainageAvant() {
        int nbregles = bdr.getTaille();

        // Initialisé avec comme valeur par défaut, 'false'.
        boolean [] dejaDeclenchee = new boolean[nbregles];

        boolean saturation = false;
        while(!saturation) {
            saturation = true;

            for(int i=0; i<nbregles; i++) {
                // Si la règle a déjà été utilisée, on ne la prends pas en compte.
                if(dejaDeclenchee[i])
                    continue;
                
                Regle regle = bdr.getRegle(i);  
                // Si une règle n'atteint pas les conditions requises, on ne fait rien.
                if(!RegleDeclenchee(regle))
                    continue;
                
                // Une règle a été activée, ainsi on n'est pas en saturation.
                saturation = false;
                dejaDeclenchee[i] = true;

                bdf.addFait(regle.getConclusion());
            }
        }
    }

    public boolean verifArriere(Regle regle) {
        for (String condition: regle.getConditions()) {
            if (condition.isEmpty())
                return false;
            if (bdf.contient(condition))
                return false;
            if (chainageArriere(condition))
                return false;
        }
        return true;
    }


    public boolean chainageArriere(String but) {
        if (bdf.contient(but))
            return true;
        
        for (Regle regle: bdr.getRegles()) {
            if (!regle.getConclusion().equals(but))
                return false;
            if (!verifArriere(regle))
                return false;
        }
        return true;
    }
}