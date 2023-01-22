package aog.b5w.components.systems.weapons;

public class NeutronCannon extends SustainableWeapon {

	public NeutronCannon(String name, int hitPoints, int armour, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, 7, arcStart, arcFinish);

		damage = new Damage(4, 10, 5);
		fireModes = "RPS";
		fireMode = "R";
		
		maxSustain = 1;
		
		intercept = Integer.MIN_VALUE;
		recharge = 3;
		
		attributes.add("Neutron");
		attributes.add("Cannon");
		attributes.add("Raking");
		attributes.add("Sustained");
		attributes.add("Piercing");
		
		fireControl = new int[] {4, 4, 1};
		
		range = 4;
	}

}
