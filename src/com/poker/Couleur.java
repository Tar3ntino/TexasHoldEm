package com.poker;

public enum Couleur {
//    Coeur("♥"), Carreau("♦"), Pique ("♠️"), Trefle("♣");

    Coeur {
        public String toString() {
            return "♥";
        }
    },
    Carreau {
        public String toString() {
            return "♦";
        }
    },
    Pique {
        public String toString() {
            return "♠";
        }
    },
    Trefle {
        public String toString() {
            return "♣";
        }
    },
}
