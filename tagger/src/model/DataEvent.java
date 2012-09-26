package model;
import java.util.ArrayList;
import java.util.Collection;

public class DataEvent
{
	private String code;
	private String label;
	private String description;
	private Collection<DataTag> tags = new ArrayList<DataTag>();

	DataEvent()
	{
		this.code = "";
		this.label = "(new event)";
		this.description = "";
	}
	
	public String getCode() { return code; }
	public String getLabel() { return label; }
	public String getDescription() { return description; }
	public Collection<DataTag> getTags() { return tags; }
	
	void setCode(String code) { this.code = code; }
	void setLabel(String label) { this.label = label; }
	void setDescription(String description) { this.description = description; }
	
	public boolean containsTagData(DataTag tagdata)
	{
		return tags.contains(tagdata);
	}
}
