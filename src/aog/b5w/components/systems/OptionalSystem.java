package aog.b5w.components.systems;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import aog.b5w.Space;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.PowerEvent;
import aog.b5w.event.PowerListener;
import aog.b5w.exception.PhaseException;


public class OptionalSystem extends System {

	protected int power; // The power the system draws from the reactor
	
	protected boolean enabled = true; // Flag to show if the system is enabled
	
	protected Set<PowerListener> powerListeners = new HashSet<PowerListener>();
	
	public OptionalSystem(String name, int hitPoints, int armour, int power) {
		super(name, hitPoints, armour);
		this.power = power;
		this.attributes.add("Optional");
	}
	
	public int getPower() {
		return power;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void enable() throws PhaseException {
		if (Space.getPhase() != PHASE_POWER) {
			throw new PhaseException("Can only enable systems in Power phase.");
		}
		if (enabled) {
			return;
		}
		if (destroyed) {
			throw new PhaseException("System is destroyed.");
		}
		
		PowerEvent e = new PowerEvent(true, power, this);
		notifyEnableListeners(e, BEFORE);
		
		if (e.getError() == null) {
			enabled = true;
			notifyEnableListeners(e, AFTER);
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "enabled", false, true));
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentPower", 0, power));
		} else {
			throw new PhaseException(e.getError());
		}
	}
	
	public void disable() throws PhaseException {
		if (Space.getPhase() != PHASE_POWER) {
			throw new PhaseException("Can only disable systems in Power phase.");
		}
		if (!enabled) {
			return;
		}
		
		PowerEvent e = new PowerEvent(false, power, this);
		notifyEnableListeners(e, BEFORE);
		if (e.getError() == null) {
			enabled = false;
			notifyEnableListeners(e, AFTER);
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "enabled", true, false));
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentPower", power, 0));
		} else {
			throw new PhaseException(e.getError());
		}
	}

	public void addPowerListener(PowerListener l) {
		synchronized(powerListeners) {
			powerListeners.add(l);
		}
	}
	
	public void removePowerListener(PowerListener l) {
		synchronized(powerListeners) {
			powerListeners.remove(l);
		}		
	}
	
	public void notifyEnableListeners(PowerEvent e, int sequence) {
		synchronized(powerListeners) {
			Iterator<PowerListener> it = powerListeners.iterator();
			while (it.hasNext()) {
				if (sequence == BEFORE) {
					if (e.getEnabled()) {
						it.next().beforeEnable(e);
					} else {
						it.next().beforeDisable(e);
					}
				} else {
					if (e.getEnabled()) {
						it.next().afterEnable(e);
					} else {
						it.next().afterDisable(e);
					}
				}
			}
		}
	}

}
