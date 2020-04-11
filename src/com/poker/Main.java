package com.poker;

import java.util.*;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        // Déclaration des attributs
        Scanner sc = new Scanner(System.in);
        int nombreDeJoueurs = 0;
        int newPot = 0;
        int potTotal = newPot;
        int tourDeParole = 0;


        // INITIALISATION du nombre de joueurs :
        // Faire tant que celui ci n'est pas compris entre 2 et 10

        do {
            try {
                System.out.println("♠️♥️POKER♦️♣️ - TEXAS HOLDEM ");
                System.out.print("Saisissez un nombre de joueurs (2 Min./10 Max.) :");
                nombreDeJoueurs = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("La saisie est incorrecte");
            }
            sc.nextLine();
        } while (nombreDeJoueurs < 2 || nombreDeJoueurs > 10);

        Joueur[] listJoueur = new Joueur[nombreDeJoueurs];  // Creation d'une liste de Joueurs

        // INITIALISATION DE LA PARTIE : Creation d'un paquet, de "N" joueurs, affectation des "Noms de Joueurs",
        // attribution des mains Joueurs du paquet melange

        System.out.println("Préparation de " + nombreDeJoueurs + " joueurs à la table ... ");
        Paquet paquet = new Paquet();
        for (int i = 0; i < (nombreDeJoueurs); i++) {
            String username;
            boolean matches;
            do {
                System.out.print("Saisir le nom du joueur " + i + " :");
                username = sc.nextLine();
                matches = username.matches("[a-zA-Z].*");
                if (!matches) {
                    System.out.println("Non valide, veuillez reessayer :");
                }
            } while (!matches);
            listJoueur[i] = new Joueur(username);               // Voir NOTION D'EXPRESSIONS REGULIERES
            System.out.print("Joueur : " + listJoueur[i]);
            Card[] pioche = paquet.piocher(2);
            listJoueur[i].setMain(pioche);                          // MAJ de la main du joueur

            for (int j = 0; j < 2; j++) {
                listJoueur[i].getCardsCommunesAndHandPlayer()[j] = pioche[j];
            } // MAJ du tableau combinaison du joueur

            System.out.println("Main : " + listJoueur[i].getMain()[0].toString() + listJoueur[i].getMain()[1]);
            System.out.println("-----------------------------");
        }

        // TIRAGE AU SORT du DEALER et attribution des Small Blind et Big Blind

        Random startDealer = new Random();
        int dealer = startDealer.nextInt(nombreDeJoueurs);
        int smallBlind = dealer + 1;
        int bigBlind = smallBlind + 1;
        LinkedList<Joueur> joueurs = new LinkedList<>();

        // On remplit l'objet LinkedList de joueurs avec une boucle For qui recupere tous les joueurs :
        for (Joueur joueur : listJoueur) {
            joueurs.add(joueur);
        }

        System.out.println("Tirage Dealer : " + listJoueur[dealer].getNamePlayer() + " a la distribution");
        // On place les i joueurs de debut de tableau se trouvant avant la position du dealer + dealer compris en fin de "pile joueur"
        int rankToBeCancelled = 0; //
        for (int i = 0; i <= dealer; i++) {
            joueurs.add(nombreDeJoueurs + i, listJoueur[i]);
            rankToBeCancelled++;
        }

        for (int i = 0; i < rankToBeCancelled; i++) {
            joueurs.removeFirst(); // suppression des n joueurs avant le dealer de la tete de la pile
        }

        // Mise en queue de pile le SB :
        joueurs.getFirst().setBetPlayer(20); // on set la mise du joueur SB situe en tete de pile
        potTotal = potTotal + 20;
        joueurs.getFirst().setChipsPlayer(joueurs.getFirst().getChipsPlayer() - joueurs.getFirst().getBetPlayer()); // on met a jour son compteur de jetons
        System.out.println(" Small Blind : " + joueurs.getFirst() + " Met " + joueurs.getFirst().getBetPlayer() + " jetons");
        System.out.println("-----------------------------");
        joueurs.addLast(joueurs.getFirst()); // on place le joueur SB actuellement en tete de pile > en queue de pile
        joueurs.removeFirst();

        // Mise en queue de pile le BB :
        joueurs.getFirst().setBetPlayer(40);// on set la mise du joueur BB situe en tete de pile
        potTotal = potTotal + 40;
        joueurs.getFirst().setChipsPlayer(joueurs.getFirst().getChipsPlayer() - joueurs.getFirst().getBetPlayer()); // on met a jour son compteur de jetons
        System.out.println(" Big Blind : " + joueurs.getFirst() + " Met " + joueurs.getFirst().getBetPlayer() + " jetons");
        System.out.println("-----------------------------");
        joueurs.addLast(joueurs.getFirst()); // on place le joueur BB actuellement en tete de pile > en queue de pile
        joueurs.removeFirst();

        System.out.println("Valeur du Pot Total : " + potTotal);

        // 1er TOUR DE PAROLE PRE FLOP
        do {
            System.out.println("1er TOUR DE PAROLE PRE FLOP");
            tourDeParole = 1;
            //Option 1 : avoir 2 listes de joueurs, une pour conserver la relance en premier,
            // l'autre pour conserver la BB en premier
            //Option 2 : avoir une seule liste, mais au lieu de vérifier les mises du premier
            //et du dernier, on vérifie toutes les mises. Du coup pas besoin de modifier la liste
            //quand qqn relance

            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

            // *** TIRAGE DU FLOP ***
            Card[] cartesCommunes = tirageFlop(paquet);
            joueurs.addFirst(joueurs.getLast()); // On remet la BB en tete de liste
            joueurs.removeLast(); // // Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB

            // *** COMBINAISONS PAR JOUEUR RESTANT EN JEU SUR LA MAIN
            for (int i = 0; i < joueurs.size(); i++) {
                joueurs.get(i).getCardsCommunesAndHandPlayer()[2] = cartesCommunes[0];
                joueurs.get(i).getCardsCommunesAndHandPlayer()[3] = cartesCommunes[1];
                joueurs.get(i).getCardsCommunesAndHandPlayer()[4] = cartesCommunes[2];
                System.out.println("Combinaison du joueur " + joueurs.get(i).getNamePlayer() + " : " + joueurs.get(i).getCardsCommunesAndHandPlayer()[0] + joueurs.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[6]);
            }

            // 2e TOUR DE PAROLE POST FLOP
            System.out.println("---------------------------------------------------------");
            System.out.println("2e TOUR DE PAROLE POST FLOP");
            tourDeParole = 2;
            resetBetPlayer(joueurs);
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);


            // *** TIRAGE DE LA TURN ***
            tirageTurn(paquet, cartesCommunes);
//            joueurs.addFirst(joueurs.getLast()); // On remet la BB en tete de liste
//            joueurs.removeLast(); // Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB

            // *** COMBINAISONS PAR JOUEUR RESTANT EN JEU SUR LA MAIN
            for (int i = 0; i < joueurs.size(); i++) {
                joueurs.get(i).getCardsCommunesAndHandPlayer()[5] = cartesCommunes[3];
                System.out.println("Combinaison du joueur " + joueurs.get(i).getNamePlayer() + " : " + joueurs.get(i).getCardsCommunesAndHandPlayer()[0] + joueurs.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[6]);
            }

            System.out.println("---------------------------------------------------------");
            System.out.println("3e TOUR DE PAROLE POST TURN");
            tourDeParole = 3;
            resetBetPlayer(joueurs);
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

            // *** TIRAGE DE LA TURN ***
            tirageRiver(paquet, cartesCommunes);
//            joueurs.addFirst(joueurs.getLast()); // On remet la BB en tete de liste
//            joueurs.removeLast(); // Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB


            // *** COMBINAISONS PAR JOUEUR RESTANT EN JEU SUR LA MAIN
            for (int i = 0; i < joueurs.size(); i++) {
                joueurs.get(i).getCardsCommunesAndHandPlayer()[6] = cartesCommunes[4];
                System.out.println("Combinaison du joueur " + joueurs.get(i).getNamePlayer() + " : " + joueurs.get(i).getCardsCommunesAndHandPlayer()[0] + joueurs.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[6]);
                System.out.println("Check Two Pair :" + joueurs.get(i).checkStraight(joueurs.get(i).getCardsCommunesAndHandPlayer()));
                System.out.println("Check Three of a Kind :" + joueurs.get(i).checkThreeOfAKind(joueurs.get(i).getCardsCommunesAndHandPlayer()));
                System.out.println("Check Straight :" + joueurs.get(i).checkStraight(joueurs.get(i).getCardsCommunesAndHandPlayer()));
                System.out.println("Check Flush :" + joueurs.get(i).checkFlush(joueurs.get(i).getCardsCommunesAndHandPlayer()));
                System.out.println("Check Four of a Kind :" + joueurs.get(i).checkFourOfAKind(joueurs.get(i).getCardsCommunesAndHandPlayer()));
                System.out.println("Check Straight Flush :" + joueurs.get(i).checkStraightFlush(joueurs.get(i).getCardsCommunesAndHandPlayer(),joueurs.get(i).checkStraight(joueurs.get(i).getCardsCommunesAndHandPlayer()),joueurs.get(i).checkFlush(joueurs.get(i).getCardsCommunesAndHandPlayer())));
            }


            System.out.println("---------------------------------------------------------");
            System.out.println("4e et dernier TOUR DE PAROLE POST RIVER");
            tourDeParole = 4;
            resetBetPlayer(joueurs);
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

            System.out.println();
            System.out.println(" TO DO LIST / CORRECTIFS A APPORTER : ");
            System.out.println();
            System.out.println(" * Redonner la parole a partir du rang Big Bling apres tirage du flop - Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB");
            System.out.println(" * Revoir la condition de fin de main car plusieurs joueurs peuvent encore etre en jeu, pas forcement tant qu'il ne reste plus d'1 joueur");
            System.out.println(" de la relance. On peut miser all in meme si < mise min autorisee ");
            System.out.println(" * Revoir affichage seulement du tour 3 et 4 : Cas Call min Post Flop (tour2) = BB, call min turn et river (tour3et4) = 2BB min");
            System.out.println(" * Remplacer Suivre a (0) par Parole && Relancer par call si betPlayer.first=betplayer.last=0 ");

        } while (joueurs.size() != 1); // Fin de la main / mene : Faire tant qu'il ne reste plus qu'un joueur, il remportera donc le pot
        System.out.println(joueurs.getFirst().getNamePlayer() + " gagne le pot : " + potTotal);

    } // Fin de la methode main

    private static Card[] tirageFlop(Paquet paquet) {

        Card[] cartesCommunes = new Card[5]; // Initialisation du tableau vide des 5 cartes communes :

        for (int i = 0; i < 5; i++) {
            cartesCommunes[i] = new Card(); // On remplit notre tableau de 5 cartes avec des cartes "vides"
        }

        paquet.piocher(1); // On brule la 1ere carte du paquet
        Card[] pioche = paquet.piocher(3); // On pioche 3 cartes ce qui nous renvoit un tableau de 3 cartes

        for (int i = 0; i < 3; i++) {
            cartesCommunes[i] = pioche[i]; // On affecte les 3 cartes a notre tableau de cartes communes
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("Tirage du Flop :  " + cartesCommunes[0] + "/" + cartesCommunes[1] + "/" + cartesCommunes[2]);
        System.out.println("---------------------------------------------------------");
        return cartesCommunes;
    }

    private static void tirageTurn(Paquet paquet, Card[] cartesCommunes) {
        paquet.piocher(1);
        cartesCommunes[3] = paquet.piocher(1)[0];
        System.out.println("---------------------------------------------------------");
        System.out.println("Tirage de Turn :  " + cartesCommunes[0] + "/" + cartesCommunes[1] + "/" + cartesCommunes[2] + "/" + cartesCommunes[3]);
        System.out.println("---------------------------------------------------------");
    }

    private static void tirageRiver(Paquet paquet, Card[] cartesCommunes) {
        paquet.piocher(1);
        cartesCommunes[4] = paquet.piocher(1)[0];
        System.out.println("---------------------------------------------------------");
        System.out.println("Tirage de River :  " + cartesCommunes[0] + "/" + cartesCommunes[1] + "/" + cartesCommunes[2] + "/" + cartesCommunes[3] + "/" + cartesCommunes[4]);
        System.out.println("---------------------------------------------------------");
    }

    private static int toursDeTableJusquaAccordMise(LinkedList<Joueur> joueurs, int potTotal, int tourDeParole) {
        potTotal = premierTourDeTable(joueurs, potTotal, tourDeParole); // renvoie un int potPotal
        System.out.println("Valeur du pot :" + potTotal);
        potTotal = tourDeParolePostFlop(joueurs, potTotal, tourDeParole);
        return potTotal;
    }

    private static void resetBetPlayer(LinkedList<Joueur> joueurs) {
        for (Joueur joueur : joueurs) {
            joueur.setBetPlayer(0);
        }
    }

    private static int tourDeParolePostFlop(LinkedList<Joueur> joueurs, int potTotal, int tourDeParole) {
        while (joueurs.getFirst().getBetPlayer() != joueurs.getLast().getBetPlayer()) { // Faire tant que la mise du joueur a parler n'est pas au niveau du dernier ayant parle
            potTotal = tourDeParole(joueurs, potTotal, tourDeParole);
        }
        return potTotal;
    }

    private static int premierTourDeTable(LinkedList<Joueur> joueurs, int potTotal, int tourDeParole) {
        int nbJoueurs = joueurs.size();
        for (int i = 0; i < nbJoueurs; i++) {
            potTotal = tourDeParole(joueurs, potTotal, tourDeParole);
        }
        return potTotal;
    }

    private static int tourDeParole(LinkedList<Joueur> joueurs, int potTotal, int tourDeParole) {
        int choix = 0;
        do {
            try {
                System.out.println(joueurs.getFirst().getNamePlayer() + " Jetons :" + joueurs.getFirst().getChipsPlayer() + " - [Pot Total:" + potTotal + "]" + "\n" + " a la parole : 1/ Passer 2/ Suivre (" + (joueurs.getLast().getBetPlayer() - joueurs.getFirst().getBetPlayer() + ") 3/ Relancer (>=" + ((joueurs.getLast().getBetPlayer()) == 0 ? (40 + ")") : (2 * joueurs.getLast().getBetPlayer()) - joueurs.getFirst().getBetPlayer() + ")")));
                Scanner choixJoueur = new Scanner(System.in);
                choix = choixJoueur.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("La saisie est incorrecte");
            }

            switch (choix) {
                case 1:
                    System.out.println(joueurs.getFirst() + " passe son tour ");
                    joueurs.removeFirst();
                    break;

                case 2:
                    int add = joueurs.getLast().getBetPlayer() - joueurs.getFirst().getBetPlayer();
                    joueurs.getFirst().setBetPlayer(joueurs.getFirst().getBetPlayer() + add);
                    joueurs.getFirst().setChipsPlayer(joueurs.getFirst().getChipsPlayer() - add);
                    System.out.println(joueurs.getFirst() + " suit " + add + " jetons");
                    joueurs.addLast(joueurs.getFirst());
                    joueurs.removeFirst();
                    potTotal = potTotal + add;
                    break;

                case 3:
                    Scanner chx = new Scanner(System.in);
                    int relance = 0;

                    if (tourDeParole == 1) {
                        System.out.print(joueurs.getFirst() + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                        relance = chx.nextInt();
                        while (relance < (2 * joueurs.getLast().getBetPlayer()) - joueurs.getFirst().getBetPlayer() || relance > joueurs.getFirst().getChipsPlayer()) {
                            System.out.println("Vous devez relancer au minimum le double de la mise actuelle OU vous n'avez pas assez de jetons ");
                            relance = chx.nextInt();
                        }
                    } else if (tourDeParole == 2) {
                        System.out.print(joueurs.getFirst() + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                        relance = chx.nextInt();
                        while ((relance < ((joueurs.getLast().getBetPlayer()) == 0 ? (40) : (2 * joueurs.getLast().getBetPlayer()) - joueurs.getFirst().getBetPlayer())) || (relance > joueurs.getFirst().getChipsPlayer())) {
                            System.out.println("Vous devez relancer au minimum la valeur de la BB Post Flop OU le double de la mise actuelle OU vous n'avez pas assez de jetons ");
                            relance = chx.nextInt();
                        }
                    } else if (tourDeParole == 3 || tourDeParole == 4) {
                        System.out.print(joueurs.getFirst() + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                        relance = chx.nextInt();
                        while ((relance < ((joueurs.getLast().getBetPlayer()) == 0 ? (2 * 40) : (2 * joueurs.getLast().getBetPlayer()) - joueurs.getFirst().getBetPlayer())) || (relance > joueurs.getFirst().getChipsPlayer())) {
                            System.out.println("Vous devez relancer au minimum la valeur de 2BB au 3e/4e tour de parole OU le double de la mise actuelle OU vous n'avez pas assez de jetons ");
                            relance = chx.nextInt();
                        }
                    }
                    joueurs.getFirst().setBetPlayer(joueurs.getFirst().getBetPlayer() + relance);
                    joueurs.getFirst().setChipsPlayer(joueurs.getFirst().getChipsPlayer() - relance);
                    System.out.println(joueurs.getFirst() + " relance a " + joueurs.getFirst().getBetPlayer() + " jetons");
                    joueurs.addLast(joueurs.getFirst());
                    joueurs.removeFirst();
                    potTotal = potTotal + relance;
                    break;
            }
            System.out.println(potTotal);
            return potTotal;
        } while (choix != 1 && choix != 2 && choix != 3); // Faire tant que le choix de l'utilisateur ne correspond pas aux actions proposees
    }
}

