package com.company.test;

import com.company.Board;
import com.company.Coup;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void getNbPionsLigneVerticale() {
        // Arrange
        Board board = new Board(boardDepart, null);

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
        Board board = new Board(boardDepart, null);

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
        Board board = new Board(boardDepart, null);

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
        Board board = new Board(boardDepart, null);

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
        Board board = new Board(board2, null);

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
        Board board = new Board(board2, null);

        // Act
        List<Board> boardsEnfantsBlancs = board.getBoardsEnfants(Board.PION_BLANC);
        List<Board> boardsEnfantsNoirs = board.getBoardsEnfants(Board.PION_NOIR);

        // Assert
        assertEquals(24, boardsEnfantsBlancs.size());
        assertEquals(24, boardsEnfantsNoirs.size());

        for (Board boardEnfantBlanc : boardsEnfantsBlancs)
            assertTrue(boardEnfantBlanc.getDernierCoupJoue() != null);

        for (Board boardEnfantNoir : boardsEnfantsNoirs)
            assertTrue(boardEnfantNoir.getDernierCoupJoue() != null);
    }

    private static int[][] board2 = new int[][] {
            {0,2,2,2,2,2,2,0},
            {4,0,0,4,4,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,2,0,0,0,0,2,4},
            {4,2,0,0,0,0,2,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,4,4,0,0,4},
            {0,2,2,2,2,2,2,0}};


    private static int[][] boardDepart = new int[][] {
            {0,2,2,2,2,2,2,0},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {0,2,2,2,2,2,2,0}};
}
