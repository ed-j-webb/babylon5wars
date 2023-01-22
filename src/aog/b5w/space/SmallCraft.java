package aog.b5w.space;

import java.util.Map;
import java.util.Set;

import aog.b5w.components.systems.weapons.Salvo;
import aog.b5w.event.PhaseEvent;
import aog.b5w.utils.Die;
import aog.b5w.utils.TargetingSolution;

public class SmallCraft extends Craft {

	protected boolean launched;
	protected int thrustPoints;
	protected int currentThrust;
	
	public SmallCraft(String name, int facing, int x, int y) {
		super(name, facing, x, y);
    	this.attributes.add("Small Craft");
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
		
		if (launched) {
			initiative -= 10;
		}
		
		initiative += Die.roll(20) + 1;
	}

	public boolean accelerate(int amount) {
		if (Math.abs(amount) > currentThrust) {
			return false;
		} else {
			velocity += amount;
			currentThrust -= Math.abs(amount); 
			return true;
		}
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_POWER) {
			currentThrust = thrustPoints;
		}
	}

	public void launch(int facing, int travel, int velocity) {
		this.directionOfFacing = facing;
		this.directionOfTravel = travel;
		this.velocity = velocity;
		this.launched = true;
	}
	
	public boolean isLaunched() {
		return launched;
	}

	public int getThrustPoints() {
		return thrustPoints;
	}

	@Override
	public void acceptTargets(Map<Integer, Set<TargetingSolution>> targetingSolutions) {
		// TODO accept targets
	}

	@Override
	public void takeSalvo(Salvo s) {
		//TODO take damage
	}
}
