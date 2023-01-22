package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.JumpPoint;

public class JumpTest extends AbstractTest {

	@Test
	public void testOpenJump() {
		advancePhase(5);
		
		try {
			jump.openJumpPoint(1, 1, 0);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can open a Jump Point");
		}
		
		assertEquals(2, Space.getObjects().size());
		JumpPoint p = (JumpPoint)Space.getObjects().get(1);
		assertEquals(1, p.getX());
		assertEquals(1, p.getY());
		assertEquals(0, p.getDirectionOfFacing());
		assertEquals(0, p.getOpen());
	}

	@Test
	public void testOpenJumpTooFar() {
		advancePhase(5);
		
		try {
			jump.openJumpPoint(1, 5, 0);
			fail("Cannot open a Jump Point more than 4 hexes away");
		} catch (PhaseException e) {
			// Expected
		}
		
		assertEquals(1, Space.getObjects().size());
	}
	
	@Test
	public void testOpenJumpTooSoon() {
		testOpenJump();
		
		advancePhase(10);
		
		try {
			jump.openJumpPoint(2, 2, 0);
			fail("Cannot open a Jump Point yet");
		} catch (PhaseException e) {
			// Expected
			assertEquals("The Jump Engine must wait 19 turns before it can open a Jump Point", e.getMessage());
		}
		
		assertEquals(2, Space.getObjects().size());
	}

	@Test
	public void testClosePoint() {
		testOpenJump();
		advancePhase(10);

		JumpPoint p = (JumpPoint)Space.getObjects().get(1);

		assertEquals(1, p.getOpen());
		
		advancePhase(10);
		
		assertEquals(-1, p.getOpen());
		
		advancePhase(1);
		
		assertEquals(1, Space.getObjects().size());
	}
	
	@Test
	public void testMaintainPoint2Turns() {
		testOpenJump();
		advancePhase(10);

		JumpPoint p = (JumpPoint)Space.getObjects().get(1);

		assertEquals(1, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Jump point can be maintained");
		}
		
		assertEquals(2, p.getOpen());

		advancePhase(1);
		
		assertEquals(2, Space.getObjects().size());
		
		advancePhase(9);

		assertEquals(-2, p.getOpen());

		advancePhase(1);
		
		assertEquals(1, Space.getObjects().size());
	}
	
	@Test
	public void testMaintainPointOver4Turns() {
		testOpenJump();
		advancePhase(10);

		JumpPoint p = (JumpPoint)Space.getObjects().get(1);

		assertEquals(1, p.getOpen());
		
		advancePhase(10);

		assertEquals(-1, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Jump point can be maintained");
		}

		assertEquals(2, p.getOpen());

		advancePhase(10);

		assertEquals(-2, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Jump point can be maintained");
		}

		assertEquals(3, p.getOpen());

		advancePhase(10);

		assertEquals(-3, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Jump point can be maintained");
		}

		assertEquals(4, p.getOpen());

		advancePhase(10);

		assertEquals(-4, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
			fail("Jump point cannot be maintained for more than 4 turns");
		} catch (PhaseException e) {
			assertEquals("This engine's Jump Point has been open for 4 turns", e.getMessage());
		}

		advancePhase(1);
		
		assertEquals(1, Space.getObjects().size());
	}
	
	@Test
	public void testMaintainPointTooFar() {
		testOpenJump();
		advancePhase(10);

		JumpPoint p = (JumpPoint)Space.getObjects().get(1);

		assertEquals(1, p.getOpen());
		
		advancePhase(10);

		assertEquals(-1, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Jump point can be maintained");
		}

		assertEquals(2, p.getOpen());
		
		advancePhase(9);
		
		try {
			thruster.thrust(8, ACCELERATE);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can accelerate in acceleration phase");
		}

		advancePhase(1);
		
		assertEquals(-2, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Jump point can be maintained");
		}

		assertEquals(3, p.getOpen());
		
		advancePhase(10);

		assertEquals(-3, p.getOpen());
		
		try {
			jump.maintainJumpPoint();
			fail("Jump point is too far away to be maintained");
		} catch (PhaseException e) {
			assertEquals("Jump Point is more than 4 hexes distant", e.getMessage());
		}

		assertEquals(-3, p.getOpen());

		advancePhase(1);

		assertEquals(1, Space.getObjects().size());
	}
	
}
