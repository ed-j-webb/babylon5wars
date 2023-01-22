package test.b5w.cases;

import static org.junit.Assert.*;

import org.junit.Test;

import aog.b5w.components.Structure;
import aog.b5w.components.systems.weapons.Salvo;
import aog.b5w.components.systems.weapons.StandardParticleBeam;
import aog.b5w.components.systems.weapons.Volley;

public class DamageTest extends AbstractTest {

	public Salvo salvo;
	public StandardParticleBeam standard; 
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		standard = new StandardParticleBeam("Test Standard", 4, 2, 1, 0, 0);
		
	}

	@Test
	public void testCraftTakeDamage() {
		salvo = new Salvo(standard.getWeaponDamage().getVolleys(5, "-", craft), "-", 5);
		craft.takeSalvo(salvo);
	}

	@Test
	public void testStructureTakeDamage() {
		salvo = new Salvo(standard.getWeaponDamage().getVolleys(5, "-", craft), "-", 5);
		craft.getStructure(AFT).takeSalvo(salvo);
	}

	@Test
	public void testStructureTakeLightStandardVolley() {
		Volley v = new Volley(3, "-");
		Structure aft = craft.getStructure(AFT); 
		assertEquals(50, aft.getCurrentHitPoints());
		assertEquals(50, aft.getHitPoints());
		assertEquals(4, aft.getArmour());
		assertEquals(false, aft.isDestroyed());
		assertEquals(3, v.getDamage());
		assertEquals("-", v.getType());

		Volley x = aft.takeVolley(v);
		
		assertEquals(4, aft.getArmour());
		assertEquals(50, aft.getHitPoints());
		assertEquals(0, aft.getDamage());
		assertNull(x);
	}
	
	@Test
	public void testStructureTakeMediumStandardVolley() {
		Volley v = new Volley(10, "-");
		Structure aft = craft.getStructure(AFT); 
		assertEquals(50, aft.getCurrentHitPoints());
		assertEquals(50, aft.getHitPoints());
		assertEquals(4, aft.getArmour());
		assertEquals(false, aft.isDestroyed());
		assertEquals(10, v.getDamage());
		assertEquals("-", v.getType());

		Volley x = aft.takeVolley(v);
		
		assertEquals(4, aft.getArmour());
		assertEquals(50, aft.getHitPoints());
		assertEquals(44, aft.getCurrentHitPoints());
		assertEquals(6, aft.getDamage());
		assertNull(x);
	}

	@Test
	public void testStructureTakeHeavyStandardVolley() {
		Volley v = new Volley(60, "-");
		Structure aft = craft.getStructure(AFT); 
		assertEquals(50, aft.getCurrentHitPoints());
		assertEquals(50, aft.getHitPoints());
		assertEquals(4, aft.getArmour());
		assertEquals(false, aft.isDestroyed());
		assertEquals(60, v.getDamage());
		assertEquals("-", v.getType());

		Volley x = aft.takeVolley(v);
		
		assertEquals(4, aft.getArmour());
		assertEquals(50, aft.getHitPoints());
		assertEquals(0, aft.getCurrentHitPoints());
		assertEquals(50, aft.getDamage());
		assertNotNull(x);
		assertEquals(6, x.getDamage());
	}

	@Test
	public void testSystemTakeLightStandardVolley() {
		Volley v = new Volley(3, "-");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(3, v.getDamage());
		assertEquals("-", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}
	
	@Test
	public void testSystemTakeMediumStandardVolley() {
		Volley v = new Volley(8, "-");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(8, v.getDamage());
		assertEquals("-", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getCurrentHitPoints());
		assertEquals(4, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeHeavyStandardVolley() {
		Volley v = new Volley(15, "-");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(15, v.getDamage());
		assertEquals("-", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(0, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getDamage());
		assertEquals(true, cannon.isDestroyed());
		assertNotNull(x);
		assertEquals(3, x.getDamage());
	}

	@Test
	public void testSystemTakeLightRakingVolley() {
		Volley v = new Volley(3, "R");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(3, v.getDamage());
		assertEquals("R", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(true, cannon.isRaked());
		assertNull(x);
	}
	
	@Test
	public void testSystemTakeMediumRakingVolley() {
		Volley v = new Volley(8, "R");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(8, v.getDamage());
		assertEquals("R", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getCurrentHitPoints());
		assertEquals(4, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(true, cannon.isRaked());
		assertNull(x);
	}

	@Test
	public void testSystemTakeHeavyRakingVolley() {
		Volley v = new Volley(15, "R");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(15, v.getDamage());
		assertEquals("R", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(0, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getDamage());
		assertEquals(true, cannon.isDestroyed());
		assertEquals(true, cannon.isRaked());
		assertNotNull(x);
		assertEquals(3, x.getDamage());
	}

	@Test
	public void testSystemTakeDoubleRakingVolley() {
		Volley v = new Volley(10, "R");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(10, v.getDamage());
		assertEquals("R", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(2, cannon.getCurrentHitPoints());
		assertEquals(6, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
		assertEquals(true, cannon.isRaked());

		v = new Volley(4, "R");
		
		x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(0, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getDamage());
		assertEquals(true, cannon.isDestroyed());
		assertNotNull(x);
		assertEquals(true, cannon.isRaked());
		assertEquals(2, x.getDamage());
	}

	@Test
	public void testSystemTakeLightPlasmaVolley() {
		Volley v = new Volley(3, "L");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(3, v.getDamage());
		assertEquals("L", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(7, cannon.getCurrentHitPoints());
		assertEquals(1, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeMediumPlasmaVolley() {
		Volley v = new Volley(6, "L");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(6, v.getDamage());
		assertEquals("L", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getCurrentHitPoints());
		assertEquals(4, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeHeavyPlasmaVolley() {
		Volley v = new Volley(15, "L");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(15, v.getDamage());
		assertEquals("L", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(0, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getDamage());
		assertEquals(true, cannon.isDestroyed());
		assertNotNull(x);
		assertEquals(5, x.getDamage());
	}
	
	@Test
	public void testSystemTakeLightMatterVolley() {
		Volley v = new Volley(3, "M");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(3, v.getDamage());
		assertEquals("M", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(5, cannon.getCurrentHitPoints());
		assertEquals(3, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeMediumMatterVolley() {
		Volley v = new Volley(6, "M");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(6, v.getDamage());
		assertEquals("M", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(2, cannon.getCurrentHitPoints());
		assertEquals(6, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeHeavyMatterVolley() {
		Volley v = new Volley(15, "M");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(15, v.getDamage());
		assertEquals("M", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(0, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getDamage());
		assertEquals(true, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeLightPiercingVolley() {
		Volley v = new Volley(3, "P");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(3, v.getDamage());
		assertEquals("P", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeMediumPiercingVolley() {
		Volley v = new Volley(6, "P");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(6, v.getDamage());
		assertEquals("P", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(6, cannon.getCurrentHitPoints());
		assertEquals(2, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertNull(x);
	}

	@Test
	public void testSystemTakeHeavyPiercingVolley() {
		Volley v = new Volley(15, "P");

		assertEquals(8, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(4, cannon.getArmour());
		assertEquals(0, cannon.getDamage());
		assertEquals(false, cannon.isDestroyed());
		assertEquals(15, v.getDamage());
		assertEquals("P", v.getType());

		Volley x = cannon.takeVolley(v);
		
		assertEquals(4, cannon.getArmour());
		assertEquals(8, cannon.getHitPoints());
		assertEquals(0, cannon.getCurrentHitPoints());
		assertEquals(8, cannon.getDamage());
		assertEquals(true, cannon.isDestroyed());
		assertNull(x);
	}
}
