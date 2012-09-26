package model;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="node")
@XmlAccessorType(XmlAccessType.NONE)
public class DataTag
{
	private DataTag parent;
	private String path = "/";
	
	@XmlElement(name="node") private Collection<DataTag> child_tags = new ArrayList<DataTag>();
	@XmlAttribute(name="label") private String label;
	@XmlAttribute(name="custom") private boolean custom;
	@XmlAttribute(name="default") private boolean dfault;
	@XmlAttribute(name="extensionAllowed") private boolean extension_allowed;
	@XmlAttribute(name="requireChild") private boolean require_child;
	@XmlAttribute(name="description") private String description;
	
	DataTag()
	{
		this.parent = null;
		this.label = "(new tag)";
		this.path = "";
		this.description = "";
	}
	
	public DataTag getParent() { return parent; }
	public Collection<DataTag> getChildTags() { return child_tags; }
	public String getPath() { return path; }
	public String getFullPath() { return path + label; }
	public String getLabel() { return label; }
	public String getDescription() { return description; }
	public boolean isCustom() { return custom; }
	public boolean isDefault() { return dfault; }
	public boolean isExtensionAllowed() { return extension_allowed; }
	public boolean isChildRequired() { return require_child; }
	
	void setParent(DataTag parent) { this.parent = parent; }
	void setPath(String path) { this.path = path; }
	void setLabel(String label) { this.label = label; }
	void setDescription(String description) { this.description = description; }
	void setCustom(boolean custom) { this.custom = custom; }
	
	void updatePath()
	{
		DataTag tag = this;
		path = "/";
		while ((tag = tag.getParent()) != null)
		{
			path = "/" + tag.getLabel() + path;
		}
	}
	
	public int getTagDepth()
	{
		int depth = 0;
		DataTag t = this;
		while ((t = t.getParent()) != null) depth++;
		return depth;
	}
}
