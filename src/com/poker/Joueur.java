package com.poker;

public class Joueur {

    String namePlayer;
    int chipsPlayer = 10000;                // Stack de Jetons par joueur, indice 0-joueur0 etc.
    Card[] main = new Card[2];
    Card[] cardsCommunesAndHandPlayer = new Card[7];
    int betPlayer = 0;
    int smallBlind = 0;
    int bigBlind = 0;
    boolean isSmallBlind = false;
    boolean isBigBlind = false;

    //Constructeur avec paramètres
    public Joueur(String namePlayer, Card[] main) {
        System.out.println("Player : " + namePlayer + "\n Chips :" + chipsPlayer + "\n MainJoueur :" + main);
    }

    public Joueur(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    //**********************************
    //*       GETTER - ACCESSEURS      *
    //**********************************

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

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public Card[] getCardsCommunesAndHandPlayer() {
        return cardsCommunesAndHandPlayer;
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    //**********************************
    //*       SETTER - MUTATEURS       *
    //**********************************

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

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setSmallBlind(boolean smallBlind) {
        isSmallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public void setBigBlind(boolean bigBlind) {
        isBigBlind = bigBlind;
    }

    public void setCardsCommunesAndHandPlayer(Card[] cardsCommunesAndHandPlayer) {
        this.cardsCommunesAndHandPlayer = cardsCommunesAndHandPlayer;
    }





    @Override
    public String toString() {
        return namePlayer + " Jetons: " + chipsPlayer + " \n";
    }

}// FIN DE LA CLASSE JOUEUR


