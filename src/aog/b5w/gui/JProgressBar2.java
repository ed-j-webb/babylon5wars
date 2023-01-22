package aog.b5w.gui;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;

public class JProgressBar2 extends JProgressBar {

	/**
	 * The version ID
	 */
	private static final long serialVersionUID = 1L;

	public JProgressBar2() {
		super();
		setString("0/0");
		setStringPainted(true);
	}

	public JProgressBar2(BoundedRangeModel newModel) {
		super(newModel);
		setString("0/0");
		setStringPainted(true);
	}

	public JProgressBar2(int orient, int min, int max) {
		super(orient, min, max);
		setString("0/0");
		setStringPainted(true);
	}

	public JProgressBar2(int min, int max) {
		super(min, max);
		setString("0/0");
		setStringPainted(true);
	}

	public JProgressBar2(int orient) {
		super(orient);
		setString("0/0");
		setStringPainted(true);
	}

	@Override
	public void setMaximum(int n) {
		super.setMaximum(n);
		setString(getValue() + "/" + getMaximum());
	}

	@Override
	public void setValue(int n) {
		super.setValue(n);
		setString(getValue() + "/" + getMaximum());
	}
	
}