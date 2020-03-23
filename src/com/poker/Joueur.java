package com.poker;

public class Joueur {

    String namePlayer = "noname";
    int nbChips = 10000;
    int handPlayer[] = new int[2];

    //Constructeur par defaut
    public Joueur() {
        System.out.println("Creation d'un joueur");
    }

    //Constructeur avec paramètres
    public Joueur(String namePlayer) {
        System.out.println("Player : " + namePlayer + "\n Chips :" + nbChips);
    }// fin de pContructeur


    //******* Mutateurs *******
    public void setName(String pName) {
        namePlayer = pName;
    }// fin de setName

}// Fin de la classe player
