package aog.b5w.event;

public interface PowerListener {
	
	public void beforeEnable(PowerEvent e);
	public void afterEnable(PowerEvent e);
	
	public void beforeDisable(PowerEvent e);
	public void afterDisable(PowerEvent e);
	
	public void beforeDrawPower(PowerEvent e);
	public void afterDrawPower(PowerEvent e);

}
