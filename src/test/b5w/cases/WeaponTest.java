package test.b5w.cases;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.SpaceObject;
import aog.b5w.utils.TargetingSolution;

public class WeaponTest extends AbstractTest {

	@Test
	public void testTarget() {
		addOneArcCraft();
		
		assertEquals(4, cannon.getRecharge());
		assertEquals(0, cannon.getCoolDown());
		advancePhase(7);
		assertEquals(PHASE_TARGET, Space.getPhase());
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), cannon.getArcStart(), cannon.getArcFinish());
		assertEquals(3, targets.size());
		assertEquals(3, cannon.getPotentialSolutions().size());

		TargetingSolution t = null;
		Iterator<TargetingSolution> it = targets.iterator();
		while (it.hasNext()) {
			t = it.next();
			if (t.getTarget().getName().equals("Arc10") && t.getRange() == 5) {
				break;
			}
		}

		try {
			cannon.target(t);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can target in Targeting Phase");
		}
		
		assertEquals(t.getTarget(), cannon.getTarget());
		assertEquals(5, t.getRange());
		assertEquals(10, t.getArc());
		assertEquals(5, cannon.getToHit());
	}

	@Test
	public void testTargetSustained() {
		addOneArcCraft();
		
		assertEquals(4, cannon.getRecharge());
		assertEquals(0, cannon.getCoolDown());
		
		advancePhase(1);
		try {
			cannon.setFireMode(Weapon.SUSTAINED);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		
		assertEquals("R", cannon.getFireMode());
		assertEquals(3, reactor.getSurplus());
		assertEquals(0, cannon.getSustained());
		
		advancePhase(6);
		assertEquals(PHASE_TARGET, Space.getPhase());
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), cannon.getArcStart(), cannon.getArcFinish());
		assertEquals(3, targets.size());
		assertEquals(3, cannon.getPotentialSolutions().size());
		
		TargetingSolution t = null;
		Iterator<TargetingSolution> it = targets.iterator();
		while (it.hasNext()) {
			t = it.next();
			if (t.getTarget().getName().equals("Arc10")) {
				break;
			}
		}
		try {
			cannon.target(t);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can target in Targeting Phase");
		}
		
		assertEquals(t.getTarget(), cannon.getTarget());
		assertEquals(5, t.getRange());
		assertEquals(10, t.getArc());
		assertEquals(5, cannon.getToHit());
		
		advancePhase(2);
		assertEquals(PHASE_CRITICAL, Space.getPhase());
		assertEquals(1, cannon.getSustained());
		assertEquals(cannon.getTarget(), cannon.getSustainedTarget());
		assertEquals(0, cannon.getCoolDown());
		
		advancePhase(2);

		try {
			cannon.setFireMode(Weapon.SUSTAINED);
		} catch (PhaseException e) {
			e.printStackTrace();
		}

		assertEquals("R", cannon.getFireMode());
		assertEquals(3, reactor.getSurplus());
		assertEquals(1, cannon.getSustained());

		advancePhase(6);
		assertEquals(PHASE_TARGET, Space.getPhase());
		try {
			cannon.target(t);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Can target in Targeting Phase");
		}
		
		assertEquals(t.getTarget(), cannon.getTarget());
		assertEquals(5, t.getRange());
		assertEquals(10, t.getArc());
		assertEquals(20, cannon.getToHit());

		advancePhase(2);
		
		assertEquals(-1, cannon.getSustained());
		assertEquals(null, cannon.getSustainedTarget());
		assertEquals(4, cannon.getCoolDown());
	}
	
	@Test
	public void testFire() {
		testTarget();
		
		advancePhase(1);
		
		try {
			cannon.fire();
		} catch (PhaseException e) {
			fail("Can fire in Fire Phase");
		}
	}
	
	@Test
	public void testTargetPiercingWithoutEW() {
		addOneArcCraft();
		
		assertEquals(3, neutron.getRecharge());
		assertEquals(0, neutron.getCoolDown());
		advancePhase(7);
		assertEquals(PHASE_TARGET, Space.getPhase());
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), cannon.getArcStart(), cannon.getArcFinish());
		assertEquals(3, targets.size());
		assertEquals(3, cannon.getPotentialSolutions().size());
		
		TargetingSolution t = null;
		Iterator<TargetingSolution> it = targets.iterator();
		while (it.hasNext()) {
			t = it.next();
			if (!t.getTarget().getName().equals("Arc10")) {
				break;
			}
		}
		
		try {
			neutron.setFireMode(Weapon.PIERCING);
		} catch (PhaseException e) {
			fail("Can change to Piercing mode");
		}
		
		try {
			neutron.target(t);
			fail("Need 4 EW to fire in Piercing Mode");
		} catch (PhaseException e) {
			// Expected
		}
	}

	@Test
	public void testTargetPiercingWithEW() {
		addOneArcCraft();
		
		assertEquals(3, neutron.getRecharge());
		assertEquals(0, neutron.getCoolDown());
		advancePhase(3);
		
		List<SpaceObject> objs = Space.getObjects();
		Iterator<SpaceObject> it = objs.iterator();
		while (it.hasNext()) {
			SpaceObject obj = it.next();
			if (obj.getName().equals("Arc02")) {
				try {
					sensors.target(obj, 4);
				} catch (PhaseException e) {
					fail("Can target EW in EW phase");
				}
			}
		}
		
		advancePhase(4);
		assertEquals(PHASE_TARGET, Space.getPhase());
		
		Set<TargetingSolution> targets = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), neutron.getArcStart(), neutron.getArcFinish());
		assertEquals(5, targets.size());
		assertEquals(5, neutron.getPotentialSolutions().size());

		TargetingSolution t = null;
		Iterator<TargetingSolution> targetIt = targets.iterator();
		while (targetIt.hasNext()) {
			t = targetIt.next();
			if (t.getTarget().getName().equals("Arc02")) {
				break;
			}
		}
		
		try {
			neutron.setFireMode(Weapon.PIERCING);
		} catch (PhaseException e) {
			fail("Can change to Piercing mode");
		}
		
		try {
			neutron.target(t);
		} catch (PhaseException e) {
			e.printStackTrace();
			fail("Has 4 EW to fire in Piercing Mode");
		}
		
		assertEquals(t.getTarget(), neutron.getTarget());
		assertEquals(5, t.getRange());
		assertEquals(2, t.getArc());
		assertEquals(8, neutron.getToHit());
	}
}
