package aog.b5w.components.systems;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aog.b5w.Space;
import aog.b5w.components.Component;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.DamageEvent;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.ThrustEvent;
import aog.b5w.event.ThrustListener;
import aog.b5w.exception.PhaseException;
import aog.b5w.utils.Die;

public class Thruster extends System {

	protected int rating;
	protected int currentThrust;
	protected int efficiency;

	protected Set<ThrustListener> thrustListeners = new HashSet<ThrustListener>();

	public Thruster(String name, int hitPoints, int armour, int rating) {
		super(name, hitPoints, armour);

		type = "Thruster";

		this.rating = rating;
		efficiency = 1;

		criticals = new int[] {15, 20, 25};

		attributes.add("Thruster");
	}

	@Override
	public void commission(List<Component> systems) {
		super.commission(systems);
		addThrustListener(getCraft());
	}



	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getCurrentThrust() {
		return currentThrust;
	}

	public void setCurrentThrust(int thrust) {
		this.currentThrust = thrust;
	}

	public int getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}

	@Override
	protected void critical1() {
		efficiency++;
	}

	@Override
	protected void critical2() {
		rating--;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "rating", rating + 1, rating));
		if (rating <= 0) {
			DamageEvent e = new DamageEvent(DamageEvent.DESTROYED, this);
			notifyDamageListeners(e, BEFORE);
			if (e.isDestroyed()) {
				destroyed = true;
				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "destroyed", false, true));
				notifyDamageListeners(e, AFTER);
			}
		}
	}

	public void thrust(int thrust, int type) throws PhaseException {
		thrust(thrust, type, structure.getLocation());
	}
	
	public void thrust(int thrust, int type, int direction) throws PhaseException {
		String reason = "";
		if (currentThrust + thrust > rating * 2) {
			reason = "Cannot overthrust more than twice the Thruster's Rating";
		} else if (Space.getPhase() == PHASE_ACCELERATION) {
			reason = checkAccelerate(type, direction);
		} else if (Space.getPhase() == PHASE_MOVE) {
			reason = checkMove(type, direction);
		} else {
			reason = "Thrusters can only fire in the Acceleration and Move Phases";
		}
		
		if (reason.length() == 0) {
			int engineThrust = thrust * efficiency;
			ThrustEvent e = new ThrustEvent(engineThrust, thrust, type, direction, this);
			notifyThrustListeners(e, BEFORE);
			if (!e.isCancelled()) {
				currentThrust += thrust;
				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThrust", currentThrust - thrust, currentThrust));
				notifyThrustListeners(e, AFTER);
				return;
			} else {
				reason = e.getReason();
			}
		}
		throw new PhaseException(reason);
	}

	private String checkAccelerate(int type, int direction) {
		if (type != ACCELERATE) {
			return "Can only accelerate in Acceleration Phase";
		}
		if (direction != FORE && direction != AFT) {
			return "Only Fore and Aft Thrusters can be used in Accleration Phase";
		}
		if (direction != structure.getLocation()) {
			return "Can only fire Thruster in the direction it is facing in Acceleration Phase"; 
		}
		return "";
	}
	
	private String checkMove(int type, int direction) {
		if (type == SLIDE) {
			if (direction != PORT && direction != STARBOARD) {
				return "Only Port and Starboard Thrusters can be used to Slide in Move Phase";
			}
			if (direction != structure.getLocation()) {
				return "Can only fire Thruster in the direction it is facing to Slide in Move Phase"; 
			}
			return "";
		}
		return "";
	}
	
	public void addThrustListener(ThrustListener l) {
		synchronized (thrustListeners) {
			thrustListeners.add(l);
		}
	}

	public void removeThrustListener(ThrustListener l) {
		synchronized (thrustListeners) {
			thrustListeners.remove(l);
		}
	}

	public void notifyThrustListeners(ThrustEvent e, int sequence) {
		synchronized (thrustListeners) {
			Iterator<ThrustListener> it = thrustListeners.iterator();
			while (it.hasNext()) {
				if (sequence == BEFORE) {
					it.next().beforeThrust(e);
				} else {
					it.next().afterThrust(e);
				}
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_POWER) {
			int temp = currentThrust;
			currentThrust = 0;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThrust", temp, 0));
		} else if (e.getPhase() == PHASE_CRITICAL) {
			// Overthrust criticals
			for (int i = rating; i < currentThrust; i++) {
				int roll = Die.roll(20);
				if (roll < 2) {
					critical1();
				} else if (roll < 5) {
					critical2();
				}
			}
			super.beforePhase(e);
			return;
		}
		super.beforePhase(e);
	}
}
