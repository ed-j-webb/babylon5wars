package aog.b5w.components.systems.weapons;

public class StandardParticleBeam extends Weapon {

	public StandardParticleBeam(String name, int hitPoints, int armour, int power, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, power, arcStart, arcFinish);

		damage = new Damage(1, 10, 6);
		fireModes = "-";
		fireMode = "-";
		
		intercept = -2;
		recharge = 1;
		
		attributes.add("Standard");
		attributes.add("Particle");
		attributes.add("Beam");
		
		fireControl = new int[] {6, 4, 4};
		
		range = 1;
	}

}
