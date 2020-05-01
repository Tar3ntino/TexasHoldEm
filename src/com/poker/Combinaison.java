package com.poker;

import java.util.List;

public enum Combinaison {

    CarteHaute(1) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkHighCard(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " Hauteur :" + hauteurEtKickers.get(0);
        }
    },
    Paire(2) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkOnePair(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return "de " + hauteurEtKickers.get(0) + " - Kicker:" + hauteurEtKickers.get(1);
        }
    },
    DoublePaire(3) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkTwoPair(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " de " + hauteurEtKickers.get(0) + "/" + hauteurEtKickers.get(1) + " Kicker :" + hauteurEtKickers.get(2);
        }
    },
    Brelan(4) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkThreeOfAKind(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " de " + hauteurEtKickers.get(0) + " Kickers " + hauteurEtKickers.get(1) + "/" + hauteurEtKickers.get(2);
        }
    },
    Suite(5) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkStraight(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " Hauteur :" + hauteurEtKickers.get(0);
        }
    },
    Couleur(6) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkFlush(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return couleurMax + " Hauteur " + hauteurEtKickers.get(0);
        }
    },
    Full(7) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkFullHouse(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " aux " + hauteurEtKickers.get(0) + " par les " + hauteurEtKickers.get(1);
        }
    },
    Carre(8) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkFourOfAKind(boardAndHandPlayer);
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " de " + hauteurEtKickers.get(0) + " Kicker " + hauteurEtKickers.get(1);
        }
    },
    QuinteFlush(9) {
        public Resultat check(Card[] boardAndHandPlayer) {
            return CombinaisonUtil.checkStraightFlush(boardAndHandPlayer, CombinaisonUtil.checkStraight(boardAndHandPlayer), CombinaisonUtil.checkFlush(boardAndHandPlayer));
        }

        @Override
        public String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax) {
            return " Hauteur " + hauteurEtKickers.get(0);
        }
    };

    // DECLARATION DES ATTRIBUTS :
    protected int value;

    // CONSTRUCTEUR :
    Combinaison(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // FONCTION ABSTRAITE CHECK :
    public abstract Resultat check(Card[] boardAndHandPlayer);

    public abstract String displayResultat(List<Rang> hauteurEtKickers, Couleur couleurMax);
}
