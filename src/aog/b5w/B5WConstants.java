package aog.b5w;

/**
 * Interface that holds widely used constants
 * 
 * Yes it is an anti-pattern but they are used EVERYWHERE
 * 
 * @author Ed Webb
 *
 */
public interface B5WConstants {

	public static final int NONE = 0;
	
	public static final int FINISHED = -1;
	
	public static final int ACTIVE = 1;

	// Phases
	public static final int PHASE_POWER = 1;
	public static final int PHASE_INITIATIVE = 2;
	public static final int PHASE_EW = 3;
	public static final int PHASE_ACCELERATION = 4;

	public static final int PHASE_JUMP_POINT = 5;
	public static final int PHASE_MOVE = 6;
	
	public static final int PHASE_TARGET = 7;
	public static final int PHASE_FIRE = 8;
	public static final int PHASE_CRITICAL = 9;
	public static final int PHASE_LAUNCH = 10;
	public static final String[] PHASES = new String[] {"", "Power", "Initiative", "EW", "Acceleration", "Jump Point", "Move", "Target", "Fire", "Critical", "Launch"};
	
	// Locations
	public static final int PRIMARY = 0;
	public static final int FORE = 1;
	public static final int STARBOARD = 2;
	public static final int AFT = 3;
	public static final int PORT = 4;
	public static final String[] LOCATIONS = new String[] {"Primary", "Fore", "Starboard", "Aft", "Port"};

	public static final int CLOCKWISE = 1;
	public static final int ANTICLOCKWISE = 2;
	public static final String[] ROTATION = new String [] {"None", "Clockwise", "Anti-clockwise"};

	// Thrust Types
	public static final int ACCELERATE = 1;
	public static final int TURN = 2;
	public static final int TURN_CLOCKWISE = TURN + CLOCKWISE;
	public static final int TURN_ANTICLOCKWISE = TURN + ANTICLOCKWISE;
	public static final int SLIDE = 5;
	public static final int ROLL = 6;
	public static final int PIVOT = 7;
	public static final int PIVOT_CLOCKWISE = PIVOT + CLOCKWISE;
	public static final int PIVOT_ANTICLOCKWISE = PIVOT + ANTICLOCKWISE;
	public static final String[] THRUSTS = new String[] {"None", "Accelerate", "Turn", "Turn Clockwise", "Turn Anti-clockwise", "Slide", "Roll", "Pivot", "Pivot Clockwise", "Pivot Anti-clockwise"};
	
	// Fractions
	public static final int NUMERATOR = 0;
	public static final int DENOMINATOR = 1;
	
	
}
