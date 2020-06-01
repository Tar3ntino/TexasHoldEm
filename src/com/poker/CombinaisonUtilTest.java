package com.poker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.poker.Combinaison.*;
import static com.poker.Couleur.*;
import static com.poker.Rang.*;
import static org.junit.jupiter.api.Assertions.*;

class CombinaisonUtilTest {

    // ************************************
    // 1 - TEST STRAIGHT FLUSH (QUINTE FLUSH)
    // ************************************

    @Test
    void checkStraightFlush() {

        Card[] quinteFlushRoyale = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(Dame, Carreau),
                new Card(Cinq, Coeur),
                new Card(Dix, Trefle),
                new Card(Dame, Trefle),
                new Card(Valet, Trefle),
        };
        assertEquals(QuinteFlush, CombinaisonUtil.checkStraightFlush(quinteFlushRoyale, CombinaisonUtil.checkStraight(quinteFlushRoyale), CombinaisonUtil.checkFlush(quinteFlushRoyale)).getCombinaison());
        assertEquals(9, CombinaisonUtil.checkStraightFlush(quinteFlushRoyale, CombinaisonUtil.checkStraight(quinteFlushRoyale), CombinaisonUtil.checkFlush(quinteFlushRoyale)).getCombinaison().getValue());
        assertEquals(Trefle, CombinaisonUtil.checkStraightFlush(quinteFlushRoyale, CombinaisonUtil.checkStraight(quinteFlushRoyale), CombinaisonUtil.checkFlush(quinteFlushRoyale)).getCouleurMax());
        assertEquals(As, CombinaisonUtil.checkStraightFlush(quinteFlushRoyale, CombinaisonUtil.checkStraight(quinteFlushRoyale), CombinaisonUtil.checkFlush(quinteFlushRoyale)).getHauteurEtKickers().get(0));


        Card[] quinteFlushAuCinq = new Card[]{
                new Card(As, Trefle),
                new Card(Trois, Trefle),
                new Card(Dame, Carreau),
                new Card(Cinq, Coeur),
                new Card(Deux, Trefle),
                new Card(Cinq, Trefle),
                new Card(Quatre, Trefle),
        };
        assertEquals(QuinteFlush, CombinaisonUtil.checkStraightFlush(quinteFlushAuCinq, CombinaisonUtil.checkStraight(quinteFlushAuCinq), CombinaisonUtil.checkFlush(quinteFlushAuCinq)).getCombinaison());
        assertEquals(9, CombinaisonUtil.checkStraightFlush(quinteFlushAuCinq, CombinaisonUtil.checkStraight(quinteFlushAuCinq), CombinaisonUtil.checkFlush(quinteFlushAuCinq)).getCombinaison().getValue());
        assertEquals(Trefle, CombinaisonUtil.checkStraightFlush(quinteFlushAuCinq, CombinaisonUtil.checkStraight(quinteFlushAuCinq), CombinaisonUtil.checkFlush(quinteFlushAuCinq)).getCouleurMax());
        assertEquals(Cinq, CombinaisonUtil.checkStraightFlush(quinteFlushAuCinq, CombinaisonUtil.checkStraight(quinteFlushAuCinq), CombinaisonUtil.checkFlush(quinteFlushAuCinq)).getHauteurEtKickers().get(0));
    }

    // ************************************
    // 2 - TEST FOUR OF A KIND (CARRE)
    // ************************************

    @Test
    void checkFourOfAKind() {
        Card[] carreDAs = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(As, Pique),
                new Card(Dame, Trefle),
                new Card(As, Coeur),
        };
        assertEquals(Carre, CombinaisonUtil.checkFourOfAKind(carreDAs).getCombinaison());
        assertEquals(8, CombinaisonUtil.checkFourOfAKind(carreDAs).getCombinaison().getValue());
        assertEquals(As, CombinaisonUtil.checkFourOfAKind(carreDAs).getHauteurEtKickers().get(0));
        assertEquals(Roi, CombinaisonUtil.checkFourOfAKind(carreDAs).getHauteurEtKickers().get(1));


        Card[] pasCarre = new Card[]{
                new Card(Roi, Pique),
                new Card(Trois, Pique),
                new Card(Neuf, Coeur),
                new Card(Dame, Carreau),
                new Card(Valet, Trefle),
                new Card(Dame, Trefle),
                new Card(Neuf, Carreau),
        };
        assertNull(CombinaisonUtil.checkFourOfAKind(pasCarre));
    }

    // ************************************
    // 3 - TEST FULL HOUSE (FULL)
    // ************************************

    @Test
    void checkFullHouse() {

        Card[] fullHouseAvec3As3Dames = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Dame, Coeur),
                new Card(As, Pique),
                new Card(Dame, Trefle),
                new Card(Dame, Pique),
        };
        assertEquals(Combinaison.Full, CombinaisonUtil.checkFullHouse(fullHouseAvec3As3Dames).getCombinaison());

        Card[] brelan = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Valet, Coeur),
                new Card(As, Pique),
                new Card(Dix, Trefle),
                new Card(Dame, Pique),
        };
        assertEquals(null, CombinaisonUtil.checkFullHouse(brelan));
    }

    // ************************************
    // 4 - TEST FLUSH (COULEUR)
    // ************************************

    @Test
    void checkFlush() {
        Card[] cartesAvecCinqTrefles = new Card[]{
                new Card(As, Pique),
                new Card(Roi, Carreau),
                new Card(Dame, Trefle),
                new Card(Cinq, Trefle),
                new Card(Quatre, Trefle),
                new Card(Dame, Trefle),
                new Card(Valet, Trefle),
        };
        assertEquals(Couleur, CombinaisonUtil.checkFlush(cartesAvecCinqTrefles).getCombinaison());
        assertEquals(6, CombinaisonUtil.checkFlush(cartesAvecCinqTrefles).getCombinaison().getValue());
        assertEquals(Trefle, CombinaisonUtil.checkFlush(cartesAvecCinqTrefles).getCouleurMax());
        assertEquals(Dame, CombinaisonUtil.checkFlush(cartesAvecCinqTrefles).getHauteurEtKickers().get(0));

        Card[] cartesAvecQuatreTrefles = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Carreau),
                new Card(Dame, Coeur),
                new Card(Cinq, Trefle),
                new Card(Quatre, Coeur),
                new Card(Dame, Trefle),
                new Card(Valet, Trefle),
        };
        assertEquals(null, CombinaisonUtil.checkFlush(cartesAvecQuatreTrefles));

        Card[] cartesAvecSixTrefles = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Carreau),
                new Card(Dame, Trefle),
                new Card(Cinq, Trefle),
                new Card(Quatre, Trefle),
                new Card(Dame, Trefle),
                new Card(Valet, Trefle),
        };

        assertEquals(Couleur, CombinaisonUtil.checkFlush(cartesAvecSixTrefles).getCombinaison());
        assertEquals(6, CombinaisonUtil.checkFlush(cartesAvecSixTrefles).getCombinaison().getValue());
        assertEquals(Trefle, CombinaisonUtil.checkFlush(cartesAvecSixTrefles).getCouleurMax());
        assertEquals(As, CombinaisonUtil.checkFlush(cartesAvecSixTrefles).getHauteurEtKickers().get(0));

        Card[] cartesAvecCinqCoeur = new Card[]{
                new Card(Huit, Coeur),
                new Card(Trois, Pique),
                new Card(Valet, Coeur),
                new Card(Roi, Coeur),
                new Card(Neuf, Coeur),
                new Card(Roi, Carreau),
                new Card(Dix, Coeur),
        };

        assertEquals(Couleur, CombinaisonUtil.checkFlush(cartesAvecCinqCoeur).getCombinaison());
        assertEquals(6, CombinaisonUtil.checkFlush(cartesAvecCinqCoeur).getCombinaison().getValue());
        assertEquals(Coeur, CombinaisonUtil.checkFlush(cartesAvecCinqCoeur).getCouleurMax());
        assertEquals(Roi, CombinaisonUtil.checkFlush(cartesAvecCinqCoeur).getHauteurEtKickers().get(0));
    }

    // ************************************
    // 5 - TEST STRAIGHT (SUITE)
    // ************************************

    @Test
    void checkStraight() {

        Card[] cardsAvecHauteurAsEtDeuxDames = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Carreau),
                new Card(Dame, Trefle),
                new Card(Cinq, Trefle),
                new Card(Dix, Trefle),
                new Card(Dame, Trefle),
                new Card(Valet, Trefle),
        };
        assertEquals(Suite, CombinaisonUtil.checkStraight(cardsAvecHauteurAsEtDeuxDames).getCombinaison());
        assertEquals(5, CombinaisonUtil.checkStraight(cardsAvecHauteurAsEtDeuxDames).getCombinaison().getValue());
        assertEquals(As, CombinaisonUtil.checkStraight(cardsAvecHauteurAsEtDeuxDames).getHauteurEtKickers().get(0));

        Card[] cardsAvecHauteurCinq = new Card[]{
                new Card(As, Trefle),
                new Card(Quatre, Carreau),
                new Card(Dame, Trefle),
                new Card(Deux, Trefle),
                new Card(Dix, Trefle),
                new Card(Trois, Trefle),
                new Card(Cinq, Trefle),
        };
        assertEquals(Suite, CombinaisonUtil.checkStraight(cardsAvecHauteurCinq).getCombinaison());
        assertEquals(5, CombinaisonUtil.checkStraight(cardsAvecHauteurCinq).getCombinaison().getValue());
        assertEquals(Cinq, CombinaisonUtil.checkStraight(cardsAvecHauteurCinq).getHauteurEtKickers().get(0));

        Card[] suitede6CartesHauteur6 = new Card[]{
                new Card(As, Trefle),
                new Card(Quatre, Carreau),
                new Card(Dame, Trefle),
                new Card(Deux, Trefle),
                new Card(Six, Trefle),
                new Card(Trois, Trefle),
                new Card(Cinq, Trefle),
        };
        assertEquals(Suite, CombinaisonUtil.checkStraight(suitede6CartesHauteur6).getCombinaison());
        assertEquals(5, CombinaisonUtil.checkStraight(suitede6CartesHauteur6).getCombinaison().getValue());
        assertEquals(Six, CombinaisonUtil.checkStraight(suitede6CartesHauteur6).getHauteurEtKickers().get(0));


        Card[] otherHand = new Card[]{
                new Card(Six, Carreau),
                new Card(Dix, Carreau),
                new Card(As, Trefle),
                new Card(Dame, Carreau),
                new Card(Cinq, Pique),
                new Card(Deux, Carreau),
                new Card(Valet, Pique),
        };
        assertNull(CombinaisonUtil.checkStraight(otherHand));
    }

    // ************************************
    // 6 - TEST THREE OF A KIND (BRELAN)
    // ************************************

    @Test
    void checkThreeOfAKind() {

        Card[] threeOfAKind = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(As, Pique),
                new Card(Valet, Trefle),
                new Card(Dame, Pique),
        };
        assertEquals(Brelan, CombinaisonUtil.checkThreeOfAKind(threeOfAKind).getCombinaison());

        Card[] full = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(As, Pique),
                new Card(Valet, Trefle),
                new Card(Valet, Pique),
        };
        assertEquals(null, CombinaisonUtil.checkThreeOfAKind(full));
    }

    // ************************************
    // 7 - TEST TWO PAIR (DOUBLE PAIR)
    // ************************************

    @Test
    void checkTwoPair() {

        Card[] Full = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(As, Pique),
                new Card(Roi, Trefle),
                new Card(Dame, Pique),
        };

        assertEquals(null, CombinaisonUtil.checkTwoPair(Full));

        Card[] TwoPair = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(Sept, Pique),
                new Card(Roi, Trefle),
                new Card(Dame, Pique),
        };

        assertEquals(DoublePaire, CombinaisonUtil.checkTwoPair(TwoPair).getCombinaison());
        assertEquals(3, CombinaisonUtil.checkTwoPair(TwoPair).getCombinaison().getValue());
        assertEquals(As, CombinaisonUtil.checkTwoPair(TwoPair).getHauteurEtKickers().get(0));
        assertEquals(Roi, CombinaisonUtil.checkTwoPair(TwoPair).getHauteurEtKickers().get(1));
        assertEquals(Dame, CombinaisonUtil.checkTwoPair(TwoPair).getHauteurEtKickers().get(2));


        Card[] DoublePaireRoiNeufKicker8 = new Card[]{
                new Card(Neuf, Carreau),
                new Card(Huit, Carreau),
                new Card(Neuf, Pique),
                new Card(Huit, Pique),
                new Card(Roi, Pique),
                new Card(Trois, Pique),
                new Card(Roi, Carreau),
        };

        assertEquals(DoublePaire, CombinaisonUtil.checkTwoPair(DoublePaireRoiNeufKicker8).getCombinaison());
        assertEquals(3, CombinaisonUtil.checkTwoPair(DoublePaireRoiNeufKicker8).getCombinaison().getValue());
        assertEquals(Roi, CombinaisonUtil.checkTwoPair(DoublePaireRoiNeufKicker8).getHauteurEtKickers().get(0));
        assertEquals(Neuf, CombinaisonUtil.checkTwoPair(DoublePaireRoiNeufKicker8).getHauteurEtKickers().get(1));
        assertEquals(Huit, CombinaisonUtil.checkTwoPair(DoublePaireRoiNeufKicker8).getHauteurEtKickers().get(2));

        Card[] DoublePaireValetetNeufKickerRoi = new Card[]{
                new Card(Neuf, Carreau),
                new Card(Valet, Carreau),
                new Card(Neuf, Pique),
                new Card(Valet, Pique),
                new Card(Roi, Pique),
                new Card(Trois, Pique),
                new Card(Dame, Carreau),
        };

        assertEquals(DoublePaire, CombinaisonUtil.checkTwoPair(DoublePaireValetetNeufKickerRoi).getCombinaison());
        assertEquals(3, CombinaisonUtil.checkTwoPair(DoublePaireValetetNeufKickerRoi).getCombinaison().getValue());
        assertEquals(Valet, CombinaisonUtil.checkTwoPair(DoublePaireValetetNeufKickerRoi).getHauteurEtKickers().get(0));
        assertEquals(Neuf, CombinaisonUtil.checkTwoPair(DoublePaireValetetNeufKickerRoi).getHauteurEtKickers().get(1));
        assertEquals(Roi, CombinaisonUtil.checkTwoPair(DoublePaireValetetNeufKickerRoi).getHauteurEtKickers().get(2));


    }
    // ************************************
    // 8 - TEST ONE PAIR (PAIRE)
    // ************************************

    @Test
    void checkOnePair() {

        Card[] threeOfAKind = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(As, Pique),
                new Card(Valet, Trefle),
                new Card(Dame, Pique),
        };
        assertEquals(null, CombinaisonUtil.checkOnePair(threeOfAKind));

        Card[] full = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(As, Pique),
                new Card(Valet, Trefle),
                new Card(Valet, Pique),
        };
        assertEquals(null, CombinaisonUtil.checkOnePair(full));

        Card[] paire = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(As, Carreau),
                new Card(Cinq, Coeur),
                new Card(Dix, Pique),
                new Card(Trois, Trefle),
                new Card(Valet, Pique),
        };
        assertEquals(Paire, CombinaisonUtil.checkOnePair(paire).getCombinaison());
        assertEquals(2, CombinaisonUtil.checkOnePair(paire).getCombinaison().getValue());
        assertEquals(As, CombinaisonUtil.checkOnePair(paire).getHauteurEtKickers().get(0));
        assertEquals(Roi, CombinaisonUtil.checkOnePair(paire).getHauteurEtKickers().get(1));
        assertEquals(Valet, CombinaisonUtil.checkOnePair(paire).getHauteurEtKickers().get(2));
        assertEquals(Dix, CombinaisonUtil.checkOnePair(paire).getHauteurEtKickers().get(3));

        Card[] paireOf4 = new Card[]{
                new Card(As, Trefle),
                new Card(Neuf, Trefle),
                new Card(Roi, Carreau),
                new Card(Quatre, Coeur),
                new Card(Dix, Pique),
                new Card(Quatre, Trefle),
                new Card(Six, Pique),
        };
        assertEquals(Paire, CombinaisonUtil.checkOnePair(paireOf4).getCombinaison());
        assertEquals(2, CombinaisonUtil.checkOnePair(paireOf4).getCombinaison().getValue());
        assertEquals(Quatre, CombinaisonUtil.checkOnePair(paireOf4).getHauteurEtKickers().get(0));
        assertEquals(As, CombinaisonUtil.checkOnePair(paireOf4).getHauteurEtKickers().get(1));
        assertEquals(Roi, CombinaisonUtil.checkOnePair(paireOf4).getHauteurEtKickers().get(2));
        assertEquals(Dix, CombinaisonUtil.checkOnePair(paireOf4).getHauteurEtKickers().get(3));
    }

    // ************************************
    // TEST COMPARAISON COMBINAISON HIGHEST
    // ************************************

    @Test
    void checkCombiHighestPlayer() {

        List<Rang> hauteurEtKickerPlayer1 = Arrays.asList(Valet, Huit, Dame);
        List<Rang> hauteurEtKickerPlayer2 = Arrays.asList(Valet, Huit, Roi);

        assertEquals(-1, CombinaisonUtil.compare(hauteurEtKickerPlayer1, hauteurEtKickerPlayer2));

        List<Rang> hauteurEtKickerP1 = Arrays.asList(Valet, Huit, Sept);
        List<Rang> hauteurEtKickerP2 = Arrays.asList(Valet, Huit, Sept);

        assertEquals(0, CombinaisonUtil.compare(hauteurEtKickerP1, hauteurEtKickerP2));

    }

    @Test
    void departagerJoueur() {

        Joueur joueur1 = new Joueur("joueur1");
        Joueur joueur2 = new Joueur("joueur2");
        Joueur joueur3 = new Joueur("joueur3");
        Joueur joueur4 = new Joueur("joueur4");
        Joueur joueur5 = new Joueur("joueur5");

        Card[] mainJoueur1 = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(Roi, Carreau),
                new Card(Huit, Coeur),
                new Card(Dix, Pique),
                new Card(Six, Trefle),
                new Card(Six, Pique),
        };

        Card[] mainJoueur2 = new Card[]{
                new Card(Six, Trefle),
                new Card(Roi, Carreau),
                new Card(Quatre, Carreau),
                new Card(Dame, Coeur),
                new Card(Dix, Pique),
                new Card(Roi, Trefle),
                new Card(Six, Pique),
        };

        Card[] mainJoueur3 = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(Roi, Carreau),
                new Card(Huit, Coeur),
                new Card(Dix, Pique),
                new Card(Six, Trefle),
                new Card(Six, Pique),
        };

        Card[] mainJoueur4 = new Card[]{
                new Card(Deux, Trefle),
                new Card(Cinq, Trefle),
                new Card(Trois, Carreau),
                new Card(Roi, Coeur),
                new Card(Huit, Pique),
                new Card(Sept, Trefle),
                new Card(Dix, Pique),
        };

        Card[] mainJoueur5 = new Card[]{
                new Card(Six, Trefle),
                new Card(Deux, Trefle),
                new Card(Trois, Carreau),
                new Card(Roi, Coeur),
                new Card(Huit, Pique),
                new Card(Sept, Trefle),
                new Card(Dix, Pique),
        };

        joueur1.setCardsCommunesAndHandPlayer(mainJoueur1);
        joueur2.setCardsCommunesAndHandPlayer(mainJoueur2);
        joueur3.setCardsCommunesAndHandPlayer(mainJoueur3);
        joueur4.setCardsCommunesAndHandPlayer(mainJoueur4);
        joueur5.setCardsCommunesAndHandPlayer(mainJoueur5);

        List<Joueur> listJoueurMaxCombi = new ArrayList<>();
        listJoueurMaxCombi.add(joueur1);
        listJoueurMaxCombi.add(joueur2);
        listJoueurMaxCombi.add(joueur3);

        List<Joueur> joueursGagnants = CombinaisonUtil.departagerJoueurs(listJoueurMaxCombi);
        assertEquals(2, joueursGagnants.size());
        assertEquals("joueur1", joueursGagnants.get(0).getNamePlayer());
        assertEquals("joueur3", joueursGagnants.get(1).getNamePlayer());


        List<Joueur> listJoueurMaxCombi2 = new ArrayList<>();
        listJoueurMaxCombi2.add(joueur4);
        listJoueurMaxCombi2.add(joueur5);

        List<Joueur> joueursGagnants2 = CombinaisonUtil.departagerJoueurs(listJoueurMaxCombi2);
        assertEquals(1, joueursGagnants2.size());
        assertEquals("joueur5", joueursGagnants2.get(0).getNamePlayer());

    }


    @Test
    void getCombinaison() {
        Joueur joueur = new Joueur("alfred");
        Card[] cards = new Card[]{
                new Card(As, Trefle),
                new Card(Roi, Trefle),
                new Card(Roi, Carreau),
                new Card(Huit, Coeur),
                new Card(Dix, Pique),
                new Card(Six, Trefle),
                new Card(Six, Pique),
        };
        joueur.setCardsCommunesAndHandPlayer(cards);
        Resultat resultat = CombinaisonUtil.getCombinaison(joueur);
        assertEquals(Combinaison.DoublePaire, resultat.getCombinaison());
    }
}
