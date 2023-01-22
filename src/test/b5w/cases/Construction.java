package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aog.b5w.B5WConstants;
import aog.b5w.components.systems.Engine;
import aog.b5w.components.systems.Reactor;
import aog.b5w.components.systems.Sensors;
import aog.b5w.components.systems.Thruster;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.LargeCraft;

import test.b5w.objects.TestCraft;

public class Construction implements B5WConstants {

	public TestCraft craft;
	public Reactor reactor;
	public Sensors sensors;
	public Engine engine;
	public Thruster thruster;
	public Weapon weapon;
	
	@Before
	public void setUp() throws Exception {
		craft = new TestCraft("Tester");
		reactor = (Reactor)craft.getStructure(LargeCraft.PRIMARY).getSystem("Reactor");
		sensors = (Sensors)craft.getStructure(LargeCraft.PRIMARY).getSystem("Sensors");
		engine = (Engine)craft.getStructure(LargeCraft.PRIMARY).getSystem("Engine");
		thruster = (Thruster)craft.getStructure(LargeCraft.FORE).getSystem("Port Retro");
		weapon = (Weapon)craft.getStructure(LargeCraft.FORE).getSystem("Hvy Laser Cannon 1");
	}

	@After
	public void tearDown() throws Exception {
		craft = null;
	}

	@Test
	public void testCreation() {
		assertNotNull(craft);
		assertEquals("Tester", craft.getName());
	}

	@Test
	public void testReactor() throws PhaseException {

		assertNotNull(reactor);
		assertEquals(9, reactor.getPower());

		int reactorPower = reactor.getPower();
		
		// Can't enable a system already enabled
		sensors.enable();
		assertEquals(reactorPower, reactor.getPower());
		
		// Can disable an enabled system
		sensors.disable();
		assertEquals(reactorPower + 4, reactor.getPower());
		
		// Can't disable a disabled system
		sensors.disable();
		assertEquals(reactorPower + 4, reactor.getPower());

		// Can enable a disabled system
		sensors.enable();
		assertEquals(reactorPower, reactor.getPower());
	}
	
	@Test
	public void testSensors() throws PhaseException {
		

		assertNotNull(sensors);
		assertEquals(8, sensors.getInitialEw());

		int reactorPower = reactor.getPower();
		int sensorsEW = sensors.getCurrentEw();
		
		// Can't produce extra EW when disabled
		sensors.disable();
		sensors.produceEW(1);
		;
		assertEquals(sensorsEW, sensors.getCurrentEw());

		sensors.enable();
		
		// Can't produce 2 extra EW
		sensors.produceEW(2);
		assertEquals(reactorPower, reactor.getPower());
		assertEquals(sensorsEW, sensors.getCurrentEw());
		
		// Can produce 1 extra EW
		sensors.produceEW(1);
		assertEquals(reactorPower - sensors.getCurrentEw(), reactor.getPower());
		assertEquals(sensorsEW + 1, sensors.getCurrentEw());
	}
	
	@Test
	public void testEngine() {
	
		assertNotNull(engine);
		assertEquals(3, engine.getEfficency());
		assertEquals(8, engine.getThrust());
		assertEquals(8, engine.getCurrentThrust());
		
		int reactorPower = reactor.getPower();
		int thrust = engine.getCurrentThrust();
		
		//Can't produce 4 extra thrust
		try {
			engine.produceThrust(4);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		assertEquals(reactorPower, reactor.getPower());
		assertEquals(thrust, engine.getThrust());
		
		// Can produce 2 extra thrust
		try {
			engine.produceThrust(2);
		} catch (PhaseException e) {
			e.printStackTrace();
		}
		assertEquals(reactorPower - 2 * engine.getEfficency(), reactor.getPower());
		assertEquals(thrust + 2, engine.getCurrentThrust());
	}
	
	@Test
	public void testThruster() {

		assertNotNull(thruster);
		assertEquals(4, thruster.getRating());
		assertEquals(1, thruster.getEfficiency());
		
		int engineThrust = engine.getCurrentThrust();
		int thrust = thruster.getCurrentThrust();
		
		// Can't thrust more than 8
		try {
			thruster.thrust(10, TURN);
		} catch (PhaseException e) {
			fail();
		}
		assertEquals(engineThrust, engine.getCurrentThrust());
		assertEquals(thrust, thruster.getCurrentThrust());
		
		// Can thrust up to 8
		try {
			thruster.thrust(8, TURN);
		} catch (PhaseException e) {
			fail();
		}
		assertEquals(engineThrust - 8, engine.getCurrentThrust());
		assertEquals(thrust + 8, thruster.getCurrentThrust());
	}
	
	@Test
	public void testWeapon() throws PhaseException {
		
		assertNotNull(weapon);
		assertEquals(4, weapon.getRecharge());
		assertEquals(3, weapon.getRange());
		assertEquals(10, weapon.getArcStart());
		assertEquals(0, weapon.getArcFinish());
		assertEquals(4, weapon.getWeaponDamage().getDieCount());
		assertEquals(10, weapon.getWeaponDamage().getDieSize());
		assertEquals(10, weapon.getWeaponDamage().getPlus());
		
		// Cannot fire this weapon in standard mode
		weapon.setFireMode("-");

		
		// Can fire this weapon in Raking mode
		weapon.setFireMode("R");


		int power = reactor.getPower();
		
		// Not enough Power to fire in sustained mode
		weapon.setFireMode("S");
		assertEquals(power, reactor.getPower());
		
		// Now there will be
		sensors.disable();
		power = reactor.getPower();
		weapon.setFireMode("S");
		assertEquals(power - (weapon.getPower() * 2), reactor.getPower());
	}
}
