package aog.b5w.gui;

import javax.swing.Icon;
import javax.swing.JLabel;

public class JLabel2 extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JLabel2() {
		super();
	}

	public JLabel2(Icon arg0, int arg1) {
		super(arg0, arg1);
	}

	public JLabel2(Icon arg0) {
		super(arg0);
	}

	public JLabel2(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public JLabel2(String arg0, int arg1) {
		super(arg0, arg1);
	}

	public JLabel2(String arg0) {
		super(arg0);
	}

	public void setText(boolean value) {
		setText(value ? "Yes" : "No");
	}
	
	public void setText(int value) {
		setText(Integer.toString(value));
	}
}
