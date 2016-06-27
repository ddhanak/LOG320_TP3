package com.company;

public class LinesOfActionAI {

    private static final int PROFONDEUR_MAX = 3;

    /**
     * NegaMax avec Alpha Beta. NegaMax est une simplification de MiniMax
     * Inspiré du pseudo code ici : https://fr.wikipedia.org/wiki/%C3%89lagage_alpha-b%C3%AAta
     * @param board
     * @param a
     * @param b
     * @return
     */
    public static Board alphaBeta(Board board, int a, int b, int niveau) {
        if (niveau == PROFONDEUR_MAX) return board;

        Board meilleurBoard = null;
        int meilleurVal = -Integer.MAX_VALUE;

        for (Board enfant : board.getBoardsEnfants(Board.PION_BLANC)) {
            Board boardEnfant = alphaBeta(enfant, -b, -a, niveau+1);
            int val = -boardEnfant.valeur;

            if (val > meilleurVal) {
                meilleurVal = val;
                meilleurBoard = boardEnfant;
                if (meilleurVal > a) {
                    a = meilleurVal;
                    if (a >= b) {
                        return meilleurBoard;
                    }
                }
            }
        }

        return meilleurBoard;
    }

    public static String getProchainCoup() {
        return "";
    }
}
