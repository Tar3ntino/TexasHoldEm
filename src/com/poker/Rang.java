package com.poker;

public enum Rang {
//    Declaration d'une liste d'enumeration pouvant etre appeles par la methode name de la classe "Enum"
//    As, Roi, Dame, Valet, Dix, Neuf, Huit, Sept, Six, Cinq, Quatre, Trois, Deux;

    As("A", 14), Roi("K", 13), Dame("Q", 12), Valet("J", 11), Dix("10", 10), Neuf("9", 9), Huit("8", 8), Sept("7", 7), Six("6", 6), Cinq("5", 5), Quatre("4", 4), Trois("3", 3), Deux("2", 2);

    protected String name = "";
    protected int value;

    private Rang(int value) {
        this.value = value;
    }

    private Rang(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
