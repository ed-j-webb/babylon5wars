package aog.b5w.components.systems.weapons;

import java.util.List;

import aog.b5w.components.Component;
import aog.b5w.event.FireEvent;
import aog.b5w.event.FireListener;
import aog.b5w.utils.Die;

public abstract class Interceptor extends Weapon implements FireListener {

	protected int interceptRating;
	
	public Interceptor(String name, int hitPoints, int armour, int power, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, power, arcStart, arcFinish);
		attributes.add("Interceptor");
	}

	@Override
	public void commission(List<Component> systems) {
		super.commission(systems);
		getCraft().addFireListener(this);
	}

	@Override
	public void takeFire(FireEvent e) {
		if (enabled && !destroyed) {
			int arc = Die.plus(e.getArc(), 6);
			if (Die.between(arc, arcStart, arcFinish)) {
				int modifier = e.getModifier(FireEvent.MOD_INTERCEPTOR);
				if (modifier > interceptRating) {
					e.modifyToHit(interceptRating - modifier, FireEvent.MOD_INTERCEPTOR);
				}
			}
		}
	}
	
	public void sendFire(FireEvent e) {
		if (e.getWeapon() == this) {
			super.sendFire(e);
		}
	}
}
