package aog.b5w.components.systems.weapons;

public class InterceptorMkII extends Interceptor {

	public InterceptorMkII(String name, int hitPoints, int armour, int power, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, power, arcStart, arcFinish);
		interceptRating = -4;
		recharge = 1;
		range = 1;
		rangePenalty = -2;
		fireControl = new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE, 8};
		damage = new Damage(1, 10, 8);
	}

}
