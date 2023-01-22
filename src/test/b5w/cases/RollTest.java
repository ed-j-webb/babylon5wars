package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.exception.PhaseException;

public class RollTest extends AbstractMoveTest {

	@Test
	public void testAftRoll() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			thruster.thrust(3, ROLL);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can roll in move phase");
		}
		
		assertEquals(7, thruster.getCurrentThrust());
		assertEquals(0, craft.getX());
		assertEquals(0, craft.getY());
		assertEquals(4, craft.getMoves());
		assertEquals(ACTIVE, craft.getRolling());
	}
	
	@Test
	public void testForeRoll() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			retroThruster.thrust(3, ROLL);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can roll in move phase");
		}
		
		assertEquals(3, retroThruster.getCurrentThrust());
		assertEquals(0, craft.getX());
		assertEquals(0, craft.getY());
		assertEquals(4, craft.getMoves());
		assertEquals(ACTIVE, craft.getRolling());
	}

	@Test
	public void testPortRoll() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			portThruster.thrust(3, ROLL);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can roll in move phase");
		}
		
		assertEquals(3, portThruster.getCurrentThrust());
		assertEquals(0, craft.getX());
		assertEquals(0, craft.getY());
		assertEquals(4, craft.getMoves());
		assertEquals(ACTIVE, craft.getRolling());
	}

	@Test
	public void testStarboardRoll() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			stbdThruster.thrust(3, ROLL);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can roll in move phase");
		}
		
		assertEquals(3, stbdThruster.getCurrentThrust());
		assertEquals(0, craft.getX());
		assertEquals(0, craft.getY());
		assertEquals(4, craft.getMoves());
		assertEquals(ACTIVE, craft.getRolling());
	}

	@Test
	public void testRollover() {
		testAftRoll();
		Weapon w = (Weapon)craft.getStructure(FORE).getSystem("Fore Hvy Laser Cannon 1");
		
		assertEquals(10, w.getArcStart());
		assertEquals(0, w.getArcFinish());
		
		advancePhase(10);
		
		assertEquals("Port", craft.getStructure(STARBOARD).getName());
		assertEquals("Starboard", craft.getStructure(PORT).getName());
		assertEquals(0, w.getArcStart());
		assertEquals(2, w.getArcFinish());
	}
	
	@Test 
	public void testRollback() {
		testRollover();
		Weapon w = (Weapon)craft.getStructure(FORE).getSystem("Fore Hvy Laser Cannon 1");
		
		assertEquals(0, w.getArcStart());
		assertEquals(2, w.getArcFinish());
		
		advancePhase(10);
		
		assertEquals("Starboard", craft.getStructure(STARBOARD).getName());
		assertEquals("Port", craft.getStructure(PORT).getName());
		assertEquals(10, w.getArcStart());
		assertEquals(0, w.getArcFinish());
	}
	
	@Test
	public void testStopRoll() {
		testRollover();
		Weapon w = (Weapon)craft.getStructure(FORE).getSystem("Fore Hvy Laser Cannon 1");
		
		assertEquals(ACTIVE, craft.getRolling());
		
		try {
			thruster.thrust(2, ROLL);
			retroThruster.thrust(2, ROLL);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can roll in move phase");
		}
		
		assertEquals(FINISHED, craft.getRolling());
		assertEquals(2, thruster.getCurrentThrust());
		assertEquals(2, retroThruster.getCurrentThrust());

		
		assertEquals(0, w.getArcStart());
		assertEquals(2, w.getArcFinish());
		assertEquals("Port", craft.getStructure(STARBOARD).getName());
		assertEquals("Starboard", craft.getStructure(PORT).getName());

		advancePhase(10);
		
		assertEquals(NONE, craft.getRolling());

		assertEquals(0, w.getArcStart());
		assertEquals(2, w.getArcFinish());
		assertEquals("Port", craft.getStructure(STARBOARD).getName());
		assertEquals("Starboard", craft.getStructure(PORT).getName());
	}
}

