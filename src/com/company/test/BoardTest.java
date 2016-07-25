package com.company.test;

import com.company.Board;
import com.company.Coup;
import com.company.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Before
    public void initialiser() {
        _boardCoups = new int[][] {
                {0,2,2,2,2,2,2,0},
                {4,0,0,4,4,0,0,4},
                {4,0,0,0,0,0,0,4},
                {4,2,0,0,0,0,2,4},
                {4,2,0,0,0,0,2,4},
                {4,0,0,0,0,0,0,4},
                {4,0,0,4,4,0,0,4},
                {0,2,2,2,2,2,2,0}};

        _boardGagnant = new int[][] {
                {0,4,4,0,0,0,0,0},
                {0,0,0,4,0,0,0,0},
                {0,0,0,0,0,2,0,0},
                {0,0,0,0,0,0,2,0},
                {0,0,0,0,0,0,2,0},
                {0,0,0,0,0,0,2,0},
                {0,0,0,0,0,2,0,0},
                {0,0,0,0,0,0,0,0}};

        _boardBlancPresqueGagnant = new int[][] {
                {0,4,4,0,0,0,2,0},
                {0,0,0,0,4,0,0,0},
                {0,0,0,0,0,2,0,0},
                {0,0,0,0,0,0,2,0},
                {0,0,0,0,0,0,2,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}};

        _boardDepart = new int[][] {
                {0,2,2,2,2,2,2,0},
                {4,0,0,0,0,0,0,4},
                {4,0,0,0,0,0,0,4},
                {4,0,0,0,0,0,0,4},
                {4,0,0,0,0,0,0,4},
                {4,0,0,0,0,0,0,4},
                {4,0,0,0,0,0,0,4},
                {0,2,2,2,2,2,2,0}};

        _boardYolo = new int[][] {
                {0,0,0,4,0,0,0,4},
                {4,0,0,0,0,0,4,0},
                {0,0,0,0,0,0,0,0},
                {0,0,2,0,0,4,0,0},
                {0,0,0,0,0,4,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,2,2,0,0,2,0},
                {0,0,0,0,0,0,0,2}};

        _boardPionsIsoles = new int[][] {
                {0,0,0,4,0,0,0,4},
                {4,0,0,0,0,0,4,0},
                {0,0,0,0,0,0,0,0},
                {0,0,2,0,0,4,0,0},
                {0,0,0,0,0,4,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,2,2,0,0,2,0},
                {0,0,0,0,0,0,0,2}};
    }

    @Test
    public void getNbPionsLigneVerticale() {
        // Arrange
        Board board = new Board(_boardDepart);

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
        Board board = new Board(_boardDepart);

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
        Board board = new Board(_boardDepart);

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
        Board board = new Board(_boardDepart);

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
        Board board = new Board(_boardCoups);

        // Act
        List<Coup> coupsPossiblesBlancs = board.getCoupsPossibles(Board.PION_BLANC);
        List<Coup> coupsPossiblesNoirs = board.getCoupsPossibles(Board.PION_NOIR);

        // Assert
        assertEquals(24, coupsPossiblesBlancs.size());
        assertEquals(24, coupsPossiblesNoirs.size());
    }

    @Test
    public void calculerValeur() {
        // Arrange
        Board boardDepart = new Board(_boardDepart);
        Board boardYolo = new Board(_boardYolo);
        Board boardGagnant = new Board(_boardGagnant);
        int profondeur = 1;

        // Act
        double valBoardDepart = boardDepart.evaluer(Board.PION_BLANC, 0);
        double valBoardYolo = boardYolo.evaluer(Board.PION_NOIR, 0);
        double valBoardGagnant = boardGagnant.evaluer(Board.PION_BLANC, 0);

        System.out.println("Valeur blancs board départ : " + valBoardDepart);
        System.out.println("Valeur noirs board yolo : " + valBoardYolo);
        System.out.println("Valeur blancs board gagnant : " + valBoardGagnant);
    }

    @Test
    public void estGagnant_BoardGagnant() {
        // Arrange
        Board board = new Board(_boardGagnant);

        // Act
        boolean blancGagnant = board.estGagnant(Board.PION_BLANC);
        boolean noirGagnant = board.estGagnant(Board.PION_NOIR);

        // Assert
        assertTrue(blancGagnant);
        assertTrue(noirGagnant);
    }

    @Test
    public void estGagnant_BoardPerdant() {
        // Arrange
        Board board = new Board(_boardDepart);

        // Act
        boolean blancGagnant = board.estGagnant(Board.PION_BLANC);
        boolean noirGagnant = board.estGagnant(Board.PION_NOIR);

        // Assert
        assertFalse(blancGagnant);
        assertFalse(noirGagnant);
    }

    @Test
    public void getCouleurAdverse() {
        assertEquals(Board.PION_BLANC, Board.getCouleurAdverse(Board.PION_NOIR));
        assertEquals(Board.PION_NOIR, Board.getCouleurAdverse(Board.PION_BLANC));
    }

    @Test
    public void effectuerCoup_PionMange() {
        // Arrange
        Board board = new Board(_boardCoups);
        Coup coup = new Coup(new Position(0, 5), new Position(2, 7));

        // Act
        board.effectuerCoup(coup);
        int[][] grid = board.getBoard();

        // Assert
        assertTrue(coup.aMangeUnPion);
        assertEquals(Board.PION_BLANC, coup.couleurPionMange);
        assertEquals(Board.CASE_VIDE, grid[coup.depart.x][coup.depart.y]);
        assertEquals(Board.PION_NOIR, grid[coup.arrivee.x][coup.arrivee.y]);
    }

    @Test
    public void annulerCoup_PionMange() {
        // Arrange
        Board board = new Board(_boardCoups);
        Board boardInitial = board.copier();
        Coup coup = new Coup(new Position(0, 5), new Position(2, 7));

        // Act
        board.effectuerCoup(coup);
        board.annulerCoup(coup);
        int[][] grid = board.getBoard();
        int[][] gridInitiale = boardInitial.getBoard();

        // Assert
        for (int x = 0; x < _boardCoups.length; x++)
            for (int y = 0; y < _boardCoups.length; y++)
                assertEquals(gridInitiale[x][y], grid[x][y]);
    }

    @Test
    public void effectuerCoup_PasDePionMange() {
        // Arrange
        Board board = new Board(_boardCoups);
        Coup coup = new Coup(new Position(2, 0), new Position(2, 2));

        // Act
        board.effectuerCoup(coup);
        int[][] grid = board.getBoard();

        // Assert
        assertFalse(coup.aMangeUnPion);
        assertEquals(Board.CASE_VIDE, grid[coup.depart.x][coup.depart.y]);
        assertEquals(Board.PION_BLANC, grid[coup.arrivee.x][coup.arrivee.y]);
    }

    @Test
    public void annulerCoup_PasDePionMange() {
        // Arrange
        Board board = new Board(_boardCoups);
        Board boardInitial = board.copier();
        Coup coup = new Coup(new Position(2, 0), new Position(2, 2));

        // Act
        board.effectuerCoup(coup);
        board.annulerCoup(coup);
        int[][] grid = board.getBoard();
        int[][] gridInitiale = boardInitial.getBoard();

        // Assert
        for (int x = 0; x < _boardCoups.length; x++)
            for (int y = 0; y < _boardCoups.length; y++)
                assertEquals(gridInitiale[x][y], grid[x][y]);
    }

    @Test
    public void getProchainCoup_BoardBlancPresqueGagnant() {
        // Arrange
        Board board = new Board(_boardBlancPresqueGagnant);

        // Act
        Coup coupGagnant = board.getProchainCoup(Board.PION_BLANC, 2);
        System.out.println("Coup gagnant : " + coupGagnant);
        board.effectuerCoup(coupGagnant);

        // Assert
        assertTrue("Partie gagnée.", board.estGagnant(Board.PION_BLANC));

    }


    private static int[][] _boardCoups;

    private static int[][] _boardGagnant;

    private static int[][] _boardBlancPresqueGagnant;

    private static int[][] _boardDepart;

    private static int[][] _boardYolo;

    private static int[][] _boardPionsIsoles;
}
