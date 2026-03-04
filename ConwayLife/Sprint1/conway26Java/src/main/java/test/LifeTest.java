package main.java.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import main.java.conway.domain.Cell;
import main.java.conway.domain.Life;

class LifeTest {
	
	Life life;

	@Before
	public void setup() {
		System.out.println("ConwayLifeTest | setup");	
	    life = new Life();
	}
	@After
	public void down() {
		System.out.println("ConwayLifeTest | down");
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
