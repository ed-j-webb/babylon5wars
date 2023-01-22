package aog.b5w.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import aog.b5w.event.ChangeEvent;
import aog.b5w.event.ChangeListener;
import aog.b5w.event.ChangeProducer;
import aog.b5w.gui.utils.ReflectUtilities;
import aog.b5w.gui.utils.SpringUtilities;

/**
 * A panel that knows enough to create JComponents and wire them up to ChangeEvents
 * 
 * @author Ed Webb
 *
 */
public abstract class BasePanel extends JPanel implements ChangeListener, ActionListener {

	protected Map<ObjectStatus, List<ComponentProperty>> components = new HashMap<ObjectStatus, List<ComponentProperty>>();
	protected Map<ObjectStatus, ChangeEvent> events = new HashMap<ObjectStatus, ChangeEvent>();
	
	/**
	 * The version ID
	 */
	private static final long serialVersionUID = 1L;

	protected ChangeProducer object;
	
	public BasePanel(ChangeProducer object) {
		this.object = object;
		object.addChangeListener(this);
	}

	public ChangeProducer getObject() {
		return object;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getEntryType() == ChangeEvent.UPDATE) {
			updateComponent(e);
		}
	}

	protected void updateComponent(ChangeEvent evt) {
		events.put(new ObjectStatus(evt), evt);
		List<ComponentProperty> cps = components.get(new ObjectStatus(evt));
		if (cps != null) {
			for (int i = 0; i < cps.size(); i++) {
				JComponent c = cps.get(i).getComponent();
				String p = cps.get(i).getProperty();
				updateProperty(c, p, evt);
				c.repaint();
			}
		}
	}

	protected void updateProperty(JComponent c, String p, ChangeEvent evt) {
		if (evt.getDataType() == ChangeEvent.INTEGER) {
			ReflectUtilities.setProperty(c, p, new Object[] {evt.getNewIntValue()}, new Class[] {Integer.TYPE});
		} else if (evt.getDataType() == ChangeEvent.BOOLEAN) {
			ReflectUtilities.setProperty(c, p, new Object[] {evt.isNewBooleanValue()}, new Class[] {Boolean.TYPE});
		} else if (evt.getDataType() == ChangeEvent.STRING) {
			ReflectUtilities.setProperty(c, p, new Object[] {evt.getNewStringValue()});
		}
	}
	
	protected void addCaption(JPanel panel, String caption) {
		JLabel label = new JLabel(caption);
		panel.add(label);
	}
	
	/**
	 * Add a label control to the form with a String property
	 * 
	 * @param panel
	 * @param caption
	 * @param name
	 * @param source
	 * @param textStatus
	 */
	protected void addLabel(JPanel panel, String caption, String name, Object source, String textStatus) {
		JLabel label = new JLabel(caption);
		JLabel2 value = new JLabel2(name);
		value.putClientProperty("Label", label);
		panel.add(label);
		panel.add(value);
		addComponent(source, textStatus, value, "text");
	}

	/**
	 * Add a label control to the form with an int property
	 * 
	 * @param panel
	 * @param caption
	 * @param value
	 * @param source
	 * @param textStatus
	 */
	protected void addLabel(JPanel panel, String caption, int value, Object source, String textStatus) {
		addLabel(panel, caption, Integer.toString(value), source, textStatus);
	}

	/**
	 * Add a label control to the form with a boolean property
	 * 
	 * @param panel the JPanel to add the control to
	 * @param caption the caption for the control
	 * @param value the value of the component
	 * @param source the source of the ChangeEvent
	 * @param textStatus the status of the ChangeEvent
	 */
	protected void addLabel(JPanel panel, String caption, boolean value, Object source, String textStatus) {
		addLabel(panel, caption, value ? "Yes" : "No", source, textStatus);
	}
	
	/**
	 * Add a progressbar control to the form
	 * 
	 * @param panel
	 * @param caption
	 * @param total
	 * @param current
	 * @param source
	 * @param totalStatus
	 * @param valueStatus
	 */
	protected void addProgressBar(JPanel panel, String caption, int total, int current, Object source, String totalStatus, String valueStatus) {
		JLabel label = new JLabel(caption);
		JProgressBar2 bar = new JProgressBar2(0, total);
		SpringUtilities.fixHeight(bar);
		bar.setValue(current);
		bar.putClientProperty("Label", label);
		panel.add(label);
		panel.add(bar);
		addComponent(source, valueStatus, bar, "value");
		addComponent(source, totalStatus, bar, "maximum");
	}
	
	protected void addToggleButton(JPanel panel, String off, String on, boolean state, Object source, String selectedStatus) {
		JToggleButton2 tog = new JToggleButton2();
		tog.setStates(off, on);
		tog.setSelected(state);
		tog.putClientProperty("System", source);
		tog.addActionListener(this);
		panel.add(tog);
		addComponent(source, selectedStatus, tog, "selected");
	}
	
	protected void addButton(JPanel panel, String caption, Object source, String method) {
		JButton but = new JButton(caption);
		but.putClientProperty("System", source);
		but.putClientProperty("Method", method);
		but.addActionListener(this);
		panel.add(but);
	}
	
	protected void addCheckbox(JPanel panel, String caption, boolean state, Object source, String selectedStatus) {
		JCheckBox box = new JCheckBox(caption);
		box.putClientProperty("System", source);
		box.addActionListener(this);
		box.setSelected(state);
		panel.add(box);
		addComponent(source, selectedStatus, box, "selected");
	}
	
	/**
	 * Add a JComponent to the components list
	 * 
	 * @param source the source of the ChangeEvent
	 * @param status the status of the ChangeEvent
	 * @param component the JComponent that will be affected by the ChangeEvent
	 * @param property the property of the JComponent that will be affected by the ChangeEvent
	 */
	public void addComponent(Object source, String status, JComponent component, String property) {
		List<ComponentProperty> comps = components.get(new ObjectStatus(source, status));
		if (comps == null) {
			comps = new ArrayList<ComponentProperty>();
			components.put(new ObjectStatus(source, status), comps);
		}
		comps.add(new ComponentProperty(component, property));
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
	}

	protected class ComponentProperty {
		protected JComponent component;
		protected String property;
		
		public ComponentProperty(JComponent component, String property) {
			this.component = component;
			this.property = property;
		}

		public JComponent getComponent() {
			return component;
		}

		public String getProperty() {
			return property;
		}
	}

	protected class ClassStatusProperty {
		protected Class<? extends JComponent> clazz;
		protected List<String> statuses = new ArrayList<String>();
		protected List<String> properties = new ArrayList<String>();
		
		public ClassStatusProperty(Class<? extends JComponent> clazz, String status, String property) {
			this.clazz = clazz;
			this.statuses.add(status);
			this.properties.add(property);
		}

		public ClassStatusProperty(Class<? extends JComponent> clazz, List<String> statuses, List<String> properties) {
			this.clazz = clazz;
			this.statuses.addAll(statuses);
			this.properties.addAll(properties);
		}

		public Class<? extends JComponent> getClazz() {
			return clazz;
		}

		public List<String> getStatuses() {
			return statuses;
		}

		public List<String> getProperties() {
			return properties;
		}
	}

	protected class ObjectStatus {
		protected Object object;
		protected String status;
		
		public ObjectStatus(Object object, String status) {
			this.object = object;
			this.status = status;
		}

		public ObjectStatus(ChangeEvent evt) {
			this.object = evt.getSource();
			this.status = evt.getStatus();
		}
		
		public Object getObject() {
			return object;
		}

		public String getStatus() {
			return status;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ObjectStatus) {
				ObjectStatus os = (ObjectStatus)obj;
				if (os.getObject().equals(object) && os.getStatus().equals(status)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return object.hashCode() + status.hashCode() >> 32;
		}
		
		
	}
}
