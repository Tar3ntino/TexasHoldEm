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
        System.out.println("Nombre de ♦:" + nbCarreau + "     Nombre de ♥:" + nbCoeur + "     Nombre de ♠:️" + nbPique + "     Nombre de ♣:" + nbTrefle);
        if (nbCoeur >= 5 || nbCarreau >= 5 || nbPique >= 5 || nbTrefle >= 5) {
            return true;
        } else
            return false;
    }

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
}// Fin de la classe Joueur

