package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.SmallCraft;

public class HangarTest extends AbstractTest {

	@Test
	public void testLaunch() {
		accelerate(thruster);
		advancePhase(4);
		
		assertEquals(2, hangar.getThroughput());
		assertEquals(2, hangar.getCurrentThroughput());
		assertEquals(6, hangar.getContents().size());
		
		try {
			hangar.launch(hangar.getContents().get(0));
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can launch in launch phase");
		}
		
		assertEquals(2, hangar.getThroughput());
		assertEquals(1, hangar.getCurrentThroughput());
		assertEquals(5, hangar.getContents().size());
		assertEquals(2, Space.getObjects().size());
		
		SmallCraft c = (SmallCraft)Space.getObjects().get(1);
		assertEquals(0, c.getDirectionOfFacing());
		assertEquals(0, c.getDirectionOfTravel());
		assertEquals(4, c.getVelocity());
	}

	@Test
	public void testLaunchBackwards() {
		accelerate(thruster);
		advancePhase(4);
		
		assertEquals(2, hangar.getThroughput());
		assertEquals(2, hangar.getCurrentThroughput());
		assertEquals(6, hangar.getContents().size());
		
		try {
			hangar.launch(hangar.getContents().get(0), 6);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can launch in launch phase");
		}
		
		assertEquals(2, hangar.getThroughput());
		assertEquals(1, hangar.getCurrentThroughput());
		assertEquals(5, hangar.getContents().size());
		assertEquals(2, Space.getObjects().size());
		
		SmallCraft c = (SmallCraft)Space.getObjects().get(1);
		assertEquals(6, c.getDirectionOfFacing());
		assertEquals(6, c.getDirectionOfTravel());
		assertEquals(-4, c.getVelocity());
	}
	
	
	@Test
	public void testLaunchTooMany() {
		accelerate(thruster);
		advancePhase(4);
		
		assertEquals(2, hangar.getThroughput());
		assertEquals(2, hangar.getCurrentThroughput());
		assertEquals(6, hangar.getContents().size());
		
		try {
			hangar.launch(hangar.getContents().get(0));
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can launch in launch phase");
		}

		assertEquals(2, hangar.getThroughput());
		assertEquals(1, hangar.getCurrentThroughput());
		assertEquals(5, hangar.getContents().size());
		assertEquals(2, Space.getObjects().size());

		try {
			hangar.launch(hangar.getContents().get(0));
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can launch in launch phase");
		}

		assertEquals(2, hangar.getThroughput());
		assertEquals(0, hangar.getCurrentThroughput());
		assertEquals(4, hangar.getContents().size());
		assertEquals(3, Space.getObjects().size());

		try {
			hangar.launch(hangar.getContents().get(0));
			fail("Launched too many");
		} catch (PhaseException e) {
			assertEquals("Hangar has no more throughput to launch craft", e.getMessage());
		}

		assertEquals(2, hangar.getThroughput());
		assertEquals(0, hangar.getCurrentThroughput());
		assertEquals(4, hangar.getContents().size());
		assertEquals(3, Space.getObjects().size());

	}

}
