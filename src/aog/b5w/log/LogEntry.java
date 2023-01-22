package aog.b5w.log;

public class LogEntry {

	public static final String DAMAGE = "Damage";
	public static final String CRITICAL = "Critical";
	public static final String POWER = "Power";
	
	private int turn;
	private int phase;
	private String source; 
	private String type;
	private StringBuilder description;

	public LogEntry(String source, int turn, int phase, String type, String description) {
		this.source = source;
		this.turn = turn;
		this.phase = phase;
		this.type = type;
		this.description = new StringBuilder(description);
	}
	
	public String getSource() {
		return source;
	}

	public int getTurn() {
		return turn;
	}

	public int getPhase() {
		return phase;
	}

	public String getType() {
		return type;
	}
	
	public String getDescription() {
		return description.toString();
	}
	
	public void append(String text) {
		description.append(text);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(turn);
		sb.append(":");
		sb.append(phase);
		sb.append(" - ");
		sb.append(source);
		sb.append(" - ");
		sb.append(type);
		sb.append(" - ");
		sb.append(description);
		return sb.toString();
	}
	
	
}
