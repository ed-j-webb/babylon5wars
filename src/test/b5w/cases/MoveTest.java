package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.exception.PhaseException;

public class MoveTest extends AbstractMoveTest {

	@Test
	public void testMoveD0H1() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testMoveD0H2() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testMoveD0H3() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(3);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(3, craft.getY());
		 assertEquals(1, craft.getMoves());
	}
	
	@Test
	public void testMoveD0H4() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(4);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(4, craft.getY());
		 assertEquals(0, craft.getMoves());
	}
	
	@Test
	public void testMoveD2H1() {
		craft.setDirectionOfTravel(2);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testMoveD2H2() {
		craft.setDirectionOfTravel(2);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(2, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testMoveD2H3() {
		craft.setDirectionOfTravel(2);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(3);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(3, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(1, craft.getMoves());
	}
	
	@Test
	public void testMoveD2H4() {
		craft.setDirectionOfTravel(2);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(4);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(4, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(0, craft.getMoves());
	}

	@Test
	public void testMoveD4H1() {
		craft.setDirectionOfTravel(4);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, craft.getX());
		 assertEquals(-1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testMoveD4H2() {
		craft.setDirectionOfTravel(4);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(2, craft.getX());
		 assertEquals(-2, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testMoveD4H3() {
		craft.setDirectionOfTravel(4);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(3);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(3, craft.getX());
		 assertEquals(-3, craft.getY());
		 assertEquals(1, craft.getMoves());
	}
	
	@Test
	public void testMoveD4H4() {
		craft.setDirectionOfTravel(4);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(4);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(4, craft.getX());
		 assertEquals(-4, craft.getY());
		 assertEquals(0, craft.getMoves());
	}
	
	@Test
	public void testMoveD6H1() {
		craft.setDirectionOfTravel(6);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(-1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testMoveD6H2() {
		craft.setDirectionOfTravel(6);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(-2, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testMoveD6H3() {
		craft.setDirectionOfTravel(6);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(3);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(-3, craft.getY());
		 assertEquals(1, craft.getMoves());
	}
	
	@Test
	public void testMoveD6H4() {
		craft.setDirectionOfTravel(6);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(4);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(-4, craft.getY());
		 assertEquals(0, craft.getMoves());
	}
	
	@Test
	public void testMoveD8H1() {
		craft.setDirectionOfTravel(8);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testMoveD8H2() {
		craft.setDirectionOfTravel(8);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-2, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testMoveD8H3() {
		craft.setDirectionOfTravel(8);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(3);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-3, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(1, craft.getMoves());
	}
	
	@Test
	public void testMoveD8H4() {
		craft.setDirectionOfTravel(8);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(4);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-4, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(0, craft.getMoves());
	}

	@Test
	public void testMoveD10H1() {
		craft.setDirectionOfTravel(10);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testMoveD10H2() {
		craft.setDirectionOfTravel(10);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-2, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testMoveD10H3() {
		craft.setDirectionOfTravel(10);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(3);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-3, craft.getX());
		 assertEquals(3, craft.getY());
		 assertEquals(1, craft.getMoves());
	}
	
	@Test
	public void testMoveD10H4() {
		craft.setDirectionOfTravel(10);
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(4);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(-4, craft.getX());
		 assertEquals(4, craft.getY());
		 assertEquals(0, craft.getMoves());
	}

	@Test
	public void testMoveTooFar() {
		accelerate(thruster);

		assertEquals(4, craft.getVelocity());

		try {
			craft.move(5);
			fail("Cannot move more than moves");
		} catch (PhaseException e) {
			// Expected
		}
		
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
	}

	@Test
	public void testMoveAtEndOfPhase() {
		accelerate(thruster);
		
		assertEquals(4, craft.getVelocity());

		try {
			craft.move(2);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can move in move phase");
		}
		
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());
		
		 advancePhase(1);
		 
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(4, craft.getY());
		 assertEquals(0, craft.getMoves());
		 
	}
}
