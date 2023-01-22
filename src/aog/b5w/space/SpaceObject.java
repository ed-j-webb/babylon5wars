package aog.b5w.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aog.b5w.B5WConstants;
import aog.b5w.components.systems.weapons.Salvo;
import aog.b5w.event.ChangeListener;
import aog.b5w.event.ChangeNotifier;
import aog.b5w.event.ChangeProducer;
import aog.b5w.event.FireEvent;
import aog.b5w.event.FireListener;
import aog.b5w.event.PhaseEvent;
import aog.b5w.event.PhaseListener;
import aog.b5w.utils.TargetingSolution;

public abstract class SpaceObject implements B5WConstants, PhaseListener, FireListener, ChangeProducer {

    public final static int CLASS_CAPITALSHIP = 0;
    public final static int CLASS_MEDIUMVESSEL = 1;
    public final static int CLASS_SMALLCRAFT = 2;
	public final static String OBJECTCLASS[] = {"Capital Ship", "Medium Vessel", "Small Craft"};

	protected ChangeNotifier changeNotifier = new ChangeNotifier();
	
	protected int objectClass; // Type/Size of Object

    protected String name; // Name of the Object

	protected int directionOfTravel; // The direction the craft is travelling in (0, 2, 4, 6, 8, 10)

    protected int directionOfFacing; // The direction the craft is facing (0, 2, 4, 6, 8, 10)

    protected int x; // Vertical co-ordinate of craft

    protected int y; // Horizontal co-ordinate of craft
    
    protected int velocity;

    protected List<String> attributes = new ArrayList<String>();
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	public int getDirectionOfTravel() {
		return directionOfTravel;
	}

	public int getDirectionOfFacing() {
		return directionOfFacing;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getVelocity() {
		return velocity;
	}

	public int getObjectClass() {
		return objectClass;
	}

	public boolean hasAttribute(String attribute) {
		return attributes.contains(attribute);
	}
	
	public List<String> getAttributes() {
		return Collections.unmodifiableList(attributes);
	}

	public abstract void takeSalvo(Salvo s);
	
	public abstract void acceptTargets(Map<Integer, Set<TargetingSolution>> targetingSolutions);
	
	@Override
	public void beforePhase(PhaseEvent e) {
	
	}

	@Override
	public void isReady(PhaseEvent e) {

	}

	@Override
	public void afterPhase(PhaseEvent e) {

	}
	
	@Override
	public void takeFire(FireEvent e) {
		
	}
	
	@Override
	public void sendFire(FireEvent e) {
		
	}
	
	public void addChangeListener(ChangeListener l) {
		changeNotifier.addChangeListener(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		changeNotifier.removeChangeListener(l);
	}
	
}
