package com.poker;

public class Paquet {

    //Declaration de variable
    Card[] paquetDeCartes;

    // Constructeur d'un paquet de cartes vides pouvant accueillir 52 cartes

    public Paquet() {

        System.out.println("Creation d'un paquet de 52 cartes");
        Couleur[] allCouleurs = Couleur.class.getEnumConstants();
        Rang[] allRangs = Rang.class.getEnumConstants();
        int nbCouleurs = allCouleurs.length;
        int nbRangs = allRangs.length;
        Card[] paquetDeCartes = new Card[nbCouleurs * nbRangs];
        int counter = 0;
        for (int i = 0; i < nbRangs; i++) {
            for (int j = 0; j < nbCouleurs; j++) {
                paquetDeCartes[counter] = new Card(allRangs[i], allCouleurs[j]);
                counter++;
            }
        }
        this.paquetDeCartes = paquetDeCartes; // la variable de la classe paquet prend la valeur de la variable paquet de carte du constructeur
        // this.variable de ma classe
    } // fin du constructeur

    public void display(){
        for (int i = 0; i<paquetDeCartes.length;i++){
            System.out.println(paquetDeCartes[i].toString());
        }
    }

    public void tirageCarte() {

        int cardRandom = 0;
        int nbCardInit = 52;
        String[] paquetDeCartes = new String[52];

        System.out.println(" Il reste " + nbCardInit + " cartes dans le paquet ");
        int carteRandom = (int) (Math.random() * (nbCardInit - 1));
        System.out.println(" la valeur tiree est " + carteRandom);
        nbCardInit--;

    }
}
