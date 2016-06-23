package com.company;

import java.util.List;

public class Board {

    public static final int CASE_VIDE = 0;
    public static final int PION_NOIR = 2;
    public static final int PION_BLANC = 4;

    public Board(int[][] board) {
        valeur = calculerValeur(board);
    }

    public Board(int valeur) {
        this.valeur = valeur;
    }

    public List<Board> getBoardsEnfants() {
        return null;
    }

    private int calculerValeur(int[][] board) {
        return 0;
    }

    public int valeur;
}
