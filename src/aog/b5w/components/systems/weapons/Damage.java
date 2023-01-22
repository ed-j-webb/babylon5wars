package aog.b5w.components.systems.weapons;

import java.util.ArrayList;
import java.util.List;

import aog.b5w.space.LargeCraft;
import aog.b5w.space.SpaceObject;
import aog.b5w.utils.Die;

public class Damage {

	protected int dieSize;
	protected int dieCount;
	protected int plus;
	protected int count;
	protected int hexPenalty;
	
	protected int diePenalty;

	public Damage(int dieCount, int dieSize, int plus, int count, int hexPenalty) {
		this.dieSize = dieSize;
		this.dieCount = dieCount;
		this.plus = plus;
		this.count = count;
		this.hexPenalty = hexPenalty;
	}
	
	/**
	 * For damage calculated as 4d10+3
	 * @param dieCount
	 * @param dieSize
	 * @param plus
	 */
	public Damage(int dieCount, int dieSize, int plus) {
		this(dieCount, dieSize, plus, 0, 0);
	}

	/**
	 * For damage calculated as 5d8+7 (-1/hex) 
	 * @param dieCount
	 * @param dieSize
	 * @param plus
	 * @param hexPenalty
	 */
	public Damage(int dieCount, int dieSize, int plus, int hexPenalty) {
		this(dieCount, dieSize, plus, 0, hexPenalty);
	}

	/**
	 * For damage calculated as 8 d5 times
	 * @param plus
	 * @param count
	 */
	public Damage(int plus, int count) {
		this(0, 0, plus, count, 0);
	}
	
	public int getDieSize() {
		return dieSize;
	}

	public int getDieCount() {
		return dieCount;
	}

	public int getPlus() {
		return plus;
	}

	public int getCount() {
		return count;
	}

	public int getHexPenalty() {
		return hexPenalty;
	}

	public int getDiePenalty() {
		return diePenalty;
	}

	public void incrementDiePenalty(int penalty) {
		this.diePenalty -= penalty;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (dieCount > 0) {
			sb.append(dieCount);
			sb.append("d");
			sb.append(dieSize);
			if (plus > 0) {
				sb.append("+");
			}
			if (plus != 0) {
				sb.append(plus);
			}
		}
		if (count > 0) {
			sb.append(plus);
			sb.append(" d");
			sb.append(count);
			sb.append(" times");
		}
		if (hexPenalty != 0) {
			sb.append(" (");
			sb.append(hexPenalty);
			sb.append("/hex)");
		}
		if (diePenalty != 0) {
			sb.append(" ");
			sb.append(diePenalty);
			sb.append(" per die");
		}
		return sb.toString();
	}
	
	public List<Volley> getVolleys(int range, String mode, SpaceObject target) {
		int damage = 0;
		for (int i = 0; i < dieCount; i++) {
			int roll = Die.roll(dieSize);
			roll -= diePenalty;
			if (roll < 0) {
				roll = 0;
			}
			damage += roll + 1;
		}
		damage += plus;
		if (hexPenalty < 0) {
			damage -= range * hexPenalty;
		} else if (hexPenalty > 0) {
			damage -= range / hexPenalty;
		}
		
		List<Volley> volleys = new ArrayList<Volley>();
		
		if (mode.equals(Weapon.STANDARD)) {
			volleys.add(new Volley(damage, mode));
		} else if (mode.equals(Weapon.PLASMA)) {
				volleys.add(new Volley(damage, mode));
		} else if (mode.equals(Weapon.RAKING) || mode.equals(Weapon.SUSTAINED)) {
			for (int i = 0; i < damage / 10; i++) {
				volleys.add(new Volley(10, Weapon.RAKING));
			}
			if ((damage % 10) > 0) {
				volleys.add(new Volley(damage % 10, Weapon.RAKING));
			}
		} else if (mode.equals(Weapon.PULSE)) {
			for (int i = 0; i < Die.roll(count); i++) {
				volleys.add(new Volley(damage, mode));
			}
		} else if (mode.equals(Weapon.PIERCING)) {
			if (target.hasAttribute("Large Craft")) {
				int size = ((LargeCraft)target).getStructures().keySet().size();
				int divisor = 1;
				if (size == 5) {
					divisor = 3;
				} else if (size == 3) {
					divisor = 2;
				}
				for (int i = 0; i < divisor; i++) { 
					if (i == 0) {
						volleys.add(new Volley(Die.roundUp(damage, divisor), mode));
					} else {
						volleys.add(new Volley(Die.roundDown(damage, divisor), mode));
					}
				}
			} else {
				// TODO Handle other sizes of ship
				volleys.add(new Volley(damage, mode));
			}
		}
		
		return volleys;
	}
}
