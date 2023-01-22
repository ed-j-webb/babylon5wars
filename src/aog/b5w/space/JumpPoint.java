package aog.b5w.space;

import java.util.Map;
import java.util.Set;

import aog.b5w.Space;
import aog.b5w.components.systems.weapons.Salvo;
import aog.b5w.event.PhaseEvent;
import aog.b5w.utils.TargetingSolution;

public class JumpPoint extends SpaceObject {

	protected int open;

	public JumpPoint(int x, int y, int facing) {
		this.x = x;
		this.y = y;
		this.directionOfFacing = facing;
		this.directionOfTravel = facing;
		this.velocity = 0;
		this.open = 0;
		
		this.attributes.add("Jump Point");
	}

	public int getOpen() {
		return open;
	}

	public void maintain() {
		open = Math.abs(open);
		open++;
	}
	
	@Override
	public void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_JUMP_POINT) {
			if (open == 0) {
				open = 1;
			} else if (open > 0) {
				open = -open;
			}
		}
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_JUMP_POINT) {
			if (open < 0) {
				Space.remove(this);
			}
		}
	}
	
	@Override
	public void acceptTargets(Map<Integer, Set<TargetingSolution>> targetingSolutions) {
		// Jump points can't fire
	}

	public void takeSalvo(Salvo s) {
		// Firing at a Jump Point has no effect?
	}
	
}
