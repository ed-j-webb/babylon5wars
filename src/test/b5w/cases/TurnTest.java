package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.exception.PhaseException;

public class TurnTest extends AbstractMoveTest {

	@Test
	public void testTurnFwdClockwise() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			thruster.thrust(2, TURN_CLOCKWISE);
			portThruster.thrust(2, TURN_CLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can turn in move phase");
		}

		 assertEquals(6, thruster.getCurrentThrust());
		 assertEquals(2, portThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(2, craft.getDirectionOfFacing());
		 assertEquals(2, craft.getDirectionOfTravel());
		 assertEquals(4, craft.getTurnWait());
	}

	@Test
	public void testTurnFwdAntiClockwise() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			thruster.thrust(2, TURN_ANTICLOCKWISE);
			stbdThruster.thrust(2, TURN_ANTICLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can turn in move phase");
		}

		 assertEquals(6, thruster.getCurrentThrust());
		 assertEquals(2, stbdThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(10, craft.getDirectionOfFacing());
		 assertEquals(10, craft.getDirectionOfTravel());
		 assertEquals(4, craft.getTurnWait());
	}

	@Test
	public void testTurnRevClockwise() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		try {
			retroThruster.thrust(2, TURN_CLOCKWISE);
			stbdThruster.thrust(2, TURN_CLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can turn in move phase");
		}

		 assertEquals(6, retroThruster.getCurrentThrust());
		 assertEquals(2, stbdThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(2, craft.getDirectionOfFacing());
		 assertEquals(2, craft.getDirectionOfTravel());
		 assertEquals(4, craft.getTurnWait());
	}

	@Test
	public void testTurnRevAntiClockwise() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		try {
			retroThruster.thrust(2, TURN_ANTICLOCKWISE);
			portThruster.thrust(2, TURN_ANTICLOCKWISE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can turn in move phase");
		}

		 assertEquals(6, retroThruster.getCurrentThrust());
		 assertEquals(2, portThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(10, craft.getDirectionOfFacing());
		 assertEquals(10, craft.getDirectionOfTravel());
		 assertEquals(4, craft.getTurnWait());
	}

	@Test
	public void testTurnFwdClockwiseStbd() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			thruster.thrust(2, TURN_CLOCKWISE);
			stbdThruster.thrust(2, TURN_CLOCKWISE);
			fail("Cannot turn clockwise with Stbd Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(6, thruster.getCurrentThrust());
		 assertEquals(0, stbdThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnFwdAntiClockwisePort() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			thruster.thrust(2, TURN_ANTICLOCKWISE);
			portThruster.thrust(2, TURN_ANTICLOCKWISE);
			fail("Cannot turn anti-clockwise with Port Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(6, thruster.getCurrentThrust());
		 assertEquals(0, portThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnRevClockwisePort() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		try {
			retroThruster.thrust(2, TURN_CLOCKWISE);
			portThruster.thrust(2, TURN_CLOCKWISE);
			fail("Cannot turn clockwise with Port Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(6, retroThruster.getCurrentThrust());
		 assertEquals(0, portThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnRevAntiClockwiseStbd() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		try {
			retroThruster.thrust(2, TURN_ANTICLOCKWISE);
			stbdThruster.thrust(2, TURN_ANTICLOCKWISE);
			fail ("Cannot turn anti-clockwise with Stbd Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(6, retroThruster.getCurrentThrust());
		 assertEquals(0, stbdThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnFwdClockwiseRetro() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			portThruster.thrust(2, TURN_CLOCKWISE);
			retroThruster.thrust(2, TURN_CLOCKWISE);
			fail("Cannot turn clockwise with Retro Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(0, retroThruster.getCurrentThrust());
		 assertEquals(2, portThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnFwdAntiClockwiseRetro() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			stbdThruster.thrust(2, TURN_ANTICLOCKWISE);
			retroThruster.thrust(2, TURN_ANTICLOCKWISE);
			fail("Cannot turn anti-clockwise with Port Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(0, retroThruster.getCurrentThrust());
		 assertEquals(2, stbdThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnRevClockwiseAft() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		try {
			stbdThruster.thrust(2, TURN_CLOCKWISE);
			thruster.thrust(2, TURN_CLOCKWISE);
			fail("Cannot turn clockwise with Aft Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(0, thruster.getCurrentThrust());
		 assertEquals(2, stbdThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}

	@Test
	public void testTurnRevAntiClockwiseAft() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		try {
			portThruster.thrust(2, TURN_ANTICLOCKWISE);
			thruster.thrust(2, TURN_ANTICLOCKWISE);
			fail ("Cannot turn anti-clockwise with Aft Thruster");
		} catch (PhaseException e) {
			// Expected
		}

		 assertEquals(0, thruster.getCurrentThrust());
		 assertEquals(2, portThruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 assertEquals(0, craft.getDirectionOfFacing());
		 assertEquals(0, craft.getDirectionOfTravel());
		 assertEquals(0, craft.getTurnWait());
	}
}
