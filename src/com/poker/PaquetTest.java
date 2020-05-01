package com.poker;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PaquetTest {

    @Test
    void piocher() {
        Card[] paquetDeCartes = new Card[]{
                new Card(Rang.As, Couleur.Pique),
                new Card(Rang.Roi, Couleur.Carreau),
                new Card(Rang.Dame, Couleur.Coeur),
        };
        Paquet paquet = new Paquet();
        paquet.setPaquetDeCartes(paquetDeCartes);
        paquet.piocher(2);
    }
}