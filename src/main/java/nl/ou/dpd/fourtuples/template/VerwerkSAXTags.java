/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.fourtuples.template;

import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public interface VerwerkSAXTags {
    public void startElement(String qName, Attributes attributes);
    public VerwerkSAXTags endElement(String qName);   
}

