package com.poker;

import java.util.*;
import java.util.LinkedList;

public class Main {

    private static int totalPot = 0;

    public static void main(String[] args) throws InterruptedException {

        // ************************************
        // PARAMETRAGE DU JEU :
        // - Initialisation des Joueurs
        // - Affectation des "Noms de Joueurs" dans le tableau de "N" joueursDansLeCoup initialise precedemment
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
        Joueur[] joueursALaTable = new Joueur[joueursInscrits.length]; // Variable qui sert a garder les mise des joueurs en cas de fold sur un coup

        int numMain = 0;
        int dealer = 0;
        do {
            checkNbJoueurRestant(joueursInscrits);
            initialisationListeJoueurDansLeCoup(joueursInscrits, joueursDansLeCoup);

            if (numMain == 0) {
                joueursALaTable = initialisationListeJoueurALaTable(joueursDansLeCoup, joueursInscrits); // initialisation qu'en debut de partie a la main 0
            }
            resetCardsCommunesAndHandPlayer(joueursDansLeCoup);

            numMain++; // Incrementation de la main suivante
            System.out.println("-----------------------------");
            System.out.println("Affichage de la liste des joueursDansLeCoup sur la Main N." + numMain + ":\n" + joueursDansLeCoup);


            if (numMain == 1) {
                Random startDealer = new Random();
                dealer = startDealer.nextInt(nombreDeJoueurs);
                System.out.println("Valeur de l'indice dealer" + dealer);
            } else {
                do {
                    // SI L'INDICE SUIVANT EXISTE :
                    if (dealer + 1 < joueursALaTable.length) {
                        dealer++; // ... ALORS ON INCREMENTE
                    } else {
                        dealer = 0; // ... SINON ON RENVOIE A ZERO
                    }
                } while (joueursALaTable[dealer].getChipsPlayer() == 0);
                // TANT QUE LE NOUVEAU DEALER N'A PAS DE JETONS (LE JOUEUR SIEGE A LA TABLE MAIS N'EST PLUS CONSIDERE
                // A LA TABLE CAR N'A PLUS DE JETONS, ON REITERE L'OPERATION
            }
            System.out.println("Tirage Dealer : " + joueursALaTable[dealer].getNamePlayer() + " a la distribution");

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

            System.out.println("Small Blind : " + joueursDansLeCoup.getFirst() + " met " + miseSmallBlindJoueur + " jetons");

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

            System.out.println("Big Blind : " + joueursDansLeCoup.getFirst() + " met " + miseBigBlindJoueur + " jetons");
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
            // ************************************
            int bbMinRelance = 40;
            boolean estRelanceBloquee = false;
            System.out.println(" [ PRE-FLOP ] ");
            tourDenchere(joueursDansLeCoup, montantBigBlind, joueursALaTable, bbMinRelance, estRelanceBloquee);

            // PARTAGE DU POT
            // Verification du cas N.1 : Tous les joueurs se sont couches.
            // Le pot est remporte integralement par le dernier joueur restant en jeu gagne le pot :
            if (returnGagnantSiUnJoueurDansLeCoup(joueursDansLeCoup)) continue;

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
            // On enregistre les mises joueurs precedentes en cas de pot secondaire en fin de tour avant de faire un
            // reset Bet Player sur le tour de parole
            resetBetPlayer(joueursDansLeCoup);
            montantSmallBlind = 0;
            montantBigBlind = 0;
            tourDenchere(joueursDansLeCoup, montantBigBlind, joueursALaTable, bbMinRelance, estRelanceBloquee);
            if (returnGagnantSiUnJoueurDansLeCoup(joueursDansLeCoup)) continue;

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
            resetBetPlayer(joueursDansLeCoup);
            montantSmallBlind = 0;
            montantBigBlind = 0;
            tourDenchere(joueursDansLeCoup, montantBigBlind, joueursALaTable, bbMinRelance, estRelanceBloquee);
            if (returnGagnantSiUnJoueurDansLeCoup(joueursDansLeCoup)) continue;

            // ************************************
            // *** TIRAGE DE LA RIVER ***
            // ************************************

            tirageRiver(paquet, cartesCommunes);

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ RIVER ] ");
            resetBetPlayer(joueursDansLeCoup);
            montantSmallBlind = 0;
            montantBigBlind = 0;
            tourDenchere(joueursDansLeCoup, montantBigBlind, joueursALaTable, bbMinRelance, estRelanceBloquee);
            if (returnGagnantSiUnJoueurDansLeCoup(joueursDansLeCoup)) continue;

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
                System.out.println(joueursDansLeCoup.get(i).getNamePlayer() + " possede une : " + CombinaisonUtil.getCombinaison(joueursDansLeCoup.get(i)) + " Force :" + CombinaisonUtil.getCombinaison(joueursDansLeCoup.get(i)).getCombinaison().value);
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
            System.out.println(" * Gerer le cas de la relance bloquee");
            // des qu'il y a une relance (Vrai ou Fausse), il va falloir verifier le statut joueurBloque Vrai ou Faux. Il est Faux par defaut.
            // Pour determiner si le joueur est bloque ou pas, il va falloir lire sa mise engagee et la comparer seulement a miseMax
            System.out.println(" * Gerer les cas de Pot 1 Pot 2 etc....");
            System.out.println(" * Creation d'une structure de blind");
            System.out.println(" * Importer un timer en debut de partie");
            System.out.println(" * Couper le son d'un joueur qui n'est plus dans le coup");
            System.out.println(" * Revoir le cas ou 2 joueurs se mettent all in successivement, le minimumARelancer doit rester identique");
            System.out.println(" * Cas du bouton mort : si un joueur BB quittent JoueurALaTable, son siege doit rester considere pour les 2 prochains tours SB et Dealer");
            System.out.println(" * Cas du bouton mort : si un joueur SB quittent JoueurALaTable, son siege doit rester considere pour le prochain tour du Dealer");
            System.out.println(" ************************************************************************************");


        } while (checkNbJoueurRestant(joueursInscrits) > 1);
        // Fin de la main / Tant qu'il reste plus qu'un joueur, on continue le tournoi

        System.out.println(" Sortie du do while");
        System.out.println(pickUpWinner(joueursInscrits) + " gagne le Tournoi");

    } // Fin de la methode main

    private static boolean returnGagnantSiUnJoueurDansLeCoup(LinkedList<Joueur> joueursDansLeCoup) {
        if (joueursDansLeCoup.size() == 1) {
            // Retour et MAJ du joueur gagnant :
            int gainJetonsJoueur = totalPot;
            int ancienSolde = joueursDansLeCoup.getFirst().getChipsPlayer();
            System.out.println(joueursDansLeCoup.getFirst().getNamePlayer() + " gagne le pot (" + gainJetonsJoueur + ")");
            joueursDansLeCoup.getFirst().setChipsPlayer(ancienSolde + gainJetonsJoueur);
            return true;
        }
        return false;
    }

    private static Joueur[] initialisationListeJoueurALaTable(LinkedList<Joueur> joueursDansLeCoup, Joueur[] joueursInscrits) {

        // Cette variable est necessaire dans le cas ou un joueur mise puis fold en cours d'enchere pour garder sa
        // mise dans la construction du pot :
        Joueur[] joueursALaTable = new Joueur[joueursInscrits.length];

        // Om remplit notre tableau de joueur a la table :
        for (int i = 0; i < joueursDansLeCoup.size(); i++) {
            joueursALaTable[i] = joueursDansLeCoup.get(i);
        }
        return joueursALaTable;
    }

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

    private static int checkNbJoueurRestant(Joueur[] listJoueursInscrits) {
        int nbJoueursRestantPartie = 0;
        for (Joueur joueur : listJoueursInscrits) {
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

    private static int tourDenchere(LinkedList<Joueur> joueursDansLeCoup, int montantBigBlind, Joueur[] joueursALaTable, int bbMinRelance, boolean estRelanceBloquee) throws InterruptedException {
        int miseMax = getMiseMax(joueursDansLeCoup, montantBigBlind); // MM:40
        int tourDeTable = 0;
        int sizeBefore = joueursDansLeCoup.size();

        ArrayList resultatParoleJoueur = new ArrayList();
        resultatParoleJoueur.add(bbMinRelance);        // affectation du int minimumARelancer dans la liste  MR:40
        resultatParoleJoueur.add(estRelanceBloquee);       // affectation du booleen dans la liste              ERB:False

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

                // Si le joueur a des jetons, on lui demande son avis (sinon on passera au joueur suivant)

                if (joueursDansLeCoup.get(i).getChipsPlayer() != 0) {
                    // 60 et false
                    resultatParoleJoueur = paroleJoueur(joueursDansLeCoup.get(i), joueursDansLeCoup, montantBigBlind, bbMinRelance, joueursALaTable, resultatParoleJoueur);
                    // 60 et false
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
                } else {
                    // On verifie tout de meme si le joueur n'a pas de mise "Blinds" en jeu meme s'il n'a pas de jetons, sinon on les convertit
                    transfertSiBlindJoueurEnMise(joueursDansLeCoup.get(i), joueursALaTable);
                }

                miseMax = getMiseMax(joueursDansLeCoup, montantBigBlind);
                // on MAJ la mise Max dans le cas ou l'un des joueursDansLeCoup aurait relance avant de verifier si toutes les
                // mises sont equivalentes

                //System.out.println("------------------------------------");
                //System.out.println("Valeur de i: " + i);
                //System.out.println("Taille de la liste joueur: " + joueursDansLeCoup.size());
                //System.out.println("Valeur de miseMax: " + miseMax);
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
        //System.out.println("Valeur de rangAMettreEnTeteDeFile" + rangARemettreEnTeteDeFile);

        for (int i = 0; i < rangARemettreEnTeteDeFile; i++) {
            //System.out.println("Affichage de la file joueur AVANT ajout/retrait" + joueurs);
            joueurs.addFirst(joueurs.getLast()); // On remet le joueur rang bB en tete de liste
            joueurs.removeLast();
            //System.out.println("Affichage de la file joueur APRES ajout/retrait" + joueurs);
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


    // ************************************
    //     INITIALISATION LISTE JOUEUR
    // On vide la file dans le cas ou elle contient des donnees precedentes
    // Initialisation des joueursDansLeCoup crees en debut de partie
    // Si le joueur du tableau de joueur JoueursInscrits a encore des jetons,
    // alors on l'ajoute a la file joueursDansLeCoup
    // ************************************

    private static void initialisationListeJoueurDansLeCoup(Joueur[] joueursInscrits, LinkedList<Joueur> joueursDansLeCoup) {
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

        System.out.println(joueur + ": Main: " + joueur.getMain()[0].toString() + joueur.getMain()[1]);
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
    //
    // miseMax different de 0 (il y a deja une mise sur table)
    //      chipsPlayer < miseMax
    //          1) Se coucher
    //          2) Suivre (all in)
    //      chipsPlayer > miseMax
    //          1) Se coucher
    //          2) Suivre
    //      chipsPlayer < sommeMiseMaxMinimumARelancer
    //          3) "Fausse" Relance en all in
    //      chipsPlayer > sommeMiseMaxMinimumARelancer
    //          3) Relancer libre
    //
    // miseMax vaut 0 (personne n'a encore mise)
    //      chipsPlayer < bigBlind
    //          2) Check (suivre a 0)
    //          3) Miser (all in)
    //      chipsPlayer > bigBlind
    //          2) Check (suivre a 0)
    //          3) Miser libre
    //
    // ************************************


    private static ArrayList paroleJoueur(Joueur joueur, LinkedList<Joueur> joueursDansLeCoup, int montantBigBlind, int bbMinRelance, Joueur[] joueursALaTable, ArrayList resultatParoleJoueur) throws
            InterruptedException {
        int choix = 0;                              // JR1 : BB(40) MM(100)
        int miseMax = getMiseMax(joueursDansLeCoup, montantBigBlind);
        int sommeMiseMaxMinimumARelancer = miseMax + (int) resultatParoleJoueur.get(0); // JR1 : 100+60
        // (int) resultatParoleJoueur.get(0) = minimimumARelancer qui a evolue suite a la parole des Joueurs precedent

        do {
            transfertSiBlindJoueurEnMise(joueur, joueursALaTable);
            System.out.println(joueur + " a la parole:" + "\n" + "Stack: " + joueur.getChipsPlayer() + "\n" + "Mise engagee: " + joueur.getBetPlayer() + "\n" + "Pot Total: " + totalPot);
            System.out.println("Main: " + joueur.getMain()[0].toString() + joueur.getMain()[1]);
            System.out.println("-----------------------------");

            int tapisPlusMiseJoueur = joueur.getChipsPlayer() + joueur.getBetPlayer();

            if (miseMax != 0) {                         // Si quelqu'un a mise....
                if (tapisPlusMiseJoueur <= miseMax) {   // Si le tapis du joueur est inferieur ou egale a miseMax, le cas de la relance bloquee ne se posse pas....
                    System.out.println("1/ Passer");
                    System.out.println("2/ All-in(" + getMontantSuivre(joueur, miseMax) + ")");
                } else {                                // Si le tapis du joueur est superieur a la miseMax actuel, le joueur peut potentiellement relancer...
                    System.out.println("1/ Passer");
                    System.out.println("2/ Suivre(" + getMontantSuivre(joueur, miseMax) + ")");

                    if (tapisPlusMiseJoueur < sommeMiseMaxMinimumARelancer) {
                        // Si le tapis du joueur est inferieur a la somme miseMax + Minimum A Relancer exemple 130+60, il ne pourra pas faire de "vraie" relance
                        System.out.println("3/ All-in (" + joueur.getChipsPlayer() + ")");
                    } else {                             // C'est dans ce cas la que le joueur peut relancer.... ON introduit une nouvelle verification avant...
                        // Si nous avons un joueur qui a fait une "fausse" relance
                        if (resultatParoleJoueur.get(1).equals(true)) {
                            // Si le joueur n'a pas mise OU si la somme de (son ancienne mise + le minimumARelancerApresSonBet) est < miseMax il a le droit de relancer :
                            if (joueur.getBetPlayer() == 0 || (joueur.getBetPlayer() + joueur.getMinimumARelancerApresBetPlayer() < miseMax)) {
                                System.out.println("3/ Relancer(>= " + sommeMiseMaxMinimumARelancer + ")");
                                resultatParoleJoueur.set(1, false);
                            }
                            // Sinon le joueur est bloque et peut seulement Suivre, pas de possibilite de relance
                        }
                        // Si aucune "fausse" relance, le joueur peut relancer normalement
                        else {
                            System.out.println("3/ Relancer(>= " + sommeMiseMaxMinimumARelancer + ")");
                        }
                    }
                }
            } else { // Si Mise Max = 0, personne n'a mise et nous ne pouvons donc pas avoir de relances bloquees...
                if (tapisPlusMiseJoueur < (int) resultatParoleJoueur.get(0)) {
                    System.out.println("2/ Check");
                    System.out.println("3/ All-in(" + getMontantSuivre(joueur, miseMax) + ")");
                } else {
                    System.out.println("2/ Check");
                    System.out.println("3/ Miser( >= " + sommeMiseMaxMinimumARelancer + ")");
                }
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
            }                                                                     // MM:100       BB:40           MR:40                          MR(0):60   ERB(1): FALSE
            resultatParoleJoueur = doActionJoueur(joueur, joueursDansLeCoup, choix, miseMax, montantBigBlind, bbMinRelance, joueursALaTable, resultatParoleJoueur);
        } while (choix != 1 && choix != 2 && choix != 3); // Faire tant que le choix de l'utilisateur ne correspond pas aux actions proposees
        return resultatParoleJoueur;
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

    private static int addMontantRelance(Joueur joueur, int miseMax, int montantBigBlind, int relance) {
        int addMontantRelance = 0;
        if (miseMax == 0) { // Si personne n'a encore mise..
            addMontantRelance = montantBigBlind - joueur.getBetPlayer(); // On relance au min le montant de la big blind
        } else { // Si une mise est deja en jeu..
            addMontantRelance = (relance - miseMax) - joueur.getBetPlayer(); // On relance au min le double de la mise
        }
        return addMontantRelance;
    }

    private static ArrayList doActionJoueur(Joueur joueur, LinkedList<Joueur> joueursDansLeCoup, int choix,
                                            int miseMax, int montantBigBlind, int bbMinRelance, Joueur[] joueursALaTable, ArrayList resultatParoleJoueur) {
        //                                      100              40                  40                                    MR(0):60   ERB(1): FALSE
        switch (choix) {
            case 1:
                actionDePasser(joueur, joueursDansLeCoup, joueursALaTable);
                break;
            case 2:                      //40
                actionDeSuivre(joueur, miseMax, joueursALaTable);
                break;
            case 3:                                              //100       40                            MR(0):60   ERB(1): FALSE
                resultatParoleJoueur = actionDeRelancer(joueur, miseMax, bbMinRelance, joueursALaTable, resultatParoleJoueur);
                break;
        }
        System.out.println("potTotal = " + totalPot); // 160
        miseMax = getMiseMax(joueursDansLeCoup, montantBigBlind);
        System.out.println("valeur de miseMax:" + miseMax); // 100
        System.out.println("Valeur de minimumARelancer :" + (int) resultatParoleJoueur.get(0)); // 60
        return resultatParoleJoueur;
    }


    // ************************************
    // METHODE "actionDeRelancer" :
    // La methode renvoie "resultatParoleJoueur" de type "ArrayList" qui contiendra 2 variables:
    //      - (int)resultatActionDeRelancer.get(0) (minimumARelancer): le montant minimum a relancer que le joueur a rajouter a la mise Max actuelle sur table
    //      - resultatActionDeRelancer.get(1) (estRelanceBloquee) : un booleen qui indique une "fausse" relance ou le joueur se retrouve all in en misant
    //     entre la mise Max actuelle sur table et le montant minimal d'une relance
    //
    //  3 Scenarios se distinguent :
    //      (1) miseMax = bigBlind        => relance > (2 * montantBigBlind)
    //      (2) miseMax = 0               => relance > montantBigBlind
    //      (3) miseMax !=0, !=bigBlind   => relance > (miseMax + minimumARelancer - joueur.getBetPlayer())
    //
    //      (1) miseMax = bigBlind        => Cas d'une 1ere relance Pre-Flop
    //      (2) miseMax = 0               => Cas ou aucun Joueur n'a encore mise, a partir du flop (car miseMax vaut min la bB en pre-flop)
    //      (3) miseMax !=0, !=bigBlind   => Cas ou l'on a deja quelqu'un qui a mise apres le flop
    //
    //  Ex. du cas (1) : A Blinds 20-40, un joueur ne peut pas relancer a 60....
    //  La miseMax vaut la BB avant que le joueur prenne la parole. C'est le cas pour une 1ere relance pre flop
    //
    //  SI      le joueur decide de mettre plus que ses jetons, il part en all in par defaut....
    //
    //      SI      le montant minimal de relance n'a pas ete atteint = cas de relance bloquee avec joueur en all in
    //  C'est un cas de "fausse" relance, le minimum A Relancer reste inchange de celui precedemment, seul miseMax differe
    //
    //      SINON   le montant minimal de la relance est atteint, on attribut au joueur une valeur de reference "mimimumARelancer"
    //  Dans le cas ou un autre joueur serait amene a faire une "fausse" relance a tapis, le joueur actuel ayant precedemment relance
    //  se retrouvera "bloque". Il ne pourra etre debloque que si la valeur de la miseMax (avant la fin du tour d'Enchere) n'excede ou
    //  ne soit egal a la valeur minimumARelancerApresBetPlayer
    //
    //  SINON    le montant saisie de la relance ne depasse pas le stack de jetons du joueur, le joueur ne sera donc pas en "all in"
    //
    //      SI      le joueur a un nombre de jetons suffisant pour payer la mise Minimale requise
    //
    //          SI  sa relance saisie est inferieure a ce montant, alors il devra saisir a nouveau
    //
    //          SINON   le montant minimal de la relance est atteint, on attribut au joueur une valeur de reference "mimimumARelancer"
    //  Dans le cas ou un autre joueur serait amene a faire une "fausse" relance a tapis, le joueur actuel ayant precedemment relance
    //  se retrouvera "bloque". Il ne pourra etre debloque que si la valeur de la miseMax (avant la fin du tour d'Enchere) n'excede ou
    //  ne soit egal a la valeur minimumARelancerApresBetPlayer
    //
    //      SINON   le montant minimal de relance n'a pas ete atteint = cas de relance bloquee avec joueur en all in
    //      C'est un cas de "fausse" relance, le minimum A Relancer reste inchange de celui precedemment, seul miseMax differe
    //
    //
    // - Cas de la gestion du "joueur bloque" : En cas de relance à tapis inférieure à la relance minimum : les joueurs ayant déjà misé ne peuvent pas
    // sur-relancer. Les joueurs placés après et n’ayant pas encore misé peuvent sur-relancer (toujours au minimu du montant du gros blind
    // ou de la mise ou relance d’origine)
    //    ex: ouverture à 200, tapis à 290, relance minimum à 490 (total). Un joueur placé avant mais dont la mise ou la relance initiale
    //    a finalement été doublée par le cumul de plusieurs petits tapis peut à nouveau sur-relancer. »
    //
    //
    // ************************************

    private static ArrayList actionDeRelancer(Joueur joueur, int miseMax, int bbMinRelance, Joueur[] joueursALaTable, ArrayList resultatParoleJoueur) {
        //300              40                                   MR(0):100   ERB(1): FALSE
        Scanner chx = new Scanner(System.in);
        int relance = 0;
        int miseEngagee = joueur.getBetPlayer();


        if (miseMax == bbMinRelance) {         // 300!=40.... on va donc donc le else

            // Condition particuliere : A 20-40, un joueur ne peut pas relancer a 60....

            if (joueur.getChipsPlayer() >= (2 * bbMinRelance)) { // LE JOUEUR A LES MOYENS DE PAYER LA RELANCE MINIMUM

                System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                relance = chx.nextInt();

                while (relance < 2 * bbMinRelance) { // TANT QUE SA RELANCE EST < A MIN.RELANCE => SAISIR A NOUVEAU
                    System.out.println("Vous devez relancer au minimum d'une big blind (+" + bbMinRelance + ") sur la miseMax actuelle (" + miseMax + ")");
                    relance = chx.nextInt();
                }
                // SA SAISIE DEPASSE MAINTENANT LE MIN. REQUIS....
                // SI SA RELANCE DEPASSE OU EST EGAL A SON STACK, ON LE MET ALL IN PAR DEFAUT :

                relance = relanceOverStackAllinAndMAJRelanceMinimum(joueur, miseMax, resultatParoleJoueur, relance);

            } else { // LE JOUEUR N'A PAS LES MOYENS DE PAYER LA RELANCE MINIMUM DE 2*bbMinRelance, IL VA DONC PARTIR EN ALL IN AVEC SON STACK

                relance = joueur.getChipsPlayer();
                System.out.println(joueur + " mise all in avec " + relance + " jetons");
                joueur.setMinimumARelancerApresBetPlayer(0);

                // le joueur se trouvant "all in" il n'a plus besoin de valeur rattachee car il ne pourra pas etre "debloque"

                resultatParoleJoueur.set(1, true);

                // LA MISE SERA CONSIDERE COMME UNE RELANCE "blocante" POUR LES FUTURS JOUEURS
                // IL N'Y A PAS DE MAJ DANS CE CAS POUR LE MIN A RELANCER CAR CE N'EST PAS CONSIDERE COMME UNE VRAIE RELANCE
            }
        } else if (miseMax == 0) {    // SI PERSONNE N'EST ENCORE RENTRE DANS LE COUP, LA RELANCE DOIT ETRE SUPERIEUR OU EGAL A LA BB   // MM=300.... on va dans le else
            if (joueur.getChipsPlayer() >= bbMinRelance) { // LE JOUEUR A LES MOYENS DE PAYER LA RELANCE MINIMUM

                System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                relance = chx.nextInt();

                while (relance < bbMinRelance) { // TANT QUE SA RELANCE EST < A MIN.RELANCE => SAISIR A NOUVEAU
                    System.out.println("Vous devez relancer au minimum d'une big blind (+" + bbMinRelance + ")");
                    relance = chx.nextInt();
                }
                // SA SAISIE DEPASSE MAINTENANT LE MIN. REQUIS....
                // SI SA RELANCE DEPASSE OU EST EGAL A SON STACK, ON LE MET ALL IN PAR DEFAUT :
                if (relance >= joueur.getChipsPlayer()) {
                    relance = joueur.getChipsPlayer();
                    System.out.println(joueur + " mise all in avec " + relance + " jetons");
                } else { // SINON LA RELANCE EST COMPRISE ENTRE LE MIN.RELANCE ET LE STACK JOUEUR :
                    // MISE A JOUR DE MIN A RELANCER :
                    if (relance > miseMax) {
                        resultatParoleJoueur.set(0, relance);
                        // ON GARGE LA VALEUR MIN A RELANCER ATTACHE AU JOUEUR DANS LE CAS OU UN AUTRE
                    }
                }
            } else { // LE JOUEUR N'A PAS LES MOYENS DE PAYER LA RELANCE MINIMUM DE bbMinRelance, IL VA DONC PARTIR EN ALL IN AVEC SON STACK
                relance = joueur.getChipsPlayer();
                System.out.println(joueur + " mise all in avec " + relance + " jetons");
                joueur.setMinimumARelancerApresBetPlayer(0); // le joueur se trouvant "all in" il n'a plus besoin de valeur rattachee car il ne pourra pas etre "debloque"
                resultatParoleJoueur.set(1, true); // LA MISE SERA CONSIDERE COMME UNE RELANCE "blocante" POUR LES FUTURS JOUEURS
                // IL N'Y A PAS DE MAJ DANS CE CAS POUR LE MIN A RELANCER CAR CE N'EST PAS CONSIDERE COMME UNE VRAIE RELANCE
            }
        } else {  // SI UNE PERSONNE A DEJA RELANCE ALORS LA DIFF ENTRE LA SURRELANCE ET LA DERNIERE MISE DOIT ETRE
            // SUPERIEURE OU EGALE A LA DIFFERENCE ENTRE LA DERNIERE  MISE ET CELLE D'AVANT

            // 9100 >= (8000 + 8000 - 0)

            if (joueur.getChipsPlayer() >= ((miseMax + (int) resultatParoleJoueur.get(0) - joueur.getBetPlayer()))) { // LE JOUEUR A LES MOYENS DE PAYER LA RELANCE MINIMUM
                // 210 <  (250+150-0)

                System.out.print(joueur + " decide de relancer : \n A combien souhaitez vous relancer ? ");
                relance = chx.nextInt();

                while (relance < ((miseMax + (int) resultatParoleJoueur.get(0) - joueur.getBetPlayer()))) { // TANT QUE SA RELANCE EST < A MIN.RELANCE => SAISIR A NOUVEAU
                    System.out.println("Vous devez relancer au minimum (+" + (int) resultatParoleJoueur.get(0) + ") sur la miseMax actuelle (" + miseMax + ")");
                    relance = chx.nextInt();
                }
                // SA SAISIE DEPASSE MAINTENANT LE MIN. REQUIS....
                // SI SA RELANCE DEPASSE OU EST EGAL A SON STACK, ON LE MET ALL IN PAR DEFAUT :
                relance = relanceOverStackAllinAndMAJRelanceMinimum(joueur, miseMax, resultatParoleJoueur, relance);
            } else { // LE JOUEUR N'A PAS LES MOYENS DE PAYER LA RELANCE MINIMUM DE bbMinRelance, IL VA DONC PARTIR EN ALL IN AVEC SON STACK

                relance = joueur.getChipsPlayer();
                System.out.println(joueur + " mise all in avec " + relance + " jetons");
                joueur.setMinimumARelancerApresBetPlayer(0); // le joueur se trouvant "all in" il n'a plus besoin de valeur rattachee car il ne pourra pas etre "debloque"
                resultatParoleJoueur.set(1, true); // LA MISE SERA CONSIDERE COMME UNE RELANCE "blocante" POUR LES FUTURS JOUEURS
                // IL N'Y A PAS DE MAJ DANS CE CAS POUR LE MIN A RELANCER CAR CE N'EST PAS CONSIDERE COMME UNE VRAIE RELANCE
            }
        }
        joueur.setBetPlayer(relance);
        copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable); // Copie du bet dans JoueurALaTable ou cas ou il se couche
        joueur.setChipsPlayer(joueur.getChipsPlayer() - relance);
        System.out.println(joueur + " relance a " + joueur.getBetPlayer() + " jetons");
        totalPot = totalPot + relance - miseEngagee;
        System.out.println("Valeur de minimumARelancer / resultatParoleJoueur.get(0) :" + (int) resultatParoleJoueur.get(0)); // 100
        System.out.println("Valeur de estRelanceBloquee / resultatParoleJoueur.get(1):" + resultatParoleJoueur.get(1)); // false

        return resultatParoleJoueur;
    }

    private static int relanceOverStackAllinAndMAJRelanceMinimum(Joueur joueur, int miseMax, ArrayList resultatParoleJoueur, int relance) {
        if (relance >= joueur.getChipsPlayer()) {
            relance = joueur.getChipsPlayer();
            System.out.println(joueur + " mise all in avec " + relance + " jetons");
        } else { // SINON LA RELANCE EST COMPRISE ENTRE LE MIN.RELANCE ET LE STACK JOUEUR :
            // MISE A JOUR DE MIN A RELANCER :
            if (relance > miseMax) {
                resultatParoleJoueur.set(0, relance - miseMax);
                // ON GARGE LA VALEUR MIN A RELANCER ATTACHE AU JOUEUR DANS LE CAS OU UN AUTRE JOUEUR SERAIT AMENER A LE BLOQUER :
                joueur.setMinimumARelancerApresBetPlayer((int) resultatParoleJoueur.get(0));
            }
        }
        return relance;
    }

    private static void actionDePasser(Joueur joueur, LinkedList<Joueur> joueursDansLeCoup, Joueur[]
            joueursALaTable) {
        System.out.println(joueur + " passe son tour ");
        copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
        joueursDansLeCoup.remove(joueur);
    }

    private static void actionDeSuivre(Joueur joueur, int miseMax, Joueur[] joueursALaTable) {
        int add = getMontantSuivre(joueur, miseMax);

        if (getMontantSuivre(joueur, miseMax) == 0) {
            joueur.setBetPlayer(joueur.getBetPlayer() + add);
            copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
            joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
            System.out.println(joueur + " check ");
        } else if (joueur.getChipsPlayer() <= miseMax) {
            joueur.setBetPlayer(joueur.getBetPlayer() + add);
            copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
            joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
            System.out.println(joueur + " suit et est all in avec " + joueur.getBetPlayer() + " jetons");
        } else {
            joueur.setBetPlayer(joueur.getBetPlayer() + add);
            copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
            joueur.setChipsPlayer(joueur.getChipsPlayer() - add);
            System.out.println(joueur + " suit " + joueur.getBetPlayer() + " jetons");
        }
        totalPot = totalPot + add;
        copieMiseJoueurDansLeCoupVersJoueurALaTable(joueur, joueursALaTable);
    }

    private static void copieMiseJoueurDansLeCoupVersJoueurALaTable(Joueur joueur, Joueur[] joueursALaTable) {
        for (Joueur joueur1 : joueursALaTable) {
            if (joueur1.getNamePlayer() == joueur.getNamePlayer()) {
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
