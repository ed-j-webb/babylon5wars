package aog.b5w.components.systems.weapons;

public class HeavyPulseCannon extends Weapon {

	public HeavyPulseCannon(String name, int hitPoints, int armour, int power, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, power, arcStart, arcFinish);

		damage = new Damage(15, 5);
		fireModes = "-";
		fireMode =  "-";
		
		intercept = -1;
		recharge = 3;
	
		attributes.add("Heavy");
		attributes.add("Pulse");
		attributes.add("Cannon");
		
		fireControl = new int[] {4, 3, -1};
		
		range = 2;
	}

}
