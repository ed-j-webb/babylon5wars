package test.b5w.cases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import aog.b5w.components.Component;
import aog.b5w.components.Structure;

public class ToHitTest extends AbstractTest {

	public void checkType(List<Component> list, int expectedSize, String expectedAttribute) {
		assertEquals(expectedSize, list.size()); 
		for (int i = 0; i < list.size(); i++) {
			assertEquals(true, list.get(i).hasAttribute(expectedAttribute));
		}
		
	}
	
	@Test
	public void testForeHit() {
		Structure fore = craft.getStructure(FORE);
		List<List<Component>> toHit = fore.getToHit();
		
		assertEquals(21, toHit.size());
		
		// Retro
		assertEquals(toHit.get(1), toHit.get(2));
		assertEquals(toHit.get(1), toHit.get(3));
		checkType(toHit.get(1), 2, "Thruster");
		
		// Laser Cannon
		assertEquals(toHit.get(4), toHit.get(5));
		assertEquals(toHit.get(4), toHit.get(6));
		checkType(toHit.get(4), 2, "Laser");

		// Pulse Cannon
		assertEquals(toHit.get(7), toHit.get(8));
		checkType(toHit.get(7), 2, "Pulse");
		
		// Interceptor
		assertEquals(toHit.get(9), toHit.get(10));
		assertEquals(toHit.get(9), toHit.get(11));
		checkType(toHit.get(9), 2, "Interceptor");

		// Structure
		assertEquals(toHit.get(12), toHit.get(13));
		assertEquals(toHit.get(12), toHit.get(14));
		assertEquals(toHit.get(12), toHit.get(15));
		assertEquals(toHit.get(12), toHit.get(16));
		assertEquals(toHit.get(12), toHit.get(17));
		assertEquals(toHit.get(12), toHit.get(18));
		checkType(toHit.get(12), 1, "Fore");

		// Primary
		assertEquals(toHit.get(19), toHit.get(20));
		checkType(toHit.get(19), 1, "Primary");

	}

	@Test
	public void testStarboardHit() {
		Structure stbd = craft.getStructure(STARBOARD);
		List<List<Component>> toHit = stbd.getToHit();
		
		assertEquals(21, toHit.size());
		
		// Thruster
		assertEquals(toHit.get(1), toHit.get(2));
		assertEquals(toHit.get(1), toHit.get(3));
		assertEquals(toHit.get(1), toHit.get(4));
		checkType(toHit.get(1), 1, "Thruster");
		
		// Particle Beam
		assertEquals(toHit.get(5), toHit.get(6));
		assertEquals(toHit.get(5), toHit.get(7));
		assertEquals(toHit.get(5), toHit.get(8));
		assertEquals(toHit.get(5), toHit.get(9));
		checkType(toHit.get(5), 6, "Particle");

		// Interceptor
		assertEquals(toHit.get(10), toHit.get(11));
		assertEquals(toHit.get(10), toHit.get(12));
		checkType(toHit.get(10), 1, "Interceptor");
		
		// Structure
		assertEquals(toHit.get(13), toHit.get(14));
		assertEquals(toHit.get(13), toHit.get(15));
		assertEquals(toHit.get(13), toHit.get(16));
		assertEquals(toHit.get(13), toHit.get(17));
		assertEquals(toHit.get(13), toHit.get(18));
		checkType(toHit.get(13), 1, "Starboard");

		// Primary
		assertEquals(toHit.get(19), toHit.get(20));
		checkType(toHit.get(19), 1, "Primary");
	}
	
	@Test
	public void testPortHit() {
		Structure port = craft.getStructure(PORT);
		List<List<Component>> toHit = port.getToHit();
		
		assertEquals(21, toHit.size());
		
		// Thruster
		assertEquals(toHit.get(1), toHit.get(2));
		assertEquals(toHit.get(1), toHit.get(3));
		assertEquals(toHit.get(1), toHit.get(4));
		checkType(toHit.get(1), 1, "Thruster");
		
		// Particle Beam
		assertEquals(toHit.get(5), toHit.get(6));
		assertEquals(toHit.get(5), toHit.get(7));
		assertEquals(toHit.get(5), toHit.get(8));
		assertEquals(toHit.get(5), toHit.get(9));
		checkType(toHit.get(5), 6, "Particle");

		// Interceptor
		assertEquals(toHit.get(10), toHit.get(11));
		assertEquals(toHit.get(10), toHit.get(12));
		checkType(toHit.get(10), 1, "Interceptor");
		
		// Structure
		assertEquals(toHit.get(13), toHit.get(14));
		assertEquals(toHit.get(13), toHit.get(15));
		assertEquals(toHit.get(13), toHit.get(16));
		assertEquals(toHit.get(13), toHit.get(17));
		assertEquals(toHit.get(13), toHit.get(18));
		checkType(toHit.get(13), 1, "Port");

		// Primary
		assertEquals(toHit.get(19), toHit.get(20));
		checkType(toHit.get(19), 1, "Primary");
	}

	@Test
	public void testAftHit() {
		Structure aft = craft.getStructure(AFT);
		List<List<Component>> toHit = aft.getToHit();
		
		assertEquals(21, toHit.size());
		
		// Thruster
		assertEquals(toHit.get(1), toHit.get(2));
		assertEquals(toHit.get(1), toHit.get(3));
		assertEquals(toHit.get(1), toHit.get(4));
		assertEquals(toHit.get(1), toHit.get(5));
		assertEquals(toHit.get(1), toHit.get(6));
		checkType(toHit.get(1), 4, "Thruster");
		
		// Laser Cannon
		assertEquals(toHit.get(7), toHit.get(8));
		assertEquals(toHit.get(7), toHit.get(9));
		checkType(toHit.get(7), 2, "Laser");

		// Interceptor
		assertEquals(toHit.get(10), toHit.get(11));
		assertEquals(toHit.get(10), toHit.get(12));
		checkType(toHit.get(10), 2, "Interceptor");
		
		// Structure
		assertEquals(toHit.get(13), toHit.get(14));
		assertEquals(toHit.get(13), toHit.get(15));
		assertEquals(toHit.get(13), toHit.get(16));
		assertEquals(toHit.get(13), toHit.get(17));
		assertEquals(toHit.get(13), toHit.get(18));
		checkType(toHit.get(13), 1, "Aft");

		// Primary
		assertEquals(toHit.get(19), toHit.get(20));
		checkType(toHit.get(19), 1, "Primary");
	}

	@Test
	public void testPrimaryHit() {
		Structure primary = craft.getStructure(PRIMARY);
		List<List<Component>> toHit = primary.getToHit();
		
		assertEquals(21, toHit.size());
		
		// Structure
		assertEquals(toHit.get(1), toHit.get(2));
		assertEquals(toHit.get(1), toHit.get(3));
		assertEquals(toHit.get(1), toHit.get(4));
		assertEquals(toHit.get(1), toHit.get(5));
		assertEquals(toHit.get(1), toHit.get(6));
		assertEquals(toHit.get(1), toHit.get(7));
		assertEquals(toHit.get(1), toHit.get(8));
		checkType(toHit.get(1), 1, "Primary");
		
		// Jump Engine
		assertEquals(toHit.get(9), toHit.get(10));
		checkType(toHit.get(9), 1, "Jump Engine");
		
		// Sensors
		assertEquals(toHit.get(11), toHit.get(12));
		assertEquals(toHit.get(11), toHit.get(13));
		assertEquals(toHit.get(11), toHit.get(14));
		checkType(toHit.get(11), 1, "Sensors");

		// Engine
		assertEquals(toHit.get(15), toHit.get(16));
		checkType(toHit.get(15), 1, "Engine");

		// Hangar
		assertEquals(toHit.get(17), toHit.get(18));
		checkType(toHit.get(17), 1, "Hangar");

		// Reactor
		checkType(toHit.get(19), 1, "Reactor");

		// CnC
		//checkType(toHit.get(20), 1, "CnC");

	}
	
}
