package aog.b5w.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aog.b5w.components.systems.System;
import aog.b5w.components.systems.weapons.Salvo;
import aog.b5w.components.systems.weapons.Volley;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.ChangeListener;
import aog.b5w.event.PhaseEvent;
import aog.b5w.space.LargeCraft;
import aog.b5w.utils.Die;

public class Structure extends Component implements ChangeListener {

	public final static String LOCATION[] = { "Primary", "Fore", "Aft", "Port", "Starboard" };

	protected LargeCraft craft; // Holds a reference to the Craft that the structure
							// is a part of
	protected List<List<Component>> toHit = new ArrayList<List<Component>>(20); // List that hold the To Hit table of the Structure
	
	protected int location; // The position of the Structure in the Craft

	public List<System> systems = new ArrayList<System>(); // Array of Systems that the Structure contains

	/**
	 * A Structure is constructed by giving it a Name, HitPoints, Armour rating
	 * and the To Hit Primary.
	 */
	public Structure(String name, int hitPoints, int armour, int structFrom, int structTo) {
		super(name, hitPoints, armour);
		this.attributes.add("Structure");
		createEmptyToHit();
		addToHit(structFrom, structTo, this);
	}

	private void createEmptyToHit() {
		for (int i = 0; i <= 20; i++) {
			toHit.add(new ArrayList<Component>());
		}
	}
	
	private void addToHit(int from, int to, Component comp) {
		for (int i = from; i <= to; i++) {
			addToHit(i, comp);
		}
	}
	
	private void addToHit(int pos, Component comp) {
		List<Component> hit = toHit.get(pos);
		hit.add(comp);
	}
	
	private void addPrimaryHit(Component comp) {
		for (int i = 1; i <= 20; i++) {
			List<Component> hit = toHit.get(i);
			if (hit.size() == 0) {
				hit.add(comp);
			}
		}
	}
	
	@Override
	public void commission(List<Component> components) {
		Iterator<Component> it = components.iterator();
		while (it.hasNext()) {
			Component s = it.next();
			if (s.hasAttribute("Primary")) {
				addPrimaryHit(s);
			}
		}
	}

	/**
	 * Adds a previously constructed system into the Structure. It takes a
	 * ShipSystem and puts it into the next available system slot if available
	 * and returns true. If all system slots are full then false is returned.
	 */
	public void addSystem(System s) {
		systems.add(s);
		s.setStructure(this);
		s.addChangeListener(this);
	}

	/**
	 * Adds a previously constructed system into the Structure. It takes a
	 * ShipSystem and puts it into the next available system slot if available
	 * and returns true. If all system slots are full then false is returned. It
	 * also puts the system type into the To Hit table for the Structure taking
	 * the low and high To Hit values. This method only needs to be called once
	 * per System type.
	 */
	public void addSystem(System s, int l, int h) {
		addSystem(s);
		addToHit(l, h, s);
	}

	public LargeCraft getCraft() {
		return craft;
	}

	public void setCraft(LargeCraft craft, int location) {
		this.craft = craft;
		this.location = location;
		attributes.add(LOCATIONS[location]);
	}

	public List<List<Component>> getToHit() {
		return toHit;
	}

	public int getLocation() {
		return location;
	}

	public List<System> getSystems() {
		return systems;
	}
	
	public System getSystem(String name) {
		Iterator<System> it = systems.iterator();
		while (it.hasNext()) {
			System s = it.next();
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

	public Set<System> getSystems(String attribute) {
		Iterator<System> it = systems.iterator();
		Set<System> set = new HashSet<System>();
		while (it.hasNext()) {
			System s = it.next();
			if (s.hasAttribute(attribute)) {
				set.add(s);
			}
		}
		return set;
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		Iterator<System> it =  systems.iterator();
		while (it.hasNext()) {
			System s = it.next();
			s.beforePhase(e);
		}
	}

	@Override
	public void isReady(PhaseEvent e) {
		Iterator<System> it =  systems.iterator();
		while (it.hasNext()) {
			System s = it.next();
			s.isReady(e);
		}
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		Iterator<System> it =  systems.iterator();
		while (it.hasNext()) {
			System s = it.next();
			s.afterPhase(e);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		changeNotifier.notifyChangeListeners(e);		
	}

	public void takeSalvo(Salvo s) {
		
		int arc = Die.plus(s.getArc(), 6);
		arc = Die.plus(arc, 12 - getCraft().getDirectionOfFacing());
		
		Iterator<Volley> it = s.getVolleys().iterator();
		Component comp = null;
		while (it.hasNext()) {
			comp = getHitLocation(arc);
			// If no suitable system found then hit the structure
			if (comp == null) {
				comp = this;
			}
			if (comp.hasAttribute("Structure") && comp != this) {
				((Structure)comp).takeSalvo(s.extractVolley(it.next()));
			} else {
				comp.takeVolley(it.next());
			}
		}
	}
	
	public Component getHitLocation(int arc) {
		
		int roll = Die.roll(20);
		List<Component> hit = toHit.get(roll);
		if (hit == null || hit.size() == 0) {
			java.lang.System.out.println("Cannot find a hit location for " + roll);
		} else if (hit.size() == 1) {
			return hit.get(0);
		} else if (hit.get(0).hasAttribute("Weapon")) {
			List<Component> bearing = new ArrayList<Component>();
			Iterator<Component> it = hit.iterator();
			while (it.hasNext()) {
				Weapon w = (Weapon)it.next();
				if (Die.between(arc, w.getArcStart(), w.getArcFinish())) {
					bearing.add(w);
				}
				
				if (bearing.size() > 0) {
					int sys = Die.roll(bearing.size());
					return bearing.get(sys);
				} else {
					return this;
				}
			}
		} else {
			int sys = Die.roll(hit.size());
			return hit.get(sys);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
