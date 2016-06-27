package com.company;

import java.util.List;

public class Board {

    public static final int CASE_VIDE = 0;
    public static final int PION_NOIR = 2;
    public static final int PION_BLANC = 4;

    public Board(int[][] board) {
        _board = board;
        valeur = calculerValeur(PION_BLANC); // hardcodé blanc pour l'instant
    }

    public List<Board> getBoardsEnfants(int pionAmi) {
        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == pionAmi) {
                    // Ligne verticale
                    int nbPionsLigneVerticale = getNbPionsLigneVerticale(y);

                    // Ligne horizontale
                    int nbPionsLigneHorizontale = getNbPionsLigneHorizontale(x);

                    // Ligne diagonale 1
                    int nbPionsLigneDiagonale1 = getNbPionsLigneDiagonaleNegative(x, y);

                    // Ligne diagonale 2
                    int nbPionsLigneDiagonale2 = getNbPionsLigneDiagonalePositive(x, y);
                }
            }
        }

        return null;
    }

    public int getNbPionsLigneDiagonalePositive(int x, int y) {
        int nbPions = 0;

        // On revient au début de la ligne
        while (x < _board.length - 1 && y > 0) {
            x++;
            y--;
        }

        while (x >= 0 && y < _board.length) {
            if (_board[x][y] != CASE_VIDE)
                nbPions++;

            x--;
            y++;
        }

        return nbPions;
    }

    public int getNbPionsLigneDiagonaleNegative(int x, int y) {
        int nbPions = 0;

        // On revient au début de la ligne
        while (x > 0 && y > 0) {
            x--;
            y--;
        }

        while (x < _board.length && y < _board.length) {
            if (_board[x][y] != CASE_VIDE)
                nbPions++;

            x++;
            y++;
        }

        return nbPions;
    }

    public int getNbPionsLigneHorizontale(int x) {
        int nbPions = 0;
        for (int y = 0; y != _board.length; y++) {
            if (_board[x][y] != CASE_VIDE)
                nbPions++;
        }
        return nbPions;
    }

    public int getNbPionsLigneVerticale(int y) {
        int nbPions = 0;
        for (int x = 0; x != _board.length; x++) {
            if (_board[x][y] != CASE_VIDE)
                nbPions++;
        }
        return nbPions;
    }

    public int calculerValeur(int pionAmi) {
        // ça serait peut-être intéressant de de calculer l'espace totale entre
        // nos pions
        return 0;
    }

    public int valeur;

    private int[][] _board;
}
