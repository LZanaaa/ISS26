package test;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import conway.domain.Life;

public class LifeTest {
	
	Life life;
	private final int ROWS = 10;
	private final int COLS = 10;

	@Before
	public void setup() {
		System.out.println("ConwayLifeTest | setup");	
	    life = new Life(10, 10);
	}
	@After
	public void down() {
		System.out.println("ConwayLifeTest | down");
	}
	
	@Test 
	public void testGetGrid() {
		System.out.println("ConwayLifeTest | testGetGrid");
		//assertNotNull(life.getGrid());
	}
	
	@Test 
	public void testGridDim() {
		System.out.println("ConwayLifeTest | testGridDim");
		//assertEquals(ROWS, life.getGrid().getRighe());
		//assertEquals(COLS, life.getGrid().getColonne());
	}
	
	@Test 
	public void testNextGeneration() {
		System.out.println("ConwayLifeTest | testNextGeneration");
		life.setCell(1, 1, true);
		life.setCell(1, 2, true);
		life.setCell(1, 3, true);
		
		life.nextGeneration();
		
		assertTrue(life.getCellState(0, 2));
		assertTrue(life.getCellState(1, 2));
		assertTrue(life.getCellState(2, 2));
		
	}
	
	@Test
	public void testSetCell() {
		System.out.println("ConwayLifeTest | testSetCell");
		life.setCell(2, 2, true);
		assertTrue(life.getCellState(2, 2));
	}
	
	@Test
	public void testGetCell() {
		System.out.println("ConwayLifeTest | testGetCell");
		life.setCell(3, 3, true);
		assertNotNull(life.getCell(3, 3));
		assertTrue(life.getCell(3, 3).isAlive());
	}
	
	@Test
	public void testReset() {
		System.out.println("ConwayLifeTest | testReset");
		life.setCell(4, 4, true);
		life.reset();
		assertFalse(life.getCellState(4, 4));
	}
	
	@Test 
	public void testGetCellFalse() {
		System.out.println("ConwayLifeTest | testIsDead");
		life.setCell(5, 5, false);
		assertFalse(life.getCellState(5, 5));
	}
	
	//Verifica che il reset riporti tutte le celle a stato morto, 
	//anche quelle che erano vive prima del reset
	@Test 
	public void testStateAfterReset() {
		System.out.println("ConwayLifeTest | testStateAfterReset");
		life.setCell(6, 6, true);
		life.reset();
		assertFalse(life.getCellState(6, 6));
	}
	
	@Test
	public void testGetCellState() {
		System.out.println("ConwayLifeTest | testGetCellState");
		life.setCell(7, 7, true);
		assertTrue(life.getCellState(7, 7));
	}
	

}
