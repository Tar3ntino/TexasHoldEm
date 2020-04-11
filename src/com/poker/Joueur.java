package com.poker;

import java.util.Arrays;
import java.util.Random;

public class Joueur {

    String namePlayer;
    int chipsPlayer = 10000;                // CREATION d'une liste de Jetons par joueur, indice 0-joueur0 etc.
    Card[] main = new Card[2];
    Card[] cardsCommunesAndHandPlayer = new Card[7];
    int betPlayer = 0;

    //Constructeur avec paramètres
    public Joueur(String namePlayer, Card[] main) {
        System.out.println("Player : " + namePlayer + "\n Chips :" + chipsPlayer + "\n MainJoueur :" + main);
    }

    public Joueur(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    //***** Getter - Accesseurs ******

    public String getNamePlayer() {
        return namePlayer;
    }

    public int getChipsPlayer() {
        return chipsPlayer;
    }

    public Card[] getMain() {
        return main;
    }

    public int getBetPlayer() {
        return betPlayer;
    }

    //******* Setter - Mutateurs *******

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public void setChipsPlayer(int chipsPlayer) {

        this.chipsPlayer = chipsPlayer;
    }

    public void setMain(Card[] main) {

        this.main = main;
    }

    public void setBetPlayer(int betPlayer) {
        this.betPlayer = betPlayer;
    }

    @Override
    public String toString() {
        return namePlayer + " Jetons: " + chipsPlayer + " \n";
    }


    // LES COMBINAISONS AU POKER

    // Hand Rankings :
    // 1.Straight Flush
    // 2.Four of a kind
    // 3.Full House
    // 4.Flush
    // 5.Straight
    // 6.Three of a kind
    // 7.Two pair
    // 8.Pair
    // 9.High Card


    public Card[] getCardsCommunesAndHandPlayer() {
        return cardsCommunesAndHandPlayer;
    }

    public void setCardsCommunesAndHandPlayer(Card[] cardsCommunesAndHandPlayer) {
        this.cardsCommunesAndHandPlayer = cardsCommunesAndHandPlayer;
    }

    // ************************************
    // METHODE CHECK FLUSH (COULEUR)
    // ************************************

    public boolean checkFlush(Card[] cardsCommunesAndHandPlayer) {
        int nbCoeur = 0;
        int nbCarreau = 0;
        int nbPique = 0;
        int nbTrefle = 0;

        for (Card card : cardsCommunesAndHandPlayer) {
            if (card.getCouleur().name() == "Coeur") {
                nbCoeur++;
            } else if (card.getCouleur().name() == "Carreau") {
                nbCarreau++;
            } else if (card.getCouleur().name() == "Pique") {
                nbPique++;
            } else if (card.getCouleur().name() == "Trefle") {
                nbTrefle++;
            }
        }
//        System.out.println("Nombre de ♦:" + nbCarreau + "     Nombre de ♥:" + nbCoeur + "     Nombre de ♠:️" + nbPique + "     Nombre de ♣:" + nbTrefle);
        if (nbCoeur >= 5 && nbCarreau >= 5 && nbPique >= 5 && nbTrefle >= 5) {
            return true;
        } else
            return false;
    } // Fin de la methode SortCardCheckStraight


    // ************************************
    // METHODE CHECK STRAIGHT (SUITE)
    // ************************************

    public boolean checkStraight(Card[] cardsCommunesAndHandPlayer) {
        Card temp = new Card();
        int inARow = 0;

        sortCardCheckStraight(cardsCommunesAndHandPlayer, 0); // 1er tri des 7 cartes par valeur
        // On incremente 1 lorsque la valeur de la carte qui suite = valeur de la carte d'avant +1
        for (int k = 1; k < cardsCommunesAndHandPlayer.length; k++) {
            if (cardsCommunesAndHandPlayer[k].getRang().getValue() == cardsCommunesAndHandPlayer[k - 1].getRang().getValue() + 1) {
                inARow++;
            } else {
                if (cardsCommunesAndHandPlayer[k].getRang().getValue() == cardsCommunesAndHandPlayer[k - 1].getRang().getValue()) {
                    continue;
                } else {
                    inARow = 0;
                }
            }
        }
        if (inARow >= 4) {
            return true;
        } else {
            inARow = 0; // on reinitialise pour la 2eme verification de suite Hauteur 5 (As-2-3-4-5)
            if (cardsCommunesAndHandPlayer[6].getRang().getValue() == 14) { // s'il y a un as en derniere carte
                changeAsAuDebut(cardsCommunesAndHandPlayer); // alors on le place au debut
                if (cardsCommunesAndHandPlayer[1].getRang().getValue() == cardsCommunesAndHandPlayer[0].getRang().getValue() - 12) {
                    inARow++; // On verifie si l'on a une suite avec l'As et le 2...
                    for (int l = 2; l < cardsCommunesAndHandPlayer.length; l++) { // ...puis l'on procede a une verif normale avec les rangs suivants
                        if (cardsCommunesAndHandPlayer[l].getRang().getValue() == cardsCommunesAndHandPlayer[l - 1].getRang().getValue() + 1) {
                            inARow++;
                        } else {
                            if (inARow < 4) {
                                inARow = 0;
                            }
                        }
                    }
                }
            } else {
                inARow = 0;
            }
        }
        if (inARow >= 4) {
            return true;
        } else {
            return false;
        }
    } // Fin de la methode CheckStraight

    private void changeAsAuDebut(Card[] cardsCommunesAndHandPlayer) {
        Card temp2 = new Card();
        temp2 = cardsCommunesAndHandPlayer[6];
        cardsCommunesAndHandPlayer[6] = cardsCommunesAndHandPlayer[5];
        cardsCommunesAndHandPlayer[5] = cardsCommunesAndHandPlayer[4];
        cardsCommunesAndHandPlayer[4] = cardsCommunesAndHandPlayer[3];
        cardsCommunesAndHandPlayer[3] = cardsCommunesAndHandPlayer[2];
        cardsCommunesAndHandPlayer[2] = cardsCommunesAndHandPlayer[1];
        cardsCommunesAndHandPlayer[1] = cardsCommunesAndHandPlayer[0];
        cardsCommunesAndHandPlayer[0] = temp2;
    } // Fin de la methode changeAsAuDebut

    private void sortCardCheckStraight(Card[] cardsCommunesAndHandPlayer, int rangDebutTri) {
        Card temp;
        for (int j = rangDebutTri; j < cardsCommunesAndHandPlayer.length; j++) { // 2/ On trie 7 fois en recomparant les cartes precedemment triees
            for (int i = 1; i < cardsCommunesAndHandPlayer.length; i++) { // 1/ On trie 1 premiere fois en comparant la carte n avec celle d'avant
                if (cardsCommunesAndHandPlayer[i].getRang().getValue() < cardsCommunesAndHandPlayer[i - 1].getRang().getValue()) {
                    temp = cardsCommunesAndHandPlayer[i];
                    cardsCommunesAndHandPlayer[i] = cardsCommunesAndHandPlayer[i - 1];
                    cardsCommunesAndHandPlayer[i - 1] = temp;
                }
            }
        }
    } // Fin de la methode SortCardCheckStraight


    // ************************************
    // METHODE CHECK STRAIGHT FLUSH (QUINTE FLUSH)
    // ************************************

    public boolean checkStraightFlush(Card[] cardsCommunesAndHandPlayer, boolean checkStraight, boolean checkFlush) {

        if (checkStraight == true && checkFlush == true) {
            return true;
        } else {
            return false;
        }
    } // Fin de la methode Straight Flush


    // ************************************
    // METHODE CHECK FOUR OF A KIND (CARRE)
    // ************************************

    public boolean checkFourOfAKind(Card[] cardsCommunesAndHandPlayer) {

        int nbAs = 0;
        int nbK = 0;
        int nbQ = 0;
        int nbJ = 0;
        int nb10 = 0;
        int nb9 = 0;
        int nb8 = 0;
        int nb7 = 0;
        int nb6 = 0;
        int nb5 = 0;
        int nb4 = 0;
        int nb3 = 0;
        int nb2 = 0;

        Rang rangCard;

        for (int i = 0; i < cardsCommunesAndHandPlayer.length; i++) {

            switch (rangCard = cardsCommunesAndHandPlayer[i].getRang()) {

                case As:
                    nbAs++;
                    break;
                case Roi:
                    nbK++;
                    break;
                case Dame:
                    nbQ++;
                    break;
                case Valet:
                    nbJ++;
                    break;
                case Dix:
                    nb10++;
                    break;
                case Neuf:
                    nb9++;
                    break;
                case Huit:
                    nb8++;
                    break;
                case Sept:
                    nb7++;
                    break;
                case Six:
                    nb6++;
                    break;
                case Cinq:
                    nb5++;
                    break;
                case Quatre:
                    nb4++;
                    break;
                case Trois:
                    nb3++;
                    break;
                case Deux:
                    nb2++;
                    break;
            }
        }
//        System.out.println("nbAs:" + nbAs + " nbK:" + nbK + " nbQ:" + nbQ + " nbJ:" + nbJ + " nb10:" + nb10 + " nb9:" + nb9 + " nb8:" + nb8 + " nb7:" + nb7 + " nb6:" + nb6 + " nb5:" + nb5 + " nb4:" + nb4 + " nb3:" + nb3 + " nb2:" + nb2);

        if (nbAs == 4 && nbK == 4 && nbQ == 4 && nbJ == 4 && nb10 == 4 && nb9 == 4 && nb8 == 4 && nb7 == 4 && nb6 == 4 && nb5 == 4 && nb4 == 4 && nb3 == 4 && nb2 == 4) {
            return true;
        } else {
            return false;
        }
    } // Fin de la methode Check Four of a kind


    // ************************************
    // METHODE CHECK FULL HOUSE (FULL)
    // ************************************

    public boolean checkFullHouse(Card[] cardsCommunesAndHandPlayer) {

        int nbAs = 0;
        int nbK = 0;
        int nbQ = 0;
        int nbJ = 0;
        int nb10 = 0;
        int nb9 = 0;
        int nb8 = 0;
        int nb7 = 0;
        int nb6 = 0;
        int nb5 = 0;
        int nb4 = 0;
        int nb3 = 0;
        int nb2 = 0;

        Rang rangCard = Rang.As;

        for (int i = 0; i < cardsCommunesAndHandPlayer.length; i++) {

            switch (rangCard = cardsCommunesAndHandPlayer[i].getRang()) {

                case As:
                    nbAs++;
                    break;
                case Roi:
                    nbK++;
                    break;
                case Dame:
                    nbQ++;
                    break;
                case Valet:
                    nbJ++;
                    break;
                case Dix:
                    nb10++;
                    break;
                case Neuf:
                    nb9++;
                    break;
                case Huit:
                    nb8++;
                    break;
                case Sept:
                    nb7++;
                    break;
                case Six:
                    nb6++;
                    break;
                case Cinq:
                    nb5++;
                    break;
                case Quatre:
                    nb4++;
                    break;
                case Trois:
                    nb3++;
                    break;
                case Deux:
                    nb2++;
                    break;
            }
        }
//        System.out.println("nbAs:" + nbAs + " nbK:" + nbK + " nbQ:" + nbQ + " nbJ:" + nbJ + " nb10:" + nb10 + " nb9:" + nb9 + " nb8:" + nb8 + " nb7:" + nb7 + " nb6:" + nb6 + " nb5:" + nb5 + " nb4:" + nb4 + " nb3:" + nb3 + " nb2:" + nb2);
        if ((nbAs == 3) && (nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2) && ((nbK == 3) && (nbAs >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nbQ == 3) && (nbAs >= 2 && nbK >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nbJ == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb10 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb9 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb8 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb7 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb6 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb5 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb4 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb4 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb3 >= 2 && nb2 >= 2)) && ((nb3 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb2 >= 2)) && ((nb2 == 3) && (nbAs >= 2 && nbK >= 2 && nbQ >= 2 && nbJ >= 2 && nb10 >= 2 && nb9 >= 2 && nb8 >= 2 && nb7 >= 2 && nb6 >= 2 && nb5 >= 2 && nb4 >= 2 && nb3 >= 2))) {
            return true;
        } else {
            return false;
        }
    } // Fin de la methode Full


    // ************************************
    // METHODE CHECK THREE OF A KIND (BRELAN)
    // ************************************
    public boolean checkThreeOfAKind(Card[] cardsCommunesAndHandPlayer) {

        int nbAs = 0;
        int nbK = 0;
        int nbQ = 0;
        int nbJ = 0;
        int nb10 = 0;
        int nb9 = 0;
        int nb8 = 0;
        int nb7 = 0;
        int nb6 = 0;
        int nb5 = 0;
        int nb4 = 0;
        int nb3 = 0;
        int nb2 = 0;

        Rang rangCard = Rang.As;

        for (int i = 0; i < cardsCommunesAndHandPlayer.length; i++) {

            switch (rangCard = cardsCommunesAndHandPlayer[i].getRang()) {

                case As:
                    nbAs++;
                    break;
                case Roi:
                    nbK++;
                    break;
                case Dame:
                    nbQ++;
                    break;
                case Valet:
                    nbJ++;
                    break;
                case Dix:
                    nb10++;
                    break;
                case Neuf:
                    nb9++;
                    break;
                case Huit:
                    nb8++;
                    break;
                case Sept:
                    nb7++;
                    break;
                case Six:
                    nb6++;
                    break;
                case Cinq:
                    nb5++;
                    break;
                case Quatre:
                    nb4++;
                    break;
                case Trois:
                    nb3++;
                    break;
                case Deux:
                    nb2++;
                    break;
            }
        }
//        System.out.println("nbAs:" + nbAs + " nbK:" + nbK + " nbQ:" + nbQ + " nbJ:" + nbJ + " nb10:" + nb10 + " nb9:" + nb9 + " nb8:" + nb8 + " nb7:" + nb7 + " nb6:" + nb6 + " nb5:" + nb5 + " nb4:" + nb4 + " nb3:" + nb3 + " nb2:" + nb2);
        if ((nbAs == 3 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nbK == 3 && nbAs <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nbQ == 3 && nbAs <= 1 && nbK <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nbJ == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb10 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb9 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb8 == 3 && (nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb7 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb6 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb5 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb4 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb4 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb3 <= 1 && nb2 <= 1) || (nb3 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb2 <= 1) || (nb2 == 3 && nbAs <= 1 && nbK <= 1 && nbQ <= 1 && nbJ <= 1 && nb10 <= 1 && nb9 <= 1 && nb8 <= 1 && nb7 <= 1 && nb6 <= 1 && nb5 <= 1 && nb4 <= 1 && nb3 <= 1))){
            return true;
        } else {
            return false;
        }
    } // Fin de la methode Three of a kind (Brelan)

    // ************************************
    // METHODE CHECK TWO PAIR
    // ************************************

    public boolean checkTwoPair(Card[] cardsCommunesAndHandPlayer) {

        int nbAs = 0;
        int nbK = 0;
        int nbQ = 0;
        int nbJ = 0;
        int nb10 = 0;
        int nb9 = 0;
        int nb8 = 0;
        int nb7 = 0;
        int nb6 = 0;
        int nb5 = 0;
        int nb4 = 0;
        int nb3 = 0;
        int nb2 = 0;

        Rang rangCard = Rang.As;

        for (int i = 0; i < cardsCommunesAndHandPlayer.length; i++) {

            switch (rangCard = cardsCommunesAndHandPlayer[i].getRang()) {

                case As:
                    nbAs++;
                    break;
                case Roi:
                    nbK++;
                    break;
                case Dame:
                    nbQ++;
                    break;
                case Valet:
                    nbJ++;
                    break;
                case Dix:
                    nb10++;
                    break;
                case Neuf:
                    nb9++;
                    break;
                case Huit:
                    nb8++;
                    break;
                case Sept:
                    nb7++;
                    break;
                case Six:
                    nb6++;
                    break;
                case Cinq:
                    nb5++;
                    break;
                case Quatre:
                    nb4++;
                    break;
                case Trois:
                    nb3++;
                    break;
                case Deux:
                    nb2++;
                    break;
            }
        }
        if ((nbAs == 2 && nbK == 2 && nbQ == 2 && nbJ == 2 && nb10 == 2 && nb9 == 2 && nb8 == 2 && nb7 == 2 && nb6 == 2 && nb5 == 2 && nb4 == 2 && nb3 == 2 && nb2 == 2) && ((nbAs < 2) && (nbK < 2) && (nbQ < 2) && (nbJ < 2) && (nb10 < 3) && (nb9 < 2) && (nb8 < 2) && (nb7 < 2) && (nb6 < 2) && (nb5 < 2) && (nb4 < 2) && (nb3 < 2) && (nb2 < 2))) {
            return true;
        } else {
            return false;
        }
    } // Fin de la methode check Two Pair


}// Fin de la classe Joueur

