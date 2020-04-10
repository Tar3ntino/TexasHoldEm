package com.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Paquet {

    // Qu'est ce qu'un Paquet ? C'est un tableau de cartes
    // Declaration de variable d'instance >> Variable d'instance car la valeur de cette variable n'est pas propre
    // a l'instance, l'objet. 2 paquets distincts peuvent avoir des cartes differentes.

    private Card[] paquetDeCartes;
    private static final int nb_iterations = 3;

    public Paquet() {                                                   // Constructeur d'un paquet de cartes vides pouvant accueillir 52 cartes
        System.out.println("Creation d'un paquet de 52 cartes...");        // Initialisation de variables pour le constructeur :
        Couleur[] allCouleurs = Couleur.class.getEnumConstants();       // Il nous faut un tableau contenant toutes les possibilites d'enum "Couleur" + "Rang"
        Rang[] allRangs = Rang.class.getEnumConstants();

        int nbCouleurs = allCouleurs.length;                            // Declaration et initialisation de la taille de chaque tableau "allCouleur" et "allRang"
        int nbRangs = allRangs.length;                                  // Soit nbCouleurs = 4 et nbRangs= 13

        Card[] paquetDeCartes = new Card[nbCouleurs * nbRangs];         // Creation d'un paquet "vides" de 52 cartes (nbCouleurs * nbRangs):

        int counter = 0;                                                // Initialisation d'un compteur pour remplir notre paquet de cartes :
        for (int i = 0; i < nbRangs; i++) {                             // Double boucle qui passe en revue toutes les combinaisons Couleur+Valeur
            for (int j = 0; j < nbCouleurs; j++) {                      // Et qui viennent se stocker dans l'instance paquet par l'appel du constructeur de carte:
                paquetDeCartes[counter] = new Card(allRangs[i], allCouleurs[j]);
                counter++;
            }
        }

        this.paquetDeCartes = paquetDeCartes;                           // la variable de la classe paquet prend la valeur de la variable paquet de carte du constructeur
        // this.variable de ma classe
        melanger();                                                     // Melanger le jeu de cartes
    }                                                                   // fin du constructeur

    private void melanger() {
        Random r = new Random();                                        // On cree un objet Random afin de pouvoir appeler par la suite la methode
        for (int i = 0; i < nb_iterations; i++)                              // NextInt qui va chercher un entier entre 0 et le parametre qui suit
        {                                                               // 1ere boucle : on reitere i fois le battage de cartes, ici 3 fois
            for (int j = 0; j < this.paquetDeCartes.length; j++)             // 2eme boucle : on passe en revue toutes les cartes restantes du paquet
            {
                echanger(r.nextInt(this.paquetDeCartes.length), r.nextInt(this.paquetDeCartes.length));
            }                                                           // a chaque tour de boucle, la methode est appelee et definie 2 entiers aleatoires
        }                                                               // en parametres entre 0 et le nombre de cartes restantes = la taille du paquet
    }

    public void echanger(int i, int j)                                  // Echange 2 cartes du paquet
    {                                                                   // @param i l'indice de la premiere carte a echanger
        Card temp;                                                      // @param j l'indice de la seconde carte a echanger
        temp = this.paquetDeCartes[i];
        this.paquetDeCartes[i] = this.paquetDeCartes[j];
        this.paquetDeCartes[j] = temp;
    }

    public void display() {                                             // Creation d'une methode Display pour voir toutes les cartes de notre paquet
        for (int i = 0; i < paquetDeCartes.length; i++) {               // Cette methode ne renvoie rien, seulement un Syst.Out d'ou le type void
            System.out.println(paquetDeCartes[i].toString());           // Appel de la methode toString de la classe "Carte" qui renvoie le rang et
        }                                                               // la couleur d'une variable de Type "Cartes"
    }

    public int getNombreDeCartes()                                      // Retourne la taille du paquet de cartes
    {
        return this.paquetDeCartes.length;
    }

    public Card[] getPaquetDeCartes() {
        return paquetDeCartes;
    }

    public Card[] piocher(int n) {                       // La methode CopyofRange de la classe Arrays va copier les
        Card[] pioche = Arrays.copyOfRange(this.paquetDeCartes, 0, n); // elements du tableau original, index de debut a copier, index de fin a copier. Dans ce cas CopyOfRange va jusqu'a n pour pouvoir attribuer la seconde carte dans la main du joueur
        this.paquetDeCartes = Arrays.copyOfRange(this.paquetDeCartes, n, this.paquetDeCartes.length); // Paquet est maintenant lisible a partir du rang n car on vient de copier les rangs 0 a n-1 dans un autre paquet (main) de pioche temporaire
        return pioche;
    }

    public void setPaquetDeCartes(Card[] paquetDeCartes) {
        this.paquetDeCartes = paquetDeCartes;
    }

}


//    public void getNombresDeCartes() {
//
//        int cardRandom = 0;
//        int nbCardInit = 52;
//        String[] paquetDeCartes = new String[52];
//
//        System.out.println(" Il reste " + nbCardInit + " cartes dans le paquet ");
//        int carteRandom = (int) (Math.random() * (nbCardInit - 1));
//        System.out.println(" la valeur tiree est " + carteRandom);
//        nbCardInit--;
//    }




