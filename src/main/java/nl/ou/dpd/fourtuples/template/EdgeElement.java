/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.fourtuples.template;

/**
 *
 * @author E.M. van Doorn
 */

import nl.ou.dpd.fourtuples.FourTuple;
import nl.ou.dpd.fourtuples.TagValue;
import org.xml.sax.Attributes;

public class EdgeElement implements VerwerkSAXTags {
    private FourTuple fourtuple;
    
    EdgeElement()
    { 
    }
    
    public void startElement(String qName, Attributes attributes)  { 
        fourtuple = new FourTuple(attributes.getValue("node1"), 
                attributes.getValue("node2"), 
                TagValue.getTagValue(attributes.getValue("type").toUpperCase()));
    }


    public VerwerkSAXTags endElement(String qName) {
        return null;
    }
    
    
    public FourTuple getFourtuple()
    {
        return fourtuple;
    }
}
