package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import test.b5w.objects.TestCraft;
import test.b5w.objects.TestSmallCraft;

import aog.b5w.Space;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.Craft;
import aog.b5w.space.SmallCraft;

public class SensorsTest extends AbstractTest {

	protected Craft target1;
	protected Craft target2;
	protected SmallCraft small1;
	protected SmallCraft small2;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		target1 = new TestCraft("Target One", 4, 4, 4);
		target2 = new TestCraft("Target Two", 10, 7, -2);
		small1 = new TestSmallCraft("Target Three", 0, 5, 3);
		small2 = new TestSmallCraft("Target Four", 0, 11, 3);
		Space.add(target1);
		Space.add(target2);
	}


	@Test
	public void testProduceOneEW() {
		advancePhase(1);
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(9, reactor.getSurplus());
		try {
			sensors.produceEW(1);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can produce EW in EW phase");
		}

		assertEquals(9, sensors.getCurrentEw());
		assertEquals(0, reactor.getSurplus());
	}

	@Test 
	public void testProduceTwoEW() {
		advancePhase(1);
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(9, reactor.getSurplus());
		try {
			sensors.produceEW(2);
			fail("Not enough power to produce 2 EW");
		} catch (PhaseException e) {
			assertEquals("Insufficient Power", e.getMessage());
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(9, reactor.getSurplus());
	}
	
	@Test 
	public void testTargetCraft() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getOffensiveEw(target1));
	}
	
	@Test 
	public void testTargetCraftTwice() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getOffensiveEw(target1));

		try {
			sensors.target(target1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(4, sensors.getDefensiveEw());
		assertEquals(4, sensors.getOffensiveEw(target1));
	}
	
	@Test 
	public void testTargetTwoCraft() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		try {
			sensors.target(target2, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(4, sensors.getDefensiveEw());
		assertEquals(2, sensors.getOffensiveEw(target1));
		assertEquals(2, sensors.getOffensiveEw(target2));
	}
	
	@Test 
	public void testTargetCraftTooMuch() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 9);
			fail("Do not have enough EW to target with");
		} catch (PhaseException e) {
			assertEquals("Insufficient EW to target with", e.getMessage());
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(8, sensors.getDefensiveEw());
		assertEquals(0, sensors.getOffensiveEw(target1));
	}
	
	@Test 
	public void testTargetAndUntargetCraft() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getOffensiveEw(target1));

		try {
			sensors.target(target1, -2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(8, sensors.getDefensiveEw());
		assertEquals(0, sensors.getOffensiveEw(target1));
	}

	@Test 
	public void testTargetAndUntargetCraftTooMuch() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getOffensiveEw(target1));

		try {
			sensors.target(target1, -4);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(8, sensors.getDefensiveEw());
		assertEquals(0, sensors.getOffensiveEw(target1));
	}

	@Test 
	public void testTargetCraftWithNegative() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, -2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(8, sensors.getDefensiveEw());
		assertEquals(0, sensors.getOffensiveEw(target1));
	}

	@Test 
	public void testTargetAndReduceCraft() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.target(target1, 4);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(4, sensors.getDefensiveEw());
		assertEquals(4, sensors.getOffensiveEw(target1));

		try {
			sensors.target(target1, -2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getOffensiveEw(target1));
	}

	@Test 
	public void testAssignCCEW() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
	}

	@Test 
	public void testAssignTooMuchCCEW() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(10);
		} catch (PhaseException e) {
			assertEquals("Insufficient EW to assign to Close Combat", e.getMessage());
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(8, sensors.getDefensiveEw());
		assertEquals(0, sensors.getCloseCombatEw());
	}

	@Test 
	public void testAssignCCEWTwice() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}

		try {
			sensors.closeCombat(3);
		} catch (PhaseException e) {
			e.printStackTrace();
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(3, sensors.getDefensiveEw());
		assertEquals(5, sensors.getCloseCombatEw());
	}

	@Test 
	public void testAssignCCEWAndTarget() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		
		advancePhase(4);
		
		try {
			sensors.targetCloseCombat(small1, 2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(0, sensors.getCloseCombatEw());
		assertEquals(2, sensors.getOffensiveEw(small1));
	}	
	
	@Test 
	public void testAssignCCEWAndTargetTooMuch() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		
		advancePhase(4);
		
		try {
			sensors.targetCloseCombat(small1, 4);
		} catch (PhaseException e) {
			assertEquals("Insufficient CCEW to target with", e.getMessage());
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		assertEquals(0, sensors.getOffensiveEw(small1));
	}	

	@Test 
	public void testAssignCCEWAndTargetInEwPhase() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		
				
		try {
			sensors.targetCloseCombat(small1, 4);
		} catch (PhaseException e) {
			assertEquals("Can only target CCEW during the Target Phase", e.getMessage());
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		assertEquals(0, sensors.getOffensiveEw(small1));
	}	

	@Test 
	public void testAssignCCEWAndTargetTooFar() {
		advancePhase(3);
		
		assertEquals(8, sensors.getCurrentEw());
		
		try {
			sensors.closeCombat(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		
		advancePhase(4);
		
		try {
			sensors.targetCloseCombat(small2, 2);
		} catch (PhaseException e) {
			assertEquals("Target must be within 10 hexes", e.getMessage());
		}

		assertEquals(8, sensors.getCurrentEw());
		assertEquals(6, sensors.getDefensiveEw());
		assertEquals(2, sensors.getCloseCombatEw());
		assertEquals(0, sensors.getOffensiveEw(small2));
	}	
}
