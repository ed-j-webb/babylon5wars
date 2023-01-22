package aog.b5w.components.systems;

import java.util.ArrayList;
import java.util.List;

import aog.b5w.Space;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.DamageEvent;
import aog.b5w.event.PhaseEvent;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.SmallCraft;
import aog.b5w.utils.Die;

public class Hangar extends System {

	protected int throughput;
	
	protected int currentThroughput;
	
	protected int[] launchDirection;
	
	protected boolean linksDamaged = false;
	
	protected List<SmallCraft> contents = new ArrayList<SmallCraft>();

	public Hangar(String name, int hitPoints, int armour, int throughput, List<SmallCraft> contents) {
		this(name, hitPoints, armour,throughput, contents, new int[] {0});
	}
	
	public Hangar(String name, int hitPoints, int armour, int throughput, List<SmallCraft> contents, int[] launchDirection) {
		super(name, hitPoints, armour);

		type = "Hangar";
		
		this.throughput = throughput;
		this.currentThroughput = throughput;
		this.launchDirection = launchDirection;
		
		attributes.add("Hangar");
		
		this.contents.addAll(contents);
	
		criticals = new int[] {13, 19, 25};
	}

	public void land(SmallCraft craft) throws PhaseException {
		if (Space.getPhase() != PHASE_LAUNCH) {
			throw new PhaseException("Can only land craft in the Launch phase");
		}
		if (getCraft().getPivoting() != NONE || getCraft().getRolling() != NONE) {
			throw new PhaseException("Cannot land craft in a turn where the craft has pivoted or rolled");
		}
		if (currentThroughput <= 0) {
			throw new PhaseException("Hangar has no more throughput to land craft");
		}
		if (contents.size() >= hitPoints) {
			throw new PhaseException("Hangar has no more space to land craft");
		}
		if (!destroyed) {
			if (craft.getDirectionOfTravel() != getCraft().getDirectionOfTravel()) {
				throw new PhaseException("Craft are not travelling in same direction");
			}
			if (craft.getVelocity() < getCraft().getVelocity() || craft.getVelocity() <= getCraft().getVelocity() + craft.getThrustPoints() ) {
				throw new PhaseException("Craft is not travelling at correct velocity to land");
			}
			contents.add(craft);
			currentThroughput--;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThroughput", currentThroughput + 1, currentThroughput));
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "craftCount", contents.size() - 1, contents.size()));
		} 
	}

	public void launch(SmallCraft craft) throws PhaseException {
		this.launch(craft, launchDirection[0]);
	}
	
	public void launch(SmallCraft craft, int direction) throws PhaseException {
		if (Space.getPhase() != PHASE_LAUNCH) {
			throw new PhaseException("Can only launch craft in the Launch phase");
		}
		if (!Die.contains(launchDirection, direction)) {
			throw new PhaseException("Cannnot launch in the specified direction");
		}
		if (getCraft().getPivoting() != NONE || getCraft().getRolling() != NONE) {
			throw new PhaseException("Cannot launch craft in a turn where the craft has pivoted or rolled");
		}
		if (currentThroughput <= 0) {
			throw new PhaseException("Hangar has no more throughput to launch craft");
		}
		if (!destroyed && currentThroughput > 0 && contents.contains(craft)) {
			contents.remove(craft);
			
			int v = getCraft().getVelocity();
			if (direction >= 4 && direction <= 8) {
				v = -v;
			}
			
			craft.launch(Die.plus(getCraft().getDirectionOfFacing(), direction), Die.plus(getCraft().getDirectionOfTravel(), direction), v);
			Space.add(craft);
			currentThroughput--;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThroughput", currentThroughput + 1, currentThroughput));
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "craftCount", contents.size() + 1, contents.size()));
		}
	}

	@Override
	protected void critical1() {
		int tempt = throughput;
		int tempc = currentThroughput;
		throughput -= 2;
		currentThroughput -= 2;
		if (throughput < 0) {
			throughput = 0;
			currentThroughput = 0;
			DamageEvent e = new DamageEvent(DamageEvent.DESTROYED, this);
			notifyDamageListeners(e, BEFORE);
			if (e.isDestroyed()) {
				destroyed = true;
				notifyDamageListeners(e, AFTER);
			}
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThroughput", tempc, currentThroughput));
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "throughput", tempt, throughput));
		}
	}

	@Override
	protected void critical2() {
		linksDamaged = true;
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		super.beforePhase(e);
		if (e.getPhase() == PHASE_LAUNCH) {
			int tempc = currentThroughput;
			currentThroughput = throughput;
			changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentThroughput", tempc, currentThroughput));
		}
	}

	public int getThroughput() {
		return throughput;
	}

	public int getCurrentThroughput() {
		return currentThroughput;
	}

	public int getCraftCount() {
		return contents.size();
	}
	
	public List<SmallCraft> getContents() {
		return contents;
	}
}
