package com.poker;

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




    public Card[] getCardsCommunesAndHandPlayer() {
        return cardsCommunesAndHandPlayer;
    }

    public void setCardsCommunesAndHandPlayer(Card[] cardsCommunesAndHandPlayer) {
        this.cardsCommunesAndHandPlayer = cardsCommunesAndHandPlayer;
    }

}// Fin de la classe Joueur


