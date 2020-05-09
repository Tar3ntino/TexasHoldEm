package com.poker;

import java.util.*;
import java.util.LinkedList;

public class Main {

    private static int potTotal = 0;

    public static void main(String[] args) throws InterruptedException {

        int tourDeParole = 0;

        // INITIALISATION DES JOUEURS :
        Scanner sc = new Scanner(System.in);
        int nombreDeJoueurs = 0;

        do {
            try {
                System.out.println("♠️♥️POKER♦️♣️ - TEXAS HOLDEM");
                System.out.println("By Patxi & Bixente [Covid-19 Corporation]");
                System.out.print("Saisissez un nombre de joueurs (2 Min./10 Max.) :");
                nombreDeJoueurs = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("La saisie est incorrecte");
            }
            sc.nextLine();
        } while (nombreDeJoueurs < 2 || nombreDeJoueurs > 10);

        Joueur[] listJoueur = new Joueur[nombreDeJoueurs];

        // ************************************
        // INITIALISATION DE LA PARTIE :
        // - Creation d'un paquet
        // - Affectation des "Noms de Joueurs" dans le tableau de "N" joueurs initialise precedemment
        // - Attribution des mains Joueurs du paquet melange
        // ************************************

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

            // CARDS DEALT : Affecte 2 cartes au joueur listJoueur[i] :
            distributionMainJoueur(listJoueur[i], paquet);
        }

        // ************************************
        // TIRAGE AU SORT du DEALER :
        // - Declaration d'un objet Random puis d'un entier "dealer"
        // - On appelle la methode nextInt de la classe "Random" de Java qui selectionne de maniere aleatoire un entier
        // entre 0 et l'entier place en parametres
        // - Instanciation d'un objet LinkedList qui servira de "File de Joueur" pour la distribution de la parole
        // ************************************

        Random startDealer = new Random();
        int dealer = startDealer.nextInt(nombreDeJoueurs);
        LinkedList<Joueur> joueurs = new LinkedList<>();

        // ************************************
        // INITIALISATION DE LA LISTE JOUEUR :
        // - Remplissage de l'objet LinkedList "joueurs" via la methode "initialisationListeJoueur" :
        // - Cette methode :
        // ► Efface la liste precedente dans le cas ou celle ci ne serait pas vierge
        // ► Affecte individuellement chacun des joueurs du tableau SSI ils leur restent des jetons
        // ************************************

        int numMain=0;
        do {

            initialisationListeJoueur(listJoueur, joueurs);
            numMain++; // Incrementation de la main suivante
            System.out.println("Affichage de la liste des joueurs sur la Main N." + numMain + ":" + joueurs);
            System.out.println("Tirage Dealer : " + listJoueur[dealer].getNamePlayer() + " a la distribution");

            // ************************************
            // On place les i joueurs de debut de tableau se trouvant avant la position du dealer + dealer compris en fin de "pile joueur"
            // ************************************

            int rankToBeCancelled = 0; //
            for (int i = 0; i <= dealer; i++) {
                joueurs.add(nombreDeJoueurs + i, listJoueur[i]);
                rankToBeCancelled++;
            }

            for (int i = 0; i < rankToBeCancelled; i++) {
                joueurs.removeFirst(); // suppression des n joueurs avant le dealer de la tete de la pile
            }

            // ************************************
            // Mise en queue de pile le SB :
            // ************************************

//        joueurs.getFirst().setBetPlayer(20); // on set la mise du joueur SB situe en tete de pile (SURTOUT PAS !!)
            int chipsPlayerBefore = joueurs.getFirst().getChipsPlayer();
            joueurs.getFirst().setSmallBlind(20);
            joueurs.getFirst().setSmallBlind(true);
            int sB = joueurs.getFirst().getSmallBlind();

            joueurs.getFirst().setChipsPlayer(chipsPlayerBefore - sB); // MAJ du stack joueur en position SB
            potTotal = potTotal + sB;
            System.out.println(" Small Blind : " + joueurs.getFirst() + " Met " + sB + " jetons");
            System.out.println("-----------------------------");
            joueurs.addLast(joueurs.getFirst()); // on place le joueur SB actuellement en tete de pile > en queue de pile
            joueurs.removeFirst();

            // ************************************
            // Mise en queue de pile le BB :
            // ************************************
            chipsPlayerBefore = joueurs.getFirst().getChipsPlayer();
            joueurs.getFirst().setBigBlind(40);
            joueurs.getFirst().setBigBlind(true);
            int bB = joueurs.getFirst().getBigBlind();

            joueurs.getFirst().setChipsPlayer(chipsPlayerBefore - bB); // MAJ du stack joueur en position BB
            potTotal = potTotal + bB;

            System.out.println(" Big Blind : " + joueurs.getFirst() + " Met " + bB + " jetons");
            System.out.println("-----------------------------");
            joueurs.addLast(joueurs.getFirst()); // on place le joueur BB actuellement en tete de file > en queue de file
            joueurs.removeFirst();

            System.out.println("Valeur du Pot Total : " + potTotal);

            // ************************************
            //             PRE-FLOP
            // ************************************

            System.out.println(" [ PRE-FLOP ] ");
            tourDeParole = 1;
            tourDenchere(tourDeParole, joueurs, sB, bB);

            if (joueurs.size() == 1) {

                // Retourner le joueur gagnant :
                continue;
            }
            // ************************************
            //              FLOP
            // ************************************

            Card[] cartesCommunes = tirageFlop(paquet);

            // On verifie que la SB et la BB sont toujours en jeu, sinon on ajuste le nombre de rang a repasser en tete de file
            // en consequence

            checkSmallAndBig(joueurs);

            // ************************************
            // *** COMBINAISONS PAR JOUEUR RESTANT EN JEU SUR LA MAIN
            // ************************************

            for (int j = 0; j < joueurs.size(); j++) {
                joueurs.get(j).getCardsCommunesAndHandPlayer()[2] = cartesCommunes[0];
                joueurs.get(j).getCardsCommunesAndHandPlayer()[3] = cartesCommunes[1];
                joueurs.get(j).getCardsCommunesAndHandPlayer()[4] = cartesCommunes[2];
                System.out.println("Combinaison du joueur " + joueurs.get(j).getNamePlayer() + " : " + joueurs.get(j).getCardsCommunesAndHandPlayer()[0] + joueurs.get(j).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(j).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(j).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(j).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(j).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(j).getCardsCommunesAndHandPlayer()[6]);
            }

            // ************************************
            // 2e TOUR DE PAROLE POST FLOP
            // ************************************

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ FLOP ] ");

            tourDeParole = 2;
            resetBetPlayer(joueurs);
            resetBlindPlayer(joueurs);
            sB = 0;
            bB = 0;
            tourDenchere(tourDeParole, joueurs, sB, bB);

            if (joueurs.size() == 1) {

                // Retourner le joueur gagnant :
                continue;
            }

//                // On initialise la valeur MiseMax a 1 pour que les getBetPlayers actuellement a 0 soit differents et que
//                // l'on puisse realiser le 1er tour de table
////            miseMax = 1;
//                // On donne la parole a tout le monde meme a la Big Blind en fin de file  = 1er tour de table :
//                // Lorsque la boucle va atteindre la taille du tableau, on refait une verif en bouclant sur la liste des joueurs
//                // restant et en verifiant les getBetPlayer. S'ils sont tous egaux a Mise Max, on sort de la boucle via le "break"
//
//                sizeBefore = joueurs.size();
//                for (int i = 0; i < sizeBefore; i++) {
//                    sizeBefore = joueurs.size();
//                    paroleJoueur(joueurs.get(i), joueurs, tourDeParole, sB, bB);
//                    int sizeAfter = joueurs.size();
//                    if (sizeBefore == sizeAfter) { // Cela signifie que le joueur reste en jeu et ne s'est pas couche.
//                        //Si on dépasse
//                        if (joueurs.get(i).getBetPlayer() != 0) { // Si le joueur a mise, on peut changer la valeur de miseMax, s'il a checke on laisse la miseMax a 1
//                            miseMax = getMiseMax(joueurs, bB);
//                        }
//                        // Si le joueur suivant sort de la taille de la liste :
//                        if (i + 1 >= sizeAfter) {
//                            for (int j = 0; j < sizeAfter; j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
//                                System.out.println("joueur.get(" + j + ").getBetPlayer() :" + joueurs.get(j).getBetPlayer());
//                                if (joueurs.get(j).getBetPlayer() != miseMax) {
//                                    i = -1;
//                                    // Si a la lecture de notre file Joueur, certains joueurs ont encore une mise != de la miseMax
//                                    // On sort de la verification de joueur et on retourne en debut de boucle,
//                                    // On met -1 car va etre incremente pour retomber a 0
//                                }
//                                // Sinon le joueur a mise la meme somme l'on peut verifier le joueur suivant
//                            }
//                        }           // Sinon on incrémente normalement
//
//                    } else {                    // Si le joueur s'est couche, alors sizeBefore != sizeAfter
//                        //Si on dépasse
//                        if (i >= sizeAfter) {   // Si le joueur sort de la taille de la liste ou est en derniere position
//                            for (int j = 0; j < joueurs.size(); j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
//                                if (joueurs.get(j).getBetPlayer() != miseMax) {
//                                    i = -1;     // On met -1 car va etre incremente pour retomber a 0
//                                } else {
//                                    break;
//                                }
//                            }              // on revient au debut
//                        } else {
//                            i--;
//                        }
//                    }
//                    System.out.println("------------------------------------");
//                    System.out.println("Valeur de i" + i);
//                    System.out.println("Taille de la liste joueur" + joueurs.size());
//                    System.out.println("Valeur de miseMax :" + miseMax);
//                    if (checkAccordMise(joueurs, miseMax) == true) {
//                        break;
//                    }
//                    System.out.println("------------------------------------");
//                }


            // ************************************
            // *** TIRAGE DE LA TURN ***
            // ************************************

            tirageTurn(paquet, cartesCommunes);

            // ************************************
            // *** COMBINAISONS PAR JOUEUR RESTANT EN JEU SUR LA MAIN
            // ************************************

            for (int i = 0; i < joueurs.size(); i++) {
                joueurs.get(i).getCardsCommunesAndHandPlayer()[5] = cartesCommunes[3];
                System.out.println("Combinaison du joueur " + joueurs.get(i).getNamePlayer() + " : " + joueurs.get(i).getCardsCommunesAndHandPlayer()[0] + joueurs.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[6]);
            }

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ TURN ] ");
            tourDeParole = 3;

            resetBetPlayer(joueurs);
            resetBlindPlayer(joueurs);
            sB = 0;
            bB = 0;

            tourDenchere(tourDeParole, joueurs, sB, bB);

            if (joueurs.size() == 1) {

                // Retourner le joueur gagnant :
                continue;
            }

//
//            // On initialise la valeur MiseMax a 1 pour que les getBetPlayers actuellement a 0 soit differents et que
//            // l'on puisse realiser le 1er tour de table
//            miseMax = 1;
//            // On donne la parole a tout le monde meme a la Big Blind en fin de file  = 1er tour de table :
//            // Lorsque la boucle va atteindre la taille du tableau, on refait une verif en bouclant sur la liste des joueurs
//            // restant et en verifiant les getBetPlayer. S'ils sont tous egaux a Mise Max, on sort de la boucle via le "break"
//
//            sizeBefore = joueurs.size();
//            for (int i = 0; i < sizeBefore; i++) {
//                sizeBefore = joueurs.size();
//                paroleJoueur(joueurs.get(i), joueurs, tourDeParole, sB, bB);
//                int sizeAfter = joueurs.size();
//                if (sizeBefore == sizeAfter) { // Cela signifie que le joueur reste en jeu et ne s'est pas couche.
//                    //Si on dépasse
//                    if (joueurs.get(i).getBetPlayer() != 0) { // Si le joueur a mise, on peut changer la valeur de miseMax, s'il a checke on laisse la miseMax a 1
//                        miseMax = getMiseMax(joueurs, bB);
//                    }
//                    // Si le joueur suivant sort de la taille de la liste :
//                    if (i + 1 >= sizeAfter) {
//                        for (int j = 0; j < sizeAfter; j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
//                            System.out.println("joueur.get(" + j + ").getBetPlayer() :" + joueurs.get(j).getBetPlayer());
//                            if (joueurs.get(j).getBetPlayer() != miseMax) {
//                                i = -1;
//                                // Si a la lecture de notre file Joueur, certains joueurs ont encore une mise != de la miseMax
//                                // On sort de la verification de joueur et on retourne en debut de boucle,
//                                // On met -1 car va etre incremente pour retomber a 0
//                            }
//                            // Sinon le joueur a mise la meme somme l'on peut verifier le joueur suivant
//                        }
//                    }           // Sinon on incrémente normalement
//
//                } else {                    // Si le joueur s'est couche, alors sizeBefore != sizeAfter
//                    //Si on dépasse
//                    if (i >= sizeAfter) {   // Si le joueur sort de la taille de la liste ou est en derniere position
//                        for (int j = 0; j < joueurs.size(); j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
//                            if (joueurs.get(j).getBetPlayer() != miseMax) {
//                                i = -1;     // On met -1 car va etre incremente pour retomber a 0
//                            } else {
//                                break;
//                            }
//                        }              // on revient au debut
//                    } else {
//                        i--;
//                    }
//                }
//                System.out.println("------------------------------------");
//                System.out.println("Valeur de i" + i);
//                System.out.println("Taille de la liste joueur" + joueurs.size());
//                System.out.println("Valeur de miseMax :" + miseMax);
//                if (checkAccordMise(joueurs, miseMax) == true) {
//                    break;
//                }
//                System.out.println("------------------------------------");
//            }

            // ************************************
            // *** TIRAGE DE LA RIVER ***
            // ************************************

            tirageRiver(paquet, cartesCommunes);

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ RIVER ] ");
            tourDeParole = 4;

            resetBetPlayer(joueurs);
            resetBlindPlayer(joueurs);
            sB = 0;
            bB = 0;

            tourDenchere(tourDeParole, joueurs, sB, bB);

            if (joueurs.size() == 1) {

                // Retourner le joueur gagnant :
                continue;
            }

//            // On initialise la valeur MiseMax a 1 pour que les getBetPlayers actuellement a 0 soit differents et que
//            // l'on puisse realiser le 1er tour de table
//            miseMax = 1;
//            // On donne la parole a tout le monde meme a la Big Blind en fin de file  = 1er tour de table :
//            // Lorsque la boucle va atteindre la taille du tableau, on refait une verif en bouclant sur la liste des joueurs
//            // restant et en verifiant les getBetPlayer. S'ils sont tous egaux a Mise Max, on sort de la boucle via le "break"
//
//            sizeBefore = joueurs.size();
//            for (int i = 0; i < sizeBefore; i++) {
//                sizeBefore = joueurs.size();
//                paroleJoueur(joueurs.get(i), joueurs, tourDeParole, sB, bB);
//                int sizeAfter = joueurs.size();
//                if (sizeBefore == sizeAfter) { // Cela signifie que le joueur reste en jeu et ne s'est pas couche.
//                    //Si on dépasse
//                    if (joueurs.get(i).getBetPlayer() != 0) { // Si le joueur a mise, on peut changer la valeur de miseMax, s'il a checke on laisse la miseMax a 1
//                        miseMax = getMiseMax(joueurs, bB);
//                    }
//                    // Si le joueur suivant sort de la taille de la liste :
//                    if (i + 1 >= sizeAfter) {
//                        for (int j = 0; j < sizeAfter; j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
//                            System.out.println("joueur.get(" + j + ").getBetPlayer() :" + joueurs.get(j).getBetPlayer());
//                            if (joueurs.get(j).getBetPlayer() != miseMax) {
//                                i = -1;
//                                // Si a la lecture de notre file Joueur, certains joueurs ont encore une mise != de la miseMax
//                                // On sort de la verification de joueur et on retourne en debut de boucle,
//                                // On met -1 car va etre incremente pour retomber a 0
//                            }
//                            // Sinon le joueur a mise la meme somme l'on peut verifier le joueur suivant
//                        }
//                    }           // Sinon on incrémente normalement
//
//                } else {                    // Si le joueur s'est couche, alors sizeBefore != sizeAfter
//                    //Si on dépasse
//                    if (i >= sizeAfter) {   // Si le joueur sort de la taille de la liste ou est en derniere position
//                        for (int j = 0; j < joueurs.size(); j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
//                            if (joueurs.get(j).getBetPlayer() != miseMax) {
//                                i = -1;     // On met -1 car va etre incremente pour retomber a 0
//                            } else {
//                                break;
//                            }
//                        }              // on revient au debut
//                    } else {
//                        i--;
//                    }
//                }
//                System.out.println("------------------------------------");
//                System.out.println("Valeur de i" + i);
//                System.out.println("Taille de la liste joueur" + joueurs.size());
//                System.out.println("Valeur de miseMax :" + miseMax);
//                if (checkAccordMise(joueurs, miseMax) == true) {
//                    break;
//                }
//                System.out.println("------------------------------------");
//            }

            // ************************************
            // *** SHOWDOWN / ABBATAGE ***
            // ************************************

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ SHOWDOWN ] ");

            // A Chaque joueur restant encore en jeu, on :
            for (int i = 0; i < joueurs.size(); i++) {
                joueurs.get(i).getCardsCommunesAndHandPlayer()[6] = cartesCommunes[4]; // ...affecte la derniere carte River au tableau de combi de chaque joueur
                System.out.println("Combinaison du joueur " + joueurs.get(i).getNamePlayer() + " : " + joueurs.get(i).getCardsCommunesAndHandPlayer()[0] + joueurs.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[6]);
                System.out.println(joueurs.get(i).getNamePlayer() + " possede une : " + CombinaisonUtil.getCombinaison(joueurs.get(i)) + " Force :" + CombinaisonUtil.getCombinaison(joueurs.get(i)).getCombinaison().value);
            }

            System.out.println("---------------------------------------------------------");
            System.out.println("Renvoie de la combinaison gagnante");

            List<Joueur> joueursWithHighestCombinaisonValue = getJoueursWithHighestCombinaisonValue(joueurs);
            List<Joueur> joueursWithHighestCombinaison = CombinaisonUtil.departagerJoueurs(joueursWithHighestCombinaisonValue);

            for (Joueur joueur : joueursWithHighestCombinaison) {
                int gainJetonsJoueur = potTotal / joueursWithHighestCombinaison.size();
                int ancienSolde = joueur.getChipsPlayer();
                System.out.println(joueur.getNamePlayer() + " gagne " + gainJetonsJoueur);
                joueur.setChipsPlayer(ancienSolde + gainJetonsJoueur);
            }

            potTotal = 0; // Remise a zero du pot pour la prochaine Main
            for (Joueur joueur : joueurs) {
                if (joueur.getChipsPlayer() == 0) {
                    joueurs.remove(joueur);
                }
            }

            System.out.println(" ************************************************************************************");
            System.out.println(" TO DO LIST / CORRECTIFS A APPORTER : ");
            System.out.println(" * PROBLEME ACTUEL DANS LE CAS OU TOUT LE MONDE CHECK POST FLOP CAR MISEMAX=1....> 1er tour de Parole");
            System.out.println(" * Revoir la condition de la relance car on doit pouvoir miser all in meme si < mise min autorisee");
            System.out.println(" * Revoir affichage seulement du tour 3 et 4 : Cas Call min Post Flop (tour2) = BB, call min turn et river (tour3et4) = 2BB min");
            System.out.println(" * Remplacer Suivre a (0) par Parole && Relancer par call si betPlayer.first=betplayer.last=0 ");
            System.out.println(" * Gerer les cas de Pot 1 Pot 2 etc....");
            System.out.println(" * Desactiver le choix relancer si les jetons du joueur sont inferieur a la mise min de relance");
            System.out.println(" * Creation d'une structure de blind");
            System.out.println(" ************************************************************************************");

        } while (joueurs.size() != 1);
        // Fin de la main / Tant qu'il reste plus qu'un joueur, il remportera donc le pot

        System.out.println(" Sortie du do while");


    } // Fin de la methode main

    private static void tourDenchere(int tourDeParole, LinkedList<Joueur> joueurs, int sB, int bB) throws InterruptedException {
        // On initialise au debut la valeur MiseMax a savoir la BB :
        int miseMax = getMiseMax(joueurs, bB);

        // On donne la parole a tout le monde meme a la Big Blind en fin de file  = 1er tour de table :
        // Lorsque la boucle va atteindre la taille du tableau, on refait une verif en bouclant sur la liste des joueurs
        // restant et en verifiant les getBetPlayer. S'ils sont tous egaux a Mise Max, on sort de la boucle via le "break"

        int sizeBefore = joueurs.size();
        for (int i = 0; i < sizeBefore; i++) {
            sizeBefore = joueurs.size();
            if (joueurs.size() == 1) {
                // Retourner le joueur gagnant :
                break;
            } else {
                paroleJoueur(joueurs.get(i), joueurs, tourDeParole, sB, bB);
                int sizeAfter = joueurs.size();
                if (sizeBefore == sizeAfter) { // Cela signifie que le joueur reste en jeu et ne s'est pas couche.
                    //Si on dépasse
                    if (i + 1 >= sizeAfter) { // Si le joueur suivant sort de la taille de la liste
                        for (int j = 0; j < sizeAfter; j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
                            System.out.println("joueur.get(" + j + ").getBetPlayer() :" + joueurs.get(j).getBetPlayer());
                            if (joueurs.get(j).getBetPlayer() != miseMax) {
                                i = -1;
                                // Si a la lecture de notre file Joueur, certains joueurs ont encore une mise differente de la miseMax
                                // on sort de la verification de joueur et on retourne en debut de boucle,
                                // on met -1 car va etre incremente pour retomber a 0
                            }
                            // Sinon cela signifie que tout le monde a mise la meme somme et que l'on peut sortir de la boucle initiale
                        }
                    } //else{                // Sinon on incrémente normalement
//                            i++;
//                        }
                } else {                    // Si le joueur s'est couche, alors sizeBefore != sizeAfter
                    //Si on dépasse
                    if (i >= sizeAfter) {   // Si le joueur sort de la taille de la liste ou est en derniere position
                        for (int j = 0; j < joueurs.size(); j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
                            if (joueurs.get(j).getBetPlayer() != miseMax) {
                                i = -1;              // On met -1 car va etre incremente pour retomber a 0
                            } else {
                                break;
                            }
                        }              // on revient au debut
                    } else {
                        i--;
                    }
                }
                miseMax = getMiseMax(joueurs, bB); // on MAJ la mise Max dans le cas ou l'un des joueurs aurait relance avant de verifier
                // si toutes les mises sont equivalents
                System.out.println("------------------------------------");
                System.out.println("Valeur de i" + i);
                System.out.println("Taille de la liste joueur" + joueurs.size());
                System.out.println("Valeur de miseMax :" + miseMax);
                if (checkAccordMise(joueurs, miseMax) == true) {
                    break;
                }
                System.out.println("------------------------------------");
            }
        }
    }


    private static void checkSmallAndBig(LinkedList<Joueur> joueurs) {
        int rangARemettreEnTeteDeFile = 0;
        if (smallBlindIsStillInGame(joueurs)) {
            rangARemettreEnTeteDeFile++;
        }
        if (bigBlindIsStillInGame(joueurs)) {
            rangARemettreEnTeteDeFile++;
        }
        System.out.println("Valeur de rangAMettreEnTeteDeFile" + rangARemettreEnTeteDeFile);

        for (int i = 0; i < rangARemettreEnTeteDeFile; i++) {
            System.out.println("Affichage de la file joueur AVANT ajout/retrait" + joueurs);
            joueurs.addFirst(joueurs.getLast()); // On remet le joueur rang bB en tete de liste
            joueurs.removeLast();
            System.out.println("Affichage de la file joueur APRES ajout/retrait" + joueurs);
        }
    }

    private static void resetBlindPlayer(LinkedList<Joueur> joueurs) {
        for (Joueur joueur : joueurs) {
            joueur.setSmallBlind(0);
            joueur.setBigBlind(0);
        }
    }

    private static boolean smallBlindIsStillInGame(LinkedList<Joueur> joueurs) {
        boolean result = false;
        for (Joueur joueur : joueurs) {
            if (joueur.isSmallBlind == true) {
                result = true;
            }
        }
        return result;
    }

    private static boolean bigBlindIsStillInGame(LinkedList<Joueur> joueurs) {
        boolean result = false;
        for (Joueur joueur : joueurs) {
            if (joueur.isBigBlind == true) {
                result = true;
            }
        }
        return result;
    }

    private static void premierTourDeTable(int tourDeParole, LinkedList<Joueur> joueurs, int sB, int bB) throws
            InterruptedException {
        int nbJoueurs = joueurs.size();
        for (int i = 0; i < nbJoueurs; i++) {
            paroleJoueur(joueurs.get(i), joueurs, tourDeParole, sB, bB);
        }
    }

    private static void initialisationListeJoueur(Joueur[] listJoueur, LinkedList<Joueur> joueurs) {
        joueurs.clear(); // On vide la file dans le cas ou elle contient des donnees pre
        for (Joueur joueur : listJoueur) { // Initialisation des joueurs crees en debut de partie dans la variable list joueur
            if (joueur.getChipsPlayer() != 0) { // Si le joueur du tableau de joueur a encore des jetons, alors on l'ajoute a la file joueurs
                joueurs.add(joueur);
            }
        }
    }

    // ************************************
    // La fonction distribution joueur pioche les 2 cartes de la variable paquet en parametre puis affecte la 1ere puis
    // la seconde carte au joueur place en parametre
    // ************************************

    private static void distributionMainJoueur(Joueur joueur, Paquet paquet) {
        Card[] pioche = paquet.piocher(2);
        joueur.setMain(pioche);                          // MAJ de la main du joueur

        for (int j = 0; j < 2; j++) {
            joueur.getCardsCommunesAndHandPlayer()[j] = pioche[j];
        } // MAJ du tableau combinaison du joueur

        System.out.println("Main : " + joueur.getMain()[0].toString() + joueur.getMain()[1]);
        System.out.println("-----------------------------");
    }

    // ***************************************************************************************************
    // 1e COMPARAISON DES VALEURS COMBI... RENVOIE UN TABLEAU DE JOUEURS AVEC LA VALUE COMBI LA PLUS FORTE

    private static List<Joueur> getJoueursWithHighestCombinaisonValue(LinkedList<Joueur> joueurs) {

        List<Joueur> joueurWithHighestCombinaisonValue = new ArrayList<>();

        // Initialisation des variables de comparaison avec le 1er joueur restant
        Resultat resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(0));
        joueurWithHighestCombinaisonValue.add(joueurs.get(0));

        for (int i = 1; i < joueurs.size(); i++) {

            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getCombinaison().getValue() > resultatTemp.getCombinaison().getValue()) {
                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
                joueurWithHighestCombinaisonValue.clear();
                joueurWithHighestCombinaisonValue.add(joueurs.get(i));
            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getCombinaison().getValue() == resultatTemp.getCombinaison().getValue()) {
                joueurWithHighestCombinaisonValue.add(joueurs.get(i));
            }
        }
        return joueurWithHighestCombinaisonValue;
    }

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

    private static void resetBetPlayer(LinkedList<Joueur> joueurs) {
        for (Joueur joueur : joueurs) {
            joueur.setBetPlayer(0);
        }
    }

    private static void tourDeParoleJusquaAccordMise(LinkedList<Joueur> joueurs, int tourDeParole,
                                                     int miseMax, int sB, int bB) throws InterruptedException {
        int i = 0;
        while (joueurs.get(i).getBetPlayer() != getMiseMax(joueurs, bB)) {
            // Faire tant que la mise du joueur a parler n'est pas au niveau du dernier ayant parle
            int sizeBefore = joueurs.size();
            paroleJoueur(joueurs.get(i), joueurs, tourDeParole, sB, bB);
            int sizeAfter = joueurs.size();
            if (sizeBefore == sizeAfter) {
                //Si on dépasse
                if (i + 1 >= sizeAfter) {
                    i = 0;
                } else {
                    //On incrémente normalement
                    i++;
                }
            } else {
                //Si on dépasse
                if (i >= sizeAfter) {
                    i = 0;
                }
            }
        }
    }

    // ************************************
    // METHODE PAROLE JOUEUR :
    // - Initialisation du choix par default a zero avant qu'il parle
    // - On recupere la valeur de miseMax a chaque debut de parole via la methode getMiseMax en parcourant les mises
    // de chaque joueur.
    // < Do... while> : Tant que le joueur n'a pas renseigne un choix valide, on repete la demande de saisie
    //      ► Creation d'une classe ConsoleInput puis instanciation d'un objet de cette classe "choixJoueur"
    //      ► Ecriture d'une methode "ReadLine" au sein de cette classe
    //      ► Appel de cette methode lors de la saisie du joueur et stockage dans une variable de type "String"
    //
    //      Methode "DoActionJoueur" qui recupere la variable "choix" et agit en consequence (Passe/Suit/Relance)
    // ************************************


    private static void paroleJoueur(Joueur joueur, LinkedList<Joueur> joueurs, int tourDeParole, int sB, int bB) throws
            InterruptedException {
        int choix = 0;
        int miseMax = getMiseMax(joueurs, bB);
        do {
            System.out.println(joueur + " Jetons :" + joueur.getChipsPlayer() + " - [Pot Total:" + potTotal + "]" + "\n" + " a la parole : 1/ Passer 2/ Suivre (" + getMontantSuivre(joueur, miseMax) + ") 3/ Relancer (>=" + getMontantRelance(joueur, miseMax, tourDeParole) + ")");
            ConsoleInput choixJoueur = new ConsoleInput(30, 5);
            String stringJoueur = choixJoueur.readLine();
            if (stringJoueur == null) {
                //le joueur n'a pas répondu
                choix = 1;
            } else {
                try {
                    //On controle que la string récupérée est bien convertible en int
                    choix = Integer.parseInt(stringJoueur);
                } catch (NumberFormatException e) {
                    System.out.println("La saisie est incorrecte");
                }
            }
            doActionJoueur(joueur, joueurs, tourDeParole, choix, miseMax, sB, bB);
        } while (choix != 1 && choix != 2 && choix != 3); // Faire tant que le choix de l'utilisateur ne correspond pas aux actions proposees
    }

    private static int getMontantSuivre(Joueur joueur, int miseMax) {
        return miseMax - joueur.getBigBlind() - joueur.getSmallBlind() - joueur.getBetPlayer();
    }

    private static int getMontantRelance(Joueur joueur, int miseMax, int tourDeParole) {
        int resultat = 0;

        if (tourDeParole == 1) {
            resultat = ((2 * miseMax) - joueur.getBigBlind() - joueur.getSmallBlind() - joueur.getBetPlayer());
        } else if (tourDeParole == 2) { //miseMax=0 un joueur doit jouer au moins 40 et sinon le double
            if (miseMax == 0) { // Si personne n'a mise...
                resultat = 40;  // ...alors on renvoie le montant de la BB
            } else {
                resultat = (2 * miseMax) - joueur.getBetPlayer();
            }
        } else if (tourDeParole == 3 || tourDeParole == 4) { //miseMax=0 un joueur doit jouer au moins 40 et sinon le double
            if (miseMax == 0) { // Si personne n'a mise...
                resultat = 2 * 40;  // ...alors on renvoie 2 fois le montant de la BB
            } else {
                resultat = (2 * miseMax) - joueur.getBetPlayer();
            }
        }
        return resultat;
    }

    private static void doActionJoueur(Joueur joueur, LinkedList<Joueur> joueurs, int tourDeParole, int choix,
                                       int miseMax, int sB, int bB) {
        switch (choix) {
            case 1:
                System.out.println(joueur + " passe son tour ");
                joueurs.remove(joueur);
                break;

            case 2:
                int add = getMontantSuivre(joueur, miseMax);
                if (joueur.getSmallBlind() > 0) {
                    joueur.setBetPlayer(sB + add);
                    joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
                } else if (joueur.getBigBlind() > 0) {
                    joueur.setBetPlayer(bB + add);
                    joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
                } else {
                    joueur.setBetPlayer(joueur.getBetPlayer() + add);
                    joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
                }
                System.out.println(joueur + " suit " + add + " jetons");
                potTotal = potTotal + add;
                break;

            case 3:
                Scanner chx = new Scanner(System.in);
                int relance = 0;

                if (tourDeParole == 1) {
                    System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                    relance = chx.nextInt();
                    while (relance < (2 * miseMax) - joueur.getBetPlayer() || relance > joueur.getChipsPlayer()) {
                        System.out.println("Vous devez relancer au minimum le double de la mise actuelle OU vous n'avez pas assez de jetons ");
                        relance = chx.nextInt();
                    }
                } else if (tourDeParole == 2) {
                    System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                    relance = chx.nextInt();
                    while ((relance < ((miseMax) == 0 ? (40) : (2 * miseMax) - joueur.getBetPlayer())) || (relance > joueur.getChipsPlayer())) {
                        System.out.println("Vous devez relancer au minimum la valeur de la BB Post Flop OU le double de la mise actuelle OU vous n'avez pas assez de jetons ");
                        relance = chx.nextInt();
                    }
                } else if (tourDeParole == 3 || tourDeParole == 4) {
                    System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                    relance = chx.nextInt();
                    while ((relance < ((miseMax) == 0 ? (2 * 40) : (2 * miseMax) - joueur.getBetPlayer())) || (relance > joueur.getChipsPlayer())) {
                        System.out.println("Vous devez relancer au minimum la valeur de 2BB au 3e/4e tour de parole OU le double de la mise actuelle OU vous n'avez pas assez de jetons ");
                        relance = chx.nextInt();
                    }
                }
                joueur.setBetPlayer(joueur.getBetPlayer() + relance);
                joueur.setChipsPlayer(joueur.getChipsPlayer() - relance);
                System.out.println(joueur + " relance a " + joueur.getBetPlayer() + " jetons");
                potTotal = potTotal + relance;
                break;
        }
        System.out.println("potTotal = " + potTotal);
    }

    private static int getMiseMax(LinkedList<Joueur> joueurs, int bigBlind) {
        int result = bigBlind;
        for (Joueur joueur : joueurs) {
            if (joueur.getBetPlayer() > result) {
                result = joueur.getBetPlayer();
            }
        }
        return result;
    }

    private static boolean checkAccordMise(LinkedList<Joueur> joueurs, int miseMax) {
        int count = 0;

        for (Joueur joueur : joueurs) {
            if (joueur.getBetPlayer() == miseMax) {
                System.out.println("joueur :" + joueur + ":getbetPlayer :" + joueur.getBetPlayer());
                count++;
            }
        }
        return (count == joueurs.size());
    }
}
