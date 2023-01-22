package aog.b5w.components.systems.weapons;

import aog.b5w.Space;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PowerEvent;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.SpaceObject;
import aog.b5w.utils.TargetingSolution;

public abstract class SustainableWeapon extends Weapon {
	
	protected int maxSustain;
	protected int sustained = -1;
	protected SpaceObject sustainedTarget;

	public SustainableWeapon(String name, int hitPoints, int armour, int power, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, power, arcStart, arcFinish);
	}
	
	public int getMaxSustain() {
		return maxSustain;
	}

	public SpaceObject getSustainedTarget() {
		return sustainedTarget;
	}

	public int getSustained() {
		return sustained;
	}
	
	@Override
	public void setFireMode(String mode) throws PhaseException {
		if (mode.equals(SUSTAINED)) {
			if (Space.getPhase() != PHASE_POWER) {
				throw new PhaseException("Can only put Weapon in Sustained Mode in the Power Phase");
			}
			PowerEvent e = new PowerEvent(power, this);
			notifyPowerListeners(e, BEFORE);
			if (e.getAmount() == power) {
				fireMode = fireModes.substring(0, 1);
				if (sustained < 0) {
					sustained = 0;
				}
				notifyPowerListeners(e, AFTER);
			} else {
				throw new PhaseException("Insufficient Power to fire in Sustained Mode");
			}
		} else {
			super.setFireMode(mode);
		}
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_FIRE) {
			if (sustained < 0) { 
				fireMode = fireModes.substring(0, 1);
			} else {
				sustained++;
				sustainedTarget = solution.getTarget();
				if (sustained > maxSustain) {
					sustained = -1;
					sustainedTarget = null;
					coolDown = recharge;
				}
			}
		} else {
			super.afterPhase(e);
		}
	}
	
	
	
	@Override
	public void disable() throws PhaseException {
		super.disable();
		sustained = -1;
	}

	public void target(TargetingSolution solution) throws PhaseException {
		super.target(solution);
		if (sustained > 0 && solution.getTarget().equals(sustainedTarget)) {
			toHit = 20;
		}
	}
}
