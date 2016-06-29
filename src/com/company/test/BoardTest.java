package com.company.test;

import com.company.Board;
import com.company.Coup;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BoardTest {

    @Test
    public void getNbPionsLigneVerticale() {
        // Arrange
        Board board = new Board(boardDepart);

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
        Board board = new Board(boardDepart);

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
        Board board = new Board(boardDepart);

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
        Board board = new Board(boardDepart);

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
        Board board = new Board(boardDepart);

        // Act
        List<Coup> coupsPossibles = board.getCoupsPossibles(Board.PION_BLANC);

        // Assert
        assertEquals(4, coupsPossibles.size());
    }


    public static int[][] boardDepart = new int[][] {
            {0,2,2,2,2,2,2,0},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {4,0,0,0,0,0,0,4},
            {0,2,2,2,2,2,2,0}};
}
