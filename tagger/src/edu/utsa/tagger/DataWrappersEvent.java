package edu.utsa.tagger;
import java.util.UUID;

public class DataWrappersEvent implements Comparable<DataWrappersEvent> {

	private UUID uuid;
	private String code;
	private String label;
	private String description;

	public DataWrappersEvent(String code, String label, String description) {
		uuid = UUID.randomUUID();
		this.code = code;
		this.label = label;
		this.description = description;
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public String getCode()
	{
		return code;
	}

	public String getLabel() {
		return label;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setLabel(String label) {
		if (label == null) {
			throw new NullPointerException();
		}
		else {
			this.label = label;
		}
	}

	@Override public int compareTo(DataWrappersEvent item) {
		if (item == null) {
			throw new NullPointerException();
		}
		else {
			return this.label.compareTo(item.label);
		}
	}

	@Override public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DataWrappersEvent)) {
			return false;
		}
		else {
			return (this.compareTo((DataWrappersEvent) obj) == 0);
		}
	}

}
