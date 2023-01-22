package aog.b5w.components.systems.weapons;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import aog.b5w.Space;
import aog.b5w.components.Rollable;
import aog.b5w.components.systems.OptionalSystem;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.FireEvent;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PhaseListener;
import aog.b5w.event.PowerEvent;
import aog.b5w.event.PowerListener;
import aog.b5w.exception.PhaseException;
import aog.b5w.log.LogEntry;
import aog.b5w.space.SpaceObject;
import aog.b5w.utils.Die;
import aog.b5w.utils.TargetingSolution;

public abstract class Weapon extends OptionalSystem implements Rollable, PhaseListener {

	private static Logger log = Logger.getLogger(Weapon.class);
	protected int intercept;

	protected int recharge;

	protected int range;

	protected int rangePenalty = -1;

	protected int[] fireControl = new int[3];

	public static final int FIRE_CONTROL_CAPITAL = 0;
	public static final int FIRE_CONTROL_MEDIUM = 1;
	public static final int FIRE_CONTROL_SMALL = 2;

	protected String fireModes = "-";

	protected String fireMode;

	public static final String STANDARD = "-";
	public static final String RAKING = "R";
	public static final String PIERCING = "P";
	public static final String SUSTAINED = "S";
	public static final String PULSE = "U";
	public static final String PLASMA = "L";
	public static final String MATTER = "M";

	protected Damage damage;

	protected int arcStart;
	protected int arcFinish;

	protected int coolDown;
	
	protected Set<TargetingSolution> potentialSolutions = new HashSet<TargetingSolution>();
	
	protected TargetingSolution solution;
	
	protected int toHit;
	
	public Weapon(String name, int hitPoints, int armour, int power, int arcStart, int arcFinish) {
		super(name, hitPoints, armour, power);

		type = "Weapon";

		attributes.add("Weapon");

		criticals = new int[] {14, 19, 25};

		this.arcStart = arcStart;
		this.arcFinish = arcFinish;
	}

	public int getIntercept() {
		return intercept;
	}

	public int getRecharge() {
		return recharge;
	}

	public int getRange() {
		return range;
	}

	public int getRangePenalty() {
		return rangePenalty;
	}

	public int[] getFireControl() {
		return fireControl;
	}

	public String getFireModes() {
		return fireModes;
	}

	public String getFireMode() {
		return fireMode;
	}

	public Damage getWeaponDamage() {
		return damage;
	}

	public int getArcStart() {
		return arcStart;
	}

	public int getArcFinish() {
		return arcFinish;
	}

	public int getCoolDown() {
		return coolDown;
	}

	public SpaceObject getTarget() {
		if (solution == null) {
			return null;
		} else {
			return solution.getTarget();
		}
	}

	public int getToHit() {
		return toHit;
	}

	/**
	 * Critical1 - Range penalty
	 */
	protected void critical1() {
		if (range == 1) {
			rangePenalty--;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "rangePenalty", rangePenalty + 1, rangePenalty));
		} else {
			range--;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "range", range + 1, range));
		}
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Weapon's range reduced.");
		log.info(entry);
	}

	/**
	 * Critical2 - Damage penalty
	 */
	protected void critical2() {
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Weapon's damage reduced.");
		log.info(entry);
	}

	public void setFireMode(String mode) throws PhaseException {
		if (fireModes.contains(mode)) {
			String temp = fireMode;
			fireMode = mode;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "fireMode", temp, fireMode));
		} else {
			throw new PhaseException("Cannot put this weapon into that Mode");
		}
	}

	protected void notifyPowerListeners(PowerEvent e, int sequence) {
		synchronized (powerListeners) {
			Iterator<PowerListener> it = powerListeners.iterator();
			while (it.hasNext()) {
				if (sequence == BEFORE) {
					it.next().beforeDrawPower(e);
				} else {
					it.next().afterDrawPower(e);
				}
			}
		}
	}

	public void roll() {
		int tempStart = arcStart;
		int tempFinish = arcFinish;
		int temp = Die.plus(12, -arcFinish);
		arcFinish = Die.plus(arcStart, -12);
		arcStart = temp;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "arcStart", tempStart, arcStart));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "arcFinish", tempFinish, arcFinish));
	}

	@Override
	public void disable() throws PhaseException {
		super.disable();
		int temp = coolDown;
		coolDown = recharge;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "coolDown", temp, coolDown));
		
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		super.beforePhase(e);
		if (e.getPhase() == PHASE_FIRE) {
			if (coolDown > 0) {
				coolDown--;
				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "coolDown", coolDown + 1, coolDown));
			}
		}
	}
	
	@Override
	public void afterPhase(PhaseEvent e) {
		super.afterPhase(e);
		if (e.getPhase() == PHASE_FIRE) {
			String mode = fireMode;
			fireMode = fireModes.substring(0, 1);
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "fireMode", mode, fireMode));
			int temp = coolDown;
			coolDown = recharge;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "arcStart", temp, coolDown));
		}
		if (e.getPhase() == PHASE_TARGET) {
			potentialSolutions.clear();
		}
	}
	
	public void acceptTargets(Set<TargetingSolution> targetingSolutions) {
		if (targetingSolutions != null) {
			potentialSolutions.addAll(targetingSolutions);
		}
	}

	public Set<TargetingSolution> getPotentialSolutions() {
		return potentialSolutions;
	}
	
	public void target(TargetingSolution solution) throws PhaseException {
		if (Space.getPhase() != PHASE_TARGET) {
			throw new PhaseException("Can only target weapons in the Target Phase");
		}
		if (destroyed) {
			throw new PhaseException("This weapon is destroyed and cannot target");
		}
		if (!enabled) {
			throw new PhaseException("This weapon has been disabled and cannot target");
		}
		if (coolDown > 0) {
			throw new PhaseException("This weapon is still recharging and cannot target");
		}
		if (solution != null && !potentialSolutions.contains(solution)) {
			throw new PhaseException("The selected target is not available to this weapon");
		}
		
		String oldTarget = "";
		String newTarget = "";
		if (this.solution != null) {
			oldTarget = this.solution.getTarget().getName();
		}
		if (solution != null) {
			newTarget = solution.getTarget().getName();
		}
		this.solution = solution;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "target", oldTarget, newTarget));
		
		if (solution == null) {
			toHit = 0;
		} else {
			FireEvent e = new FireEvent(solution, getCraft(), this);
			
			this.sendFire(e);
			getCraft().sendFire(e);
			
			if (getFireMode().equals(PIERCING) && e.getModifier(FireEvent.MOD_OFFENSIVE_EW) < 4) {
				throw new PhaseException("Target needs to have 4 EW assigned to it to fire in Piercing Mode");
			}
			solution.getTarget().takeFire(e);
		
			toHit = e.getToHit();
		}
	}
	
	public void fire() throws PhaseException {
		if (solution == null || solution.getTarget() == null) {
			return;
		}
		if (Space.getPhase() != PHASE_FIRE) {
			throw new PhaseException("Weapons can only fire in the Fire Phase");
		} 
		if (!enabled) {
			throw new PhaseException("Weapons is disabled");
		}
		if (Die.roll(20) < toHit) {
			Salvo salvo = new Salvo(damage.getVolleys(solution.getRange(), fireMode, solution.getTarget()), fireMode, solution.getArc());

			solution.getTarget().takeSalvo(salvo);
		}
		coolDown = recharge;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "coolDown", 0, coolDown));
	}
	
	public void sendFire(FireEvent e) {
		e.modifyToHit(Die.roundUp(e.getRange(), range) * rangePenalty, FireEvent.MOD_RANGE);
		e.modifyToHit(fireControl[e.getTarget().getObjectClass()], FireEvent.MOD_FIRE_CONTROL);
		if (fireMode.equals(PIERCING)) {
			e.modifyToHit(-4, FireEvent.MOD_PIERCING);
		}
	}
}
