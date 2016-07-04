package com.company.test;

import com.company.Board;
import com.company.Coup;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void getNbPionsLigneVerticale() {
        // Arrange
        Board board = new Board(_boardDepart, null);

        // Act
        int nbPionsColonne1 = board.getNbPionsLigneVerticale(0);
        int nbPionsColonne2 = board.getNbPionsLigneVerticale(1);

        // Assert
        assertEquals(6, nbPionsColonne1);
        assertEquals(2, nbPionsColonne2);
    }

    @Test
    public void getNbPionsLigneHorizontale() {
        // Arrange
        Board board = new Board(_boardDepart, null);

        // Act
        int nbPionsLigne1 = board.getNbPionsLigneHorizontale(0);
        int nbPionsLigne2 = board.getNbPionsLigneHorizontale(1);

        // Assert
        assertEquals(6, nbPionsLigne1);
        assertEquals(2, nbPionsLigne2);
    }

    @Test
    public void getNbPionsLigneDiagonaleNegative() {
        // Arrange
        Board board = new Board(_boardDepart, null);

        // Act
        int nbPionsDiagonale1 = board.getNbPionsLigneDiagonaleNegative(3, 3);
        int nbPionsDiagonale2 = board.getNbPionsLigneDiagonaleNegative(2, 0);

        // Assert
        assertEquals(0, nbPionsDiagonale1);
        assertEquals(2, nbPionsDiagonale2);
    }

    @Test
    public void getNbPionsLigneDiagonalePositive() {
        // Arrange
        Board board = new Board(_boardDepart, null);

        // Act
        int nbPionsDiagonale1 = board.getNbPionsLigneDiagonalePositive(3, 4);
        int nbPionsDiagonale2 = board.getNbPionsLigneDiagonalePositive(2, 0);

        // Assert
        assertEquals(0, nbPionsDiagonale1);
        assertEquals(2, nbPionsDiagonale2);
    }

    @Test
    public void getCoupsPossibles() {
        // Arrange
        Board board = new Board(_boardCoups, null);

        // Act
        List<Coup> coupsPossiblesBlancs = board.getCoupsPossibles(Board.PION_BLANC);
        List<Coup> coupsPossiblesNoirs = board.getCoupsPossibles(Board.PION_NOIR);

        // Assert
        assertEquals(24, coupsPossiblesBlancs.size());
        assertEquals(24, coupsPossiblesNoirs.size());
    }

    @Test
    public void getBoardsEnfants() {
        // Arrange
        Board board = new Board(_boardCoups, null);

        // Act
        List<Board> boardsEnfantsBlancs = board.getBoardsEnfants(Board.PION_BLANC);
        List<Board> boardsEnfantsNoirs = board.getBoardsEnfants(Board.PION_NOIR);

        // Assert
        assertEquals(24, boardsEnfantsBlancs.size());
        assertEquals(24, boardsEnfantsNoirs.size());

        for (Board boardEnfantBlanc : boardsEnfantsBlancs)
            assertNotNull(boardEnfantBlanc.getDernierCoupJoue());

        for (Board boardEnfantNoir : boardsEnfantsNoirs)
            assertNotNull(boardEnfantNoir.getDernierCoupJoue());
    }

    @Test
    public void pionEstIsole() {
        // Arrange
        Board board = new Board(_boardPionsIsoles, null);

        // Assert

        // Pions isolés
        assertTrue(board.pionEstIsole(1, 0));
        assertTrue(board.pionEstIsole(0, 3));
        assertTrue(board.pionEstIsole(3, 2));

        // Pions non isolés
        assertFalse(board.pionEstIsole(0, 7));
        assertFalse(board.pionEstIsole(3, 5));
        assertFalse(board.pionEstIsole(7, 2));
        assertFalse(board.pionEstIsole(7, 7));

        // Case vide
        assertFalse(board.pionEstIsole(0, 0));
    }

    @Test
    public void calculerValeur() {
        // Arrange
        Board boardDepart = new Board(_boardDepart, null);
        Board boardYolo = new Board(_boardYolo, null);

        // Act
        int valBoardDepart = boardDepart.calculerValeur(Board.PION_BLANC);
        int valBoardYolo = boardYolo.calculerValeur(Board.PION_NOIR);

        // Assert
        assertEquals(0, valBoardDepart);
        assertEquals(1, valBoardYolo);
    }

    @Test
    public void getProchainCoup_BoardDepart() {
        // Arrange
        Board boardDepart = new Board(_boardDepart, null);

        Coup coupBlancBoardDepart = boardDepart.getProchainCoup(Board.PION_BLANC);
        Coup coupNoirBoardDepart = boardDepart.getProchainCoup(Board.PION_NOIR);

        System.out.println(coupBlancBoardDepart);
        System.out.println(coupNoirBoardDepart);
    }

    @Test
    public void getProchainCoup_BoardBlancPresqueGagnant() {
        // Arrange
        Board board = new Board(_boardBlancPresqueGagnant, null);

        // Act
        Board boardGagnant = board.getBoardApresProchainCoup(Board.PION_BLANC);
        Coup coupGagnant = boardGagnant.getDernierCoupJoue();
        System.out.println("Coup gagnant : " + coupGagnant);

        // Assert
        assertTrue("Partie gagnée.", boardGagnant.calculerValeur(Board.PION_BLANC) > 1000);

    }

    private static int[][] _boardBlancPresqueGagnant = new int[][] {
            {0,4,4,0,0,0,2,0},
            {0,0,0,0,4,0,0,0},
            {0,0,0,0,0,2,0,0},
            {0,0,0,0,0,0,2,0},
            {0,0,0,0,0,0,2,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0}};

    private static int[][] _boardDepart = new int[][] {
            {0,2,2,2,2,2,2,0},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {0,2,2,2,2,2,2,0}};

    private static int[][] _boardYolo = new int[][] {
            {0,0,0,4,0,0,0,4},
            {4,0,0,0,0,0,4,0},
            {0,0,0,0,0,0,0,0},
            {0,0,2,0,0,4,0,0},
            {0,0,0,0,0,4,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,2,2,0,0,2,0},
            {0,0,0,0,0,0,0,2}};

    private static int[][] _boardPionsIsoles = new int[][] {
            {0,0,0,4,0,0,0,4},
            {4,0,0,0,0,0,4,0},
            {0,0,0,0,0,0,0,0},
            {0,0,2,0,0,4,0,0},
            {0,0,0,0,0,4,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,2,2,0,0,2,0},
            {0,0,0,0,0,0,0,2}};

    private static int[][] _boardCoups = new int[][] {
            {0,2,2,2,2,2,2,0},
            {4,0,0,4,4,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,2,0,0,0,0,2,4},
            {4,2,0,0,0,0,2,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,4,4,0,0,4},
            {0,2,2,2,2,2,2,0}};
}
