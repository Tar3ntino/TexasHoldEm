package com.poker;

import java.util.*;

public class CombinaisonUtil {

    // ************************************
    // HAND RANKINGS :          Force Combinaison : Constructeur Resultat renvoye :
    //      1.Straight Flush        Force: 9        Int [Value Combinaison, Hauteur quinte flush]
    //      2.Four of a kind        Force: 8        Int [Value Combinaison, Hauteur carre, Kicker 1]
    //      3.Full House            Force: 7        Int [Value Combinaison, Hauteur brelan, Hauteur paire , Kicker 1]
    //      4.Flush                 Force: 6        Int [Value Combinaison, Hauteur couleur]
    //      5.Straight              Force: 5        Int [Value Combinaison, Hauteur suite]
    //      6.Three of a kind       Force: 4        Int [Value Combinaison, Hauteur brelan, Kicker 1, Kicker 2]
    //      7.Two pair              Force: 3        Int [Value Combinaison, Hauteur Paire 1, Hauteur Paire 2, Kicker 1]
    //      8.Pair                  Force: 2        Int [Value Combinaison, Hauteur Paire 1, Kicker 1, Kicker 2, Kicker 3]
    //      9.High Card             Force: 1        Int [Value Combinaison, Kicker 1, Kicker 2, Kicker 3, Kicker 4, Kicker 5]
    // ************************************

    // ************************************
    // 1 - STRAIGHT FLUSH (QUINTE FLUSH)
    // Methode qui place en parametres nos 7 cartes (Board + Hand Player) ainsi que nos conditions est une suite +
    // est une couleur. Si les 2 conditions sont respectees, la quinte flush est renvoyee "true"
    // ************************************

    public static Resultat checkStraightFlush(Card[] cardsCommunesAndHandPlayer, Resultat resultatCheckStraight, Resultat resultatCheckFlush) {
        Resultat resultat = new Resultat();

        if (resultatCheckStraight.isUneCombinaisonValide() == true && resultatCheckFlush.isUneCombinaisonValide() == true) {
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(9);
            resultat.setCouleurMax(resultatCheckFlush.getCouleurMax());
            resultat.setHauteur(resultatCheckStraight.getHauteur());
        }
        return resultat;
    }

    // ************************************
    // 2 - FOUR OF A KIND (CARRE)
    // Declaration d'une variable "Map" qui va associer et stocker les variables de Type Rang/Integer.
    // Initialisation dans la declaration de cet objet par la methode "countByRang" cree (Ctrl+Alt+M - Extraction de methode)
    // Declaration d'une variable Arraylist pour stocker les int afin de pouvoir les classer
    // Appel de la classe utilitaire "Collections" avec la methode de tri inverse "sort"
    // Renvoi un booleen true si la condition ((counts.get(0) == 4)) est respectee
    //
    //
    // Methode "countByRang" : initialise un objet new HashMap (Table de Hashage qui accepte les valeurs Null)
    // On parcourt dans une boucle for notre variable cardsCommunesAndHandPlayer (Board + Main Joueur)
    // On declare une variable de type Rang qui va recuperer le rang de la carte a l'index de la boucle.
    // On declare une variable Integer qui va recuperer le nombre de Rang actuel. Si celui ci est nul initialement,
    // renvoie une valeur par defaut.
    // On incremente d'un la valeur du rang trouve.
    // On MAJ la valeur "countRang" de type Integer, dans la table de hashage a l'aide de la methode "put"
    // On renvoie la variable de type Map qui possede une valeur int pour chaque rang present dans "cardCommunesAndHandPlayer"
    //

    //
    // ************************************

    public static Resultat checkFourOfAKind(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer);
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values());
        Collections.sort(counts, Comparator.reverseOrder());

        if (counts.get(0) == 4) {
            Rang rangLePlusNombreux = null;
            int countMax = 0;
            for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
                if (countByRang.get(rang) > countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                    countMax = countByRang.get(rang); // on MAJ la valeur countMax
                    rangLePlusNombreux = rang;
                }
            }
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(8);
            resultat.setRangCarre(rangLePlusNombreux); // et l'on setup le Rang du Carre
            countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang

            Rang kicker1 = null;
            int kickervalue = 0;

            System.out.print("affichage du KeySet apres remove :" + countByRang.keySet());

            for (Rang rang1 : countByRang.keySet()) {
                if (rang1.getValue() > kickervalue) {
                    kicker1 = rang1;
                    kicker1.setValue(rang1.getValue());
                    kickervalue = rang1.getValue();
                }
            }
            resultat.setKicker1(kicker1);
        } else {
            resultat.setUneCombinaisonValide(false);
        }
        return resultat;
    }

    // ************************************
    // 3 - FULL HOUSE (FULL)
    // ************************************

    public static Resultat checkFullHouse(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 3 && counts.get(1) >= 2) {
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(7);
            Rang hauteurBrelan = CombinaisonUtil.checkThreeOfAKind(cardsCommunesAndHandPlayer).getHauteur();
            resultat.setHauteur(hauteurBrelan);
            Rang hauteurPaire = CombinaisonUtil.checkOnePair(cardsCommunesAndHandPlayer).getHauteur();
            resultat.setKicker1(hauteurPaire);
        }
        ; // On renvoie un booleen true si la condition est respectee
        return resultat;
    } // Fin de la methode Full

    private static Map<Rang, Integer> countByRang(Card[] cardsCommunesAndHandPlayer) {
        Map<Rang, Integer> countByRang = new HashMap<>();
        for (Card card : cardsCommunesAndHandPlayer) {
            Rang rang = card.getRang(); // equivaut a un index i... en quelques sortes....
            Integer countRang = countByRang.getOrDefault(rang, 0);
            countRang++;
            countByRang.put(rang, countRang);
        }
        return countByRang;
    }

    // ************************************
    // 4 - FLUSH (COULEUR)
    // ************************************

    public static Resultat checkFlush(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Couleur, Integer> countByColor = countByColor(cardsCommunesAndHandPlayer);
        ArrayList<Integer> counts = new ArrayList<>(countByColor.values()); // on recupere les valeurs de comptage couleur
        Collections.sort(counts, Comparator.reverseOrder()); // on les classe decroissant

        setCouleurMax(resultat, countByColor);

        if (counts.get(0) >= 5) {
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(6);

            int hauteurMax = 0;

            for (Card card : cardsCommunesAndHandPlayer) {
                if (card.getCouleur() == resultat.getCouleurMax() && card.getRang().getValue() > hauteurMax) {
                    hauteurMax = card.getRang().getValue();
                    resultat.setHauteur(card.getRang());
                }
            }
        }
        return resultat;
    }

    private static void setCouleurMax(Resultat resultat, Map<Couleur, Integer> countByColor) {
        int countMax = 0;

        for (Couleur couleur : countByColor.keySet()) {
            if (countByColor.get(couleur) > countMax) {
                countMax = countByColor.get(couleur);
                resultat.setCouleurMax(couleur);
            }
        }
    }

    private static Map<Couleur, Integer> countByColor(Card[] cardsCommunesAndHandPlayer) {
        Map<Couleur, Integer> countByColor = new HashMap<>();
        for (Card card : cardsCommunesAndHandPlayer) {
            Couleur couleur = card.getCouleur();
            Integer countColor = countByColor.getOrDefault(couleur, 0);
            countColor++;
            countByColor.put(couleur, countColor);
        }
        return countByColor;
    }

    // ************************************
    // 5 - STRAIGHT (SUITE)
    // ************************************

    public static Resultat checkStraight(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();

        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        Set<Rang> rangs = countByRang.keySet();// on collecte les rangs du tirage des 7 cartes du board
        Set<Rang> allRangs = Set.of(Rang.Deux, Rang.Trois, Rang.Quatre, Rang.Cinq, Rang.Six, Rang.Sept, Rang.Huit, Rang.Neuf, Rang.Dix, Rang.Valet, Rang.Dame, Rang.Roi, Rang.As);

        if (rangs.size() < 5) {
            resultat.setUneCombinaisonValide(false);
            resultat.setValueCombinaison(0);
        }
        Set<Rang> fromAsToCinq = Set.of(Rang.As, Rang.Deux, Rang.Trois, Rang.Quatre, Rang.Cinq);
        if (rangs.containsAll(fromAsToCinq)) {
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(5);
            resultat.setHauteur(Rang.Cinq);
        }
        List<Integer> hauteurs = new ArrayList<>();
        for (Rang rang : rangs) {
            hauteurs.add(rang.getValue());
        }
        Collections.sort(hauteurs);
        for (int i = 0; i < hauteurs.size() - 4; i++) {
            if (hauteurs.get(i + 4) - hauteurs.get(i) == 4) {
                resultat.setUneCombinaisonValide(true);
                resultat.setValueCombinaison(5);
                for (Rang allRang : allRangs) {
                    if (allRang.getValue() == hauteurs.get(i + 4)) {
                        resultat.setHauteur(allRang);
                    }
                }
            }
        }
        return resultat;
    }

    // ************************************
    // 6 - THREE OF A KIND (BRELAN)
    // ************************************

    public static Resultat checkThreeOfAKind(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 3 && counts.get(1) < 2) {
            Rang rangLePlusNombreux = null;
            int countMax = 0;

            for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
                if (countByRang.get(rang) > countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                    countMax = countByRang.get(rang); // on MAJ la valeur countMax
                    rangLePlusNombreux = rang;
                }
            }
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(4);
            resultat.setHauteur(rangLePlusNombreux); // et l'on setup la hauteur du Brelan
            countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang

            Rang kicker1 = null;
            Rang kicker2 = null;
            int kickervalue = 0;

            setKicker1(resultat, countByRang, kicker1, kickervalue);
            setKicker2(resultat, countByRang, kicker2, kickervalue);

        } else {
            resultat.setUneCombinaisonValide(false);
        }
        return resultat;
    } // Fin de la methode Three of a kind (Brelan)

    // ************************************
    // 7 - TWO PAIR (DOUBLE PAIR)
    // ************************************

    public static Resultat checkTwoPair(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 2 && counts.get(1) == 2) { // Si la conditionest respectee, on est sur d'avoir une double paire

            Rang rangLePlusNombreux = null;
            int countMax = 0;

            setHauteurRangPaire(resultat, countByRang, rangLePlusNombreux, countMax);
            setHauteurRangPaire2(resultat, countByRang, rangLePlusNombreux, countMax);

            Rang kicker1 = null;
            int kickervalue = 0;

            for (Rang rang1 : countByRang.keySet()) {
                if (rang1.getValue() > kickervalue) {
                    kicker1 = rang1;
                    kicker1.setValue(rang1.getValue());
                    kickervalue = rang1.getValue();
                }
            }
            resultat.setKicker1(kicker1);
        } else {
            resultat.setUneCombinaisonValide(false);
        }
        return resultat;
    }

    private static void setHauteurRangPaire(Resultat resultat, Map<Rang, Integer> countByRang, Rang rangLePlusNombreux, int countMax) {
        int valueMaxPaire = 0;

        for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
            if (rang.getValue() > valueMaxPaire && countByRang.get(rang) >= countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                countMax = countByRang.get(rang); // on MAJ la valeur countMax
                if (countByRang.get(rang) == 2) {
                    valueMaxPaire = rang.getValue();
                    rangLePlusNombreux = rang;
                }
            }
        }
        resultat.setUneCombinaisonValide(true);
        resultat.setValueCombinaison(3);
        resultat.setHauteur(rangLePlusNombreux); // et l'on setup la hauteur de la 1ere paire
        countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang
    }

    private static void setHauteurRangPaire2(Resultat resultat, Map<Rang, Integer> countByRang, Rang rangLePlusNombreux, int countMax) {
        int valueMaxPaire = 0;
        countMax = 0;
        for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant

            if (rang.getValue() > valueMaxPaire && countByRang.get(rang) >= countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                countMax = countByRang.get(rang); // on MAJ la valeur countMax
                if (countByRang.get(rang) == 2) {
                    valueMaxPaire = rang.getValue();
                    rangLePlusNombreux = rang;
                }
            }
        }
        resultat.setHauteur2(rangLePlusNombreux); // et l'on setup la hauteur de la 2eme paire
        countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang
    }

    // ************************************
    // 8 - PAIR (PAIRE)
    // ************************************

    public static Resultat checkOnePair(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 2 && counts.get(1) < 2) {
            Rang rangLePlusNombreux = null;
            int countMax = 0;
            int valueMaxPaire = 0;

            for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
                if (rang.getValue() > valueMaxPaire && countByRang.get(rang) >= countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                    countMax = countByRang.get(rang); // on MAJ la valeur countMax
                    if (countByRang.get(rang) == 2) {
                        valueMaxPaire = rang.getValue();
                        rangLePlusNombreux = rang;
                    }
                }
            }
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(2);
            resultat.setHauteur(rangLePlusNombreux); // et l'on setup la hauteur de la paire
            countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang

            Rang kicker1 = null;
            Rang kicker2 = null;
            Rang kicker3 = null;
            int kickervalue = 0;

            setKicker1(resultat, countByRang, kicker1, kickervalue);
            setKicker2(resultat, countByRang, kicker2, kickervalue);
            setKicker3(resultat, countByRang, kicker3, kickervalue);

        } else {
            resultat.setUneCombinaisonValide(false);
        }
        return resultat;
    }

    private static void setKicker1(Resultat resultat, Map<Rang, Integer> countByRang, Rang kicker1, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker1 = rang1;
                kicker1.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        resultat.setKicker1(kicker1);
        countByRang.keySet().remove(kicker1);
    }

    private static void setKicker2(Resultat resultat, Map<Rang, Integer> countByRang, Rang kicker2, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker2 = rang1;
                kicker2.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        resultat.setKicker2(kicker2);
        countByRang.keySet().remove(kicker2);
    }

    private static void setKicker3(Resultat resultat, Map<Rang, Integer> countByRang, Rang kicker3, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker3 = rang1;
                kicker3.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        resultat.setKicker3(kicker3);
        countByRang.keySet().remove(kicker3);
    }


    // ************************************
// 9 - HIGH CARD (CARTE HAUTE)
// ************************************
    public static Resultat checkHighCard(Card[] cardsCommunesAndHandPlayer) {
        Resultat resultat = new Resultat();
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer);
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) < 2 && CombinaisonUtil.checkFlush(cardsCommunesAndHandPlayer).isUneCombinaisonValide() == false && CombinaisonUtil.checkStraight(cardsCommunesAndHandPlayer).isUneCombinaisonValide() == false) {
            resultat.setUneCombinaisonValide(true);
            resultat.setValueCombinaison(1);

            Rang kicker1 = null;
            Rang kicker2 = null;
            Rang kicker3 = null;
            Rang kicker4 = null;
            Rang kicker5 = null;

            int kickervalue = 0;

            setKicker1(resultat, countByRang, kicker1, kickervalue);
            setKicker2(resultat, countByRang, kicker2, kickervalue);
            setKicker3(resultat, countByRang, kicker3, kickervalue);
            setKicker4(resultat, countByRang, kicker4, kickervalue);
            setKicker5(resultat, countByRang, kicker5, kickervalue);

        }
        return resultat;
    }

    private static void setKicker4(Resultat resultat, Map<Rang, Integer> countByRang, Rang kicker4, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker4 = rang1;
                kicker4.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        resultat.setKicker4(kicker4);
        countByRang.keySet().remove(kicker4);
    }

    private static void setKicker5(Resultat resultat, Map<Rang, Integer> countByRang, Rang kicker5, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker5 = rang1;
                kicker5.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        resultat.setKicker5(kicker5);
        countByRang.keySet().remove(kicker5);
    }

    // Formule pour retourner le resultat de la combinaison true du joueur
    public static Resultat getCombinaison(Joueur joueur) {
        Resultat resultat = new Resultat();

        List<Combinaison> listCombinaison = new ArrayList<Combinaison>();
        listCombinaison.add(Combinaison.CarteHaute);
        listCombinaison.add(Combinaison.Paire);
        listCombinaison.add(Combinaison.DoublePaire);
        listCombinaison.add(Combinaison.Brelan);
        listCombinaison.add(Combinaison.Suite);
        listCombinaison.add(Combinaison.Couleur);
        listCombinaison.add(Combinaison.Carre);
        listCombinaison.add(Combinaison.QuinteFlush);

        for (Combinaison combinaison : listCombinaison) {
            if (combinaison.check(joueur.getCardsCommunesAndHandPlayer()).getValueCombinaison()!=0) {
                System.out.println(" Joueur possede une : " + combinaison.toString() );
            }
        }
        return resultat;
    }
}
//
//        public static Resultat getHighestResultat (List < Resultat > resultats) {
//            Resultat resultat = new Resultat();
//            return resultat;
//        }


