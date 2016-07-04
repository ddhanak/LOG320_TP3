package com.company;


public class Position {
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
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
}
