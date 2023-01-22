package aog.b5w.components.systems;

import aog.b5w.Space;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PhaseListener;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.JumpPoint;
import aog.b5w.utils.Die;

public class JumpEngine extends OptionalSystem implements PhaseListener {

	protected int jumpDelay;
	protected int currentJumpDelay;
	
	protected int range;
	
	protected JumpPoint point;
	
	public JumpEngine(String name, int hitPoints, int armour, int power, int delay) {
		this(name, hitPoints, armour, power, delay, 4);
	}

	public JumpEngine(String name, int hitPoints, int armour, int power, int delay, int range) {
		super(name, hitPoints, armour, power);
		this.jumpDelay = delay;
		this.range = range;
		this.currentJumpDelay = 0;
		attributes.add("Jump Engine");
	}

	public int getJumpDelay() {
		return jumpDelay;
	}

	public int getCurrentJumpDelay() {
		return currentJumpDelay;
	}

	public int getRange() {
		return range;
	}

	public JumpPoint getPoint() {
		return point;
	}

	@Override
	public void enable() throws PhaseException {
		super.enable();
		int temp = currentJumpDelay;
		currentJumpDelay = jumpDelay * 10;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentJumpDelay", temp, currentJumpDelay));
	}

	public void openJumpPoint(int x, int y, int facing) throws PhaseException {
		if (Space.getPhase() != PHASE_JUMP_POINT) {
			throw new PhaseException("Can only open a Jump Point in the Jump Point Phase");
		}
		if (currentJumpDelay > 0) {
			throw new PhaseException("The Jump Engine must wait " + currentJumpDelay + " turns before it can open a Jump Point");
		}
		if (Die.distance(structure.getCraft().getX(), structure.getCraft().getY(), x, y) > range) {
			throw new PhaseException("The Jump Engine cannot open a point more than " + range + " hexes distant");
		}
		
		int critical = Die.roundUp(damage, hitPoints);
		if (Die.roll(100) < critical) {
			//TODO The ship exploded!
		}
		
		point = new JumpPoint(x, y, facing);
		Space.add(point);
		
		int temp = currentJumpDelay;
		currentJumpDelay = jumpDelay;
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentJumpDelay", temp, currentJumpDelay));
	}

	public void maintainJumpPoint() throws PhaseException {
		if (Space.getPhase() != PHASE_JUMP_POINT) {
			throw new PhaseException("Can only maintain a Jump Point in the Jump Point Phase");
		}
		if (point == null) {
			throw new PhaseException("This engine does not have an open Jump Point");
		}
		if (Die.distance(structure.getCraft().getX(), structure.getCraft().getY(), point.getX(), point.getY()) > range) {
			throw new PhaseException("Jump Point is more than " + range + " hexes distant");
		}
		if (point.getOpen() <= -4) {
			throw new PhaseException("This engine's Jump Point has been open for 4 turns");
		}
		//TODO check that the Engine can maintain the point
		point.maintain();
	}
	
	@Override
	public void beforePhase(PhaseEvent e) {
		if (e.getPhase() == PHASE_JUMP_POINT) {
			if (point != null) {
				if (!Space.getObjects().contains(point)) {
					point = null;
				}
			}
			if (currentJumpDelay > 0) {
				currentJumpDelay--;
				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentJumpDelay", currentJumpDelay + 1, currentJumpDelay));
			}
		}
	}
}
