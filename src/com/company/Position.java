package com.company;


public class Position {
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(String positionEnTexte) {
        this.x = 8 - Integer.parseInt(positionEnTexte.charAt(1) + "");
        this.y = getIndexLettre(positionEnTexte.charAt(0));
    }

    public int x;
    public int y;

    @Override
    public String toString() {
        return "" + lettres[y] + (8 - x);
    }

    private char[] lettres = {
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H'
    };

    private int getIndexLettre(char lettre) {
        for (int i = 0; i < lettres.length; i++)
            if (lettres[i] == lettre)
                return i;

        return -1;
    }
}
