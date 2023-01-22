package aog.b5w.components.systems.weapons;

public class Volley {
	protected int damage;
	protected String type;
	
	public Volley(int damage, String type) {
		this.damage = damage;
		this.type = type;
	}

	public int getDamage() {
		return damage;
	}

	public String getType() {
		return type;
	}
	
	public Volley overKill(int overKill) {
		return new Volley(overKill, type);
	}
	
}
