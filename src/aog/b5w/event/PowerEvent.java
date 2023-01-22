package aog.b5w.event;

import aog.b5w.components.Component;

public class PowerEvent {

	protected Component component;
	protected boolean enabled;
	protected int amount;
	protected String error;
	
	public PowerEvent(boolean enabled, int amount, Component component) {
		this.enabled = enabled;
		this.amount = amount;
		this.component = component;
	}

	public PowerEvent(int amount, Component component) {
		this.amount = amount;
		this.component = component;
	}

	public Component getComponent() {
		return component;
	}

	public boolean getEnabled() {
		return enabled;
	}
	
	public int getAmount() {
		return amount;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
		this.amount = 0;
	}
	
	
}
