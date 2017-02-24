/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.sax;

import org.xml.sax.Attributes;

/**
 * An {@link ElementHandler} processes start and end tags of XML elements.
 *
 * @author E.M. van Doorn
 */
public interface ElementHandler {
    /**
     * Handles a start tag of an XML element.
     *
     * @param qName      the name of the element (tag name)
     * @param attributes the element's attributes
     */
    void startElement(String qName, Attributes attributes);

    /**
     * Handles the end tag of an XML element.
     *
     * @param qName the name of the element (tag name).
     * @return an {@link ElementHandler} instance.
     */
    ElementHandler endElement(String qName);
}

