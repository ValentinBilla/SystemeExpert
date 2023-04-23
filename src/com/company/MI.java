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
        // On détermine s'il est possible de réunir TOUTES les conditions pour activer la règles.
        for (String condition: regle.getConditions()) {
            // Une condition vide compte pour rien, elle n'est vérifiée.
            if (condition.isEmpty())
                continue;
            if (bdf.contient(condition))
                continue;
            // S'il est impossible de satisfaire une des conditions, alors la règle n'est pas activable.
            if (!chainageArriere(condition))
                return false;
        }
        // Aucune condition n'a pas pu être remplie: la règle est activable.
        return true;
    }

    public boolean chainageArriere(String but) {
        // Si le but est déjà dans la base de fait, pas besoin de chercher plus loin...
        if (bdf.contient(but))
            return true;
        
        for (Regle regle: bdr.getRegles()) {
            // Si la règle ne peut pas mener à but, on ne poursuit pas dans cette direction.
            if (!regle.getConclusion().equals(but))
                continue;
            // Dans le cas contraîre, on regarde les conditions sont réunies pour l'activer.
            if (verifArriere(regle))
                return true;
        }
        // Après être passé sur toutes les règles, aucune n'a pu nous mener à but.
        return false;
    }
}