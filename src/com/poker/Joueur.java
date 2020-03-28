package com.poker;

import java.util.Arrays;

public class Joueur {

    String namePlayer;
    int chipsPlayer = 10000;                // CREATION d'une liste de Jetons par joueur, indice 0-joueur0 etc.
    Card [] main = new Card[2];


    //Constructeur avec paramètres
    public Joueur(String namePlayer, Card [] main) {
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
    public Card[] getMain() { return main; }

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

    @Override
    public String toString() {
        return "Joueur{" +
                "namePlayer='" + namePlayer + '\'' +
                ", chipsPlayer=" + chipsPlayer + " }";
    }
}// Fin de la classe player

