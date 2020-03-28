package com.poker;

public class Card {

    // Qu'est ce qu'une Carte ?
    // C'est un type de couleur et un type de valeur
    // Declaration de variable :
    Rang rang;
    Couleur couleur;
    Rang[] tabRang = {Rang.As, Rang.Roi, Rang.Dame, Rang.Valet, Rang.Dix, Rang.Neuf, Rang.Huit, Rang.Sept, Rang.Six, Rang.Cinq, Rang.Quatre, Rang.Trois, Rang.Deux};
    Couleur[] tabCouleur = {Couleur.Coeur, Couleur.Carreau, Couleur.Pique, Couleur.Trefle};

    public Card(){};

    // Constructor with parameters :
    public Card(Rang pRang, Couleur pCouleur) {
        // Les variables rentrees en parametres vont servir a etre stockees
        this.rang = pRang;           // creation de la variable de stockage qui va prendre la valeur du parametre rang
        this.couleur = pCouleur;     // creation de la variable de stockage qui va prendre la valeur du parametre couleur
        }

    //***** Getter - Accesseurs ******
    public Rang getRang() {
        return this.rang;
    }
    public Couleur getCouleur() {
        return this.couleur;
    }
    public Rang[] getTabRang() { return tabRang; }
    public Couleur[] getTabCouleur() { return tabCouleur; }

    //******* Setter - Mutateurs *******
    public void setCard(Rang pRang, Couleur pCouleur) {
        rang = pRang;
        couleur = pCouleur;
    }

    public String toString() {
        return rang.name() + " de " + couleur.name();
    }

}
