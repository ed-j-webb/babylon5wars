package aog.b5w.components.systems;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aog.b5w.Space;
import aog.b5w.components.Component;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.PowerEvent;
import aog.b5w.event.PowerListener;
import aog.b5w.event.PhaseEvent;
import aog.b5w.log.LogEntry;

public class Reactor extends System implements PowerListener {

	private static Logger log = Logger.getLogger(Reactor.class);
	
    int power = 0;                // Extra Power Reactor can produce in current state.
    int surplus = 0;              // Extra Power remaining this turn.

    /**
     * A Reactor is constructed by giving it a Name, HitPoints, Armour rating
     * and initial Power.
     */
    public Reactor(String name,int hitPoints,int armour, int power) {
        super(name, hitPoints, armour);

        type = "Reactor";

        this.power = power;
        this.surplus = power;
        
        attributes.add("Reactor");

        criticals = new int[] {11, 15, 19, 27};
    }

	/**
	 * If the initial power is omitted the Reactor is assumed to have no surplus
	 */
	public Reactor(String n, int h, int a){
        this(n,h,a,0);
    }

    /**
     * Returns the extra Power
     */
    public int getPower(){
        return power;
    }

    /**
     * Returns the extra power remaining this turn
     * @return
     */
    public int getSurplus() {
    	return surplus;
    }
    
    public void commission(List<Component> systems) {
    	Iterator<Component> it = systems.iterator();
    	while (it.hasNext()) {
    		Component s = it.next();
    		if (s.hasAttribute("Optional")) {
    			((OptionalSystem)s).addPowerListener(this);
    		}
    		if (s.hasAttribute("Engine")) {
    			((Engine)s).addPowerListener(this);
    		}
    	}
    }
    
    /**
     * Calculate the surplus power produced by the Reactor at the start of the Power phase.
     */
    public void beforePhase(PhaseEvent e){
    	super.beforePhase(e);
    	if (e.getPhase() == PHASE_POWER) {
    		int oldSurplus = surplus;
    		surplus = power;
    		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", oldSurplus, surplus));
    	}
    }

    public void isReady(PhaseEvent e) {
    	super.isReady(e);
    	if (e.getPhase() == PHASE_POWER) {
    		if (surplus < 0) {
    			e.unready(structure.getCraft());
    		}
    	}
    }
    
    public void afterPhase(PhaseEvent e) {
    	super.afterPhase(e);
    	if (e.getPhase() == PHASE_POWER) {
    		if (surplus < 0) {
    			// TODO deactivate systems until surplus = 0
    			// If surplus is still < 0 set destroyed flag
    		}
    	}
    }
    
    /**
     * If Critical1 is suffered then the Reactor loses 2 of its power
     * output.
     */
    protected void critical1(){
        power -= 2;
        surplus -= 2;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power + 2, power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + 2, surplus));
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Critical: Reactor Output reduced by 2.");
		log.info(entry);
    }

    /**
     * If Critical2 is suffered then the Reactor loses 4 of its power
     * output.
     */
    protected void critical2(){
        power -= 4;
        surplus -= 4;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power + 4, power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + 4, surplus));
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Critical: Reactor Output reduced by 4.");
		log.info(entry);
    }

	/**
     * If Critical3 is suffered then the Reactor loses 6 of its power
     * output.
     */
    protected void critical3(){
        power -= 6;
        surplus -= 6;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power + 6, power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + 6, surplus));
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Critical: Reactor Output reduced by 6.");
		log.info(entry);
    }
    
    /**
     * If Critical4 is suffered then the Reactor loses 8 of its power
     * output.
     */
    protected void critical4(){
        power -= 8;
        surplus -= 8;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power + 8, power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + 8, surplus));
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Critical: Reactor Output reduced by 8.");
		log.info(entry);
    }

    /**
     * If Critical5 is suffered then the Reactor loses 10 of its power
     * output.
     */
    protected void critical5(){
        power -= 10;
        surplus -= 10;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power + 10, power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + 10, surplus));
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.CRITICAL, "Critical: Reactor Output reduced by 10.");
		log.info(entry);
    }

	@Override
	public void beforeEnable(PowerEvent e) {
		if (destroyed || e.getAmount() > surplus) {
			e.setError("Insufficient Power to Enable System");
		}
	}

	@Override
	public void afterEnable(PowerEvent e) {
		surplus -= e.getAmount();
		power -= e.getAmount();
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power + e.getAmount(), power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + e.getAmount(), surplus));
	}

	@Override
	public void beforeDisable(PowerEvent e) {
		// Do nothing
	}

	@Override
	public void afterDisable(PowerEvent e) {
		surplus += e.getAmount();
		power += e.getAmount();
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "power", power - e.getAmount(), power));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus - e.getAmount(), surplus));
	}

	@Override
	public void beforeDrawPower(PowerEvent e) {
		if (destroyed || e.getAmount() > surplus) {
			e.setError("Insufficient Power");
		}
	}

	@Override
	public void afterDrawPower(PowerEvent e) {
		surplus -= e.getAmount();
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "surplus", surplus + e.getAmount(), surplus));
		//power -= e.getAmount();
	}
}
