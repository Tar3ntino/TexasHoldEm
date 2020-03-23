package com.poker;

public class Card {

    // Qu'est ce qu'une Carte ?
    // C'est un type de couleur et un type de valeur
    // Declaration de variable :
    Rang rang;
    Couleur couleur;

    Rang[] tabRang = {Rang.As, Rang.Roi, Rang.Dame, Rang.Valet, Rang.Dix, Rang.Neuf, Rang.Huit, Rang.Sept, Rang.Six, Rang.Cinq, Rang.Quatre, Rang.Trois, Rang.Deux};
    Couleur[] tabCouleur = {Couleur.Coeur, Couleur.Carreau, Couleur.Pique, Couleur.Trefle};

    // Default Constructor :
    public Card() {
        System.out.println("Creation d'une carte");
    }

    // Constructor with parameters :
    public Card(Rang pRang, Couleur pCouleur) {
        // Les variables rentrees en parametres vont servir a etre stockees
        System.out.println("Creation d'une carte avec des parametres :");
        this.rang = pRang;           // creation de la variable de stockage qui va prendre la valeur du parametre rang
        this.couleur = pCouleur;     // creation de la variable de stockage qui va prendre la valeur du parametre couleur
        System.out.println(this.rang.name() + " de " + this.couleur.name());
    }

    // Accessors = Getters des variables d'instances (Parameters of constructor)
    public Rang getRang() {
        return this.rang;
    }
    public Couleur getCouleur() {
        return this.couleur;
    }

    // Mutators = Setters des variables d'instances (Parameters of constructor)
    public void setCard(Rang pRang, Couleur pCouleur) {
        rang = pRang;
        couleur = pCouleur;
    }

    public String toString() {
        return rang.name() + " de " + couleur.name();
    }

}
