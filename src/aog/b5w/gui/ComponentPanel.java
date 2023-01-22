package aog.b5w.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

import aog.b5w.components.Component;
import aog.b5w.components.Structure;
import aog.b5w.components.systems.Engine;
import aog.b5w.components.systems.Hangar;
import aog.b5w.components.systems.JumpEngine;
import aog.b5w.components.systems.OptionalSystem;
import aog.b5w.components.systems.Reactor;
import aog.b5w.components.systems.Sensors;
import aog.b5w.components.systems.System;
import aog.b5w.components.systems.Thruster;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.event.ChangeEvent;
import aog.b5w.gui.utils.SpringUtilities;
import aog.b5w.gui.utils.SwingUtilities;

public class ComponentPanel extends BasePanel {

	/**
	 * the version ID
	 */
	private static final long serialVersionUID = 1L;
	
	protected Map<ObjectStatus, List<ClassStatusProperty>> clazzes = new HashMap<ObjectStatus, List<ClassStatusProperty>>();

	public ComponentPanel(Component component) {
		super(component);
		this.setLayout(new SpringLayout());
		addLabel(this, "Name:", component.getName(), component, "name");
		addProgressBar(this, "Hit Points:", component.getHitPoints(), component.getCurrentHitPoints(), component, "hitPoints", "currentHitPoints");
		addLabel(this, "Armour:", component.getArmour(), component, "armour");
		
		if (component.hasAttribute("Structure")) {
			Structure structure = (Structure)component;
			Iterator<System> it = structure.systems.iterator();
			while (it.hasNext()) {
				System sys = it.next();
				addProgressBar(this, sys.getName(), sys.getHitPoints(), sys.getCurrentHitPoints(), sys, "hitPoints", "currentHitPoints");
			}
		}
		if (component.hasAttribute("Optional")) {
			OptionalSystem opt = (OptionalSystem)component;
			addLabel(this, "Power:", opt.getPower(), opt, "power");
			addLabel(this, "Active:", opt.isEnabled(), opt, "enabled");
		}
		if (component.hasAttribute("Reactor")) {
			Reactor reactor = (Reactor)component;
			addProgressBar(this, "Power:", reactor.getPower(), reactor.getSurplus(), reactor, "power", "surplus");
		}
		if (component.hasAttribute("Sensors")) {
			Sensors sensors = (Sensors)component;
			addLabel(this, "Initial EW", sensors.getInitialEw(), sensors, "initialEw");
			addProgressBar(this, "Defensive EW:", sensors.getCurrentEw(), sensors.getDefensiveEw(), sensors, "currentEw", "defensiveEw");
			addProgressBar(this, "CCEW:", sensors.getCurrentEw(), sensors.getCloseCombatEw(), sensors, "currentEw", "closeCombatEw");
			addProgressBarClass("offensiveEw", sensors, "currentEw", "offensiveEw");
		}
		if (component.hasAttribute("Engine")) {
			Engine engine = (Engine)component;
			addLabel(this, "Efficiency:", engine.getEfficency(), engine, "efficiency");
			addLabel(this, "Initial Thrust:", engine.getInitialThrust(), engine, "initialThrust");
			addProgressBar(this, "Thrust:", engine.getThrust(), engine.getCurrentThrust(), engine, "thrust", "currentThrust");
		}
		if (component.hasAttribute("Jump Engine")) {
			JumpEngine engine = (JumpEngine)component;
			addLabel(this, "Range:", engine.getRange(), engine, "range");
			addProgressBar(this, "Delay:", engine.getJumpDelay(), engine.getCurrentJumpDelay(), engine, "jumpDelay", "currentJumpDelay");
		}
		if (component.hasAttribute("Thruster")) {
			Thruster thruster = (Thruster)component;
			addLabel(this, "Efficiency:", thruster.getEfficiency(), thruster, "efficiency");
			addProgressBar(this, "Thrust:", thruster.getRating(), thruster.getCurrentThrust(), thruster, "rating", "currentThrust");
		}
		if (component.hasAttribute("Weapon")) {
			Weapon weapon = (Weapon)component;
			addLabel(this, "Fire Control:", weapon.getFireControl()[0] + "/" + weapon.getFireControl()[1] + "/" + weapon.getFireControl()[2], weapon, "fireControl");
			addLabel(this, "Fire Modes:", weapon.getFireModes(), weapon, "fireModes");
			addLabel(this, "Damage:", weapon.getWeaponDamage().toString(), weapon, "weaponDamage");
			addProgressBar(this, "Recharge:", weapon.getRecharge(), weapon.getCoolDown(), weapon, "recharge", "coolDown");
			addLabel(this, "Target:", weapon.getTarget() == null ? "None" : weapon.getTarget().getName(), weapon, "target");
		}
		if (component.hasAttribute("Hangar")) {
			Hangar hangar = (Hangar)component;
			addProgressBar(this, "Throughput:", hangar.getThroughput(), hangar.getCurrentThroughput(), hangar, "throughput", "currentThroughput");
			addProgressBar(this, "Capacity:", hangar.getCurrentHitPoints(), hangar.getCraftCount(), hangar, "currentHitPoints", "craftCount");
		}
		SpringUtilities.makePropertyGrid(this, this.getComponentCount() / 2, 2, 5, 5, 3, 3);

	}

	protected void addProgressBarClass(String status, Object source, String total, String value) {
		List<String> statuses = Arrays.asList(new String[] {total, value});
		List<String> properties = Arrays.asList(new String[] {"maximum", "value"});
		addClass(source, status, JProgressBar2.class, statuses, properties);
	}

	protected void addClass(Object source, String status, Class<? extends JComponent> clazz, List<String> statuses, List<String> properties) {
		List<ClassStatusProperty> list = clazzes.get(new ObjectStatus(source, status));
		if (list == null) {
			list = new ArrayList<ClassStatusProperty>();
			clazzes.put(new ObjectStatus(source, status), list);
		}
		list.add(new ClassStatusProperty(clazz, statuses, properties));
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getEntryType() == ChangeEvent.ADD) {
			addComponent(e);
		} else if (e.getEntryType() == ChangeEvent.REMOVE) {
			removeComponent(e);
		} else {
			updateComponent(e);
		}
	}

	private void addComponent(ChangeEvent evt) {
		String status = evt.getStatus().substring(0, evt.getStatus().indexOf(":"));
		String title = evt.getStatus().substring(evt.getStatus().indexOf(":") + 1);
		List<ClassStatusProperty> list = clazzes.get(new ObjectStatus(evt.getSource(), status));
		if (list != null) {
			Iterator<ClassStatusProperty> it = list.iterator();
			while (it.hasNext()) {
				ClassStatusProperty csp = it.next();
				try {
					JComponent comp = csp.getClazz().newInstance();
					JLabel label = new JLabel(title + ":");
					comp.putClientProperty("Label", label);
					this.add(label);
					this.add(comp);
					for (int i = 0; i < csp.getStatuses().size(); i++) {
						if (csp.getStatuses().get(i).equals(status)) {
							addComponent(evt.getSource(), evt.getStatus(), comp, csp.getProperties().get(i));
							updateProperty(comp, csp.getProperties().get(i), evt);
						} else {
							addComponent(evt.getSource(), csp.getStatuses().get(i), comp, csp.getProperties().get(i));
							updateProperty(comp, csp.getProperties().get(i), events.get(new ObjectStatus(evt.getSource(), csp.getStatuses().get(i))));
						}
					}
					SpringUtilities.makePropertyGrid(this, this.getComponentCount() / 2, 2, 5, 5, 3, 3);
					SwingUtilities.getRootFrame(this).pack();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void removeComponent(ChangeEvent evt) {
		List<ComponentProperty> cps = components.get(new ObjectStatus(evt));
		if (cps != null) {
			for (int i = 0; i < cps.size(); i++) {
				ComponentProperty cp = cps.get(i);
				this.remove(cp.getComponent());
				this.remove((JComponent)cp.getComponent().getClientProperty("Label"));
			}
			cps.remove(evt.getStatus());
			SpringUtilities.makePropertyGrid(this, this.getComponentCount() / 2, 2, 5, 5, 3, 3);
			SwingUtilities.getRootFrame(this).pack();
		}
	}


}
