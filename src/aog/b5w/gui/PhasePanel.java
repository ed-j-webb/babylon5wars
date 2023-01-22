package aog.b5w.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import aog.b5w.B5WConstants;
import aog.b5w.Space;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PhaseListener;
import aog.b5w.gui.utils.SpringUtilities;
import aog.b5w.space.SpaceObject;

public class PhasePanel extends JPanel implements PhaseListener, B5WConstants, ActionListener {

	protected JLabel lblTurn;
	protected JLabel[] lblPhase = new JLabel[PHASES.length];
	protected JButton cmdAdvance;
	
	/**
	 * The version ID
	 */
	private static final long serialVersionUID = 1L;

	public PhasePanel() {
		Space.addPhaseListener(this);
		setLayout(new SpringLayout());
		add(getCmdAdvance());
		add(getLblTurn());
		for (int i = 1; i < PHASES.length; i++) {
			add(getLblPhase(i));
		}
		SpringUtilities.makePropertyGrid(this, getComponentCount(), 1, 5, 5, 3, 3);
	}

	protected JLabel getLblTurn() {
		if (lblTurn == null) {
			lblTurn = new JLabel("Turn: " + Space.getTurn());
		}
		return lblTurn;
	}

	protected JButton getCmdAdvance() {
		if (cmdAdvance == null) {
			cmdAdvance = new JButton("Advance");
			cmdAdvance.addActionListener(this);
			//SpringUtilities.fixHeight(cmdAdvance, 20);
		}
		return cmdAdvance;
	}
	
	protected JLabel getLblPhase(int i) {
		if (lblPhase[i] == null) {
			lblPhase[i] = new JLabel(PHASES[i], SwingConstants.CENTER);
			lblPhase[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		}
		return lblPhase[i];
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		getLblTurn().setText("Turn: " + Space.getTurn());
		getLblPhase(e.getPhase()).setOpaque(true);
		getLblPhase(e.getPhase()).setBackground(Color.GREEN);
		getLblPhase(e.getPhase()).repaint();
	}

	@Override
	public void isReady(PhaseEvent e) {
		getLblPhase(e.getPhase()).setBackground(Color.ORANGE);
		getLblPhase(e.getPhase()).repaint();
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		getLblPhase(e.getPhase()).setOpaque(false);
		getLblPhase(e.getPhase()).setBackground(null);
		getLblPhase(e.getPhase()).repaint();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		List<SpaceObject> list = Space.advancePhase();
		if (list.size() != 0) {
			//TODO show failed objects
		}
	}
	
	
}
