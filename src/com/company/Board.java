package com.company;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int CASE_VIDE = 0;
    public static final int PION_NOIR = 2;
    public static final int PION_BLANC = 4;

    private static final int PROFONDEUR_MAX = 5;

    public Board(int[][] board, Coup dernierCoupJoue) {
        _board = board;
        _dernierCoupJoue = dernierCoupJoue;
    }

    public Coup getProchainCoup(int couleurEquipe) {
        return getBoardApresProchainCoup(couleurEquipe).getDernierCoupJoue();
    }

    public Board getBoardApresProchainCoup(int couleurEquipe) {
        return alphaBeta(this, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, couleurEquipe);
    }

    /**
     * NegaMax avec Alpha Beta. NegaMax est une simplification de MiniMax
     * Inspiré du pseudo code ici : https://fr.wikipedia.org/wiki/%C3%89lagage_alpha-b%C3%AAta
     * @param board
     * @param a
     * @param b
     * @return
     */
    public static Board alphaBeta(Board board, int a, int b, int niveau, int couleurEquipe) {
        if (niveau == PROFONDEUR_MAX) return board;

        int meilleurVal = -Integer.MAX_VALUE;
        Board meilleurBoard = null;
        // Sert seulement pour la racine pour pas tenter de faire un coup plus profond dans l'arbre
        Board parentMeilleurBoard = null;

        for (Board enfant : board.getBoardsEnfants(couleurEquipe)) {
            // Si board est gagnant
            if (niveau == 0 && enfant.calculerValeur(couleurEquipe) > 1000)
                return enfant;

            Board boardEnfant = alphaBeta(enfant, -b, -a, niveau+1, couleurEquipe);
            int val = -boardEnfant.calculerValeur(couleurEquipe);

            if (val > meilleurVal) {
                meilleurVal = val;
                meilleurBoard = boardEnfant;
                parentMeilleurBoard = enfant;
                if (meilleurVal > a) {
                    a = meilleurVal;
                    if (a >= b) {
                        return meilleurBoard;
                    }
                }
            }
        }

        return (niveau != 0) ? meilleurBoard : parentMeilleurBoard;
    }

    /**
     * Optimisation possible en calculant une seule fois le nombre de pions par ligne/colonnes
     * @param couleurPions
     * @return
     */
    public List<Board> getBoardsEnfants(int couleurPions) {
        List<Coup> coupsPossibles = getCoupsPossibles(couleurPions);
        List<Board> boardsEnfants = new ArrayList<>();

        for (Coup coup : coupsPossibles) {
            int[][] boardEnfant = new int[8][8];
            int xDepart = coup.depart.x;
            int yDepart = coup.depart.y;
            int xArrivee = coup.arrivee.x;
            int yArrivee = coup.arrivee.y;

            for (int x = 0; x < _board.length; x++) {
                for (int y = 0; y < _board.length; y++) {
                    if (x == xDepart && y == yDepart)
                        boardEnfant[x][y] = CASE_VIDE;
                    else if (x == xArrivee && y == yArrivee)
                        boardEnfant[x][y] = couleurPions;
                    else
                        boardEnfant[x][y] = _board[x][y];
                }
            }

            boardsEnfants.add(new Board(boardEnfant, coup));
        }

        return boardsEnfants;
    }

    /**
     * Générateur de mouvements.
     * @param couleurPions couleur des pions de l'équipe pour laquelle générer les coups
     * @return une liste de coup possibles.
     */
    public List<Coup> getCoupsPossibles(int couleurPions) {
        List<Coup> coupsPossibles = new ArrayList<>();

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurPions) {
                    getCoupsLigneVerticale(couleurPions, coupsPossibles, x, y);

                    getCoupsLigneHorizontale(couleurPions, coupsPossibles, x, y);

                    getCoupsLigneDiagonaleNegative(couleurPions, coupsPossibles, x, y);

                    getCoupsLigneDiagonalePositive(couleurPions, coupsPossibles, x, y);
                }
            }
        }

        return coupsPossibles;
    }

    private void getCoupsLigneVerticale(int couleurPions, List<Coup> coupsPossibles, int x, int y) {
        int nbPionsLigne = getNbPionsLigneVerticale(y);
        for (int i = 1; i <= nbPionsLigne; i++) {
            int xPlus = x + i;
            if (xPlus < _board.length) {
                int positionExploree = _board[xPlus][y];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(xPlus, y)));
                }
            }
        }
        for (int i = 1; i <= nbPionsLigne; i++) {
            int xMoins = x - i;
            if (xMoins >= 0) {
                int positionExploree = _board[xMoins][y];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(xMoins, y)));
                }
            }
        }
    }

    private void getCoupsLigneHorizontale(int couleurPions, List<Coup> coupsPossibles, int x, int y) {
        int nbPionsLigne = getNbPionsLigneHorizontale(x);
        for (int i = 1; i <= nbPionsLigne; i++) {
            int yPlus = y + i;
            if (yPlus < _board.length) {
                int positionExploree = _board[x][yPlus];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(x, yPlus)));
                }
            }
        }
        for (int i = 1; i <= nbPionsLigne; i++) {
            int yMoins = y - i;
            if (yMoins >= 0) {
                int positionExploree = _board[x][yMoins];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(x, yMoins)));
                }
            }
        }
    }

    private void getCoupsLigneDiagonaleNegative(int couleurPions, List<Coup> coupsPossibles, int x, int y) {
        int nbPionsLigne = getNbPionsLigneDiagonaleNegative(x, y);
        for (int i = 1; i <= nbPionsLigne; i++) {
            int yPlus = y + i;
            int xPlus = x + i;
            if (yPlus < _board.length && xPlus < _board.length) {
                int positionExploree = _board[xPlus][yPlus];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(xPlus, yPlus)));
                }
            }
        }
        for (int i = 1; i <= nbPionsLigne; i++) {
            int yMoins = y - i;
            int xMoins = x - i;
            if (yMoins >= 0 && xMoins >= 0) {
                int positionExploree = _board[xMoins][yMoins];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(xMoins, yMoins)));
                }
            }
        }

    }

    private void getCoupsLigneDiagonalePositive(int couleurPions, List<Coup> coupsPossibles, int x, int y) {
        int nbPionsLigne = getNbPionsLigneDiagonalePositive(x, y);
        for (int i = 1; i <= nbPionsLigne; i++) {
            int yPlus = y + i;
            int xMoins = x - i;
            if (yPlus < _board.length && xMoins >= 0) {
                int positionExploree = _board[xMoins][yPlus];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(xMoins, yPlus)));
                }
            }
        }
        for (int i = 1; i <= nbPionsLigne; i++) {
            int yMoins = y - i;
            int xPlus = x + i;
            if (yMoins >= 0 && xPlus < _board.length) {
                int positionExploree = _board[xPlus][yMoins];
                boolean estPionAdverse = positionExploree != couleurPions && positionExploree != CASE_VIDE;

                if (i != nbPionsLigne && estPionAdverse)
                    break;

                if (i == nbPionsLigne) {
                    if (positionExploree == CASE_VIDE || estPionAdverse)
                        coupsPossibles.add(new Coup(new Position(x, y), new Position(xPlus, yMoins)));
                }
            }
        }
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
        int nbPionsAmis = 0;
        int nbPionsEnnemis = 0;
        int nbPionsAmisIsoles = 0;
        int nbPionsEnnemisIsoles = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == pionAmi) {
                    nbPionsAmis++;
                    if (pionEstIsole(x, y))
                        nbPionsAmisIsoles++;
                }
                else if (_board[x][y] != CASE_VIDE) { // Ennemi
                    nbPionsEnnemis++;
                    if (pionEstIsole(x, y))
                        nbPionsEnnemisIsoles++;
                }
            }
        }

        int valeurAmi = 0;
        int valeurEnnemi = 0;

        valeurAmi = (nbPionsAmis > 1 && nbPionsAmisIsoles > 0) ? 1000 - nbPionsAmisIsoles : 5000; // Victoire ami
        valeurEnnemi = (nbPionsEnnemis > 1 && nbPionsEnnemisIsoles > 0) ? 1000 - nbPionsEnnemisIsoles : 5000; // Victoire ennemi

        return valeurAmi - valeurEnnemi;
    }

    public boolean pionEstIsole(int x, int y) {
        int couleurPion = _board[x][y];

        if (couleurPion == CASE_VIDE)
            return false;

        if (y + 1 < _board.length && _board[x][y + 1] == couleurPion)
            return false;

        if (x + 1 < _board.length && _board[x + 1][y] == couleurPion)
            return false;

        if (y - 1 >= 0 && _board[x][y - 1] == couleurPion)
            return false;

        if (x - 1 >= 0 && _board[x - 1][y] == couleurPion)
            return false;

        if (x + 1 < _board.length && y + 1 < _board.length && _board[x + 1][y + 1] == couleurPion)
            return false;

        if (x + 1 < _board.length && y - 1 >= 0 && _board[x + 1][y - 1] == couleurPion)
            return false;

        if (x - 1 >= 0 && y + 1 < _board.length && _board[x - 1][y + 1] == couleurPion)
            return false;

        if (x - 1 >= 0 && y - 1 >= 0 && _board[x - 1][y - 1] == couleurPion)
            return false;

        return true;
    }

    public Coup getDernierCoupJoue() {
        return _dernierCoupJoue;
    }

    private int[][] _board;

    private Coup _dernierCoupJoue;
}
