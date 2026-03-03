package test.java.conway.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import main.java.conway.domain.Grid;
import main.java.conway.domain.IGrid;
import main.java.conway.domain.ICell;

public class GridTest {

    private IGrid g;
    private final int RIGHE = 10;
    private final int COLONNE = 10;
    
    @Before 
    public void setup() {
        // Setup iniziale come nel tuo file
        g = new Grid(RIGHE, COLONNE);
    }
    
    @Test
    public void testDim() {
        // Verifica dimensioni
        assertEquals(RIGHE, g.getRighe());
        assertEquals(COLONNE, g.getColonne());        
    }
    
    @Test
    public void testStatoInizialeCelle() {
        for(int i = 0 ; i < RIGHE ; i++) {
            for(int j = 0 ; j < COLONNE ; j ++) {
                // CORREZIONE: Uso getCellState() per ottenere il boolean
                assertFalse(g.getCellState(i, j));
            }
        }
    }

    @Test
    public void testSetAndGetCellState() {
        // Testiamo il metodo setCell e la lettura dello stato
        g.setCell(5, 5, true);
        assertTrue( g.getCellState(5, 5));
        assertFalse(g.getCellState(0, 0));
    }

    @Test
    public void testGetCellObject() {
        // Verifica che getCell restituisca l'oggetto ICell correttamente
        ICell cell = g.getCell(1, 1);
        assertNotNull("getCell non deve restituire null", cell);
        
        // Se modifico l'oggetto direttamente, la griglia deve rifletterlo
        cell.setStatus(true); //
        assertTrue( g.getCellState(1, 1));
    }

    @Test
    public void testClear() {
        // Popoliamo e poi puliamo
        g.setCell(0, 0, true);
        g.setCell(9, 9, true);
        
        g.clear();
        
        assertFalse(g.getCellState(0, 0));
        assertFalse(g.getCellState(9, 9));
    }
}