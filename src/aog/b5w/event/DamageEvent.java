package aog.b5w.event;

import aog.b5w.components.Component;

public class DamageEvent {

	public static int DESTROYED = 101;
	public static int DAMAGED = 100;
	
	protected Component component;
	protected int type;
	protected int amount;
	protected boolean destroyed;
	protected String desc;
	
	public DamageEvent(int type, Component component) {
		this.type = type;
		this.component = component;
	}
	
	public DamageEvent(int type, Component component, int amount) {
		this(type, component);
		this.amount = amount;
		if (type == DESTROYED) {
			destroyed = true;
		}
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
