package aog.b5w;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PhaseListener;
import aog.b5w.space.SpaceObject;
import aog.b5w.utils.Arc;

public class Space implements B5WConstants {

	
	protected static List<SpaceObject> objects = new ArrayList<SpaceObject>();
	
	protected static List<PhaseListener> phaseListeners = new CopyOnWriteArrayList<PhaseListener>();
	
	protected static int turn = 0;
	protected static int phase = 0;
	
	protected static Arc targetingArcs;
	
	private Space() {
	}
	
	public static void clear() {
		turn = 0;
		phase = 0;
		objects.clear();
		phaseListeners.clear();
	}
	
	public static int getTurn() {
		return turn;
	}
	
	public static int getPhase() {
		return phase;
	}

	public static void add(SpaceObject o) {
		objects.add(o);
		addPhaseListener(o);
	}

	public static void remove(SpaceObject o) {
		removePhaseListener(o);
		objects.remove(o);
	}

	public static List<SpaceObject> getObjects() {
		return objects;
	}

	public static Arc getTargetingArcs() {
		return targetingArcs;
	}
	
	public static void addPhaseListener(PhaseListener l) {
		synchronized(phaseListeners) {
			if (!phaseListeners.contains(l)) {
				phaseListeners.add(l);
			}
		}
	}
	
	public static void removePhaseListener(PhaseListener l) {
		synchronized(phaseListeners) {
			phaseListeners.remove(l);
		}		
	}
	
	public static List<SpaceObject> advancePhase() {
		return advancePhase(false);
	}
	
	public static List<SpaceObject> advancePhase(boolean force) {
		int sequence = 0;
		
		if (turn > 0) {
			// Check Readiness
			sequence = PhaseEvent.READY;
			PhaseEvent e = new PhaseEvent(sequence, phase);
			notifyPhaseListeners(e, sequence);
			if (e.getUnready().size() > 0 && ! force) {
				sequence = PhaseEvent.INPHASE;
				return e.getUnready();
			}
			
			// Notify After Phase Event
			sequence = PhaseEvent.AFTER;
			e = new PhaseEvent(sequence, phase);
			notifyPhaseListeners(e, sequence);
			
			if (phase == PHASE_LAUNCH) {
				phase = PHASE_POWER;
				turn++;
			} else {
				phase++;
			}
				
			// Notify Before Phase Event
			sequence = PhaseEvent.BEFORE;
			e = new PhaseEvent(sequence, phase);
			notifyPhaseListeners(e, sequence);
			
			sequence = PhaseEvent.INPHASE;
		} else {
			turn = 1;
			sequence = PhaseEvent.BEFORE;
			phase = PHASE_POWER;
			PhaseEvent e = new PhaseEvent(sequence, phase);
			notifyPhaseListeners(e, sequence);
			sequence = PhaseEvent.INPHASE;
		}
		
		return new ArrayList<SpaceObject>();
	}
	
	public static void notifyPhaseListeners(PhaseEvent e, int sequence) {
		if (sequence == PhaseEvent.BEFORE){
			beforePhase(e);
		} 
		
		synchronized(phaseListeners) {
			Iterator<PhaseListener> it = phaseListeners.iterator();
			while (it.hasNext()) {
				if (sequence == PhaseEvent.BEFORE) {
					it.next().beforePhase(e);
				} else if (sequence == PhaseEvent.READY) {
					it.next().isReady(e);
				} else {
					it.next().afterPhase(e);
				}
			}
		}

		if (sequence == PhaseEvent.AFTER) {
			afterPhase(e);
		}
	}

	public static void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_TARGET) {
			targetingArcs = new Arc(objects);
			Iterator<SpaceObject> it = objects.iterator();
			while (it.hasNext()) {
				SpaceObject o = it.next();
				o.acceptTargets(targetingArcs.getArcs(o.getX(), o.getY()));
			}
			
		}
	}

	public static void afterPhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_FIRE) {
			targetingArcs = null;
		}
	}
	
	
	
}
