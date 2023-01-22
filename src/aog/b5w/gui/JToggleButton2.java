package aog.b5w.gui;

import javax.swing.JToggleButton;

public class JToggleButton2 extends JToggleButton {

	/**
	 * the version ID
	 */
	private static final long serialVersionUID = 1L;
	
	protected String[] states = new String[] {"On", "Off"};
	
	public void setStates(String on, String off) {
		states[0] = on;
		states[1] = off;
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		System.out.println(selected ? states[0] : states[1]);
		setText(selected ? states[0] : states[1]);
	}
	
	
}
