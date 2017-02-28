package nl.ou.dpd.data.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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
     * @throws SAXException if an unexpected tag occurs
     */
    void startElement(String qName, Attributes attributes) throws SAXException;

    /**
     * Handles the end tag of an XML element.
     *
     * @param qName the name of the element (tag name).
     * @return an {@link ElementHandler} instance.
     */
    ElementHandler endElement(String qName);
}

