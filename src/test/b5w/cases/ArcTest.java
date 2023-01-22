package test.b5w.cases;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.utils.Arc;
import aog.b5w.utils.TargetingSolution;

public class ArcTest extends AbstractTest {


	@Test
	public void testOneArc() {
		addOneArcCraft();
		
		Arc arc = new Arc(Space.getObjects());
		Map<Integer, Set<TargetingSolution>> m = arc.getArcs(0, 0);
		assertNotNull(m);
		for (int i = 0; i < 12; i++) {
			Set<TargetingSolution> l = m.get(i);
			assertNotNull(l);
			assertEquals(1, l.size());
			assertEquals("Arc" + String.format("%02d", i), l.iterator().next().getTarget().getName());
		}
	}

	@Test
	public void testTwoArcs() {
		addTwoArcCraft();
		Arc arc = new Arc(Space.getObjects());
		Map<Integer, Set<TargetingSolution>> m = arc.getArcs(0, 0);
		assertNotNull(m);
		for (int i = 0; i < 12; i++) {
			Set<TargetingSolution> l = m.get(i);
			assertNotNull(l);
			assertEquals(2, l.size());
			Iterator<TargetingSolution> it = l.iterator();
			while (it.hasNext()) {
				TargetingSolution t = it.next();
				if (!t.getTarget().getName().contains(String.format("%02d", i))) {
					fail(t.getTarget().getName() + " must contain" + String.format("%02d", i));
				}
			}
		}
	}

	@Test
	public void testWeaponArc0001() {
		addOneArcCraft();
		addTwoArcCraft();
		Arc arc = new Arc(Space.getObjects());
		Set<TargetingSolution> l = arc.getTargetsInArc(0, 0, 0, 1);
		assertNotNull(l);
		assertEquals(5, l.size());
		Iterator<TargetingSolution> it = l.iterator();
		while (it.hasNext()) {
			TargetingSolution t = it.next();
			if (!t.getTarget().getName().contains(String.format("%02d", 0)) && !t.getTarget().getName().contains(String.format("%02d", 1))) {
				fail(t.getTarget().getName() + " must contain 00 or 01");
			}
		}
	}
	
	@Test
	public void testWeaponArc1011() {
		addOneArcCraft();
		addTwoArcCraft();
		Arc arc = new Arc(Space.getObjects());
		Set<TargetingSolution> l = arc.getTargetsInArc(0, 0, 10, 11);
		assertNotNull(l);
		assertEquals(5, l.size());
		Iterator<TargetingSolution> it = l.iterator();
		while (it.hasNext()) {
			TargetingSolution t = it.next();
			if (!t.getTarget().getName().contains(String.format("%02d", 10)) && !t.getTarget().getName().contains(String.format("%02d", 11))) {
				fail(t.getTarget().getName() + " must contain 10 or 11");
			}
		}
	}
	
	@Test
	public void testWeaponArc1100() {
		addOneArcCraft();
		addTwoArcCraft();
		Arc arc = new Arc(Space.getObjects());
		Set<TargetingSolution> l = arc.getTargetsInArc(0, 0, 11, 0);
		assertNotNull(l);
		assertEquals(5, l.size());
		Iterator<TargetingSolution> it = l.iterator();
		while (it.hasNext()) {
			TargetingSolution t = it.next();
			if (!t.getTarget().getName().contains(String.format("%02d", 0)) && !t.getTarget().getName().contains(String.format("%02d", 11))) {
				fail(t.getTarget().getName() + " must contain 11 or 00");
			}
		}
	}
	
}
