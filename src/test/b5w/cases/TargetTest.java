package test.b5w.cases;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.exception.PhaseException;
import aog.b5w.utils.Die;
import aog.b5w.utils.TargetingSolution;

public class TargetTest extends AbstractTest {

	@Test
	public void testOneArcTarget() {
		addOneArcCraft();
		
		advancePhase(7);
		
		assertEquals(PHASE_TARGET, Space.getPhase());

		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), 0, 11);
		assertEquals(12, targets.size());
		
		targets = cannon.getPotentialSolutions();
		assertEquals(3, cannon.getPotentialSolutions().size());
		
		Iterator<TargetingSolution> it = targets.iterator();
		while (it.hasNext()) {
			TargetingSolution t = it.next();
			assertTrue(Die.between(t.getArc(), cannon.getArcStart(), cannon.getArcFinish()));
		}
	}

	@Test
	public void testTargetNullCraft() {
		addOneArcCraft();
		
		advancePhase(7);
		
		assertEquals(PHASE_TARGET, Space.getPhase());
	
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), 0, 11);
		assertEquals(12, targets.size());
		
		targets = cannon.getPotentialSolutions();
		assertEquals(3, cannon.getPotentialSolutions().size());
		
		try {
			cannon.target(null);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Should be able to target nothing");
		}
	}
	
	
	@Test
	public void testTargetOutOfArcCraft() {
		addOneArcCraft();
		
		advancePhase(7);
		
		assertEquals(PHASE_TARGET, Space.getPhase());
	
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), 0, 11);
		assertEquals(12, targets.size());
		
		Iterator<TargetingSolution> it = targets.iterator();
		TargetingSolution target = null;  
		while (it.hasNext()) {
			TargetingSolution t = it.next();
			if (!Die.between(t.getArc(), cannon.getArcStart(), cannon.getArcFinish())) {
				target = t;
				break;
			}
		}
		
		targets = cannon.getPotentialSolutions();
		assertEquals(3, cannon.getPotentialSolutions().size());
		
		try {
			cannon.target(target);
			fail("Cannot fire at this target");
		} catch (PhaseException e) {
			assertEquals("The selected target is not available to this weapon", e.getMessage());
		}
	}
	
	@Test
	public void testTargetInArcCraft() {
		addOneArcCraft();
		
		advancePhase(7);
		
		assertEquals(PHASE_TARGET, Space.getPhase());
	
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), 0, 11);
		assertEquals(12, targets.size());
		
		Iterator<TargetingSolution> it = targets.iterator();
		
		targets = cannon.getPotentialSolutions();
		assertEquals(3, cannon.getPotentialSolutions().size());

		TargetingSolution target = null;  
		while (it.hasNext()) {
			TargetingSolution t = it.next();
			if (Die.between(t.getArc(), cannon.getArcStart(), cannon.getArcFinish()) && t.getArc() == 0) {
				target = t;
				break;
			}
		}
		
		try {
			cannon.target(target);
		} catch (PhaseException e) {
			fail("Should be able to target in arc craft");
		}
		
		assertEquals("Arc00", cannon.getTarget().getName());
	}
	
}
