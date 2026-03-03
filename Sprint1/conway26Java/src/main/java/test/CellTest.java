package test.java.conway.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.java.conway.domain.Cell;
import main.java.conway.domain.ICell;

class CellTest {

    @Test
    void testStatoIniziale() {
        ICell cell = new Cell();
        // Una cella appena creata deve essere morta
        assertFalse(cell.isAlive());
    }

    @Test
    void testCambioStato() {
        ICell cell = new Cell();
        
        // Test attivazione
        cell.setStatus(true); //
        assertTrue(cell.isAlive());
        
        // Test disattivazione
        cell.setStatus(false); //
        assertFalse(cell.isAlive());
    }
}