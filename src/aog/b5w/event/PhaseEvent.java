package aog.b5w.event;

import java.util.ArrayList;
import java.util.List;

import aog.b5w.space.SpaceObject;

public class PhaseEvent extends AbstractEvent {


	protected int phase;
	
	protected List<SpaceObject> objects = new ArrayList<SpaceObject>();

	public PhaseEvent(int sequence, int phase) {
		super(sequence);
		this.phase = phase;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}
	
	public boolean isClear() {
		return objects.size() == 0;
	}
	
	public List<SpaceObject> getUnready() {
		return objects;
	}
	
	public void unready(SpaceObject o) {
		objects.add(o);
	}
}

