package edu.utsa.tagger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="node")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataWrappersTag
{
	@XmlElement(name="uuid")
	private UUID uuid;
	@XmlElement(name="")
	private UUID parent_uuid;
	@XmlElement(name="label")
	private String label;
	@XmlElement(name="path")
	private String path;
	@XmlElement(name="description")
	private String description;
	@XmlElement(name="comments")
	private String comments;
	@XmlElement(name="frequency")
	private int frequency;
	@XmlElement(name="node")
	private Collection<DataWrappersTag> child_tags = new ArrayList<DataWrappersTag>();

	public Collection<DataWrappersTag> getChildTags()
	{
		return child_tags;
	}
	
	public DataWrappersTag()
	{
		this.uuid = null;
		this.parent_uuid = null;
		this.label = "";
		this.path = "";
		this.description = "";
		this.comments = "";
		this.frequency = 0;
	}
	
	public DataWrappersTag(UUID uuid) {
		this.uuid = uuid;
		this.parent_uuid = null;
		this.label = "";
		this.path = "";
		this.description = "";
		this.comments = "";
		this.frequency = 0;
	}

	public UUID getUuid() {
		return uuid;
	}

	public UUID getParentUuid() {
		return parent_uuid;
	}

	public String getLabel() {
		return label;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getComments() {
		return comments;
	}
	
	public int getFrequency() {
		return frequency;
	}

	public void setParentUuid(UUID parent_uuid) {
		this.parent_uuid = parent_uuid;
	}

	public void setLabel(String label) {
		if (label == null) {
			throw new NullPointerException();
		}
		else {
			this.label = label;
		}
	}
	
	public void setPath(String path) {
		if (path == null) {
			throw new NullPointerException();
		}
		else {
			this.path = path;
		}
	}
	
	public void setDescription(String description) {
		if (description == null) {
			throw new NullPointerException();
		}
		else {
			this.description = description;
		}
	}
	
	public void setComments(String comments) {
		if (comments == null) {
			throw new NullPointerException();
		}
		else {
			this.comments = comments;
		}
	}
	
	public void appendComments(String comment) {
		if (comment == null) {
			throw new NullPointerException();
		}
		else {
			if (this.comments.equals("")) {
				this.comments = comment;
			}
			else {
				this.comments = this.comments + "\n" + comment;
			}
		}
	}
	
	public void setFrequency(int frequency) {
		if (frequency < 0) {
			throw new IllegalArgumentException("Frequency cannot be less than zero.");
		}
		else {
			this.frequency = frequency;
		}
	}
}
