package com.poker;

public class Card {
    // Caractristique d'une carte + methodes rattachees a la carte


    // Parametres pour la methode (tirageCartes)

    Rang rang;
    Couleur couleur;

    Rang [] tabRang = {Rang.As,Rang.Roi,Rang.Dame,Rang.Valet,Rang.Dix,Rang.Neuf,Rang.Huit,Rang.Sept,Rang.Six,Rang.Cinq,Rang.Quatre,Rang.Trois,Rang.Deux};
    Couleur [] tabCouleur = {Couleur.Coeur,Couleur.Carreau,Couleur.Pique,Couleur.Trefle};


    public Card (){
        System.out.println("Creation d'une carte");
    }

    public Card (Rang pRang, Couleur pCouleur){ // Les variables rentrees en parametres vont servir a etre stockees
        System.out.println("Creation d'une carte avec des parametres :");
        rang = pRang;           // creation de la variable de stockage qui va prendre la valeur du parametre rang
        couleur = pCouleur;     // creation de la variable de stockage qui va prendre la valeur du parametre couleur
    }

    // Fonction tirage de cartes dans Paquet

    // La fonction actuellement choisie un nombre aleatoire dans le tableau parmi un nombre de cartes restantes.
    // Objectif recuperer une carte a un position de tableau
    // Creer un tableau de carte

    // Dans paquet de cartes vides
    // aller en position 1 et affecter (Couleur, hauteur)
    // paquetdecartesvides[i] pour i=52
    // en i=0 Couleur

    //  Accesseurs et Mutateurs

public String toString(){
    return rang.name() + " de " + couleur.name();
}

}
