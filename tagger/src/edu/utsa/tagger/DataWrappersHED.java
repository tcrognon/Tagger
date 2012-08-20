package edu.utsa.tagger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="HED")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataWrappersHED
{
	@XmlAttribute(name="version")
	private String version;
    @XmlElement(name="node")
    private Collection<DataWrappersTag> tags = new ArrayList<DataWrappersTag>();

    public Collection<DataWrappersTag> getTags()
    {
        return tags;
    }

    public void setTags(Collection<DataWrappersTag> tags)
    {
        this.tags = tags;
    }
    
    @Override public String toString()
    {
    	String s = "";
    	for (DataWrappersTag n : tags)
    	{
    		s += n.toString() + "\n";
    	}
    	return s;
    }

}

