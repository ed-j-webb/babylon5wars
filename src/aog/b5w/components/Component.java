package aog.b5w.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import aog.b5w.B5WConstants;
import aog.b5w.Space;
import aog.b5w.components.systems.weapons.Volley;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.event.ChangeEvent;
import aog.b5w.event.ChangeListener;
import aog.b5w.event.ChangeNotifier;
import aog.b5w.event.ChangeProducer;
import aog.b5w.event.DamageEvent;
import aog.b5w.event.DamageListener;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PhaseListener;
import aog.b5w.log.LogEntry;
import aog.b5w.utils.Die;

/**
 * A Component of a space ship. A component cannot be created use one of its
 * subclasses System or Structure
 * 
 * @author edw
 * 
 */
public abstract class Component implements PhaseListener, B5WConstants, ChangeProducer {

	private static Logger log = Logger.getLogger(Component.class);
	
	protected int hitPoints = 0; // Hits component can take before being destroyed

	protected int armour = 0; // Armour class of component

	protected int damage = 0; // Amount of damage taken by component

	protected boolean destroyed = false; // Flags when the component has been destroyed

	protected boolean raked = false; // Flags when the component has been hit by raking fire
	
	protected String name = ""; // The name of the component
	
	protected List<String> attributes = new ArrayList<String>(); // List of attributes for the Component
	
	protected Set<DamageListener> damageListeners = new HashSet<DamageListener>();

	protected ChangeNotifier changeNotifier = new ChangeNotifier();
	
	public static int BEFORE = -1;
	public static int AFTER = 1;
	
	public Component(String name, int hitPoints, int armour) {
		this.name = name;
		this.hitPoints = hitPoints;
		this.armour = armour;
		this.attributes.add("Component");
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public int getArmour() {
		return armour;
	}

	public int getDamage() {
		return damage;
	}

	public int getCurrentHitPoints() {
		return hitPoints - damage;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	public boolean isRaked() {
		return raked;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Links the system to other systems in the Craft
	 * @param components other components in the craft  
	 */
	public void commission(List<Component> components) {
		
	}

	/**
	 * TakeDamage is used by the component to calculate the amount of damage it
	 * sustains from an attack. Armour is subtracted unless the component has
	 * already been hit in a raking attack. If the damage sustained by the
	 * component is greater than its HitPoints then the component is destroyed
	 * and any excess damage is returned to the class that called the method.
	 */
	protected int takeDamage(int amount) {

		int overKill = 0;
		LogEntry entry = new LogEntry(this.toString(), Space.getTurn(), Space.getPhase(), LogEntry.DAMAGE, "Hit for " + amount + ". ");
		
		// Notify listeners that this Component is being damaged
		DamageEvent e = new DamageEvent(DamageEvent.DAMAGED, this, amount);
		notifyDamageListeners(e, BEFORE);
		amount = e.getAmount();
		int prevDamage = damage;
		damage += amount;
		notifyDamageListeners(e, AFTER);
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "damage", prevDamage, damage));
		changeNotifier.notifyChangeListeners(new ChangeEvent(this, "currentHitPoints", hitPoints - prevDamage, hitPoints - damage));
		if (damage >= hitPoints) {
			overKill = damage - hitPoints;
			damage = hitPoints;
			// Notify listeners that this Component is being destroyed
			DamageEvent d = new DamageEvent(DamageEvent.DESTROYED, this, overKill);
			notifyDamageListeners(d, BEFORE);
			destroyed = d.isDestroyed();
			overKill = d.getAmount();
			if (destroyed) {
				damage = hitPoints;
				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "destroyed", false, true));
				entry.append("Destroyed.");
				notifyDamageListeners(d, AFTER);
			}
		}
		
		log.info(entry);
		return overKill;
	}

	public boolean hasAttribute(String attribute) {
		return attributes.contains(attribute);
	}
	
	public void addChangeListener(ChangeListener l) {
		changeNotifier.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		changeNotifier.removeChangeListener(l);
	}

	public void addDamageListener(DamageListener l) {
		synchronized(damageListeners) {
			damageListeners.add(l);
		}
	}
	
	public void removeDamageListener(DamageListener l) {
		synchronized(damageListeners) {
			damageListeners.remove(l);
		}		
	}
	
	public void notifyDamageListeners(DamageEvent e, int sequence) {
		synchronized(damageListeners) {
			Iterator<DamageListener> it = damageListeners.iterator();
			while (it.hasNext()) {
				if (sequence == BEFORE) {
					it.next().beforeDamage(e);
				} else {
					it.next().afterDamage(e);
				}
			}
		}
	}

	public Volley takeVolley(Volley v) {
		int amount = v.getDamage();
		if (raked && v.getType().equals(Weapon.RAKING)) {
			// Don't subtract armour
		} else if (v.getType().equals(Weapon.MATTER)) {
			// Don't subtract armour
		} else if (v.getType().equals(Weapon.PLASMA)) {
			amount -= Die.roundDown(armour, 2);
		} else {
			amount -= armour;
		}

		if (v.getType().equals(Weapon.RAKING)) {
			if (!raked) {
				raked = true;
				changeNotifier.notifyChangeListeners(new ChangeEvent(this, "raked", false, true));
			}
		}
		
		if (amount > 0) {
			int overKill = takeDamage(amount);
			if (overKill > 0 && !v.getType().equals(Weapon.MATTER) && !v.getType().equals(Weapon.PIERCING)) {
				return v.overKill(overKill);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public void beforePhase(PhaseEvent e) {
	}

	@Override
	public void isReady(PhaseEvent e) {
	}

	@Override
	public void afterPhase(PhaseEvent e) {
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" HP: ");
		sb.append(getCurrentHitPoints());
		sb.append(" (");
		sb.append(hitPoints);
		sb.append(") Armour: ");
		sb.append(armour);
		return sb.toString();
	}
}
