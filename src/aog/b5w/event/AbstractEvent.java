package aog.b5w.event;

public abstract class AbstractEvent {

	public static final int BEFORE = -1;
	public static final int INPHASE = 0;
	public static final int READY = 1;
	public static final int AFTER = 2;

	protected int sequence;

	public AbstractEvent(int sequence) {
		this.sequence = sequence;
	}
	
	public int getSequence() {
		return sequence;
	}
	
	
}
