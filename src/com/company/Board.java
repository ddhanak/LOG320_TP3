package com.company;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int CASE_VIDE = 0;
    public static final int PION_NOIR = 2;
    public static final int PION_BLANC = 4;

    public Board(int[][] board) {
        _board = board;
    }

    public Coup getProchainCoup(int couleurEquipe, int profondeur) {
        Coup meilleurCoup = null;
        double meilleurVal = Integer.MIN_VALUE;

        for (Coup coup : getCoupsPossibles(couleurEquipe)) {
            effectuerCoup(coup);
            double val = alphaBeta(profondeur - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, couleurEquipe, false);
            annulerCoup(coup);
            if (val > meilleurVal) {
                meilleurVal = val;
                meilleurCoup = coup;
            }
        }
        return meilleurCoup;
    }

    public double alphaBeta(int profondeur, double a, double b, int couleurEquipe, boolean max) {
        if (profondeur == 0 || estGagnant())
            return evaluer(couleurEquipe, profondeur);

        if (max) {
            double meilleureVal = Integer.MIN_VALUE;
            for (Coup coup : getCoupsPossibles(couleurEquipe)) {
                effectuerCoup(coup);
                double val = alphaBeta(profondeur-1, a, b, couleurEquipe, false);
                annulerCoup(coup);
                if (val > meilleureVal)
                    meilleureVal = val;
                if (meilleureVal > a)
                    a = meilleureVal;
                if (b <= a)
                    break;
            }
            return meilleureVal;
        }
        else { // MIN
            double meilleureVal = Integer.MAX_VALUE;
            for (Coup coup : getCoupsPossibles(getCouleurAdverse(couleurEquipe))) {
                effectuerCoup(coup);
                double val = alphaBeta(profondeur-1, a, b, couleurEquipe, true);
                annulerCoup(coup);
                if (val < meilleureVal)
                    meilleureVal = val;
                if (meilleureVal < b)
                    b = meilleureVal;
                if (b <= a)
                    break;
            }
            return meilleureVal;
        }
    }

    public static int getCouleurAdverse(int couleurEquipe) {
        return (couleurEquipe == PION_BLANC) ? PION_NOIR : PION_BLANC;
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

    public double evaluer(int couleurEquipe, int profondeur) {
        int couleurAdversaire = getCouleurAdverse(couleurEquipe);

        if (estGagnant(couleurEquipe))
            return 100000 + profondeur;
        else if (estGagnant(couleurAdversaire))
            return -(100000 + profondeur);

        double totalXEquipe = 0;
        double totalYEquipe = 0;
        double totalXAdversaire = 0;
        double totalYAdversaire = 0;
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

        double xEquipe = totalXEquipe / nbPionsEquipe;
        double yEquipe = totalYEquipe / nbPionsEquipe;
        double xAdversaire = totalXAdversaire / nbPionsAdversaire;
        double yAdversaire = totalYAdversaire / nbPionsAdversaire;

        double poidsTotalCentralisationEtDispertionEquipe = 0;
        double poidsTotalCentralisationEtDispertionAdversaire = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurEquipe) {
                    poidsTotalCentralisationEtDispertionEquipe += Math.pow(y - yAdversaire, 2) + Math.pow(x - xAdversaire, 2) + Math.pow(y - yEquipe, 2) + Math.pow(x - xEquipe, 2);
                }
                else if (_board[x][y] == couleurAdversaire) {
                    poidsTotalCentralisationEtDispertionAdversaire += Math.pow(y - yEquipe, 2) + Math.pow(x - xEquipe, 2)
                            + Math.pow(y - yAdversaire, 2) + Math.pow(x - xAdversaire, 2);
                }
            }
        }

        double poidsMoyenCentralisationEtDispertionEquipe = poidsTotalCentralisationEtDispertionEquipe / nbPionsEquipe;
        double poidsMoyenCentralisationEtDispertionAdversaire = poidsTotalCentralisationEtDispertionAdversaire / nbPionsAdversaire;

        double isolementEquipe = calculerIsolement(couleurEquipe) / nbPionsEquipe;
        double isolementAdversaire = calculerIsolement(couleurAdversaire) / nbPionsAdversaire;

        double connectiviteEquipe = calculerConnectivite(couleurEquipe, nbPionsEquipe);
        double connectiviteAdversaire = calculerConnectivite(couleurAdversaire, nbPionsAdversaire);

        double valeurEquipe = 10000 + connectiviteEquipe - poidsMoyenCentralisationEtDispertionEquipe - isolementEquipe;
        double valeurAdversaire = 10000 + connectiviteAdversaire - poidsMoyenCentralisationEtDispertionAdversaire - isolementAdversaire;

        return valeurEquipe - valeurAdversaire;
    }

    public double calculerConnectivite(int couleurPions, int nbPions) {
        double nbConnexions = 0;

        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurPions) {
                    int xDepart = x;
                    int yDepart = y;

                    for (int x2 = 0; x2 != _board.length; x2++) {
                        for (int y2 = 0; y2 != _board.length; y2++) {
                            if (_board[x2][y2] == couleurPions) {
                                if (atteindrePion(x, y, couleurPions, xDepart, yDepart, nbPions - 1))
                                    nbConnexions++;
                            }
                        }
                    }
                }
            }
        }

        return nbConnexions / (nbPions);
    }

    public int calculerIsolement(int couleurEquipe) {
        int isolement = 0;
        for (int x = 0; x != _board.length; x++) {
            for (int y = 0; y != _board.length; y++) {
                if (_board[x][y] == couleurEquipe) {
                    isolement += calculerIsolement(x, y, couleurEquipe);
                }
            }
        }
        return isolement;
    }

    private int calculerIsolement(int x, int y, int couleurEquipe) {
        int isolement = 0;
        int couleurAdverse = getCouleurAdverse(couleurEquipe);
        boolean estIsole = false;

        // Isolement vertical
        for (int i = 1; i <= 7; i++) {
            int xPlus = x + i;
            if (xPlus < _board.length) {
                if (!estIsole && _board[xPlus][y] == couleurAdverse)
                    estIsole = true;
                else if (_board[xPlus][y] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        for (int i = 1; i <= 7; i++) {
            int xMoins = x - i;
            if (xMoins >= 0) {
                if (!estIsole && _board[xMoins][y] == couleurAdverse)
                    estIsole = true;
                else if (_board[xMoins][y] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        // Isolement horizontal
        for (int i = 1; i <= 7; i++) {
            int yPlus = y + i;
            if (yPlus < _board.length) {
                if (!estIsole && _board[x][yPlus] == couleurAdverse)
                    estIsole = true;
                else if (_board[x][yPlus] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        for (int i = 1; i <= 7; i++) {
            int yMoins = y - i;
            if (yMoins >= 0) {
                if (!estIsole && _board[x][yMoins] == couleurAdverse)
                    estIsole = true;
                else if (_board[x][yMoins] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        // Isolement diagonale positive
        for (int i = 1; i <= 7; i++) {
            int yPlus = y + i;
            int xMoins = x - i;
            if (yPlus < _board.length && xMoins >= 0) {
                if (!estIsole && _board[xMoins][yPlus] == couleurAdverse)
                    estIsole = true;
                else if (_board[xMoins][yPlus] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        for (int i = 1; i <= 7; i++) {
            int yMoins = y - i;
            int xPlus = x + i;
            if (yMoins >= 0 && xPlus < _board.length) {
                if (!estIsole && _board[xPlus][yMoins] == couleurAdverse)
                    estIsole = true;
                else if (_board[xPlus][yMoins] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        // Isolement diagonale negative
        for (int i = 1; i <= 7; i++) {
            int yPlus = y + i;
            int xPlus = x + i;
            if (yPlus < _board.length && xPlus < _board.length) {
                if (!estIsole && _board[xPlus][yPlus] == couleurAdverse)
                    estIsole = true;
                else if (_board[xPlus][yPlus] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        for (int i = 1; i <= 7; i++) {
            int yMoins = y - i;
            int xMoins = x - i;
            if (yMoins >= 0 && xMoins >= 0) {
                if (!estIsole && _board[xMoins][yMoins] == couleurAdverse)
                    estIsole = true;
                else if (_board[xMoins][yMoins] == couleurEquipe)
                    break;
            }
            else
                break;
        }
        if (estIsole) {
            isolement++;
            estIsole = false;
        }

        return isolement;
    }

    public boolean estGagnant() {
        return estGagnant(PION_BLANC) || estGagnant(PION_NOIR);
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

    public void effectuerCoup(Coup coup) {
        int couleurPion = _board[coup.depart.x][coup.depart.y];
        _board[coup.depart.x][coup.depart.y] = CASE_VIDE;
        int caseArrivee = _board[coup.arrivee.x][coup.arrivee.y];
        if (caseArrivee != CASE_VIDE) {
            coup.aMangeUnPion = true;
            coup.couleurPionMange = caseArrivee;
        }
        _board[coup.arrivee.x][coup.arrivee.y] = couleurPion;
    }

    public void annulerCoup(Coup coup) {
        _board[coup.depart.x][coup.depart.y] = _board[coup.arrivee.x][coup.arrivee.y];
        if (coup.aMangeUnPion)
            _board[coup.arrivee.x][coup.arrivee.y] = coup.couleurPionMange;
        else
            _board[coup.arrivee.x][coup.arrivee.y] = CASE_VIDE;
    }

    public int[][] getBoard() {
        return _board;
    }

    public Board copier() {
        int[][] nouveauBoard = new int[8][8];

        for (int x = 0; x < nouveauBoard.length; x++)
            for (int y = 0; y < nouveauBoard.length; y++)
                nouveauBoard[x][y] = this._board[x][y];

        return new Board(nouveauBoard);
    }

    private int[][] _board;
}
