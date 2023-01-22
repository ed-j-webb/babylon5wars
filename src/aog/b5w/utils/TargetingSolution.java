package aog.b5w.utils;

import aog.b5w.space.SpaceObject;

public class TargetingSolution {

	protected SpaceObject target;
	protected int[] arcs;
	protected int arc;
	protected int range;
	
	public TargetingSolution(SpaceObject target, int[] arcs, int range) {
		this.target = target;
		this.arcs = arcs;
		this.range = range;

		if (arcs.length == 1) {
			arc = arcs[0];
		} else {
			int i = (int)(System.currentTimeMillis() % 2);
			arc = arcs[i]; 
		}
}
	
	public SpaceObject getTarget() {
		return target;
	}
	
	public int[] getArcs() {
		return arcs;
	}
	
	public int getArc() {
		return arc;
	}
	
	public int getRange() {
		return range;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		return  ((TargetingSolution)obj).getTarget() == this.getTarget();
	}

	@Override
	public int hashCode() {
		return target.hashCode();
	}
	
	
}
