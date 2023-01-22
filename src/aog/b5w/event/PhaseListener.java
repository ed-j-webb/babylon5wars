package aog.b5w.event;

public interface PhaseListener {

	public void beforePhase(PhaseEvent e);
	public void isReady(PhaseEvent e);
	public void afterPhase(PhaseEvent e);
}
