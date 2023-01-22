package aog.b5w.components.systems;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aog.b5w.Space;
import aog.b5w.components.Component;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PowerEvent;
import aog.b5w.event.PowerListener;
import aog.b5w.event.ThrustEvent;
import aog.b5w.event.ThrustListener;
import aog.b5w.exception.PhaseException;

public class Engine extends System implements ThrustListener {

	
	protected int initialThrust;
	protected int thrust;
	protected int currentThrust;
	protected int efficency;

	protected Set<PowerListener> powerListeners = new HashSet<PowerListener>();

	public Engine(String name, int hitPoints, int armour, int thrust, int efficiency) {
		super(name, hitPoints, armour);

		type = "Engine";

		this.initialThrust = thrust;
		this.thrust = thrust;
		this.currentThrust = thrust;
		this.efficency = efficiency;

		attributes.add("Engine");

		criticals = new int[] {15, 21, 28};
	}

	public int getInitialThrust() {
		return initialThrust;
	}

	public int getThrust() {
		return thrust;
	}
	
	public int getCurrentThrust() {
		return currentThrust;
	}

	public int getEfficency() {
		return efficency;
	}

	public void commission(List<Component> systems) {
		Iterator<Component> it = systems.iterator();
		while (it.hasNext()) {
			Component s = it.next();
			if (s.hasAttribute("Thruster")) {
				((Thruster) s).addThrustListener(this);
			}
		}
	}

	public void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_POWER) {
			int temp = currentThrust;
			currentThrust = initialThrust;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThrust", temp, currentThrust));
			temp = thrust;
			thrust = initialThrust;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "thrust", temp, thrust));
		}
	}

	public void produceThrust(int t) throws PhaseException {
		if (Space.getPhase() != PHASE_POWER) {
			throw new PhaseException("Engine can only produce thrust in the Power Phase");
		}
		if (destroyed) {
			throw new PhaseException("Engine is destroyed");
		}
		int power = t * efficency;
		PowerEvent e = new PowerEvent(power, this);
		notifyPowerListeners(e, BEFORE);
		if (e.getError() == null) {
			thrust += t;
			currentThrust += t;
			notifyPowerListeners(e, AFTER);
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "thrust", thrust - t, thrust));
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThrust", currentThrust - t, currentThrust));
		} else {
			throw new PhaseException(e.getError());
		}
	}

	@Override
	public void beforeThrust(ThrustEvent e) {
		if (e.getInputThrust() > currentThrust) {
			e.cancel("Insufficient Engine thrust");
		}
	}

	@Override
	public void afterThrust(ThrustEvent e) {
		currentThrust -= e.getInputThrust();
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThrust", currentThrust + e.getInputThrust(), currentThrust));
	}

	@Override
	protected void critical1() {
		initialThrust -= 2;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "initialThrust", initialThrust + 2, initialThrust));
		if (initialThrust < 0) {
			initialThrust = 0;
		}
	}

	@Override
	protected void critical2() {
		efficency += 1;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "efficiency", efficency - 1, efficency));
	}

	public void addPowerListener(PowerListener l) {
		synchronized (powerListeners) {
			powerListeners.add(l);
		}
	}

	public void removePowerListener(PowerListener l) {
		synchronized (powerListeners) {
			powerListeners.remove(l);
		}
	}

	public void notifyPowerListeners(PowerEvent e, int sequence) {
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

}
