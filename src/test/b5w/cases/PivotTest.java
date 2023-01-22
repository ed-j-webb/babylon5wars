package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.exception.PhaseException;

public class PivotTest extends AbstractMoveTest {
	
	@Test
	public void testPivotClockwiseStbdAft() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			stbdThruster.thrust(1, PIVOT_CLOCKWISE);
			thruster.thrust(1, PIVOT_CLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can pivot in move phase");
		}
		
		assertEquals(1, stbdThruster.getCurrentThrust());
		assertEquals(5, thruster.getCurrentThrust());
		assertEquals(2, engine.getCurrentThrust());
		assertEquals(2, craft.getDirectionOfFacing());
		assertEquals(0, craft.getDirectionOfTravel());
		assertEquals(CLOCKWISE, craft.getPivoting());
	}

	@Test
	public void testPivotAntiClockwiseStbdFore() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			stbdThruster.thrust(1, PIVOT_ANTICLOCKWISE);
			retroThruster.thrust(1, PIVOT_ANTICLOCKWISE);
		} catch (PhaseException e) {
			fail("Can pivot in move phase");
		}
		
		assertEquals(1, stbdThruster.getCurrentThrust());
		assertEquals(1, retroThruster.getCurrentThrust());
		assertEquals(2, engine.getCurrentThrust());
		assertEquals(10, craft.getDirectionOfFacing());
		assertEquals(0, craft.getDirectionOfTravel());
		assertEquals(ANTICLOCKWISE, craft.getPivoting());
	}

	@Test
	public void testPivotClockwisePortFore() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			portThruster.thrust(1, PIVOT_CLOCKWISE);
			retroThruster.thrust(1, PIVOT_CLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can pivot in move phase");
		}
		
		assertEquals(1, portThruster.getCurrentThrust());
		assertEquals(1, retroThruster.getCurrentThrust());
		assertEquals(2, engine.getCurrentThrust());
		assertEquals(2, craft.getDirectionOfFacing());
		assertEquals(0, craft.getDirectionOfTravel());
		assertEquals(CLOCKWISE, craft.getPivoting());
	}

	@Test
	public void testPivotAntiClockwisePortAft() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			portThruster.thrust(1, PIVOT_ANTICLOCKWISE);
			thruster.thrust(1, PIVOT_ANTICLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can pivot in move phase");
		}
		
		assertEquals(1, portThruster.getCurrentThrust());
		assertEquals(5, thruster.getCurrentThrust());
		assertEquals(2, engine.getCurrentThrust());
		assertEquals(10, craft.getDirectionOfFacing());
		assertEquals(0, craft.getDirectionOfTravel());
		assertEquals(ANTICLOCKWISE, craft.getPivoting());
	}

	@Test
	public void testPivot180() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());
		
		try {
			portThruster.thrust(1, PIVOT_ANTICLOCKWISE);
			thruster.thrust(1, PIVOT_ANTICLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can pivot in move phase");
		}
		
		assertEquals(ANTICLOCKWISE, craft.getPivoting());
		assertEquals(1, portThruster.getCurrentThrust());
		assertEquals(5, thruster.getCurrentThrust());
		assertEquals(2, engine.getCurrentThrust());
		assertEquals(10, craft.getDirectionOfFacing());
		assertEquals(0, craft.getDirectionOfTravel());
		assertEquals(ANTICLOCKWISE, craft.getPivoting());

		advancePhase(10);

		assertEquals(8, craft.getDirectionOfFacing());
		assertEquals(0, craft.getDirectionOfTravel());
		assertEquals(4, craft.getVelocity());

		advancePhase(10);

		assertEquals(6, craft.getDirectionOfFacing());
		assertEquals(6, craft.getDirectionOfTravel());
		assertEquals(-4, craft.getVelocity());

		try {
			portThruster.thrust(1, PIVOT_CLOCKWISE);
			retroThruster.thrust(1, PIVOT_CLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can pivot in move phase");
		}
	
		assertEquals(FINISHED, craft.getPivoting());
	}
}
