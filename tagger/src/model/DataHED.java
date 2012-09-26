package model;

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
public class DataHED
{
	@XmlAttribute(name="version") private String version;
    @XmlElement(name="node") private Collection<DataTag> tags = new ArrayList<DataTag>();

    Collection<DataTag> getTags()
    {
        return tags;
    }
    
    @Override public String toString()
    {
    	String s = "";
    	for (DataTag n : tags)
    	{
    		s += n.toString() + "\n";
    	}
    	return s;
    }

}

