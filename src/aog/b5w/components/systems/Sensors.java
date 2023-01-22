package aog.b5w.components.systems;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import aog.b5w.Space;
import aog.b5w.components.Component;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.FireEvent;
import aog.b5w.event.FireListener;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PowerEvent;
import aog.b5w.event.PowerListener;
import aog.b5w.exception.PhaseException;
import aog.b5w.log.LogEntry;
import aog.b5w.space.SmallCraft;
import aog.b5w.space.SpaceObject;
import aog.b5w.utils.Die;

public class Sensors extends OptionalSystem implements FireListener {

	private static Logger log = Logger.getLogger(Sensors.class);
	
	protected int initialEw; // the EW generated for free per turn
	protected int currentEw; // the current EW points 
	protected int defensiveEw;
	protected int closeCombatEw;
	
	protected boolean overloaded;
	
	protected Map<SpaceObject, Integer> offensiveEw = new HashMap<SpaceObject, Integer>();
	
	public Sensors(String name, int hitPoints, int armour, int power, int ew) {
		super(name, hitPoints, armour, power);
		this.initialEw = ew;
		this.defensiveEw = ew;
		this.currentEw = ew;
		attributes.add("Sensors");
		type = "Sensors";
		
		criticals = new int[] {15, 19, 23, 27};
	}

	@Override
	public void commission(List<Component> systems) {
		super.commission(systems);
		getCraft().addFireListener(this);
	}

    public int getInitialEw() {
		return initialEw;
	}

	public int getCurrentEw() {
		return currentEw;
	}

	public int getDefensiveEw() {
		return defensiveEw;
	}

	public int getCloseCombatEw() {
		return closeCombatEw;
	}

	public Map<SpaceObject, Integer> getOffensiveEw() {
		return offensiveEw;
	}
	
	public int getOffensiveEw(SpaceObject o) {
		if (offensiveEw.keySet().contains(o)) {
			return offensiveEw.get(o);
		} else {
			return 0;
		}
	}

	/**
     * Method to produce extra EW. This method takes an Integer that is the
     * amount of EW to be produced and a Reactor object that is to provide
     * the extra power. If the reactor has sufficient surplus power then this
     * is subtracted from the reactor and true if not then it returns false.
     */
    public void produceEW(int extra) throws PhaseException {
        if (Space.getPhase() != PHASE_POWER) {
        	throw new PhaseException("Can only create EW during the Power phase");
        }
    	if (!destroyed && enabled) {
            int power = 0;
            for (int i = 1; i <= extra; i++) {
                power += currentEw + i;
            }
            PowerEvent e = new PowerEvent(power, this);
            notifyPowerListeners(e, BEFORE);
            if (e.getError() == null) {
                currentEw += extra;
                defensiveEw += extra;
                changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentEw", currentEw - extra, currentEw));
                changeNotifier.notifyChangeListeners(new ChangeEvent(this, "defensiveEw", defensiveEw - extra, defensiveEw));
                notifyPowerListeners(e, AFTER);
        		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.POWER, extra + " Extra EW Produced.");
        		log.info(entry);
            } else {
            	throw new PhaseException(e.getError());
            }
        }
    }
	
    public void target(SpaceObject o, int ew) throws PhaseException {
        if (Space.getPhase() != PHASE_EW) {
        	throw new PhaseException("Can only target EW during the EW phase");
        }
    	if (destroyed) {
    		throw new PhaseException("Sensors are destroyed");
    	}
    	if (!enabled) {
    		throw new PhaseException("Sensors are disabled");
    	}
       	if (ew <= defensiveEw) {
       		if (offensiveEw.containsKey(o)) {
       			int currEw = offensiveEw.get(o);
       			if (currEw + ew <= 0) {
       				ew = -currEw;
       				offensiveEw.remove(o);
       				changeNotifier.notifyChangeListeners(new ChangeEvent(this, false, "offensiveEw:" + o.getName(), currEw));
       			} else {
       				offensiveEw.put(o, offensiveEw.get(o) + ew);
       				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "offensiveEw:" + o.getName(), currEw, currEw + ew));
       			}
       		} else {
       			if (ew > 0) {
       				offensiveEw.put(o, ew);
       				changeNotifier.notifyChangeListeners(new ChangeEvent(this, true, "offensiveEw:" + o.getName(), ew));
       			} else {
       				ew = 0;
       			}
       		}
       		defensiveEw -= ew;
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "defensiveEw", defensiveEw + ew, defensiveEw));

       	} else {
       		throw new PhaseException("Insufficient EW to target with");
        }
    }

    public void targetCloseCombat(SmallCraft o, int ew) throws PhaseException {
    	if (Space.getPhase() != PHASE_TARGET) {
    		throw new PhaseException("Can only target CCEW during the Target Phase");
    	}
    	if (destroyed) {
    		throw new PhaseException("Sensors are destroyed");
    	}
    	if (!enabled) {
    		throw new PhaseException("Sensors are disabled");
    	}
    	if (Die.distance(getCraft().getX(), getCraft().getY(), o.getX(), o.getY()) > 10) {
    		throw new PhaseException("Target must be within 10 hexes");
    	}
    	
       	if (ew <= closeCombatEw) {
       		if (offensiveEw.containsKey(o)) {
       			int currEw = offensiveEw.get(o);
       			if (currEw + ew < 0) {
       				ew = -currEw;
       				offensiveEw.remove(o);
       			} else {
       				offensiveEw.put(o, offensiveEw.get(o) + ew);
       			}
       		} else {
       			if (ew > 0) {
       				offensiveEw.put(o, ew);
       			} else {
       				ew = 0;
       			}
       		}
       		closeCombatEw -= ew;
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "closeCombatEw", closeCombatEw + ew, closeCombatEw));
       	} else {
       		throw new PhaseException("Insufficient CCEW to target with");
        }
    }
    
    public void closeCombat(int ew) throws PhaseException {
        if (Space.getPhase() != PHASE_EW) {
        	throw new PhaseException("Can only assign Close Combat EW during the EW phase");
        }
    	if (destroyed) {
    		throw new PhaseException("Sensors are destroyed");
    	}
    	if (!enabled) {
    		throw new PhaseException("Sensors are disabled");
    	}
       	if (ew <= defensiveEw) {
       		closeCombatEw += ew;
       		defensiveEw -= ew;
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "defensiveEw", defensiveEw + ew, defensiveEw));
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "closeCombatEw", closeCombatEw - ew, closeCombatEw));

       	} else {
       		throw new PhaseException("Insufficient EW to assign to Close Combat");
       	}
    }    	
    
	/**
	 * Sensors efficiency reduced by 2
	 */
    @Override
	protected void critical1() {
		initialEw -= 2;
		if (initialEw < 0) {
			initialEw = 0;
		}
	}
    
    /**
     * Sensors overloaded for a turn
     */
    protected void critical2() {
    	overloaded = true;
    }
    
    protected void notifyPowerListeners(PowerEvent e, int sequence) {
		synchronized(powerListeners) {
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

	@Override
	public void beforePhase(PhaseEvent e) {
		super.beforePhase(e);
		if (e.getPhase() == PHASE_POWER) {
			int oldCurrentEw = currentEw;
			int oldDefensiveEw = defensiveEw;
			int oldCloseCombatEw = closeCombatEw;
			Map<SpaceObject, Integer> oldOffensiveEw = new HashMap<SpaceObject, Integer>();
			oldOffensiveEw.putAll(offensiveEw);
			if (overloaded) {
				currentEw = 0;
				defensiveEw = 0;
				closeCombatEw = 0;
				offensiveEw.clear();
			} else {
				offensiveEw.clear();
				defensiveEw = initialEw;
				currentEw = initialEw;
				closeCombatEw = 0;
			}
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentEw", oldCurrentEw, currentEw));
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "defensiveEw", oldDefensiveEw, defensiveEw));
            changeNotifier.notifyChangeListeners(new ChangeEvent(this, "closeCombatEw", oldCloseCombatEw, closeCombatEw));
            Iterator<Map.Entry<SpaceObject, Integer>> it = oldOffensiveEw.entrySet().iterator();
            while (it.hasNext()) {
            	Map.Entry<SpaceObject, Integer> off = it.next();
   				changeNotifier.notifyChangeListeners(new ChangeEvent(this, false, "offensiveEw:" + off.getKey().getName(), off.getValue()));
            }
		
		} else if (e.getPhase() == PHASE_CRITICAL) {
			overloaded = false;
		}
	}
    
	@Override
	public void takeFire(FireEvent e) {
		e.modifyToHit(-defensiveEw, FireEvent.MOD_DEFENSIVE_EW);
	}


	@Override
	public void sendFire(FireEvent e) {
		if (offensiveEw.containsKey(e.getTarget())) {
			e.modifyToHit(offensiveEw.get(e.getTarget()), FireEvent.MOD_OFFENSIVE_EW);
		} else {
			e.modifyToHit(e.getModifier(FireEvent.MOD_RANGE), FireEvent.MOD_OFFENSIVE_EW);
		}
	}
}
