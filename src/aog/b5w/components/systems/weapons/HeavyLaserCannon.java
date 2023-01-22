package aog.b5w.components.systems.weapons;

public class HeavyLaserCannon extends SustainableWeapon {

	public HeavyLaserCannon(String name, int hitPoints, int armour, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, 6, arcStart, arcFinish);

		damage = new Damage(4, 10, 10);
		fireModes = "RS";
		fireMode = "R";
		
		maxSustain = 1;
		
		intercept = Integer.MIN_VALUE;
		recharge = 4;
		
		attributes.add("Heavy");
		attributes.add("Laser");
		attributes.add("Cannon");
		attributes.add("Raking");
		attributes.add("Sustained");
		
		fireControl = new int[] {3, 2, -4};
		
		range = 3;
	}

}
