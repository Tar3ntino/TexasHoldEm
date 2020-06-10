package com.poker;

import java.util.*;

public class CombinaisonUtil {

    // ************************************
    // HAND RANKINGS :          Force Combinaison : Constructeur Resultat renvoye :
    //      1.Straight Flush        Force: 9        Int [Value Combinaison, Hauteur quinte flush]
    //      2.Four of a kind        Force: 8        Int [Value Combinaison, Hauteur carre, Kicker 1]
    //      3.Full House            Force: 7        Int [Value Combinaison, Hauteur brelan, Hauteur paire]
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
        if (resultatCheckStraight != null && resultatCheckFlush != null) {
            Resultat resultat = new Resultat();
            resultat.setCombinaison(Combinaison.QuinteFlush);
            List<Rang> hauteur = new ArrayList<>();
            hauteur.add(resultatCheckStraight.getHauteurEtKickers().get(0));
            resultat.setCouleurMax(resultatCheckFlush.getCouleurMax());
            resultat.setHauteurEtKickers(hauteur);
            return resultat;
        }
        return null;
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
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer);
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values());
        Collections.sort(counts, Comparator.reverseOrder());

        if (counts.get(0) == 4) {
            Resultat resultat = new Resultat();
            Rang rangLePlusNombreux = null;
            int countMax = 0;
            for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
                if (countByRang.get(rang) > countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                    countMax = countByRang.get(rang); // on MAJ la valeur countMax
                    rangLePlusNombreux = rang;
                }
            }
            resultat.setCombinaison(Combinaison.Carre);
            List<Rang> hauteurs = new ArrayList<>();
            hauteurs.add(rangLePlusNombreux);
            countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang
            Rang kicker1 = null;
            int kickervalue = 0;

            //System.out.print("affichage du KeySet apres remove :" + countByRang.keySet());

            for (Rang rang1 : countByRang.keySet()) {
                if (rang1.getValue() > kickervalue) {
                    kicker1 = rang1;
                    kicker1.setValue(rang1.getValue());
                    kickervalue = rang1.getValue();
                }
            }
            hauteurs.add(kicker1);
            resultat.setHauteurEtKickers(hauteurs);
            return resultat;
        }
        return null;
    }

    // ************************************
    // 3 - FULL HOUSE (FULL)
    // ************************************

    public static Resultat checkFullHouse(Card[] cardsCommunesAndHandPlayer) {

        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 3 && counts.get(1) >= 2) {
            Resultat resultat = new Resultat();
            resultat.setCombinaison(Combinaison.Full);
            List<Rang> hauteursetKickers = new ArrayList<>();
            getHauteurBrelan(countByRang, hauteursetKickers);
            getHauteurPaire(countByRang, hauteursetKickers);
            resultat.setHauteurEtKickers(hauteursetKickers);// et l'on setup la hauteur du Brelan

            return resultat;
        }
        return null;
    } // Fin de la methode Full

    private static void getHauteurPaire(Map<Rang, Integer> countByRang, List<Rang> hauteursetKickers) {
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
        hauteursetKickers.add(rangLePlusNombreux);
        countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang
    }

    private static void getHauteurBrelan(Map<Rang, Integer> countByRang, List<Rang> hauteursetKickers) {
        Rang rangLePlusNombreux = null;
        int countMax = 0;

        for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
            if (countByRang.get(rang) > countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                countMax = countByRang.get(rang); // on MAJ la valeur countMax
                rangLePlusNombreux = rang;
            }
        }
        hauteursetKickers.add(rangLePlusNombreux);
        countByRang.keySet().remove(rangLePlusNombreux);// on enleve ensuite le rang du Set de rang
    }

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
    // Declaration d'une variable de type "Map" qui associe les types "Couleur" et "Integer"
    // ************************************

    public static Resultat checkFlush(Card[] cardsCommunesAndHandPlayer) {

        Map<Couleur, Integer> countByColor = countByColor(cardsCommunesAndHandPlayer); // Notre tableau Map qui contient le nombre de chaque couleur rencontree
        ArrayList<Integer> counts = new ArrayList<>(countByColor.values()); // on recupere les valeurs de comptage couleur
        Collections.sort(counts, Comparator.reverseOrder()); // on les classe decroissant

        if (counts.get(0) >= 5) {
            Resultat resultat = new Resultat();
            setCouleurMax(resultat, countByColor);
            resultat.setCombinaison(Combinaison.Couleur); // On a detecte une couleur donc on setup la combinaison en couleur

            int hauteurMax = 0;

            for (Card card : cardsCommunesAndHandPlayer) {
                if (card.getCouleur() == resultat.getCouleurMax() && card.getRang().getValue() > hauteurMax) {
                    hauteurMax = card.getRang().getValue();

                    List<Rang> hauteursetKickers = new ArrayList<>();
                    hauteursetKickers.add(card.getRang());
                    resultat.setHauteurEtKickers(hauteursetKickers);// et l'on setup la hauteur de la flush
                }
            }
            return resultat;
        }
        return null;
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

    // ************************************
    //        Methode countByColor
    // Instanciation d'un objet "HashMap"
    // Iteration sur "cardsCommunesAndHandPlayer"
    //    - On recupere la couleur de chaque carte
    //    - On recupere dans la Map la couleur correspondante lue precedemment et son nb actuel a savoir au debut 0
    //    - On incremente de 1 le compteur a la couleur rencontree
    //
    // On renvoie la Map "countByColor"
    // ***********************************

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

        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        Set<Rang> rangs = countByRang.keySet();// on collecte les rangs du tirage des 7 cartes du board
        Rang[] allRangs = Rang.values();

        if (rangs.size() < 5) {
            return null;
        }

        List<Integer> hauteurs = collectRangValue(rangs);
        Collections.sort(hauteurs);
        for (int i = 0; i < hauteurs.size() - 4; i++) {
            if (hauteurs.get(i + 4) - hauteurs.get(i) == 4) {
                Resultat resultat = new Resultat();
                resultat.setCombinaison(Combinaison.Suite);
                getHauteurSuite(allRangs, hauteurs, i, resultat);
                return resultat;
            }
        }
        return checkSuiteHauteur5(rangs);
    }

    private static List<Integer> collectRangValue(Set<Rang> rangs) {
        List<Integer> hauteurs = new ArrayList<>();
        for (Rang rang : rangs) {
            hauteurs.add(rang.getValue());
        }
        return hauteurs;
    }

    private static Resultat checkSuiteHauteur5(Set<Rang> rangs) {
        Set<Rang> fromAsToCinq = new HashSet<>(Arrays.asList(Rang.As, Rang.Deux, Rang.Trois, Rang.Quatre, Rang.Cinq));
        if (rangs.containsAll(fromAsToCinq)) {
            Resultat resultat = new Resultat();
            resultat.setCombinaison(Combinaison.Suite);
            List<Rang> hauteursetKickers = new ArrayList<>();
            hauteursetKickers.add(Rang.Cinq);
            resultat.setHauteurEtKickers(hauteursetKickers);// et l'on setup la hauteur de la suite
            return resultat;
        } else return null;
    }

    private static void getHauteurSuite(Rang[] allRangs, List<Integer> hauteurs, int i, Resultat resultat) {
        for (Rang allRang : allRangs) {
            if (allRang.getValue() == hauteurs.get(i + 4)) {
                List<Rang> hauteursetKickers = new ArrayList<>();
                hauteursetKickers.add(allRang);
                resultat.setHauteurEtKickers(hauteursetKickers);
            }
        }
    }

    // ************************************
    // 6 - THREE OF A KIND (BRELAN)
    // ************************************

    public static Resultat checkThreeOfAKind(Card[] cardsCommunesAndHandPlayer) {

        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 3 && counts.get(1) < 2) {
            Resultat resultat = new Resultat();
            Rang rangLePlusNombreux = null;
            int countMax = 0;

            for (Rang rang : countByRang.keySet()) {    // On parcourt le set de rang existant
                if (countByRang.get(rang) > countMax) { // Si l'on a un int de rang plus important que le precedent enregistre
                    countMax = countByRang.get(rang); // on MAJ la valeur countMax
                    rangLePlusNombreux = rang;
                }
            }
            resultat.setCombinaison(Combinaison.Brelan);

            List<Rang> hauteursetKickers = new ArrayList<>();
            hauteursetKickers.add(rangLePlusNombreux);
            resultat.setHauteurEtKickers(hauteursetKickers);// et l'on setup la hauteur du Brelan
            countByRang.keySet().remove(rangLePlusNombreux);// on enleve ensuite le rang du Set de rang

            Rang kicker1 = null;
            Rang kicker2 = null;
            int kickervalue = 0;

            kicker1 = getKicker1(countByRang, kicker1, kickervalue);
            kicker2 = getKicker2(countByRang, kicker2, kickervalue);
            resultat.setHauteurEtKickers(Arrays.asList(rangLePlusNombreux, kicker1, kicker2));
            return resultat;
        }
        return null;
    } // Fin de la methode Three of a kind (Brelan)

    // ************************************
    // 7 - TWO PAIR (DOUBLE PAIR)
    // ************************************

    public static Resultat checkTwoPair(Card[] cardsCommunesAndHandPlayer) {

        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 2 && counts.get(1) == 2) { // Si la conditionest respectee, on est sur d'avoir une double paire
            Resultat resultat = new Resultat();
            Rang rangLePlusNombreux = null;
            int countMax = 0;
            Rang hauteurRangPaire = getHauteurRangPaire(countByRang, rangLePlusNombreux, countMax);
            Rang hauteurRangPaire2 = getHauteurRangPaire2(resultat, countByRang, rangLePlusNombreux, countMax);

            Rang kicker1 = null;
            int kickervalue = 0;

            for (Rang rang1 : countByRang.keySet()) {
                if (rang1.getValue() > kickervalue) {
                    kicker1 = rang1;
                    kicker1.setValue(rang1.getValue());
                    kickervalue = rang1.getValue();
                }
            }
            resultat.setHauteurEtKickers(Arrays.asList(hauteurRangPaire, hauteurRangPaire2, kicker1));
            resultat.setCombinaison(Combinaison.DoublePaire);
            return resultat;
        }
        return null;
    }

    private static Rang getHauteurRangPaire(Map<Rang, Integer> countByRang, Rang rangLePlusNombreux, int countMax) {
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
        countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang
        return rangLePlusNombreux;
    }

    private static Rang getHauteurRangPaire2(Resultat resultat, Map<Rang, Integer> countByRang, Rang rangLePlusNombreux, int countMax) {
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
        countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang
        return rangLePlusNombreux;
    }

    // ************************************
    // 8 - PAIR (PAIRE)
    // ************************************

    public static Resultat checkOnePair(Card[] cardsCommunesAndHandPlayer) {

        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer); // on declare une variable Map qui va associer et stocker les variables rangs et les int
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values()); // on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder()); // on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) == 2 && counts.get(1) < 2) {
            Resultat resultat = new Resultat();
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
            resultat.setCombinaison(Combinaison.Paire);
            countByRang.keySet().remove(rangLePlusNombreux); // on enleve ensuite le rang du Set de rang

            Rang kicker1 = null;
            Rang kicker2 = null;
            Rang kicker3 = null;
            int kickervalue = 0;

            kicker1 = getKicker1(countByRang, kicker1, kickervalue);
            kicker2 = getKicker2(countByRang, kicker2, kickervalue);
            kicker3 = getKicker3(countByRang, kicker3, kickervalue);
            resultat.setHauteurEtKickers(Arrays.asList(rangLePlusNombreux, kicker1, kicker2, kicker3));
            return resultat;
        }
        return null;
    }

    // ************************************
// 9 - HIGH CARD (CARTE HAUTE)
// ************************************
    public static Resultat checkHighCard(Card[] cardsCommunesAndHandPlayer) {
        Map<Rang, Integer> countByRang = countByRang(cardsCommunesAndHandPlayer);
        ArrayList<Integer> counts = new ArrayList<>(countByRang.values());// on declare une variable Arraylist pour stocker les int afin de pouvoir les classer
        Collections.sort(counts, Comparator.reverseOrder());// on appelle une classe utilitaire Collections avec la methode de tri "sort"
        if (counts.get(0) < 2 && CombinaisonUtil.checkFlush(cardsCommunesAndHandPlayer) == null && CombinaisonUtil.checkStraight(cardsCommunesAndHandPlayer) == null) {
            Resultat resultat = new Resultat();
            resultat.setCombinaison(Combinaison.CarteHaute);

            Rang kicker1 = null;
            Rang kicker2 = null;
            Rang kicker3 = null;
            Rang kicker4 = null;
            Rang kicker5 = null;

            int kickervalue = 0;

            kicker1 = getKicker1(countByRang, kicker1, kickervalue);
            kicker2 = getKicker2(countByRang, kicker2, kickervalue);
            kicker3 = getKicker3(countByRang, kicker3, kickervalue);
            kicker4 = getKicker4(countByRang, kicker4, kickervalue);
            kicker5 = getKicker5(countByRang, kicker5, kickervalue);
            resultat.setHauteurEtKickers(Arrays.asList(kicker1, kicker2, kicker3, kicker4, kicker5));

            return resultat;
        }
        return null;
    }

    private static Rang getKicker1(Map<Rang, Integer> countByRang, Rang kicker1, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker1 = rang1;
                kicker1.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        countByRang.keySet().remove(kicker1);
        return kicker1;
    }

    private static Rang getKicker2(Map<Rang, Integer> countByRang, Rang kicker2, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker2 = rang1;
                kicker2.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        countByRang.keySet().remove(kicker2);
        return kicker2;
    }

    private static Rang getKicker3(Map<Rang, Integer> countByRang, Rang kicker3, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker3 = rang1;
                kicker3.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        countByRang.keySet().remove(kicker3);
        return kicker3;
    }

    private static Rang getKicker4(Map<Rang, Integer> countByRang, Rang kicker4, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker4 = rang1;
                kicker4.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        countByRang.keySet().remove(kicker4);
        return kicker4;
    }

    private static Rang getKicker5(Map<Rang, Integer> countByRang, Rang kicker5, int kickervalue) {
        for (Rang rang1 : countByRang.keySet()) {
            if (rang1.getValue() > kickervalue) {
                kicker5 = rang1;
                kicker5.setValue(rang1.getValue());
                kickervalue = rang1.getValue();
            }
        }
        countByRang.keySet().remove(kicker5);
        return kicker5;
    }

    // Formule pour retourner le resultat de la combinaison true du joueur
    public static Resultat getCombinaison(Joueur joueur) {
        List<Combinaison> listCombinaison = initListCombi(); // declaration tableau list combi qui rassemble les checks de chaque combinaison
        for (Combinaison combinaison : listCombinaison) {
            Resultat resultat = combinaison.check(joueur.getCardsCommunesAndHandPlayer());
            if (resultat != null) { // on appelle la methode check de l'objet combinaison pour verifier s'il renvoie une valeur =! de 0
                return resultat;
            }
        }
        return null;
    }

    private static List<Combinaison> initListCombi() {
        List<Combinaison> listCombinaison = new ArrayList<Combinaison>();
        listCombinaison.add(Combinaison.QuinteFlush);
        listCombinaison.add(Combinaison.Carre);
        listCombinaison.add(Combinaison.Full);
        listCombinaison.add(Combinaison.Couleur);
        listCombinaison.add(Combinaison.Suite);
        listCombinaison.add(Combinaison.Brelan);
        listCombinaison.add(Combinaison.DoublePaire);
        listCombinaison.add(Combinaison.Paire);
        listCombinaison.add(Combinaison.CarteHaute);
        return listCombinaison;
    }

    /**
     * @param rangs1
     * @param rangs2
     * @return 1 si rangs1 > rangs2, -1 si rangs2 > rangs1, 0 si egalite
     */
    public static int compare(List<Rang> rangs1, List<Rang> rangs2) {
        if (rangs2 == null || rangs2.isEmpty()) {
            return 1;
        }
        for (int i = 0; i < rangs1.size(); i++) {
            if (rangs1.get(i).value > rangs2.get(i).value) {
                return 1;
            } else if (rangs1.get(i).value < rangs2.get(i).value) {
                return -1;
            }
        }
        return 0;
    }

    public static List<Joueur> departagerJoueurs(List<Joueur> joueurs) {
        List<Joueur> result = new ArrayList<>();
        List<Rang> rangsEtHauteursMax = new ArrayList<>();
        for (Joueur joueur : joueurs) { // On boucle sur les joueursWithHighestCombinaisonValue restant en lice
            Resultat combinaison = CombinaisonUtil.getCombinaison(joueur); // On affecte la combi du joueur dans une variable
            int compareResult = CombinaisonUtil.compare(combinaison.getHauteurEtKickers(), rangsEtHauteursMax); // declaration d'un int pour comparer
            switch (compareResult) {
                case 1:
                    rangsEtHauteursMax = combinaison.getHauteurEtKickers();
                    result.clear();
                    result.add(joueur);
                    break;
                case 0:
                    result.add(joueur);
                    break;
            }
        }
        return result;
    }
}
