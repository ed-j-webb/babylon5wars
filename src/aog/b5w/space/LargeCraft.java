package aog.b5w.space;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aog.b5w.Space;
import aog.b5w.components.Component;
import aog.b5w.components.Rollable;
import aog.b5w.components.Structure;
import aog.b5w.components.systems.System;
import aog.b5w.components.systems.weapons.Salvo;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.ChangeListener;
import aog.b5w.event.FireEvent;
import aog.b5w.event.FireListener;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.ThrustEvent;
import aog.b5w.event.ThrustListener;
import aog.b5w.utils.ClockIterator;
import aog.b5w.utils.Die;
import aog.b5w.utils.TargetingSolution;

public class LargeCraft extends Craft implements ThrustListener, ChangeListener {

	protected Map<Integer, Structure> structures = new HashMap<Integer, Structure>();
	
    protected int accelerationCost = 1;
    protected int partialAcceleration = 0;
	
    protected int[] turnCost = new int[2];
    protected int[] turnDelay = new int[2];
    
    protected int pivotStartCost;
    protected int pivotEndCost;
    
    protected int rollStartCost;
    protected int rollEndCost;
    
    protected int moveCost[] = new int[THRUSTS.length];
    protected int moveThrust[][] = new int[THRUSTS.length][LOCATIONS.length];
    protected int moveDelay[] = new int[THRUSTS.length];
    protected int moveWait[] = new int[THRUSTS.length];
    protected int slideThrustDirection = NONE; 
    protected int pivoting = NONE;
    protected int rolling = NONE;
    
    public static final int[] CAPITAL_STRUCTURE_FACING = new int[] {FORE, STARBOARD, STARBOARD, STARBOARD, STARBOARD, AFT, AFT, PORT, PORT, PORT, PORT, FORE};
    public static final int[] MEDIUM_STRUCTURE_FACING = new int[] {FORE, FORE, FORE, AFT, AFT, AFT, AFT, AFT, AFT, FORE, FORE, FORE};
    
    protected int[] structureFacing;
    
	protected List<FireListener> fireListeners = new ArrayList<FireListener>();

	public LargeCraft(String name, int facing, int x, int y) {
		super(name, facing, x, y);
    	this.attributes.add("Craft");
    	this.attributes.add("Large Craft");
	}

	public void addStructure(Structure structure, int location) {
		if (location >= 0 && location < LOCATIONS.length) {
			structures.put(location, structure);
			structure.setCraft(this, location);
			structure.addChangeListener(this);
		}
	}
	
	public Map<Integer, Structure> getStructures() {
		return structures;
	}
	
	public Structure getStructure(int location) {
		return structures.get(location);
	}
	
	public int getAccelerationCost() {
		return accelerationCost;
	}
	
	public int getPartialAcceleration() {
		return partialAcceleration;
	}

	public int[] getTurnCost() {
		return turnCost;
	}

	public int[] getTurnDelay() {
		return turnDelay;
	}

	public int getPivotStartCost() {
		return pivotStartCost;
	}

	public int getPivotEndCost() {
		return pivotEndCost;
	}

	public int[] getMoveCost() {
		return moveCost;
	}

	public int[][] getMoveThrust() {
		return moveThrust;
	}

	public int[] getMoveDelay() {
		return moveDelay;
	}

	public int[] getMoveWait() {
		return moveWait;
	}

	public int getSlideThrustDirection() {
		return slideThrustDirection;
	}

	public int getPivoting() {
		return pivoting;
	}
	
	public int getRolling() {
		return rolling;
	}

	@Override
	public void calculateInitiative() {
		initiative = 0;
		
		if (hasAttribute("Light")) {
			initiative += 4;
		} else if (hasAttribute("Medium")) {
			initiative += 2;
		}
		
		if (hasAttribute("Minbari")) {
			initiative += 1;
		}
		
		if (velocity < 5) {
			initiative -= 5 - velocity;
		}
		
		// TODO if the craft has launched fighters then -4 penalty
		//if (launched) {
		//	initiative -= 4;
		//}
		
		initiative += Die.roll(20) + 1;
	}

	protected void calculateMoveCosts() {

		int speed = Math.abs(velocity);
		
		// Turn
		moveCost[TURN] = Die.roundUp(speed * turnCost[NUMERATOR], turnCost[DENOMINATOR]);
		if (moveCost[TURN] <= 0) {
			moveCost[TURN] = 1;
		}
		moveDelay[TURN] = Die.roundUp(speed * turnDelay[NUMERATOR], turnDelay[DENOMINATOR]);
		
		// Slide
		moveCost[SLIDE] = Die.roundUp(speed, 5);
		if (moveCost[SLIDE] <= 0) {
			moveCost[SLIDE] = 1;
		}
		moveDelay[SLIDE] = 1;
		
		// Pivot		
		if (pivoting == NONE || pivoting == FINISHED) {
			moveCost[PIVOT] = pivotStartCost;
			moveCost[PIVOT] = pivotStartCost;
		} else {
			moveCost[PIVOT] = pivotEndCost;
			moveCost[PIVOT] = pivotEndCost;
		}

		// Roll
		if (rolling == NONE || rolling == FINISHED) {
			moveCost[ROLL] = rollStartCost;
		} else {
			moveCost[ROLL] = rollEndCost;
		}

		for (int i = 0; i < THRUSTS.length; i++) {
			for (int j = 0; j < LOCATIONS.length; j++) {
				moveThrust[i][j] = 0;
			}
		}
	}
	
	private void clearThrust(int[] thrust) {
		for (int i = 0; i < LOCATIONS.length; i++) {
			thrust[i] = 0;
		}
	}
	
	public Set<System> getSystems(String attribute) {
		Iterator<Structure> it = structures.values().iterator();
		Set<System> set = new HashSet<System>();
		while (it.hasNext()) {
			set.addAll(it.next().getSystems(attribute));
		}
		return set;
	}
	
	public void commission() {
		List<Component> components = new ArrayList<Component>();
		Iterator<Structure> it = structures.values().iterator();
		while (it.hasNext()) {
			Structure s = it.next();
			components.add(s);
			components.addAll(s.getSystems());
		}
		
		Iterator<Component> sub = components.iterator();
		while (sub.hasNext()) {
			Component comp = sub.next();
			comp.commission(components);
		}
	}
	
	public void addFireListener(FireListener l) {
		synchronized (fireListeners) {
			if (!fireListeners.contains(l)) {
				fireListeners.add(l);
			}
		}
	}

	public void removeFireListener(FireListener l) {
		synchronized (fireListeners) {
			fireListeners.remove(l);
		}
	}

	protected void changePosition(int amount, int direction) {
		super.changePosition(amount, direction);
		for (int i = 0; i< THRUSTS.length; i++) {
			moveWait[i] -= Math.abs(amount);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		changeNotifier.notifyChangeListeners(e);
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		super.beforePhase(e);
		Iterator<Structure> it = structures.values().iterator();
		while (it.hasNext()) {
			Structure s = it.next();
			s.beforePhase(e);
		}
		if (e.getPhase() == PHASE_ACCELERATION) {
			partialAcceleration = 0;
		} else if (e.getPhase() == PHASE_MOVE) {
			calculateMoveCosts();
			if (pivoting == CLOCKWISE) {
				directionOfFacing = Die.plus(directionOfFacing, 2);
			} else if (pivoting == ANTICLOCKWISE) {
				directionOfFacing = Die.plus(directionOfFacing, 10);
			}
			if (pivoting > NONE) {
				if (Math.abs(directionOfFacing - directionOfTravel) == 6) {
					directionOfTravel = Die.plus(directionOfTravel, 6);
					velocity = -velocity;
				}
			} else if (pivoting == FINISHED) {
				pivoting = NONE;
			}
			if (rolling == ACTIVE) {
				roll();
			} else if (rolling == FINISHED) {
				rolling = NONE;
			}
		}
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		super.afterPhase(e);
		Iterator<Structure> it = structures.values().iterator();
		while (it.hasNext()) {
			Structure s = it.next();
			s.afterPhase(e);
		}
	}

	@Override
	public void beforeThrust(ThrustEvent e) {
		if (Space.getPhase() == PHASE_MOVE) {
			if (e.getType() == TURN_CLOCKWISE || e.getType() == TURN_ANTICLOCKWISE) {
				beforeTurn(e);
			} else if (e.getType() == SLIDE) {
				beforeSlide(e);
			} else if (e.getType() == PIVOT_CLOCKWISE || e.getType() == PIVOT_ANTICLOCKWISE) {
				beforePivot(e);
			} else if (e.getType() == ROLL) {
				beforeRoll(e);
			}
		}
	}

	protected void beforeTurn(ThrustEvent e) {
		if (directionOfFacing != directionOfTravel) {
			e.cancel("Craft is not travelling in the direction it is facing");
			return;
		} else if (rolling == ACTIVE) {
			e.cancel("Craft is rolling and cannot turn");
			return;
		} else if (moveWait[TURN] > 0) {
			e.cancel("Craft must move " + moveWait[TURN] + " hexes before turning again");
			return;
		}
		if (e.getType() == TURN_CLOCKWISE) {
			if (velocity >= 0) {
				if (moveThrust[TURN][STARBOARD] > 0) {
					e.cancel("Craft is already turning Anti-clockwise");
					return;
				} else if (e.getDirection() == STARBOARD) {
					e.cancel("Cannot use Starboard thrusters to turn Clockwise");
					return;
				} else if (e.getDirection() == FORE) {
					e.cancel("Cannot use Retro thrusters to turn Clockwise");
					return;
				}
			} else if (velocity < 0) {
				if (moveThrust[TURN][PORT] > 0) {
					e.cancel("Craft is already turning Anti-clockwise");
					return;
				} else if (e.getDirection() == PORT) {
					e.cancel("Cannot use Port thrusters to turn Clockwise");
					return;
				} else if (e.getDirection() == AFT) {
					e.cancel("Cannot use Aft thrusters to turn Clockwise");
					return;
				}
			}
		} else if (e.getType() == TURN_ANTICLOCKWISE) {
			if (velocity >= 0 && moveThrust[TURN][PORT] > 0) {
				e.cancel("Craft is already turning Clockwise");
				return;
			} else if (velocity < 0 && moveThrust[TURN][PORT] > 0) {
				e.cancel("Craft is already turning Anti-clockwise");
				return;
			}
			if (velocity >= 0) {
				if (moveThrust[TURN][PORT] > 0) {
					e.cancel("Craft is already turning Clockwise");
					return;
				} else if (e.getDirection() == PORT) {
					e.cancel("Cannot use Port thrusters to turn Anti-clockwise");
					return;
				} else if (e.getDirection() == FORE) {
					e.cancel("Cannot use Retro thrusters to turn Anti-clockwise");
					return;
				}
			} else if (velocity < 0) {
				if (moveThrust[TURN][STARBOARD] > 0) {
					e.cancel("Craft is already turning Clockwise");
					return;
				} else if (e.getDirection() == STARBOARD) {
					e.cancel("Cannot use Starboard thrusters to turn Anti-clockwise");
					return;
				} else if (e.getDirection() == AFT) {
					e.cancel("Cannot use Aft thrusters to turn Anti-clockwise");
					return;
				}
			}
		}
	}

	protected void beforeSlide(ThrustEvent e) {
		if (directionOfFacing != directionOfTravel) {
			e.cancel("Craft is not travelling in the direction it is facing");
			return;
		} else if (rolling == ACTIVE) {
			e.cancel("Craft is rolling and cannot slide");
			return;
		} else if (slideThrustDirection != NONE && e.getDirection() != slideThrustDirection) {
			e.cancel("Craft has already slid in opposite direction");
			return;
		} else if (moveWait[SLIDE] > 0) {
			e.cancel("Craft must move 1 hex before sliding again");
			return;
		} else if (moves <= 0) {
			e.cancel("Craft cannot move further this turn");
			return;
		}
	}

	protected void beforePivot(ThrustEvent e) {
		if (pivoting == e.getType() - PIVOT && pivoting != 0) {
			e.cancel("Craft is already pivoting " + ROTATION[pivoting]);
			return;
		} else if (rolling == ACTIVE) {
			e.cancel("Craft is rolling and cannot pivot");
			return;
		}
		if (e.getType() == PIVOT_CLOCKWISE) {
			if (moveThrust[PIVOT][STARBOARD] + moveThrust[PIVOT][AFT] > 0) {
				if (e.getDirection() != STARBOARD && e.getDirection() != AFT) {
					e.cancel("Craft is already using Starboard + Aft Pair to pivot");
					return;
				}
			} else if (moveThrust[PIVOT][PORT] + moveThrust[PIVOT][FORE] > 0) {
				if (e.getDirection() != PORT && e.getDirection() != FORE) {
					e.cancel("Craft is already using Port + Fore Pair to pivot");
					return;
				}
			}
		} else if (e.getType() == PIVOT_ANTICLOCKWISE) {
			if (moveThrust[PIVOT][STARBOARD] + moveThrust[PIVOT][FORE] > 0) {
				if (e.getDirection() != STARBOARD && e.getDirection() != FORE) {
					e.cancel("Craft is already using Starboard + Fore Pair to pivot");
					return;
				}
			} else if (moveThrust[PIVOT][PORT] + moveThrust[PIVOT][AFT] > 0) {
				if (e.getDirection() != PORT && e.getDirection() != AFT) {
					e.cancel("Craft is already using Port + Aft Pair to pivot");
					return;
				}
			}
		}
	}
	
	/**
	 * Called before a roll event
	 * @param e the ThrustEvent that caused the roll
	 */
	protected void beforeRoll(ThrustEvent e) {
		// Don't think there's anything yet
	}
	
	@Override
	public void afterThrust(ThrustEvent e) {
		if (Space.getPhase() == PHASE_ACCELERATION) {
			if (e.getDirection() == FORE) {
				velocity -= e.getOutputThrust() / accelerationCost;
				partialAcceleration -= e.getOutputThrust() % accelerationCost;
				if (partialAcceleration >= accelerationCost) {
					velocity --;
					partialAcceleration -= accelerationCost;
				}
			} else if (e.getDirection() == AFT) {
				velocity += e.getOutputThrust() / accelerationCost;
				partialAcceleration += e.getOutputThrust() % accelerationCost;
				if (partialAcceleration >= accelerationCost) {
					velocity ++;
					partialAcceleration -= accelerationCost;
				}
			}
		} else if (Space.getPhase() == PHASE_MOVE) {
			if (e.getType() == TURN_CLOCKWISE || e.getType() == TURN_ANTICLOCKWISE) {
				afterTurn(e);
			} else if (e.getType() == SLIDE) {
				afterSlide(e);
			} else if (e.getType() == PIVOT_CLOCKWISE || e.getType() == PIVOT_ANTICLOCKWISE) {
				afterPivot(e);
			} else if (e.getType() == ROLL) {
				afterRoll(e);
			}
		}
	}

	protected void afterTurn(ThrustEvent e) {
		int useableThrust = Math.min(Die.roundUp(moveCost[TURN] - moveThrust[TURN][e.getDirection()], 2), e.getOutputThrust());
		moveThrust[TURN][NONE] += useableThrust;
		moveThrust[TURN][e.getDirection()] += useableThrust;
		if (moveThrust[TURN][NONE] >= moveCost[TURN]) {
			int turning = e.getType() - TURN;
			if (turning == CLOCKWISE) {
				directionOfFacing = Die.plus(directionOfFacing, 2);
				directionOfTravel = Die.plus(directionOfTravel, 2);
			} else {
				directionOfFacing = Die.plus(directionOfFacing, 10);
				directionOfTravel = Die.plus(directionOfTravel, 10);
			}
			
			moveWait[TURN] = moveDelay[TURN];
			clearThrust(moveThrust[TURN]);
		}
	}
	
	protected void afterSlide(ThrustEvent e) {
		slideThrustDirection = e.getDirection();
		moveThrust[SLIDE][NONE] += e.getOutputThrust();
		if (moveThrust[SLIDE][NONE] >= moveCost[SLIDE]) {
			int direction = 0;
			if (velocity > 0) {
				if (slideThrustDirection == PORT) {
					direction = 2;
				} else {
					direction = 10;
				}
			} else {
				if (slideThrustDirection == PORT) {
					direction = 4;
				} else {
					direction = 8;
				}
			}
			changePosition(1, Die.plus(directionOfTravel, direction));
			moveWait[SLIDE] = moveDelay[SLIDE];
			clearThrust(moveThrust[SLIDE]);
		}
	}

	protected void afterPivot(ThrustEvent e) {
		int useableThrust = Math.min(Die.roundUp(moveCost[PIVOT] - moveThrust[PIVOT][e.getDirection()], 2), e.getOutputThrust());
		moveThrust[PIVOT][NONE] += useableThrust;
		moveThrust[PIVOT][e.getDirection()] += useableThrust;
		if (moveThrust[PIVOT][NONE] >= moveCost[PIVOT]) {
			
			// Either start or end a pivot
			if (pivoting == NONE) {
				pivoting = e.getType() - PIVOT;
			} else {
				pivoting = FINISHED;
			}
			
			// Immediately change the facing of the craft
			if (pivoting == CLOCKWISE) {
				directionOfFacing = Die.plus(directionOfFacing, 2);
			} else {
				directionOfFacing = Die.plus(directionOfFacing, 10);
			}
			clearThrust(moveThrust[PIVOT]);
		}
	}
	
	protected void afterRoll(ThrustEvent e) {
		moveThrust[ROLL][NONE] += e.getOutputThrust();
		moveThrust[ROLL][e.getDirection()] += e.getOutputThrust();
		if (moveThrust[ROLL][NONE] >= moveCost[ROLL]) {
			
			if (rolling == ACTIVE) {
				rolling = FINISHED;
			} else if (rolling == NONE) {
				rolling = ACTIVE;
			}
			clearThrust(moveThrust[ROLL]);
		}
	}
	
	public void sendFire(FireEvent e) {
		synchronized (fireListeners) {
			Iterator<FireListener> it = fireListeners.iterator();
			while (it.hasNext()) {
				it.next().sendFire(e);
			}
		}
	}
	
	public void takeFire(FireEvent e) {
		int arc = Die.plus(e.getArc(), 6);
		arc = Die.plus(arc, 12 - directionOfFacing);
		switch (arc) {
		case 0:
		case 11:
			e.modifyToHit(defence[FORE] - 20, FireEvent.MOD_DEFENCE);
			break;
		case 1:
		case 2:
		case 3:
		case 4:
			e.modifyToHit(defence[PORT] - 20, FireEvent.MOD_DEFENCE);
			break;
		case 5:
		case 6:
			e.modifyToHit(defence[AFT] - 20, FireEvent.MOD_DEFENCE);
			break;
		case 7:
		case 8:
		case 9:
		case 10:
			e.modifyToHit(defence[STARBOARD] - 20, FireEvent.MOD_DEFENCE);
			break;
		default:
		}

		synchronized (fireListeners) {
			Iterator<FireListener> it = fireListeners.iterator();
			while (it.hasNext()) {
				it.next().takeFire(e);
			}
		}
	}
	
	public void roll() {
		Structure temp = structures.get(PORT);
		structures.put(PORT, structures.get(STARBOARD));
		structures.put(STARBOARD, temp);
		Iterator<Structure> it = structures.values().iterator();
		while (it.hasNext()) {
			Iterator<System> sys = it.next().getSystems().iterator();
			while (sys.hasNext()) {
				System s = sys.next();
				if (Rollable.class.isAssignableFrom(s.getClass())) {
					((Rollable)s).roll();
				}
			}
		}
	}

	@Override
	public void takeSalvo(Salvo s) {
		int arc = Die.plus(s.getArc(), 6);
		arc = Die.plus(arc, 12 - directionOfFacing);
		if (s.getFireMode().equals(Weapon.PIERCING)) {
			if (structures.size() == 5) {
				structures.get(structureFacing[arc]).takeSalvo(s.extractVolley(0));
				structures.get(PRIMARY).takeSalvo(s.extractVolley(1));
				structures.get(structureFacing[Die.plus(arc, 6)]).takeSalvo(s.extractVolley(2));
			} else if (structures.size() == 3) {
				structures.get(structureFacing[arc]).takeSalvo(s.extractVolley(0));
				structures.get(PRIMARY).takeSalvo(s.extractVolley(1));
			} 
		} else {
			Structure struct = structures.get(structureFacing[arc]);
			struct.takeSalvo(s);
		}
	}

	@Override
	public void acceptTargets(Map<Integer, Set<TargetingSolution>> targetingSolutions) {
		Iterator<Structure> structs = structures.values().iterator();
		while (structs.hasNext()) {
			Iterator<System> it = structs.next().getSystems().iterator();
			while (it.hasNext()) {
				System sys = it.next();
				if (sys.getType().equals("Weapon")) {
					Weapon w = (Weapon)sys;
					Iterator<Integer> arcs = new ClockIterator(w.getArcStart(), w.getArcFinish());
					while (arcs.hasNext()) {
						w.acceptTargets(targetingSolutions.get(arcs.next()));
					}
				}
			}
		}
	}
	
	
}

