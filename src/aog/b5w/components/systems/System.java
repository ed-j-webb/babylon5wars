package aog.b5w.components.systems;

import aog.b5w.components.Component;
import aog.b5w.components.Structure;
import aog.b5w.event.PhaseEvent;
import aog.b5w.space.LargeCraft;
import aog.b5w.utils.Die;

public abstract class System extends Component {

    protected String type = "System";          // The type of system
    protected Structure structure;             // Reference to the structure that holds the system.
    protected boolean critical = false;        // True if System has taken damage this turn
    protected int criticals[];                  // stores the critical hit table

    /**
     * A ship system is constructed by giving it a Name, HitPoints and
     * Armour rating.
     */
    public System(String name, int hitPoints, int armour) {
        super(name, hitPoints, armour);
        this.attributes.add("System");
    }

    /**
     * Return whether a system may have suffered a critical hit.
     */
    public boolean isCritical(){
        return critical;
    }

    /**
     * Return the type of system this is.
     */
    public String getType(){
        return type;
    }

    public LargeCraft getCraft() {
		return structure.getCraft();
	}

	/**
	 * Called by the structure to register itself with the system
	 */
	public void setStructure(Structure s){
        structure = s;
    }

    /**
     * If any damage is sustained then a Critical may occur.
     */
    protected int takeDamage(int a) {
        int currDamage = damage;
        int overKill = super.takeDamage(a);
        if (damage > currDamage) {
        	critical = true;
		}
        return overKill;
	}

    /**
     * Used to resolve any critical damage that may occur
     * to the system. If system has been damaged and the number picked
     * from the intCritical array is greater than 0 then the Critical
     * method of that number is called.
     */
    public void takeCritical() {
        if (critical) {
            int number = Die.roll(20) + damage;
			
            int result = -1;
            for (int i = 0; i < criticals.length; i++) {
            	if (number < criticals[i]) {
            		result = i;
            		break;
            	}
            }
            if (result == -1) {
            	result = criticals.length;
            }
            
            switch (result) {
                case 0:
					break;
				case 1:
                    critical1();
					break;
				case 2:
                    critical2();
					break;
				case 3:
                    critical3();
					break;
				case 4:
                    critical4();
					break;
				case 5:
                    critical5();
					break;
				case 6:
                    critical6();
					break;
			}
            critical = false;
		}
	}

    /**
     * Updates the system if Critical 1 occurs.
     */
    protected void critical1() {
	}

    /**
     * Updates the system if Critical 2 occurs.
     */
    protected void critical2() {
	}

    /**
     * Updates the system if Critical 3 occurs.
     */
    protected void critical3() {
	}

    /**
     * Updates the system if Critical 4 occurs.
     */
    protected void critical4() {
	}

    /**
     * Updates the system if Critical 5 occurs.
     */
    protected void critical5() {
	}

    /**
     * Updates the system if Critical 6 occurs.
     */
    protected void critical6() {
	}

	@Override
	public void beforePhase(PhaseEvent e) {
		super.beforePhase(e);
		if (e.getPhase() == PHASE_CRITICAL && critical) {
			takeCritical();
		}
	}

	@Override
	public void isReady(PhaseEvent e) {
		super.isReady(e);
	}

	@Override
	public void afterPhase(PhaseEvent e) {
		super.afterPhase(e);
	}
	
	
	
}
