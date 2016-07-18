package com.company;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int CASE_VIDE = 0;
    public static final int PION_NOIR = 2;
    public static final int PION_BLANC = 4;

    private static final int PROFONDEUR_MAX = 3;

    public Board(int[][] board, Coup dernierCoupJoue) {
        _board = board;
        _dernierCoupJoue = dernierCoupJoue;
    }

    public Board(int[][] board, Coup dernierCoupJoue, int profondeur) {
        _board = board;
        _dernierCoupJoue = dernierCoupJoue;
        _profondeur = profondeur;
    }

    public Coup getProchainCoup(int couleurEquipe) {
        return getBoardApresProchainCoup(couleurEquipe).getDernierCoupJoue();
    }

    public Coup getProchainCoupRapide(int couleurEquipe) {
        Board meilleurBoard = null;
        double meilleurVal = Integer.MIN_VALUE;

        for (Board enfant : getBoardsEnfants(couleurEquipe, 0)) {
            double val = enfant.calculerValeur(couleurEquipe);
            if (val > meilleurVal) {
                meilleurVal = val;
                meilleurBoard = enfant;
            }
        }

        return meilleurBoard.getDernierCoupJoue();
    }

    public Board getBoardApresProchainCoup(int couleurEquipe) {
        Board meilleurBoard = null;
        double meilleurVal = Integer.MIN_VALUE;

        for (Board enfant : getBoardsEnfants(couleurEquipe, 0)) {
            double val = alphaBeta(enfant, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, couleurEquipe, false);
            if (val > meilleurVal) {
                meilleurVal = val;
                meilleurBoard = enfant;
            }
        }

        return meilleurBoard;
    }

    public double alphaBeta(Board board, int profondeur, double a, double b, int couleurEquipe, boolean max) {
        if (profondeur == PROFONDEUR_MAX || board.estGagnant())
            return board.calculerValeur(couleurEquipe);

        if (max) {
            double meilleurVal = Integer.MIN_VALUE;
            for (Board enfant : board.getBoardsEnfants(couleurEquipe, profondeur)) {
                double val = alphaBeta(enfant, profondeur+1, a, b, couleurEquipe, false);
                if (val > meilleurVal)
                    meilleurVal = val;
                if (meilleurVal > a)
                    a = meilleurVal;
                if (b <= a)
                    break;
            }
            return meilleurVal;
        }
        else { // MIN
            double meilleurVal = Integer.MAX_VALUE;
            for (Board enfant : board.getBoardsEnfants(getCouleurAdverse(couleurEquipe), profondeur)) {
                double val = alphaBeta(enfant, profondeur+1, a, b, couleurEquipe, true);
                if (val < meilleurVal)
                    meilleurVal = val;
                if (meilleurVal < b)
                    b = meilleurVal;
                if (b <= a)
                    break;
            }
            return meilleurVal;
        }
    }

    public static int getCouleurAdverse(int couleurEquipe) {
        return (couleurEquipe == PION_BLANC) ? PION_NOIR : PION_BLANC;
    }

    /**
     * Optimisation possible en calculant une seule fois le nombre de pions par ligne/colonnes
     * @param couleurPions
     * @return
     */
    public List<Board> getBoardsEnfants(int couleurPions, int profondeur) {
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

            boardsEnfants.add(new Board(boardEnfant, coup, profondeur));
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

    public double calculerValeur(int couleurEquipe) {
        int couleurAdversaire = getCouleurAdverse(couleurEquipe);

        int totalXEquipe = 0;
        int totalYEquipe = 0;
        int totalXAdversaire = 0;
        int totalYAdversaire = 0;
        int nbPionsAdversaire = 0;
        int nbPionsEquipe = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurEquipe) {
                    totalXEquipe += x;
                    totalYEquipe += y;
                    nbPionsEquipe++;
                }
                else if (_board[x][y] == couleurAdversaire) {
                    totalXAdversaire += x;
                    totalYAdversaire += y;
                    nbPionsAdversaire++;
                }
            }
        }

        int xEquipe = totalXEquipe / nbPionsEquipe;
        int yEquipe = totalYEquipe / nbPionsEquipe;
        int xAdversaire = totalXAdversaire / nbPionsAdversaire;
        int yAdversaire = totalYAdversaire / nbPionsAdversaire;

        double poidsTotalEquipe = 0;
        double poidsTotalAdversaire = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurEquipe) {
                    poidsTotalEquipe += 100 - Math.sqrt(Math.pow(y - yEquipe, 2) + Math.pow(x - xEquipe, 2));
                }
                else if (_board[x][y] == couleurAdversaire) {
                    poidsTotalAdversaire += 100 - Math.sqrt(Math.pow(y - yAdversaire, 2) + Math.pow(x - xAdversaire, 2));
                }
            }
        }
            
        double poidsMoyenEquipe = poidsTotalEquipe / nbPionsEquipe;
        double poidsMoyenAdversaire = poidsTotalAdversaire / nbPionsAdversaire;

        double bonusVictoire = 0;

        if (estGagnant(couleurEquipe))
            bonusVictoire += 1000 - _profondeur;
        else if (estGagnant(couleurAdversaire))
            bonusVictoire -= 1000 - _profondeur;

        return poidsMoyenEquipe - poidsMoyenAdversaire + bonusVictoire;
    }

    public boolean estGagnant() {
        int xDepartBlanc = 0;
        int yDepartBlanc = 0;
        int nbPionsBlanc = 0;
        int xDepartNoir = 0;
        int yDepartNoir = 0;
        int nbPionsNoir = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == PION_BLANC) {
                    xDepartBlanc = x;
                    yDepartBlanc = y;
                    nbPionsBlanc++;
                }
                else if (_board[x][y] == PION_NOIR) {
                    xDepartNoir = x;
                    yDepartNoir = y;
                    nbPionsNoir++;
                }
            }
        }

        boolean estGagnant = true;

        for (int x = 0; x != _board.length; x++)
            for (int y = 0; y != _board.length; y++)
                if (estGagnant) {
                    if (_board[x][y] == PION_BLANC)
                        if (!atteindrePion(x, y, PION_BLANC, xDepartBlanc, yDepartBlanc, nbPionsBlanc - 1))
                            estGagnant = false;
                }
                else {
                    break;
                }

        if (estGagnant)
            return true;

        for (int x = 0; x != _board.length; x++)
            for (int y = 0; y != _board.length; y++)
                if (_board[x][y] == PION_NOIR)
                    if (!atteindrePion(x, y, PION_NOIR, xDepartNoir, yDepartNoir, nbPionsNoir - 1))
                        return false;

        return true;
    }

    public boolean estGagnant(int couleurPions) {
        int xDepart = 0;
        int yDepart = 0;
        int nbPions = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurPions) {
                    xDepart = x;
                    yDepart = y;
                    nbPions++;
                }
            }
        }

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurPions) {
                    if (!atteindrePion(x, y, couleurPions, xDepart, yDepart, nbPions - 1))
                        return false;
                }
            }
        }

        return true;
    }

    public boolean atteindrePion(int xAAtteindre, int yAAtteindre, int couleurPions, int x, int y, double mouvementsRestants) {
        if (x == xAAtteindre && y == yAAtteindre)
            return true;

        if (mouvementsRestants == 0)
            return false;

        if (y + 1 < _board.length && _board[x][y + 1] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x, y + 1, mouvementsRestants - 1))
                return true;

        if (x + 1 < _board.length && _board[x + 1][y] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x + 1, y, mouvementsRestants - 1))
                return true;

        if (y - 1 >= 0 && _board[x][y - 1] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x, y - 1, mouvementsRestants - 1))
                return true;

        if (x - 1 >= 0 && _board[x - 1][y] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x - 1, y, mouvementsRestants - 1))
                return true;

        if (x + 1 < _board.length && y + 1 < _board.length && _board[x + 1][y + 1] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x + 1, y + 1, mouvementsRestants - 1))
                return true;

        if (x + 1 < _board.length && y - 1 >= 0 && _board[x + 1][y - 1] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x + 1, y - 1, mouvementsRestants - 1))
                return true;

        if (x - 1 >= 0 && y + 1 < _board.length && _board[x - 1][y + 1] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x - 1, y + 1, mouvementsRestants - 1))
                return true;

        if (x - 1 >= 0 && y - 1 >= 0 && _board[x - 1][y - 1] == couleurPions)
            if (atteindrePion(xAAtteindre,yAAtteindre, couleurPions, x - 1, y - 1, mouvementsRestants - 1))
                return true;

        return false;
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

    public void effectuerCoup(Coup coup) {
        int couleurPion = _board[coup.depart.x][coup.depart.y];
        _board[coup.depart.x][coup.depart.y] = CASE_VIDE;
        _board[coup.arrivee.x][coup.arrivee.y] = couleurPion;
        _dernierCoupJoue = coup;
    }

    private int[][] _board;

    private int _profondeur = 1;

    private Coup _dernierCoupJoue;
}
