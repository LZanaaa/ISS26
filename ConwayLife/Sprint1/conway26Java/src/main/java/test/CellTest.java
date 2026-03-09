package main.java.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.*;
import main.java.conway.domain.Cell; 
import main.java.conway.domain.ICell;

public class CellTest {
	
	ICell cell;
	
	@Before
	public void setup() {
		System.out.println("ConwayLifeTest | setup");	
	    cell = new Cell();
	}
	@After
	public void down() {
		System.out.println("ConwayLifeTest | down");
	}

    @Test
    public void testStatoIniziale() {
        ICell cell = new Cell();
        // Una cella appena creata deve essere morta
        assertFalse(cell.isAlive());
    }

    @Test
    public void testCambioStato() {
        ICell cell = new Cell();
        
        // Test attivazione
        cell.setStatus(true); 
        assertTrue(cell.isAlive());
        
        // Test disattivazione
        cell.setStatus(false); //
        assertFalse(cell.isAlive());
    }
}