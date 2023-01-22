package test.b5w.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;

import test.b5w.objects.TestCraft;
import aog.b5w.B5WConstants;
import aog.b5w.Space;
import aog.b5w.components.systems.Engine;
import aog.b5w.components.systems.Hangar;
import aog.b5w.components.systems.JumpEngine;
import aog.b5w.components.systems.Reactor;
import aog.b5w.components.systems.Sensors;
import aog.b5w.components.systems.Thruster;
import aog.b5w.components.systems.weapons.HeavyLaserCannon;
import aog.b5w.components.systems.weapons.NeutronCannon;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.SpaceObject;

public abstract class AbstractTest implements B5WConstants {

	public TestCraft craft;
	public Reactor reactor;
	public Engine engine;
	public Thruster retroThruster;
	public Thruster stbdThruster;
	public Thruster portThruster;
	public Thruster thruster;
	public JumpEngine jump;
	public Sensors sensors;
	public Hangar hangar;
	public HeavyLaserCannon cannon;
	public NeutronCannon neutron;

	@Before
	public void setUp() throws Exception {
		Space.clear();
		craft = new TestCraft("Tester");
		reactor = (Reactor)craft.getStructure(PRIMARY).getSystem("Reactor");
		engine = (Engine)craft.getStructure(PRIMARY).getSystem("Engine");
		retroThruster = (Thruster)craft.getStructure(FORE).getSystem("Port Retro");
		stbdThruster = (Thruster)craft.getStructure(STARBOARD).getSystem("Starboard Thruster");
		portThruster = (Thruster)craft.getStructure(PORT).getSystem("Port Thruster");
		thruster = (Thruster)craft.getStructure(AFT).getSystem("Main Inner Starboard Thruster");
		jump = (JumpEngine)craft.getStructure(PRIMARY).getSystem("Jump Engine");
		sensors = (Sensors)craft.getStructure(PRIMARY).getSystem("Sensors");
		hangar = (Hangar)craft.getStructure(PRIMARY).getSystem("Hangar");
		cannon = (HeavyLaserCannon)craft.getStructure(FORE).getSystem("Fore Hvy Laser Cannon 1");
		neutron = (NeutronCannon)craft.getStructure(FORE).getSystem("Neutron Cannon");
		Space.add(craft);
	}

	@After
	public void tearDown() throws Exception {
	}

	public void accelerate(Thruster t) {
		advancePhase(4);

		assertEquals(PHASE_ACCELERATION, Space.getPhase());
		
		try {
			t.thrust(4, ACCELERATE);
		} catch (PhaseException e) {
			fail("Thrusters can thrust in Accel phase");
		}

		advancePhase(2);

		assertEquals(PHASE_MOVE, Space.getPhase());

	}
	
	public void advancePhase(int phases) {
		for (int i = 0; i < phases; i++) {
			 Space.advancePhase();
		}
	}
	
	public void addOneArcCraft() {
		SpaceObject o = new TestCraft("Arc00", 0, 1, 4);
		Space.add(o);
		o = new TestCraft("Arc01", 0, 4, 2);
		Space.add(o);
		o = new TestCraft("Arc02", 0, 5, -1);
		Space.add(o);
		o = new TestCraft("Arc03", 0, 4, -3);
		Space.add(o);
		o = new TestCraft("Arc04", 0, 2, -3);
		Space.add(o);
		o = new TestCraft("Arc05", 0, 1, -3);
		Space.add(o);
		o = new TestCraft("Arc06", 0, -2, -4);
		Space.add(o);
		o = new TestCraft("Arc07", 0, -2, -1);
		Space.add(o);
		o = new TestCraft("Arc08", 0, -4, 1);
		Space.add(o);
		o = new TestCraft("Arc09", 0, -3, 2);
		Space.add(o);
		o = new TestCraft("Arc10", 0, -4, 5);
		Space.add(o);
		o = new TestCraft("Arc11", 0, -2, 5);
		Space.add(o);
	}
	
	public void addTwoArcCraft() {
		SpaceObject o = new TestCraft("Arc11,00", 0, 0, 4);
		Space.add(o);
		o = new TestCraft("Arc00,01", 0, 2, 2);
		Space.add(o);
		o = new TestCraft("Arc01,02", 0, 3, 0);
		Space.add(o);
		o = new TestCraft("Arc02,03", 0, 6, -3);
		Space.add(o);
		o = new TestCraft("Arc03,04", 0, 4, -4);
		Space.add(o);
		o = new TestCraft("Arc04,05", 0, 2, -4);
		Space.add(o);
		o = new TestCraft("Arc05,06", 0, 0, -4);
		Space.add(o);
		o = new TestCraft("Arc06,07", 0, -3, -3);
		Space.add(o);
		o = new TestCraft("Arc07,08", 0, -5, 0);
		Space.add(o);
		o = new TestCraft("Arc08,09", 0, -2, 1);
		Space.add(o);
		o = new TestCraft("Arc09,10", 0, -5, 5);
		Space.add(o);
		o = new TestCraft("Arc10,11", 0, -4, 8);
		Space.add(o);
	}
	
}
