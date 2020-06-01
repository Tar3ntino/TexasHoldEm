package com.poker;

import java.util.*;
import java.util.LinkedList;

public class Main {

    private static int totalPot = 0;

    public static void main(String[] args) throws InterruptedException {

        int tourDeParole = 0;

        // ************************************
        // PARAMETRAGE DU JEU :
        // - Initialisation des Joueurs
        // - Affectation des "Noms de Joueurs" dans le tableau de "N" joueursDansLeCoup initialise precedemment
        // - Attribution des mains Joueurs du paquet melange
        // ************************************

        // Initialisation :
        Scanner sc = new Scanner(System.in);
        int nombreDeJoueurs = 0;

        do {
            try {
                System.out.println("♠️♥️POKER♦️♣️ - TEXAS HOLDEM");
                System.out.println("By Patxi & Bixente [Covid-19 Corporation]");
                System.out.print("Saisissez un nombre de joueursDansLeCoup (2 Min./10 Max.) :");
                nombreDeJoueurs = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("La saisie est incorrecte");
            }
            sc.nextLine();
        } while (nombreDeJoueurs < 2 || nombreDeJoueurs > 10);

        Joueur[] joueursInscrits = new Joueur[nombreDeJoueurs];

        System.out.println("Préparation de " + nombreDeJoueurs + " joueursDansLeCoup à la table ... ");
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
            joueursInscrits[i] = new Joueur(username);               // Voir NOTION D'EXPRESSIONS REGULIERES
            System.out.print("Joueur : " + joueursInscrits[i]);
        }

        // ************************************
        // INITIALISATION DE LA LISTE JOUEUR :
        // - Instanciation d'un objet LinkedList "joueursDansLeCoup" qui servira de "File de Joueur" pour la
        //   distribution de la parole
        // - Verification du nombre de joueurs restants en jeu pour savoir si le tournoi continue au debut de chaque
        //   tour de main. Si checkNbJoueurRestant vaut 1, le tournoi s'arrete.
        // - Remplissage de l'objet LinkedList "joueursDansLeCoup" via la methode "initialisationListeJoueur" :
        //
        //      ► Efface la liste precedente dans le cas ou celle ci ne serait pas vierge
        //      ► Affecte individuellement chacun des joueursDansLeCoup du tableau SSI il leur reste des jetons
        //
        // TIRAGE AU SORT DU BOUTON POUR LE DEALER :
        // ► Lors de la Main N.1 :      - Declaration d'un objet Random puis d'un entier "dealer"
        //                              - On appelle la methode nextInt de la classe "Random" de Java qui selectionne
        //                                de maniere aleatoire un entier entre 0 et l'entier place en parametres
        //
        // ► Pour les mains suivantes : - On passe au joueur d'apres de la file <Joueurs>
        //
        // ► DISTRIBUTION CARTES :
        // - Reinitialisation du Paquet de Cartes
        // - Attribution des mains Joueurs du paquet melange
        // ************************************

        LinkedList<Joueur> joueursDansLeCoup = new LinkedList<>(); // Variable pour check combinaison

        int numMain = 0;
        int dealer = 0;
        do {
            checkNbJoueurRestant(joueursInscrits);
            initialisationListeJoueur(joueursInscrits, joueursDansLeCoup);
            resetCardsCommunesAndHandPlayer(joueursDansLeCoup);

            numMain++; // Incrementation de la main suivante
            System.out.println("Affichage de la liste des joueursDansLeCoup sur la Main N." + numMain + ":\n" + joueursDansLeCoup);

            if (numMain == 1) {
                Random startDealer = new Random();
                dealer = startDealer.nextInt(nombreDeJoueurs);
            } else {
                if (dealer + 1 < joueursDansLeCoup.size()) {
                    dealer++;
                } else {
                    dealer = 0;
                }
            }

            System.out.println("Tirage Dealer : " + joueursDansLeCoup.get(dealer).getNamePlayer() + " a la distribution");

            // ************************************
            // On place les i joueursDansLeCoup de debut de tableau se trouvant avant la position du dealer
            // + dealer compris en fin de "pile joueur"
            // ************************************

            int rankToBeCancelled = 0; //
            for (int i = 0; i <= dealer; i++) {
                joueursDansLeCoup.add(checkNbJoueurRestant(joueursInscrits) + i, joueursInscrits[i]);
                rankToBeCancelled++;
            }

            for (int i = 0; i < rankToBeCancelled; i++) {
                joueursDansLeCoup.removeFirst();
                // suppression des n joueursDansLeCoup avant et comprenant le dealer de la tete de la pile
            }

            // ************************************
            // Mise en queue de pile le SB :
            // ************************************

            int chipsPlayerBefore = joueursDansLeCoup.getFirst().getChipsPlayer();
            int montantSmallBlind = 20; // On modifiera par la suite cette valeur avec un tableau de palier

            // Si le joueur n'a pas assez de jetons pour payer la blind, alors la valeur de sa blind sera egale au
            // reste de son stack

            if (chipsPlayerBefore < montantSmallBlind) {
                joueursDansLeCoup.getFirst().setSmallBlind(chipsPlayerBefore);
            } else {
                joueursDansLeCoup.getFirst().setSmallBlind(montantSmallBlind);
            }

            joueursDansLeCoup.getFirst().setSmallBlind(true);
            int miseSmallBlindJoueur = joueursDansLeCoup.getFirst().getSmallBlind();

            // MAJ du stack joueur en position SB :
            joueursDansLeCoup.getFirst().setChipsPlayer(chipsPlayerBefore - miseSmallBlindJoueur);
            totalPot = totalPot + miseSmallBlindJoueur;

            System.out.println(" Small Blind : " + joueursDansLeCoup.getFirst() + " Met " + miseSmallBlindJoueur + " jetons");
            System.out.println("-----------------------------");

            // on place le joueur SB actuellement en tete de pile > en queue de pile :
            joueursDansLeCoup.addLast(joueursDansLeCoup.getFirst());
            joueursDansLeCoup.removeFirst();

            // ************************************
            // Mise en queue de pile le BB :
            // ************************************

            chipsPlayerBefore = joueursDansLeCoup.getFirst().getChipsPlayer();
            int montantBigBlind = 40;

            // Si le joueur n'a pas assez de jetons pour payer la blind, alors la valeur de sa blind sera egale au
            // reste de son stack

            if (chipsPlayerBefore < montantBigBlind) {
                joueursDansLeCoup.getFirst().setBigBlind(chipsPlayerBefore);
            } else {
                joueursDansLeCoup.getFirst().setBigBlind(montantBigBlind);
            }
            joueursDansLeCoup.getFirst().setBigBlind(true);
            int miseBigBlindJoueur = joueursDansLeCoup.getFirst().getBigBlind();

            // MAJ du stack joueur en position BB :

            joueursDansLeCoup.getFirst().setChipsPlayer(chipsPlayerBefore - miseBigBlindJoueur);
            totalPot = totalPot + miseBigBlindJoueur;

            System.out.println(" Big Blind : " + joueursDansLeCoup.getFirst() + " Met " + miseBigBlindJoueur + " jetons");
            System.out.println("-----------------------------");

            // on place le joueur BB actuellement en tete de file > en queue de file :

            joueursDansLeCoup.addLast(joueursDansLeCoup.getFirst());
            joueursDansLeCoup.removeFirst();

            System.out.println("Valeur du Pot Total : " + totalPot);

            Paquet paquet = new Paquet();

            // CARDS DEALT : Affectation de 2 cartes au joueur joueursDansLeCoup[i] :
            for (Joueur joueur : joueursDansLeCoup) {
                distributionMainJoueur(joueur, paquet);
            }

            // Cette variable est necessaire dans le cas ou un joueur mise puis fold en cours d'enchere pour garder sa
            // mise dans la construction du pot :
            Joueur[] joueursALaTable = new Joueur[joueursDansLeCoup.size()];

            // Om remplit notre tableau de joueur a la table :
            for (int i = 0; i < joueursDansLeCoup.size(); i++) {
                joueursALaTable[i] = joueursDansLeCoup.get(i);
            }

            // ************************************
            //             PRE-FLOP
            // - Demarrage du tour d'enchere
            // - A l'issue de ce tour d'enchere, on verifie :
            //      * s'il ne reste qu'un seul joueur dans le coup auquel cas, il remporte le pot total
            //      * qu'un ou plusieurs joueurs ne sont pas partis a tapis (allin)
            //
            //   Verification de l'activation d'un pot secondaire ?
            //   Regle : Si l'un des joueurs se retrouve all in ET qu'au moins 2 autres joueurs restent en jeu
            //   Alors toutes les mises supplementaires, une fois le montant du all in paye, sont regroupees dans
            //   un pot secondaire que seuls ces joueurs peuvent gagner.
            //
            //   Si oui, il faut payer le montant du all in de la plus petite mise de la list "joueurAllIn" :
            //      > Creation d'une liste "joueurAllIn"
            //      > Si "joueurAllIn" > 1 :
            //   On classe par ordre croissant le montant du tapis de chaque joueur afin de definir N-1 pots
            //
            //
            // ************************************

            System.out.println(" [ PRE-FLOP ] ");

            tourDeParole = 1;
            tourDenchere(tourDeParole, joueursDansLeCoup, montantSmallBlind, montantBigBlind, joueursALaTable);

            // PARTAGE DU POT
            // Verification du cas N.1 : Tous les joueurs se sont couches.
            // Le pot est remporte integralement par le dernier joueur restant en jeu gagne le pot :

            if (joueursDansLeCoup.size() == 1) {
                // Retour et MAJ du joueur gagnant :
                int gainJetonsJoueur = totalPot;
                int ancienSolde = joueursDansLeCoup.getFirst().getChipsPlayer();
                System.out.println(joueursDansLeCoup.getFirst().getNamePlayer() + " gagne le pot (" + gainJetonsJoueur + ")");
                joueursDansLeCoup.getFirst().setChipsPlayer(ancienSolde + gainJetonsJoueur);
                continue;
            }
            // y a t'il un pot secondaire ?
            if (nbDeJoueurAllIn(joueursDansLeCoup) != 0 && joueursDansLeCoup.size() >= 3) {
                LinkedList<Joueur> joueurAllIn = checkAllInPreFlop(joueursDansLeCoup);
                if (joueurAllIn.size() > 1) {
                    ordonnerStackJoueurAllIn(joueurAllIn);
                    setBetTourPlayer(joueursInscrits);

                    int[] listeDePots = new int[joueurAllIn.size()];

                    // Il y aura N Pots de N joueurs all in
                    for (int i = 0; i < joueurAllIn.size(); i++) {

                        // 2eme boucle imbriquee pour savoir combien de joueurs ont paye le montant du tapis du joueur en cas
                        // de gain de celui ci :

                        int nbJoueursSuiventAllIn = checkPlayersFollowAllIn(joueursInscrits, joueurAllIn, i);

                        // on attribue a l'index correspondant la mise du joueur * le nombre de Joueurs ayant couvert cette
                        // mise. Ce sera le pot que le joueur pourra pretendre gagner :

                        calculDePot(joueurAllIn, listeDePots, i, nbJoueursSuiventAllIn);
                        updateBetPlayerAfterPaymentAllin(joueursInscrits, joueurAllIn, i);
                    }
                }
            }
            //retourneValueDuPlusPetitStackAllIn(joueurAllIn); // retourne le joueur qui a le plus petit tapis all in

            // Calcul du pot principal:
            // Valeur du plus petit Stack All In x nombre de joueurs ayant au moins paye le montant du plus petit
            // stack All In a l'issue du PRE FLOP. Attention : Le joueur peut ne plus etre "DansLeCoup" mais avoir
            // mise avant de se coucher


            // On parcourt la liste des joueurs present encore dans le coup a l'issue du tour d'enchere et l'on
            // commence donc par le plus petit stack, on créé le pot principal. Exemple : Joueur A possède 10€ et égalise
            // 10€ pour chacun des autres joueurs. Un pot principal de 30€ est donc créé. Cela correspond à l'argent
            // auquel le Joueur A peut prétendre s'il gagne la main. Ni plus, ni moins.

//                int potPrincipal = 0;
//                for (Joueur joueur : joueursDansLeCoup) {
//                    potPrincipal = potPrincipal + valeurDuStackAllInMinimum;
//                    joueur.setBetPlayer(joueur.getBetPlayer() - valeurDuStackAllInMinimum);
//                }


            // 

            // ************************************
            //              FLOP
            // - Tirage des 3 cartes du Flop que l'on renvoie dans un tableau de cartes "cartesCommunes"
            // - Verification que SB et BB soient toujours en jeu via la methode "checkSmallAndBig"
            //      ► Ajustement du nombre de rangs a repasser en tete de file
            // - Affectation des 3 cartes du Board dans les mains de chaque joueur via une boucle for
            // ************************************

            Card[] cartesCommunes = tirageFlop(paquet);
            checkSmallAndBig(joueursDansLeCoup);

            for (int j = 0; j < joueursDansLeCoup.size(); j++) {
                joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[2] = cartesCommunes[0];
                joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[3] = cartesCommunes[1];
                joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[4] = cartesCommunes[2];
                System.out.println("Combinaison du joueur " + joueursDansLeCoup.get(j).getNamePlayer() + " : " + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[0] + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[1] + " + " + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[2] + " " + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[3] + " " + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[4] + " " + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[5] + " " + joueursDansLeCoup.get(j).getCardsCommunesAndHandPlayer()[6]);
            }

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ FLOP ] ");
            tourDeParole = 2;
            // On enregistre les mises joueurs precedentes en cas de pot secondaire en fin de tour avant de faire un
            // reset Bet Player sur le tour de parole


            resetBetPlayer(joueursDansLeCoup);
//            resetBlindPlayer(joueursDansLeCoup);
            montantSmallBlind = 0;
            montantBigBlind = 0;
            tourDenchere(tourDeParole, joueursDansLeCoup, montantSmallBlind, montantBigBlind, joueursALaTable);

            if (joueursDansLeCoup.size() == 1) {
                // Retour et MAJ du joueur gagnant :
                int gainJetonsJoueur = totalPot;
                int ancienSolde = joueursDansLeCoup.getFirst().getChipsPlayer();
                System.out.println(joueursDansLeCoup.getFirst().getNamePlayer() + " gagne le pot (" + gainJetonsJoueur + ")");
                joueursDansLeCoup.getFirst().setChipsPlayer(ancienSolde + gainJetonsJoueur);
                continue;
            }

            // ************************************
            // *** TIRAGE DE LA TURN ***
            // ************************************

            tirageTurn(paquet, cartesCommunes);

            // Affichage des combinaisons des joueurs restant en jeu :
            for (int i = 0; i < joueursDansLeCoup.size(); i++) {
                joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[5] = cartesCommunes[3];
                System.out.println("Combinaison du joueur " + joueursDansLeCoup.get(i).getNamePlayer() + " : " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[0] + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[6]);
            }

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ TURN ] ");
            tourDeParole = 3;
            resetBetPlayer(joueursDansLeCoup);
//            resetBlindPlayer(joueursDansLeCoup);
            montantSmallBlind = 0;
            montantBigBlind = 0;
            tourDenchere(tourDeParole, joueursDansLeCoup, montantSmallBlind, montantBigBlind, joueursALaTable);

            if (joueursDansLeCoup.size() == 1) {
                // Retour et MAJ du joueur gagnant :
                int gainJetonsJoueur = totalPot;
                int ancienSolde = joueursDansLeCoup.getFirst().getChipsPlayer();
                System.out.println(joueursDansLeCoup.getFirst().getNamePlayer() + " gagne le pot (" + gainJetonsJoueur + ")");
                joueursDansLeCoup.getFirst().setChipsPlayer(ancienSolde + gainJetonsJoueur);
                continue;
            }

            // ************************************
            // *** TIRAGE DE LA RIVER ***
            // ************************************

            tirageRiver(paquet, cartesCommunes);

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ RIVER ] ");
            tourDeParole = 4;
            resetBetPlayer(joueursDansLeCoup);
//            resetBlindPlayer(joueursDansLeCoup);
            montantSmallBlind = 0;
            montantBigBlind = 0;
            tourDenchere(tourDeParole, joueursDansLeCoup, montantSmallBlind, montantBigBlind, joueursALaTable);

            if (joueursDansLeCoup.size() == 1) {
                // Retour et MAJ du joueur gagnant :
                int gainJetonsJoueur = totalPot;
                int ancienSolde = joueursDansLeCoup.getFirst().getChipsPlayer();
                System.out.println(joueursDansLeCoup.getFirst().getNamePlayer() + " gagne le pot (" + gainJetonsJoueur + ")");
                joueursDansLeCoup.getFirst().setChipsPlayer(ancienSolde + gainJetonsJoueur);
                continue;
            }

            // ************************************
            // *** SHOWDOWN / ABBATAGE ***
            // ************************************

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ SHOWDOWN ] ");

            // PARTAGE DU POT
            // Verification du cas N.2 : Le pot est remporte par le joueur ayant la meilleure main
            // Verification du cas N.3 : Le pot est partage par les joueurs si main de meme valeur

            // A Chaque joueur restant encore en jeu, on :
            for (int i = 0; i < joueursDansLeCoup.size(); i++) {
                joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[6] = cartesCommunes[4]; // ...affecte la derniere carte River au tableau de combi de chaque joueur
                System.out.println("Combinaison du joueur " + joueursDansLeCoup.get(i).getNamePlayer() + " : " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[0] + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueursDansLeCoup.get(i).getCardsCommunesAndHandPlayer()[6]);
                System.out.println(joueursDansLeCoup.get(i).getNamePlayer() + " possede une : " + CombinaisonUtil.getCombinaison(joueursDansLeCoup.get(i)) + " Force :" + CombinaisonUtil.getCombinaison(joueursDansLeCoup.get(i)). getCombinaison().value);
            }

            System.out.println("---------------------------------------------------------");
            System.out.println("Renvoie de la combinaison gagnante");

            List<Joueur> joueursWithHighestCombinaisonValue = getJoueursWithHighestCombinaisonValue(joueursDansLeCoup);
            List<Joueur> joueursWithHighestCombinaison = CombinaisonUtil.departagerJoueurs(joueursWithHighestCombinaisonValue);

            for (Joueur joueur : joueursWithHighestCombinaison) {
                int gainJetonsJoueur = totalPot / joueursWithHighestCombinaison.size();
                int ancienSolde = joueur.getChipsPlayer();
                System.out.println(joueur.getNamePlayer() + " gagne " + gainJetonsJoueur);
                joueur.setChipsPlayer(ancienSolde + gainJetonsJoueur);
            }

            totalPot = 0; // Remise a zero du pot pour la prochaine Main
//            for (Joueur joueur : joueursDansLeCoup) {
//                if (joueur.getChipsPlayer() == 0) {
//                    joueursDansLeCoup.remove(joueur);
//                }
//            }

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


        } while (checkNbJoueurRestant(joueursInscrits) > 1);
        // Fin de la main / Tant qu'il reste plus qu'un joueur, on continue le tournoi

        System.out.println(" Sortie du do while");
        System.out.println(

                pickUpWinner(joueursInscrits) + " gagne le Tournoi");

    } // Fin de la methode main

    private static void calculDePot(LinkedList<Joueur> joueurAllIn, int[] listeDePots, int i, int nbJoueursSuiventAllIn) {
        listeDePots[i] = joueurAllIn.get(i).getBetPlayer() * nbJoueursSuiventAllIn;
    }

    private static void updateBetPlayerAfterPaymentAllin(Joueur[] joueursInscrits, LinkedList<Joueur> joueurAllIn, int i) {
        for (Joueur joueursInscrit : joueursInscrits) {
            joueursInscrit.setBetPlayer(joueursInscrit.getBetPlayer() - joueurAllIn.get(i).getBetPlayer());
        }
    }

    private static int checkPlayersFollowAllIn(Joueur[] joueursInscrits, LinkedList<Joueur> joueurAllIn, int i) {
        int nbJoueursQuiOntPayeLeMontantDuAllIn = 0;
        for (Joueur joueursInscrit : joueursInscrits) {
            if (joueursInscrit.getBetPlayer() > joueurAllIn.get(i).getBetPlayer()) {
                nbJoueursQuiOntPayeLeMontantDuAllIn++;
            }
        }
        return nbJoueursQuiOntPayeLeMontantDuAllIn;
    }

    private static void setBetTourPlayer(Joueur[] joueursInscrits) {
        for (Joueur joueursInscrit : joueursInscrits) {
            joueursInscrit.setBetTourPlayer(joueursInscrit.getBetPlayer());
        }
    }

    public static void ordonnerStackJoueurAllIn(LinkedList<Joueur> joueurAllIn) {

        for (int i = 1; i < joueurAllIn.size(); i++) {

            // Si la mise du joueur est inferieure a la mise du joueur precedent, alors on place le joueur actuellement
            // en lecture en tete de LinkedList

            if (joueurAllIn.get(i).getBetPlayer() < joueurAllIn.get(i - 1).getBetPlayer()) {
                joueurAllIn.add(i - 1, joueurAllIn.get(i));
                joueurAllIn.remove(i + 1);
                i = 0; // on refait le check depuis le debut, va s'incrementer i++ donc prendre la valeur de 1
            }
        }
    }

    private static int nbDeJoueurAllIn(LinkedList<Joueur> joueursDansLeCoup) {
        int resultat = 0;
        for (Joueur joueur : joueursDansLeCoup) {
            if (joueur.getChipsPlayer() == 0) {
                resultat++;
            }
        }
        return resultat;
    }

    public static Joueur retourneValueDuPlusPetitStackAllIn(List<Joueur> joueurAllIn) {
        // Le nombre maxi de jetons en jeu dans la partie 10 000 x 10 joueurs max a la table :
        int temp = 100000;
        Joueur plusPetitStack = new Joueur("");
        for (Joueur joueur : joueurAllIn) {
            // Si la mise du joueur est inferieure a "temp", "temp" prend la valeur de cette mise
            if (joueur.getBetPlayer() < temp) {
                temp = joueur.getBetPlayer();
                plusPetitStack = joueur;
            }
        }
        //joueurAllIn.remove(plusPetitStack);
        return plusPetitStack;
    }

    private static LinkedList<Joueur> checkAllInPreFlop(LinkedList<Joueur> joueursDansLeCoup) {
        LinkedList<Joueur> joueurAllIn = new LinkedList<>();
        for (Joueur joueur : joueursDansLeCoup) {
            if (joueur.getChipsPlayer() == 0) { // Si le joueur a fait all in
                joueurAllIn.add(joueur);
            }
        }
        return joueurAllIn;
    }

    private static String pickUpWinner(Joueur[] listJoueur) {
        String winner = "noName";
        for (Joueur joueur : listJoueur) {
            if (joueur.getChipsPlayer() != 0) {
                winner = joueur.getNamePlayer();
            }
        }
        return winner;
    }

    private static int checkNbJoueurRestant(Joueur[] listJoueur) {
        int nbJoueursRestantPartie = 0;
        for (Joueur joueur : listJoueur) {
            if (joueur.getChipsPlayer() != 0) {
                nbJoueursRestantPartie++;
            }
        }
        return nbJoueursRestantPartie;
    }

    private static void resetCardsCommunesAndHandPlayer(LinkedList<Joueur> joueurs) {
        for (Joueur joueur : joueurs) {
            for (int j = 0; j < 7; j++) {
                joueur.getCardsCommunesAndHandPlayer()[j] = null;
            }
        }
    }

    private static int tourDenchere(int tourDeParole, LinkedList<Joueur> joueursDansLeCoup, int montantSmallBlind, int montantBigBlind, Joueur[] joueursALaTable) throws InterruptedException {

        int miseMax = getMiseMax(joueursDansLeCoup, montantBigBlind);
        int tourDeTable = 0;
        int sizeBefore = joueursDansLeCoup.size();
        for (int i = 0; i < sizeBefore; i++) {
            sizeBefore = joueursDansLeCoup.size();
            if (joueursDansLeCoup.size() == 1) {
                // Retour et MAJ du joueur gagnant :
                int gainJetonsJoueur = totalPot;
                int ancienSolde = joueursDansLeCoup.getFirst().getChipsPlayer();
                System.out.println(joueursDansLeCoup.getFirst().getNamePlayer() + " gagne le pot (" + gainJetonsJoueur + ")");
                joueursDansLeCoup.getFirst().setChipsPlayer(ancienSolde + gainJetonsJoueur);
                continue;
            } else {
                if (joueursDansLeCoup.get(i).getChipsPlayer() == 0) {
                    // Si un joueur n'a plus de jetons, on ne lui demande pas
                    // son avis et on passe au joueur suivant

                    // On verifie tout de meme si le joueur n'a pas de mise "Blinds" en jeu, sinon on les convertit
                    transfertSiBlindJoueurEnMise(joueursDansLeCoup.get(i), joueursALaTable);

//                    if (joueursDansLeCoup.get(i).getBetPlayer() != 0) {
//                        // On stocke la mise dans UN TABLEAU DE MISES en cas de partage de pot
//                        misesJoueurs.add(joueursDansLeCoup.get(i).getBetPlayer());
//                    }
//                    continue;
                } else {
                    paroleJoueur(joueursDansLeCoup.get(i), joueursDansLeCoup, montantSmallBlind, montantBigBlind, joueursALaTable);
                    int sizeAfter = joueursDansLeCoup.size();
                    if (sizeBefore == sizeAfter) {

                        // Cela signifie que le joueur reste en jeu et ne s'est pas couche.
                        // Si on dépasse

                        if (i + 1 >= sizeAfter) { // Si le joueur suivant sort de la taille de la liste
                            for (int j = 0; j < sizeAfter; j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
                                System.out.println("joueur.get(" + j + ").getBetPlayer() :" + joueursDansLeCoup.get(j).getBetPlayer());
                                if (joueursDansLeCoup.get(j).getBetPlayer() != miseMax) {
                                    i = -1;
                                    tourDeTable++;

                                    // Si a la lecture de notre file Joueur, certains joueursDansLeCoup ont encore une mise differente de la miseMax
                                    // on sort de la verification de joueur et on retourne en debut de boucle,
                                    // on met -1 car va etre incremente pour retomber a 0
                                }
                                // Sinon cela signifie que tout le monde a mise la meme somme et que l'on peut sortir de la boucle initiale
                            }
                        } // Sinon on incremente normalement
                    } else {                    // Si le joueur s'est couche, alors sizeBefore != sizeAfter
                        //Si on dépasse
                        if (i >= sizeAfter) {   // Si le joueur sort de la taille de la liste ou est en derniere position
                            for (int j = 0; j < joueursDansLeCoup.size(); j++) { // Si tous les betPlayers restant sont egaux a MiseMax, on sort de la boucle
                                if (joueursDansLeCoup.get(j).getBetPlayer() != miseMax) {
                                    i = -1;              // On met -1 car va etre incremente pour retomber a 0
                                } else {
                                    tourDeTable++;
                                    break;
                                }
                            }              // on revient au debut
                        } else {
                            i--;
                        }
                    }
                }
                miseMax = getMiseMax(joueursDansLeCoup, montantBigBlind);
                // on MAJ la mise Max dans le cas ou l'un des joueursDansLeCoup aurait relance avant de verifier si toutes les
                // mises sont equivalentes

                System.out.println("------------------------------------");
                System.out.println("Valeur de i: " + i);
                System.out.println("Taille de la liste joueur: " + joueursDansLeCoup.size());
                System.out.println("Valeur de miseMax: " + miseMax);
                if (tourDeTable != 0 && checkAccordMise(joueursDansLeCoup, miseMax)) {
                    break;
                }
                System.out.println("------------------------------------");
            }
        }
        return miseMax;
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

//    private static void resetBlindPlayer(LinkedList<Joueur> joueurs) {
//        for (Joueur joueur : joueurs) {
//            joueur.setSmallBlind(0);
//            joueur.setBigBlind(0);
//        }
//    }

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


    // ************************************
    //     INITIALISATION LISTE JOUEUR
    // On vide la file dans le cas ou elle contient des donnees precedentes
    // Initialisation des joueursDansLeCoup crees en debut de partie
    // Si le joueur du tableau de joueur JoueursInscrits a encore des jetons,
    // alors on l'ajoute a la file joueursDansLeCoup
    // ************************************

    private static void initialisationListeJoueur(Joueur[] joueursInscrits, LinkedList<Joueur> joueursDansLeCoup) {
        joueursDansLeCoup.clear();
        for (Joueur joueur : joueursInscrits) {
            if (joueur.getChipsPlayer() != 0) {
                joueursDansLeCoup.add(joueur);
            }
        }
    }

    // ************************************
    //     DISTRIBUTION MAIN JOUEUR
    // La fonction distribution joueur pioche les 2 cartes de la variable paquet en parametre puis affecte la 1ere puis
    // la seconde carte au joueur place en parametre
    // ************************************

    private static void distributionMainJoueur(Joueur joueur, Paquet paquet) {
        Card[] pioche = paquet.piocher(2);
        joueur.setMain(pioche);                          // MAJ de la main du joueur

        for (int j = 0; j < 2; j++) {
            joueur.getCardsCommunesAndHandPlayer()[j] = pioche[j];
        } // MAJ du tableau combinaison du joueur

        System.out.println(joueur + " : Main : " + joueur.getMain()[0].toString() + joueur.getMain()[1]);
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

    private static void paroleJoueur(Joueur joueur, LinkedList<Joueur> joueursDansLeCoup, int sB, int bB, Joueur[] joueursALaTable) throws
            InterruptedException {
        int choix = 0;
        int miseMax = getMiseMax(joueursDansLeCoup, bB);
        int montantBigBlind = 40;
        do {
            transfertSiBlindJoueurEnMise(joueur, joueursALaTable);
            System.out.println(joueur + " Jetons :" + joueur.getChipsPlayer() + " - [Pot Total:" + totalPot + "]" + "\n" + " a la parole :" + "\n"+ "1/ Passer ");
            if (getMontantSuivre(joueur,miseMax) == 0) {
                System.out.println("2/ Check");
            } else {
                System.out.println("2/ Suivre(" + getMontantSuivre(joueur, miseMax) + ")");
            }
            if (joueur.getChipsPlayer() > miseMax) {
                System.out.println("3/ Relancer( >= " + getMontantRelance(joueur, miseMax, montantBigBlind) + ")");
            }
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
            doActionJoueur(joueur, joueursDansLeCoup, choix, miseMax, montantBigBlind, joueursALaTable);
        } while (choix != 1 && choix != 2 && choix != 3); // Faire tant que le choix de l'utilisateur ne correspond pas aux actions proposees
    }

    private static int getMontantSuivre(Joueur joueur, int miseMax) {
        int montantSuivre = 0;
        if (joueur.getChipsPlayer() > miseMax) {
            montantSuivre = miseMax - joueur.getBetPlayer();
        } else { // Si le joueur n'a pas suffisamment de jetons...
            montantSuivre = joueur.getChipsPlayer(); // On affiche le montant des jetons restants
        }
        return montantSuivre;
    }

    private static int getMontantRelance(Joueur joueur, int miseMax, int montantBigBlind) {
        int resultat = 0;

        if (miseMax == 0) { // Si personne n'a encore mise..
            resultat = montantBigBlind - joueur.getBetPlayer(); // On relance au min le montant de la big blind
        } else { // Si une mise est deja en jeu..
            resultat = ((2 * miseMax) - joueur.getBetPlayer()); // On relance au min le double de la mise
        }
        return resultat;
    }

    private static void doActionJoueur(Joueur joueur, LinkedList<Joueur> joueursDansLeCoup, int choix,
                                       int miseMax, int montantBigBlind, Joueur[] joueursALaTable) {
        switch (choix) {
            case 1:
                System.out.println(joueur + " passe son tour ");
                copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
                joueursDansLeCoup.remove(joueur);
                break;

            case 2:
                int add = getMontantSuivre(joueur, miseMax);
                joueur.setBetPlayer(joueur.getBetPlayer() + add);
                copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
                joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
                System.out.println(joueur + " suit " + joueur.getBetPlayer() + " jetons");
                totalPot = totalPot + add;
                copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur,joueursALaTable);
                break;

            case 3:
                Scanner chx = new Scanner(System.in);
                int relance = 0;
                System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                relance = chx.nextInt();
                if (miseMax == montantBigBlind) { // Cas 1ERE RELANCE PRE FLOP
                    while (relance > joueur.getChipsPlayer() || relance < montantBigBlind) {
                        if (relance > joueur.getChipsPlayer()) {
                            System.out.println("Vous n'avez pas assez de jetons. Saisir a nouveau :");
                            relance = chx.nextInt();
                        } else {
                            System.out.println("Vous devez relancer au minimum le montant de la big blind");
                            relance = chx.nextInt();
                        }
                    }
                }
                // ***************************************************************************
                // Si personne n'est encore entré dans le coup,
                // alors la relance doit être supérieure ou égal au double de la grosse blind.
                // ***************************************************************************

                else if (miseMax == 0) {
                    while (relance > joueur.getChipsPlayer() || relance < montantBigBlind) {

                        // if(miseMax=0)... Si aucun Joueur n'a encore mise, a partir du flop (car miseMax vaut min
                        // la bB en pre-flop), on rentre dans la condition :
                        // Tant que le montant saisie de la relance est superieur aux jetons ou que le montant de la
                        // relance est inferieur au montant de la bigBlind, alors on reste dans le "while"
                        // Affichage de l'erreur de saisie selon le cas :

                        if (relance > joueur.getChipsPlayer()) {
                            System.out.println("Vous n'avez pas assez de jetons. Saisir a nouveau :");
                            relance = chx.nextInt();
                        } else {
                            System.out.println("Vous devez relancer au minimum le montant de la big blind");
                            relance = chx.nextInt();
                        }
                    }
                }
                // ***************************************************************************
                // Si une personne a déjà relancé, alors la différence entre la surrelance et la dernière mise
                // doit être supérieure ou égale à la différence entre la dernière mise et celle d'avant.
                // ***************************************************************************

                else { // Cas ou l'on a deja quelqu'un qui a mise apres le flop
                    while (relance > joueur.getChipsPlayer() || relance < ((2 * miseMax) - joueur.getBetPlayer())) {
                        if (relance > joueur.getChipsPlayer()) {
                            System.out.println("Vous n'avez pas assez de jetons. Saisir a nouveau :");
                            relance = chx.nextInt();
                        } else {
                            System.out.println("Vous devez relancer au minimum le double de la mise actuelle");
                        }
                        relance = chx.nextInt();
                    }
                }
                joueur.setBetPlayer(joueur.getBetPlayer() + relance);
                copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
                joueur.setChipsPlayer(joueur.getChipsPlayer() - relance);
                System.out.println(joueur + " relance a " + joueur.getBetPlayer() + " jetons");
                totalPot = totalPot + relance;

                break;
        }
        System.out.println("potTotal = " + totalPot);
    }

    private static void copieMiseJoueurDansLeCoupVersJoueurALaTable(Joueur joueur, Joueur[] joueursALaTable) {
        for (Joueur joueur1 : joueursALaTable) {
            if (joueur1.getNamePlayer()==joueur.getNamePlayer()) {
                joueur1.setBetTourPlayer(joueur.getBetPlayer());
                // MAJ de la MISE DU JOUEUR DANS LE TABLEAU JOUEURALATABLE AU CAS OU LE JOUEUR SE COUCHE (Sort de JoueurDansLeCoup)
                // AVANT LE TOUR D'ENCHERE POUR GARDER UN HISTORIQUE
            }
        }
    }

    private static void transfertSiBlindJoueurEnMise(Joueur joueurDansLeCoup, Joueur[] joueurALaTable) {
        // Si le joueurDansLeCoup est small blind, on convertit sa blind dans sa mise et on reset la blind
        if (joueurDansLeCoup.getSmallBlind() > 0) {
            joueurDansLeCoup.setBetPlayer(joueurDansLeCoup.getSmallBlind());
            // On va chercher joueur dans le tableau de JoueurALaTable et on MAJ egalement sa mise dans le cas ou celui
            // ci quitte le coup et prendre en consideration ses jetons engages sur la table pour le calcul des pots :
            for (Joueur joueur : joueurALaTable) {
                if (joueur == joueurDansLeCoup) {
                    joueur.setBetPlayer(joueurDansLeCoup.getSmallBlind());
                }
            }
            joueurDansLeCoup.setSmallBlind(0);
        }
        // Si le joueurDansLeCoup est big blind, on convertit sa blind dans sa mise et on reset la blind
        if (joueurDansLeCoup.getBigBlind() > 0) {
            joueurDansLeCoup.setBetPlayer(joueurDansLeCoup.getBigBlind());
            // On va chercher joueur dans le tableau de JoueurALaTable et on MAJ egalement sa mise dans le cas ou celui
            // ci quitte le coup et prendre en consideration ses jetons engages sur la table pour le calcul des pots :
            for (Joueur joueur : joueurALaTable) {
                if (joueur == joueurDansLeCoup) {
                    joueur.setBetPlayer(joueurDansLeCoup.getBigBlind());
                }
            }
            joueurDansLeCoup.setBigBlind(0);
        }
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
            } else { // Il se peut que la mise du joueur ne soit pas egale a a la miseMax car il est au tapis, dans ce
                // cas on compte egalement count++ pour que le tourDenchere prenne fin
                if (joueur.getChipsPlayer() == 0) {
                    count++;
                }
            }
        }
        return (count == joueurs.size());
    }
}
