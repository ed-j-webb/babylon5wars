package aog.b5w.space;

import aog.b5w.Space;
import aog.b5w.event.PhaseEvent;
import aog.b5w.exception.PhaseException;

public abstract class Craft extends SpaceObject {

	public final static String CRAFT_DEFENCE[] = {"","Fore","Aft","Port","Stbd"};

	protected int points; // Point Value of Craft

	protected int id; // Unique identity number

    protected boolean destroyed = false; // Destroyed flag

    protected int initiative; // Current initiative of craft

    protected int initiativeBonus; // Initiative bonus of craft

    protected int defence[] = new int[5]; // Defensive bonus of craft
    
    protected int moves = 0;
    
    public Craft(String name, int facing, int x, int y) {
    	this.name = name;
    	this.directionOfTravel = facing;
    	this.x = x;
    	this.y = y;
    	this.attributes.add("Craft");
    }

	public int getPoints() {
		return points;
	}

	public int getId() {
		return id;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public int getInitiative() {
		return initiative;
	}

	public int getInitiativeBonus() {
		return initiativeBonus;
	}

	public int[] getDefence() {
		return defence;
	}
	
	public abstract void calculateInitiative();

	public void move(int amount) throws PhaseException {
		if (Space.getPhase() != PHASE_MOVE) {
			throw new PhaseException("Can only move in Move phase");
		}
		if (amount > moves) {
			throw new PhaseException("Cannot move more than " + moves + " hexes");
		}
		if (velocity < 0) {
			amount = -amount;
		}
		changePosition(amount, directionOfTravel);
	}
	
	protected void changePosition(int amount, int direction) {
		switch (direction) {
		case 0:
			y += amount;
			break;
		case 2:
			x += amount;
			break;
		case 4:
			x += amount;
			y -= amount;
			break;
		case 6:
			y -= amount;
			break;
		case 8:
			x -= amount;
			break;
		case 10:
			x -= amount;
			y += amount;
			break;
		}
		moves -= Math.abs(amount);
	}
	
	@Override
	public void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_INITIATIVE) {
			calculateInitiative();
		} else if (e.getPhase() == PHASE_MOVE) {
			moves = Math.abs(velocity);
		}
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_MOVE) {
			// Craft MUST move all their hexes each turn
			if (moves > 0) {
				changePosition(moves, directionOfTravel);
			}
		}
	}
	
	@Override
	public void isReady(PhaseEvent e) {
	}

	public int getMoves() {
		return moves;
	}
	
	
	
}
