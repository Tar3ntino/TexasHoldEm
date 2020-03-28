package com.poker;

import java.awt.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Déclaration des attributs
        Scanner sc = new Scanner(System.in);
        int nombreDeJoueurs = 0;

        //Faire tant que le nombre de joueurs n'est pas compris entre 2 et 10
        do {
            try {
                System.out.println("♠️♥️POKER♦️♣️ - TEXAS HOLDEM ");
                System.out.println("Saisissez un nombre de joueurs (2 Min./10 Max.) :");
                nombreDeJoueurs = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("La saisie est incorrecte");
            }
            sc.nextLine();
        } while (nombreDeJoueurs < 2 || nombreDeJoueurs > 10);

        Joueur[] listJoueur = new Joueur[nombreDeJoueurs]; // CREATION d'une listede Nom de joueur, indice 0-joueur0, etc.

        //        // Tableau d'initialisation pour eviter le NullpointerNull lors de l'atribution des Noms de joueur.
//        for (int i=0;i<listJoueur.length;i++){
//            listJoueur[i]=new Joueur("No_Name",10000,card1Player,card2Player);
//        }

        System.out.println("Préparation des " + nombreDeJoueurs + " joueurs à la table ... ");
        //Création d'un nombre de joueur selon la saisie de l'utilisateur

        Paquet paquet = new Paquet();

        for (int i = 0; i < (nombreDeJoueurs); i++) {
            String username;
            boolean matches;
            do {
                System.out.println("Saisir le nom du joueur " + i + " :");
                username = sc.nextLine();
                matches = !username.matches("[a-zA-Z].*");
                if (!matches) {
                    System.out.println("fuck");
                }
            } while (!matches);
            listJoueur[i] = new Joueur(username);

            // NOTION D'EXPRESSIONS REGULIERES si l'on souhaite un format particulier. Ici un String"2" est valide.

            System.out.println("Joueur :" + listJoueur[i]);
            System.out.println(listJoueur[i].getChipsPlayer() + " jetons");
            Card[] pioche = paquet.piocher(2);
            listJoueur[i].setMain(pioche);
            System.out.println("Main du joueur" + i + " :");
            System.out.println(listJoueur[i].getMain()[0] + "\n" + listJoueur[i].getMain()[1]);
            System.out.println("-----------------------------");
        }


        System.out.println("-----------------------------");
        paquet.display();

    }
}


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



