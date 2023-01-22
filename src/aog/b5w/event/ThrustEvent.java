package aog.b5w.event;

import aog.b5w.components.Component;

public class ThrustEvent {

	protected Component component;
	protected int inputThrust;
	protected int outputThrust;
	protected int type;
	protected int direction;
	protected boolean cancelled;
	protected String reason;
	
	public ThrustEvent(int input, int output, int type, int direction, Component component) {
		this.inputThrust = input;
		this.outputThrust = output;
		this.type = type;
		this.direction = direction;
		this.component = component;
	}

	public Component getComponent() {
		return component;
	}

	public int getInputThrust() {
		return inputThrust;
	}

	public int getOutputThrust() {
		return outputThrust;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void cancel(String reason) {
		this.reason = reason;
		this.cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public String getReason() {
		return reason;
	}

	public int getDirection() {
		return direction;
	}
	
	
}
