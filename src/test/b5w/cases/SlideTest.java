package test.b5w.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.exception.PhaseException;

public class SlideTest extends AbstractMoveTest {

	@Test
	public void testSlideForePort() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testSlideForeStbd() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
			 portThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, portThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(1, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(3, craft.getMoves());
	}
	
	@Test
	public void testSlideAftPort() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, retroThruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(3, craft.getMoves());
	}

	@Test
	public void testSlideAftStbd() {
		accelerate(retroThruster);
		assertEquals(-4, craft.getVelocity());

		 try {
			 portThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, retroThruster.getCurrentThrust());
		 assertEquals(1, portThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(1, craft.getX());
		 assertEquals(-1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}
	
	@Test
	public void testSlideSlide() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());

		 try {
			 stbdThruster.thrust(1, SLIDE);
			 fail("Must move 1 hex before sliding again");
		 } catch (PhaseException e) {
			 // Expected
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());
	}
	
	@Test
	public void testSlideMoveSlide() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());

			try {
				craft.move(1);
			} catch (PhaseException e) {
				e.printStackTrace();
				fail("Can move in move phase");
			}

		 assertEquals(-1, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }
		 
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(2, stbdThruster.getCurrentThrust());
		 assertEquals(2, engine.getCurrentThrust());
		 assertEquals(-2, craft.getX());
		 assertEquals(3, craft.getY());
		 assertEquals(1, craft.getMoves());
	}

	@Test
	public void testSlideMoveSlideBack() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());

			try {
				craft.move(1);
			} catch (PhaseException e) {
				e.printStackTrace();
				fail("Can move in move phase");
			}

		 assertEquals(-1, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());

		 try {
			 portThruster.thrust(1, SLIDE);
			 fail("Cannot slide in opposite direction in same turn");
		 } catch (PhaseException e) {
			 // Expected
		 }
		 
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(0, portThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

	@Test
	public void testSlideSlideAcrossTurn() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
  			 craft.move(3);
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(4, craft.getY());
		 assertEquals(0, craft.getMoves());

		 advancePhase(10);
		 
		 assertEquals(PHASE_MOVE, Space.getPhase());
		 assertEquals(2, Space.getTurn());

		 try {
			 stbdThruster.thrust(1, SLIDE);
			 fail("Must move 1 hex between slides");
		 } catch (PhaseException e) {
			 // Expected
		 }
	}

	@Test
	public void testSlideMoveSlideAcrossTurn() {
		accelerate(thruster);
		assertEquals(4, craft.getVelocity());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());

		 advancePhase(10);
		 
		 assertEquals(PHASE_MOVE, Space.getPhase());
		 assertEquals(2, Space.getTurn());

			try {
				craft.move(1);
			} catch (PhaseException e) {
				e.printStackTrace();
				fail("Can move in move phase");
			}

		 assertEquals(-1, craft.getX());
		 assertEquals(5, craft.getY());
		 assertEquals(3, craft.getMoves());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 fail("Can slide after moving 1 hex");
		 }

		 assertEquals(-2, craft.getX());
		 assertEquals(6, craft.getY());
		 assertEquals(2, craft.getMoves());
	}

}
