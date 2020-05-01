package com.poker;

import java.util.*;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

        // ************************************
        // Déclaration des attributs
        // ************************************

        int newPot = 0;
        int potTotal = newPot;
        int tourDeParole = 0;

        // ************************************
        // INITIALISATION DES JOUEURS :
        // ************************************

        Scanner sc = new Scanner(System.in);
        int nombreDeJoueurs = 0;
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

        // ************************************
        // INITIALISATION DE LA PARTIE :
        // Creation d'un paquet, de "N" joueurs, affectation des "Noms de Joueurs",
        // attribution des mains Joueurs du paquet melange
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

            // ************************************
            //             CARDS DEALT
            // ************************************

            distributionMainJoueur(listJoueur[i], paquet);  // affecte 2 cartes au joueur listJoueur[i]
        }

        // ************************************
        // TIRAGE AU SORT du DEALER et attribution des Small Blind et Big Blind
        // ************************************

        Random startDealer = new Random();
        int dealer = startDealer.nextInt(nombreDeJoueurs);
        int smallBlind = dealer + 1;
        int bigBlind = smallBlind + 1;
        LinkedList<Joueur> joueurs = new LinkedList<>();
        // Variable liste de nos joueurs encore en jeu, si un joueur n'a plus de jetons, il doit etre retire de cette liste

        // ************************************
        // On remplit l'objet LinkedList de joueurs avec une boucle For qui recupere tous les joueurs :
        // ************************************

        initialisationListeJoueur(listJoueur, joueurs);

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

        joueurs.getFirst().setBetPlayer(20); // on set la mise du joueur SB situe en tete de pile
        potTotal = potTotal + 20;
        joueurs.getFirst().setChipsPlayer(joueurs.getFirst().getChipsPlayer() - joueurs.getFirst().getBetPlayer()); // on met a jour son compteur de jetons
        System.out.println(" Small Blind : " + joueurs.getFirst() + " Met " + joueurs.getFirst().getBetPlayer() + " jetons");
        System.out.println("-----------------------------");
        joueurs.addLast(joueurs.getFirst()); // on place le joueur SB actuellement en tete de pile > en queue de pile
        joueurs.removeFirst();

        // ************************************
        // Mise en queue de pile le BB :
        // ************************************

        joueurs.getFirst().setBetPlayer(40);// on set la mise du joueur BB situe en tete de pile
        potTotal = potTotal + 40;
        joueurs.getFirst().setChipsPlayer(joueurs.getFirst().getChipsPlayer() - joueurs.getFirst().getBetPlayer()); // on met a jour son compteur de jetons
        System.out.println(" Big Blind : " + joueurs.getFirst() + " Met " + joueurs.getFirst().getBetPlayer() + " jetons");
        System.out.println("-----------------------------");
        joueurs.addLast(joueurs.getFirst()); // on place le joueur BB actuellement en tete de pile > en queue de pile
        joueurs.removeFirst();

        System.out.println("Valeur du Pot Total : " + potTotal);

        // ************************************
        //             PRE-FLOP
        // ************************************

        do {
            System.out.println(" [ PRE-FLOP ] ");
            tourDeParole = 1;
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

            // ************************************
            //Option 1 : avoir 2 listes de joueurs, une pour conserver la relance en premier,
            // l'autre pour conserver la BB en premier
            //Option 2 : avoir une seule liste, mais au lieu de vérifier les mises du premier
            //et du dernier, on vérifie toutes les mises. Du coup pas besoin de modifier la liste
            //quand qqn relance
            // ************************************

            // ************************************
            //              FLOP
            // ************************************

            Card[] cartesCommunes = tirageFlop(paquet);
            joueurs.addFirst(joueurs.getLast()); // On remet la BB en tete de liste
            joueurs.removeLast(); // // Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB

            // ************************************
            // *** COMBINAISONS PAR JOUEUR RESTANT EN JEU SUR LA MAIN
            // ************************************

            for (int i = 0; i < joueurs.size(); i++) {
                joueurs.get(i).getCardsCommunesAndHandPlayer()[2] = cartesCommunes[0];
                joueurs.get(i).getCardsCommunesAndHandPlayer()[3] = cartesCommunes[1];
                joueurs.get(i).getCardsCommunesAndHandPlayer()[4] = cartesCommunes[2];
                System.out.println("Combinaison du joueur " + joueurs.get(i).getNamePlayer() + " : " + joueurs.get(i).getCardsCommunesAndHandPlayer()[0] + joueurs.get(i).getCardsCommunesAndHandPlayer()[1] + " + " + joueurs.get(i).getCardsCommunesAndHandPlayer()[2] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[3] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[4] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[5] + " " + joueurs.get(i).getCardsCommunesAndHandPlayer()[6]);
            }

            // ************************************
            // 2e TOUR DE PAROLE POST FLOP
            // ************************************

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ FLOP ] ");
            tourDeParole = 2;
            resetBetPlayer(joueurs);
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

            // ************************************
            // *** TIRAGE DE LA TURN ***
            // ************************************

            tirageTurn(paquet, cartesCommunes);
//            joueurs.addFirst(joueurs.getLast()); // On remet la BB en tete de liste
//            joueurs.removeLast(); // Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB

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
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

            // ************************************
            // *** TIRAGE DE LA RIVER ***
            // ************************************

            tirageRiver(paquet, cartesCommunes);
//            joueurs.addFirst(joueurs.getLast()); // On remet la BB en tete de liste
//            joueurs.removeLast(); // Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB

            System.out.println("---------------------------------------------------------");
            System.out.println(" [ RIVER ] ");
            tourDeParole = 4;
            resetBetPlayer(joueurs);
            potTotal = toursDeTableJusquaAccordMise(joueurs, potTotal, tourDeParole);

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

            System.out.println();
            System.out.println(" TO DO LIST / CORRECTIFS A APPORTER : ");
            System.out.println();
            System.out.println(" * Redonner la parole a partir du rang Big Bling apres tirage du flop - Code ne marche pas dans le cas ou l'un joueur autre relance et tout le monde call car le dernier joueur sera en queue de pile a la place de la BB");
            System.out.println(" * Revoir la condition de fin de main car plusieurs joueurs peuvent encore etre en jeu, pas forcement tant qu'il ne reste plus d'1 joueur");
            System.out.println(" de la relance. On peut miser all in meme si < mise  min autorisee ");
            System.out.println(" * Revoir affichage seulement du tour 3 et 4 : Cas Call min Post Flop (tour2) = BB, call min turn et river (tour3et4) = 2BB min");
            System.out.println(" * Remplacer Suivre a (0) par Parole && Relancer par call si betPlayer.first=betplayer.last=0 ");
            System.out.println(" * Gerer les cas de Pot 1 Pot 2 etc....");
            System.out.println(" * Desactiver le choix relancer si les jetons du joueur sont inferieur a la mise min de relance");
            System.out.println(" * Creation d'un time Bank Joueur 30s");
            System.out.println(" * Creation d'une structure de blind");

        } while (joueurs.size() != 1 && tourDeParole < 4);
        // Fin de la main / Tant qu'il reste plus qu'un joueur, il remportera donc le pot

        System.out.println(" Sortie du do while");
        System.out.println("Affichage de la liste des joueurs sur la Main N.2 :" + joueurs);

        initialisationListeJoueur(listJoueur, joueurs);

    } // Fin de la methode main

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
                Timer chrono = new Timer();

                chrono.schedule(new TimerTask() {
                    int time = 30;
                    @Override
                    public void run() {
                        System.out.print(time + "s ... ");
                        if (time == 0) {
                            cancel();
                        }
                        time=time-10;
                    }
                }, 1000, 10000);
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




// 2e COMPARAISON EN CAS D'EGALITE : Si les Valeurs Combi sont identiques, il va falloir comparer avec un autre parametre selon la combinaison

//                    switch (valueCombi) {
//                        case 1: // si High Card
//                            // On compare la hauteur des kickers 1 puis 2 si egalite, etc...
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value > resultatTemp.kicker1.value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value == resultatTemp.kicker1.value) {
//                                if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker2.value > resultatTemp.kicker2.value) {
//                                    resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                    joueurHigherCombi.clear();
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker2.value == resultatTemp.kicker2.value) {
//                                    if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker3.value > resultatTemp.kicker3.value) {
//                                        resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                        joueurHigherCombi.clear();
//                                        joueurHigherCombi.add(joueurs.get(i));
//                                    } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker3.value == resultatTemp.kicker3.value) {
//                                        if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker4.value > resultatTemp.kicker4.value) {
//                                            resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                            joueurHigherCombi.clear();
//                                            joueurHigherCombi.add(joueurs.get(i));
//                                        } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker4.value == resultatTemp.kicker4.value) {
//                                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker5.value > resultatTemp.kicker5.value) {
//                                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                                joueurHigherCombi.clear();
//                                                joueurHigherCombi.add(joueurs.get(i));
//                                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker5.value == resultatTemp.kicker5.value) {
//                                                joueurHigherCombi.add(joueurs.get(i));
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                            break;
//                        case 2: // si Paire
//                            // On compare la hauteur des paires puis le kicker 1  si egalite etc...
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value > resultatTemp.kicker1.value) {
//                                    resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                    joueurHigherCombi.clear();
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value == resultatTemp.kicker1.value) {
//                                    if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker2.value > resultatTemp.kicker2.value) {
//                                        resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                        joueurHigherCombi.clear();
//                                        joueurHigherCombi.add(joueurs.get(i));
//                                    } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker2.value == resultatTemp.kicker2.value) {
//                                        if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker3.value > resultatTemp.kicker3.value) {
//                                            resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                            joueurHigherCombi.clear();
//                                            joueurHigherCombi.add(joueurs.get(i));
//                                        } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker3.value == resultatTemp.kicker3.value) {
//                                            joueurHigherCombi.add(joueurs.get(i));
//                                        }
//                                    }
//                                }
//                            }
//                            break;
//                        case 3: // Si Double Paire
//                            // On compare la hauteur de la 1ere paire puis la hauteur de la 2e paire puis la hauteur kicker 1  si egalite etc...
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur2().value > resultatTemp.getHauteur2().value) {
//                                    resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                    joueurHigherCombi.clear();
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur2().value == resultatTemp.getHauteur2().value) {
//                                    if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value > resultatTemp.kicker1.value) {
//                                        resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                        joueurHigherCombi.clear();
//                                        joueurHigherCombi.add(joueurs.get(i));
//                                    } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value == resultatTemp.kicker1.value) {
//                                        joueurHigherCombi.add(joueurs.get(i));
//                                    }
//                                }
//                            }
//                            break;
//                        case 4: // Si Brelan
//                            // On compare la hauteur du brelan puis kicker 1 et 2 si egalite
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value > resultatTemp.kicker1.value) {
//                                    resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                    joueurHigherCombi.clear();
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker1.value == resultatTemp.kicker1.value) {
//                                    if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker2.value > resultatTemp.kicker2.value) {
//                                        resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                        joueurHigherCombi.clear();
//                                        joueurHigherCombi.add(joueurs.get(i));
//                                    } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).kicker2.value == resultatTemp.kicker2.value) {
//                                        joueurHigherCombi.add(joueurs.get(i));
//                                    }
//                                }
//                            }
//                            break;
//                        case 5: // Si Suite
//                            // On compare la hauteur de la suite
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                joueurHigherCombi.add(joueurs.get(i));
//                            }
//                            break;
//                        case 6: // Si Couleur
//                            // On compare la hauteur de la couleur
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                joueurHigherCombi.add(joueurs.get(i));
//                            }
//                            break;
//                        case 7: // Si Full
//                            // On compare la hauteur du brelan puis la hauteur de la paire en cas d'egalite
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getKicker1().value > resultatTemp.getKicker1().value) {
//                                    resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                    joueurHigherCombi.clear();
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getKicker1().value == resultatTemp.getKicker1().value) {
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                }
//                            }
//                            break;
//                        case 8: // Si Carre
//                            // On compare la hauteur du carre puis le kicker 1 en cas de carre commun sur le board
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getRangCarre().value > resultatTemp.getRangCarre().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getRangCarre().value == resultatTemp.getRangCarre().value) {
//                                if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getKicker1().value > resultatTemp.getKicker1().value) {
//                                    resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                    joueurHigherCombi.clear();
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getKicker1().value == resultatTemp.getKicker1().value) {
//                                    joueurHigherCombi.add(joueurs.get(i));
//                                }
//                            }
//                            break;
//                        case 9: // Si Quinte Flush
//                            // On compare la hauteur
//                            if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value > resultatTemp.getHauteur().value) {
//                                resultatTemp = CombinaisonUtil.getCombinaison(joueurs.get(i));
//                                joueurHigherCombi.clear();
//                                joueurHigherCombi.add(joueurs.get(i));
//                            } else if (CombinaisonUtil.getCombinaison(joueurs.get(i)).getHauteur().value == resultatTemp.getHauteur().value) {
//                                joueurHigherCombi.add(joueurs.get(i));
//                            }
//                            break;
//                    }
//                }
//                valueMaxCombiJoueur = CombinaisonUtil.getCombinaison(joueurs.get(i)).getValueCombinaison();
//            }

