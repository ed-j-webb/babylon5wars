package aog.b5w.event;

import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.space.Craft;

import aog.b5w.space.SpaceObject;
import aog.b5w.utils.TargetingSolution;

public class FireEvent {

	public static final int MOD_RANGE = 0;
	public static final int MOD_OFFENSIVE_EW = 1;
	public static final int MOD_DEFENSIVE_EW = 2;
	public static final int MOD_DEFENCE = 3;
	public static final int MOD_FIRE_CONTROL = 4;
	public static final int MOD_INTERCEPTOR = 5;
	public static final int MOD_PIERCING = 6;
	public static final int MODIFIER_COUNT = 7;
	
	
	protected Weapon weapon;
	protected Craft source;
	protected SpaceObject target;
	protected int arc;
	protected int range;
	
	protected int[] modifiers = new int[MODIFIER_COUNT];
	
	protected int toHit = 20;

	public FireEvent(Craft source, Weapon weapon, SpaceObject target, int arc, int range) {
		this.source = source;
		this.weapon = weapon;
		this.target = target;
		this.arc = arc;
		this.range = range;
	}
	
	public FireEvent(TargetingSolution solution, Craft source, Weapon weapon) {
		this.source = source;
		this.weapon = weapon;
		this.target = solution.getTarget();
		this.arc = solution.getArc();
		this.range = solution.getRange();
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public Craft getSource() {
		return source;
	}

	public SpaceObject getTarget() {
		return target;
	}

	public int getToHit() {
		return toHit;
	}
	
	public int getArc() {
		return arc;
	}

	public int getRange() {
		return range;
	}

	public void modifyToHit(int amount, int type) {
		toHit += amount;
		modifiers[type] += amount;
	}
	
	public int getModifier(int type) {
		return modifiers[type];
	}
}
