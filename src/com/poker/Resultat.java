package com.poker;

import java.util.ArrayList;
import java.util.List;

public class Resultat {

    Combinaison combinaison; // Variable pour retourner le nom de la combinaison
    //Tableau ou liste de rangs
    List<Rang> hauteurEtKickers = new ArrayList<>();
    Couleur couleurMax;

    public Resultat() {
    }
    public Couleur getCouleurMax() {
        return couleurMax;
    }

    public void setCouleurMax(Couleur couleurMax) {
        this.couleurMax = couleurMax;
    }

    public List<Rang> getHauteurEtKickers() {
        return hauteurEtKickers;
    }
    public void setHauteurEtKickers(List<Rang> hauteurEtKickers) {
        this.hauteurEtKickers = hauteurEtKickers;
    }

    public Combinaison getCombinaison() {
        return combinaison;
    }

    public void setCombinaison(Combinaison combinaison) {
        this.combinaison = combinaison;
    }

    public String toString() {
        return combinaison.name() + " " + combinaison.displayResultat(hauteurEtKickers, couleurMax);
    }

}

