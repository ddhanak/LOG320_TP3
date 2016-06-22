package com.company;

public class LinesOfActionAI {

    /**
     * NegaMax avec Alpha Beta. NegaMax est une simplification de MiniMax
     * @param board
     * @param a
     * @param b
     * @return
     */
    public static Board alphaBeta(Board board, int a, int b, int niveau) {
        if (niveau == 3) return board;

        Board meilleurBoard = new Board(-Integer.MAX_VALUE);
        int meilleurVal = -Integer.MAX_VALUE;

        // Pour chaque board après tous les coups possibles
        for (Board enfant : board.getBoardsEnfants()) {
            Board boardEnfant = alphaBeta(enfant, -b, -a, niveau+1);
            int val = -boardEnfant.valeur;

            if (val > meilleurVal) {
                meilleurVal = val;
                meilleurBoard = boardEnfant;
                if (meilleurVal > a) {
                    a = meilleurVal;
                    if (a > b) {
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
