package domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MathUtilsTest {
	
	@Before
	public void setUp() {
		System.out.println("Setting up test environment...");
	}
	
	@After
	public void down() {
		System.out.println("Cleaning up test environment...");
	}

	@Test
	public void testCalculatePositive() {
		double x = 1.0;
		
		double expected = Math.sin(x) + Math.cos(Math.sqrt(3) * x);
		
		double result = MathUtils.calculate(x);
		
		assertEquals(expected, result, 0.0001);
	}

	@Test
    public void testCalculateZero() {
        double x = 0.0;
        
        double expected = 1.0;
        
        double result = MathUtils.calculate(x);
        
        assertEquals(expected, result, 0.0001);
    }

}
