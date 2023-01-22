package aog.b5w.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import aog.b5w.components.Structure;
import aog.b5w.components.systems.Engine;
import aog.b5w.components.systems.OptionalSystem;
import aog.b5w.components.systems.Reactor;
import aog.b5w.components.systems.Sensors;
import aog.b5w.components.systems.System;
import aog.b5w.event.ChangeListener;
import aog.b5w.exception.PhaseException;
import aog.b5w.gui.utils.SpringUtilities;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import aog.b5w.space.LargeCraft;

public class PowerPanel extends BasePanel implements ChangeListener, ActionListener {
	
	private static final String[] positions = new String[] {BorderLayout.CENTER, BorderLayout.PAGE_START, BorderLayout.LINE_END, BorderLayout.PAGE_END, BorderLayout.LINE_START};
	
	/**
	 * The version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panReactor;
	private JPanel panOptional;
	private JPanel panDrain;
	
	protected LargeCraft craft;
	
	public PowerPanel(LargeCraft craft) {
		super(craft);
		this.craft = craft;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(getReactorPanel());
		this.add(getDrainerPanel());
		this.add(getOptionalPanel());
	}
	
	public JPanel getReactorPanel() {
		if (panReactor == null) {
			panReactor = new JPanel();
			panReactor.setLayout(new SpringLayout());
			Iterator<System> it = craft.getSystems("Reactor").iterator();
			while (it.hasNext()) {
				Reactor r = (Reactor)it.next();
				addProgressBar(panReactor, r.getName(), r.getPower(), r.getSurplus(), r, "power", "surplus");
			}
			SpringUtilities.makePropertyGrid(panReactor, panReactor.getComponentCount() / 2, 2, 5, 5, 5, 5);
		}
		return panReactor;
	}

	public JPanel getDrainerPanel() {
		if (panDrain == null) {
			panDrain = new JPanel();
			panDrain.setLayout(new SpringLayout());
			Iterator<System> it = craft.getSystems("Sensors").iterator();
			while (it.hasNext()) {
				Sensors s = (Sensors)it.next();
				addProgressBar(panDrain, s.getName(), s.getCurrentEw(), s.getDefensiveEw(), s, "currentEw", "defensiveEw");
				addButton(panDrain, "Create EW", s, "produceEW");
			}
			it = craft.getSystems("Engine").iterator();
			while (it.hasNext()) {
				Engine e = (Engine)it.next();
				addProgressBar(panDrain, e.getName(), e.getThrust(), e.getCurrentThrust(), e, "thrust", "currentThrust");
				addButton(panDrain, "Create Thrust", e, "produceThrust");
			}
			java.lang.System.out.println(panDrain.getComponentCount());
			SpringUtilities.makePropertyGrid(panDrain, panDrain.getComponentCount() / 3, 3, 5, 5, 5, 5);
		}
		return panDrain;
	}

	public JPanel getOptionalPanel() {
		if (panOptional == null) {
			panOptional = new JPanel();
			panOptional.setLayout(new BorderLayout());
			Iterator<Map.Entry<Integer,Structure>> it = craft.getStructures().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Structure> struct = it.next();
				JPanel pan = new JPanel(new SpringLayout());
				pan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), struct.getValue().getName()));
				Iterator<System> syss = struct.getValue().getSystems("Optional").iterator();
				while (syss.hasNext()) {
					OptionalSystem o = (OptionalSystem)syss.next();
					addCheckbox(pan, o.getName() + " (" + o.getPower() + ")", o.isEnabled(), o, "enabled");
				}
				if (struct.getKey() == 1 || struct.getKey() == 3) {
					if (pan.getComponentCount() % 2 == 1) {
						pan.add(new JLabel(""));
					}
					SpringUtilities.makePropertyGrid(pan, pan.getComponentCount() / 2, 2, 5, 5, 5, 5);
				} else {
					SpringUtilities.makePropertyGrid(pan, pan.getComponentCount(), 1, 5, 5, 5, 5);
				}
				panOptional.add(pan, positions[struct.getKey()]);
			}
		}
		return panOptional;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JCheckBox) {
			JCheckBox box = (JCheckBox)evt.getSource();
			boolean selected = box.getModel().isSelected();
			OptionalSystem system = (OptionalSystem)box.getClientProperty("System");
			try {
				if (selected) {
					system.enable();
					//tog.setText("Disable");
				} else {
					system.disable();
					//tog.setText("Enable");
				}
			} catch (PhaseException e) {
				box.setSelected(!selected);
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		} else if (evt.getSource() instanceof JButton) {
			JButton but = (JButton) evt.getSource();
			System system = (System)but.getClientProperty("System");
			if (system.hasAttribute("Sensors")) {
				try {
					((Sensors)system).produceEW(1);
				} catch (PhaseException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			} else if (system.hasAttribute("Engine")) {
				try {
					((Engine)system).produceThrust(1);
				} catch (PhaseException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}
			}
		}
	}
}
