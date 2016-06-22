package com.company;

import java.util.List;

public class Board {

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
