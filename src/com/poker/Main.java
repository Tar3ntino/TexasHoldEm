package com.poker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Déclaration des variables
        Scanner sc = new Scanner(System.in);
        int nombreDeJoueurs = 0;
        String[] mainJoueur = {"Card1", "Card2"};
        Card indicetirage = new Card();

        System.out.println("♠️♥️POKER♦️♣️ - TEXAS HOLDEM ");

        //Faire tant que nb joueurs ne correspond pas
        do {
            System.out.println("Saisissez un nombre de joueurs (2 Min. / 10   Max.) :");
            try {
                nombreDeJoueurs = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("La saisie est incorrecte : " + e.getMessage());
                sc.nextLine();
                continue;
            }
            sc.nextLine();

        } while (nombreDeJoueurs < 2 || nombreDeJoueurs > 10);

        System.out.println("Préparation des " + nombreDeJoueurs + " joueurs à la table");

        //Création d'un nombre de joueur selon la saisie de l'utilisateur

        for (int i = 0; i < (nombreDeJoueurs); i++) {
            System.out.println("Saisir le nom de player" + i + " :");
            String namePlayer = sc.nextLine();
            Joueur joueur1 = new Joueur(namePlayer);

        } // fin de boucle for

        // Creation d'un paquet de 52 cartes
        Paquet paquet = new Paquet();
        paquet.display();


        // DEMARRAGE DE LA PARTIE
        // TIRAGE AU SORT
        // Retirer une carte au paquet de 52 cartes et l'attribuer au tableau MainJoueur1
        // Retirer une carte au paquet de cartes restantes et l'attribuer au tableau MainJoueur2
        // Tant que MainJoueur

//        indicetirage.tirageCarte(); // edite un nombre i parmi une place de 52 cartes defini dans la methode
//        indicetirage.tirageCarte(); // 2eme tirage...

        // Retirer une carte de maniere aleatoire du paquet et l'attribuer en 0.0 du Tableau HandCard Joueur 1
        // Retirer une carte de maniere aleatoire du paquet restant et l'attribuer en 0.1 du tableau HandCard Joueur 1
        // Passer au joueur i+1 et reproduire la meme operation jusqu'a n joueurs

//        Card card1 = new Card(Rang.As,Couleur.Carreau);

        // Tous nos joueurs ont donc maintenant 2 cartes par personnes
        // Il ne reste plus que 52 cartes - 2x n joueurs dans le paquet.


        //

    }
}
