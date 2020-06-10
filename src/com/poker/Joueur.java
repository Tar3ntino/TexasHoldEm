package com.poker;

public class Joueur {

    String namePlayer;
    int chipsPlayer = 10000;                // Stack de Jetons par joueur, indice 0-joueur0 etc.
    Card[] main = new Card[2];
    Card[] cardsCommunesAndHandPlayer = new Card[7];
    int betPlayer = 0;                      // Mise du joueur au tour de parole
    int betTourPlayer = 0;                   // Somme cumule des mises joueurs a chaque tour de parole afin de pouvoir
    // comparer les differentes hauteur de pot en cas de joueur all in
    int smallBlind = 0;
    int bigBlind = 0;
    boolean isSmallBlind = false;
    boolean isBigBlind = false;
    boolean joueurBloque = false;
    int minimumARelancerApresBetPlayer = 0;

    // La variable "minimumARelancerApresBetPlayer" sert a garder en memoire la difference entre la derniere mise sur
    // la table et la relance du joueur apres sa mise. Elle nous servira pour "debloquer" notre joueur dans le cas ou
    // un joueur fait une "fausse" relance a tapis.

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

    public int getBetTourPlayer() {
        return betTourPlayer;
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

    public int getMinimumARelancerApresBetPlayer() {
        return minimumARelancerApresBetPlayer;
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    public boolean isJoueurBloque() {
        return joueurBloque;
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

    public void setBetTourPlayer(int betTourPlayer) {
        this.betTourPlayer = betTourPlayer;
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

    public void setMinimumARelancerApresBetPlayer(int minimumARelancerApresBetPlayer) {
        this.minimumARelancerApresBetPlayer = minimumARelancerApresBetPlayer;
    }

    public void setJoueurBloque(boolean joueurBloque) {
        this.joueurBloque = joueurBloque;
    }

    @Override
    public String toString() {
        return namePlayer;
    }

}// FIN DE LA CLASSE JOUEUR


