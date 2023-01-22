package aog.b5w.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import aog.b5w.B5WConstants;
import aog.b5w.components.Structure;
import aog.b5w.components.systems.Engine;
import aog.b5w.components.systems.System;
import aog.b5w.components.systems.Thruster;
import aog.b5w.event.ChangeListener;
import aog.b5w.exception.PhaseException;
import aog.b5w.gui.utils.SpringUtilities;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import aog.b5w.space.LargeCraft;

public class ThrustPanel extends BasePanel implements ChangeListener, ActionListener, B5WConstants {
	
	private static final String[] positions = new String[] {BorderLayout.CENTER, BorderLayout.PAGE_START, BorderLayout.LINE_END, BorderLayout.PAGE_END, BorderLayout.LINE_START};
	
	/**
	 * The version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panEngine;
	private JPanel panMove;
	private JPanel panThruster;
	private JLabel2 lblCost;
	private JLabel2[] lblThrust;
	
	protected LargeCraft craft;
	protected int type;
	
	public ThrustPanel(LargeCraft craft, int type) {
		super(craft);
		this.craft = craft;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.type = type;
		this.add(getEnginePanel());
		this.add(getMovePanel());
		this.add(getThrusterPanel());
	}

	public JPanel getMovePanel() {
		if (panMove == null) {
			panMove = new JPanel();
			panMove.setLayout(new SpringLayout());
			panMove.add(new JLabel("Cost"));
			for (int i = 1; i < LOCATIONS.length; i++) {
				panMove.add(new JLabel(LOCATIONS[i]));
			}
			lblCost = new JLabel2();
			lblCost.setText(craft.getMoveCost()[type]);
			panMove.add(lblCost);
			
			lblThrust = new JLabel2[LOCATIONS.length];
			for (int i = 1; i < LOCATIONS.length; i++) {
				lblThrust[i] = new JLabel2();
				lblThrust[i].setText(craft.getMoveThrust()[type][i]);
				panMove.add(lblThrust[i]);
			}
			SpringUtilities.makePropertyGrid(panMove, 2, panMove.getComponentCount() / 2, 5, 5, 5, 5);
		}
		return panMove;
	}

	public JPanel getEnginePanel() {
		if (panEngine == null) {
			panEngine = new JPanel();
			panEngine.setLayout(new SpringLayout());
			Iterator<System> it = craft.getSystems("Engine").iterator();
			while (it.hasNext()) {
				Engine r = (Engine)it.next();
				addProgressBar(panEngine, r.getName(), r.getThrust(), r.getCurrentThrust(), r, "thrust", "currentThrust");
			}
			SpringUtilities.makePropertyGrid(panEngine, panEngine.getComponentCount() / 2, 2, 5, 5, 5, 5);
		}
		return panEngine;
	}

	public JPanel getThrusterPanel() {
		if (panThruster == null) {
			panThruster = new JPanel();
			panThruster.setLayout(new BorderLayout());
			Iterator<Map.Entry<Integer,Structure>> it = craft.getStructures().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Structure> struct = it.next();
				JPanel pan = new JPanel(new SpringLayout());
				pan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), struct.getValue().getName()));
				Iterator<System> syss = struct.getValue().getSystems("Thruster").iterator();
				while (syss.hasNext()) {
					Thruster t = (Thruster)syss.next();
					addProgressBar(pan, t.getName() + " (" + t.getEfficiency() + ")", t.getRating(), t.getCurrentThrust(), t, "rating", "currentThrust");
					addButton(pan, "Thrust", t, "thrust");
				}
				if (struct.getKey() == 1 || struct.getKey() == 3) {
					if (pan.getComponentCount() % 2 == 1) {
						pan.add(new JLabel(""));
					}
					SpringUtilities.makePropertyGrid(pan, pan.getComponentCount() / 3, 3, 5, 5, 5, 5);
				} else {
					SpringUtilities.makePropertyGrid(pan, pan.getComponentCount() / 3, 3, 5, 5, 5, 5);
				}
				panThruster.add(pan, positions[struct.getKey()]);
			}
		}
		return panThruster;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JButton) {
			JButton but = (JButton) evt.getSource();
			System system = (System)but.getClientProperty("System");
			if (system.hasAttribute("Thruster")) {
				try {
					((Thruster)system).thrust(1, type);
					lblCost.setText(craft.getMoveCost()[type]);
					for (int i = 1; i < LOCATIONS.length; i++) {
						lblThrust[i].setText(craft.getMoveThrust()[type][i]);
					}
				} catch (PhaseException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			}
		}
	}
}
