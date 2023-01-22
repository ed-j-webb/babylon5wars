package aog.b5w.event;

public class ChangeEvent {
	public static final int BOOLEAN = 0;
	public static final int INTEGER = 1;
	public static final int STRING = 2;
	
	public static final int UPDATE = 0;
	public static final int ADD = 1;
	public static final int REMOVE = 2;
	
	protected Object source;

	protected String status;
	
	protected int dataType;

	protected int entryType;
	
	protected int oldIntValue;
	protected boolean oldBooleanValue;
	protected String oldStringValue;

	protected int newIntValue;
	protected boolean newBooleanValue;
	protected String newStringValue;
	
	public ChangeEvent(Object source, String status, int oldValue, int newValue) {
		this.source = source;
		this.status = status;
		this.dataType = INTEGER;
		this.oldIntValue = oldValue;
		this.newIntValue = newValue;
	}

	public ChangeEvent(Object source, String status, boolean oldValue, boolean newValue) {
		this.source = source;
		this.status = status;
		this.dataType = BOOLEAN;
		this.oldBooleanValue = oldValue;
		this.newBooleanValue = newValue;
	}
	
	public ChangeEvent(Object source, String status, String oldValue, String newValue) {
		this.source = source;
		this.status = status;
		this.dataType = STRING;
		this.oldStringValue = oldValue;
		this.newStringValue = newValue;
	}

	public ChangeEvent(Object source, boolean add, String status, String value) {
		this.source = source;
		this.status = status;
		this.dataType = STRING;
		if (add) {
			this.newStringValue = value;
			this.entryType = ADD;
		} else {
			this.oldStringValue = value;
			this.entryType = REMOVE;
		}
	}
	
	public ChangeEvent(Object source, boolean add, String status, boolean value) {
		this.source = source;
		this.status = status;
		this.dataType = BOOLEAN;
		if (add) {
			this.newBooleanValue = value;
			this.entryType = ADD;
		} else {
			this.oldBooleanValue = value;
			this.entryType = REMOVE;
		}
	}

	public ChangeEvent(Object source, boolean add, String status, int value) {
		this.source = source;
		this.status = status;
		this.dataType = INTEGER;
		if (add) {
			this.newIntValue = value;
			this.entryType = ADD;
		} else {
			this.oldIntValue = value;
			this.entryType = REMOVE;
		}
	}

	public int getDataType() {
		return dataType;
	}

	public Object getSource() {
		return source;
	}

	public String getStatus() {
		return status;
	}

	public int getOldIntValue() {
		return oldIntValue;
	}

	public boolean isOldBooleanValue() {
		return oldBooleanValue;
	}

	public String getOldStringValue() {
		return oldStringValue;
	}

	public int getNewIntValue() {
		return newIntValue;
	}

	public boolean isNewBooleanValue() {
		return newBooleanValue;
	}

	public String getNewStringValue() {
		return newStringValue;
	}
	
	public int getEntryType() {
		return entryType;
	}

	public String getOldValue() {
		switch (dataType) {
		case BOOLEAN:
			return oldBooleanValue ? "Yes" : "No";
		case INTEGER:
			return Integer.toString(oldIntValue);
		case STRING:
			return oldStringValue;
		default:
			return "";
		}
	}

	public String getNewValue() {
		switch (dataType) {
		case BOOLEAN:
			return newBooleanValue ? "Yes" : "No";
		case INTEGER:
			return Integer.toString(newIntValue);
		case STRING:
			return newStringValue;
		default:
			return "";
		}
	}
}
