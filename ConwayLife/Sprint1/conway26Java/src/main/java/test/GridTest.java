package main.java.test;

import static org.junit.Assert.*;

import org.junit.After;
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
        // Setup 
        g = new Grid(RIGHE, COLONNE);
    }
    
    @After
	public void down() {
    	System.out.println("GridTest | down");
    }
    
    @Test
    public void testDim() {
        // Verifica dimensioni
        assertEquals(RIGHE, g.getRighe());
        assertEquals(COLONNE, g.getColonne());        
    }
    
    @Test
    public void testStatoInizialeCelle() {
    	
    	//Devo verificare che tutte le celle siano inizialmente morte
    	
        for(int i = 0 ; i < RIGHE ; i++) {
            for(int j = 0 ; j < COLONNE ; j ++) {
                assertFalse(g.getCellState(i, j));
            }
        }
    }

    @Test
    public void testSetAndGetCellState() {
    	
		// Imposto una cella a vivo e verifico che valga effettuvamente true (vero)
        g.setCell(5, 5, true);
        assertTrue( g.getCellState(5, 5));

    }

    @Test
    public void testGetCellObject() {

        ICell cell = g.getCell(1, 1);
        assertNotNull(cell);
        
        // Se modifico l'oggetto direttamente, la griglia deve rifletterlo
        cell.setStatus(true); //
        assertTrue( g.getCellState(1, 1));
    }

    @Test
    public void testReset() {
        // Imposto alcune celle a vivo
        g.setCell(0, 2, true);
        g.setCell(9, 11, true);
        
        g.reset();
        
        // Dopo il reset, tutte le celle devono essere morte
        assertFalse(g.getCellState(0, 2));
        assertFalse(g.getCellState(9, 11));
    }
}