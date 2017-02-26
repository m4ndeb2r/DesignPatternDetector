package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.data.parser.ElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A {@link DefaultHandler} for the {@link ArgoXMIParser}. It handles start tags and end tags, encountered in an XMI
 * file.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public class ArgoXmiSaxHandler extends DefaultHandler implements Constants {

    private ElementHandler handler;

    /**
     * A default constructor.
     */
    public ArgoXmiSaxHandler() {
        handler = null;
    }

    /**
     * Handles a start element in the XMI file.
     *
     * @param uri        is ignored
     * @param localName  is ignored
     * @param qName      the tag name
     * @param attributes the tag attributes
     * @throws SAXException when an error occurs
     */
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        if (handler != null) {
            handler.startElement(qName, attributes);
            return;
        }

        switch (qName) {
            case ABSTRACTION_TAG: {
                handler = new AbstractionElement();
                break;
            }

            case CLASS_TAG:
            case INTERFACE_TAG: {
                final ClassElement cel = new ClassElement();
                ArgoXMIParser.classElements.put(attributes.getValue(ID), cel);
                handler = cel;
                cel.startElement(qName, attributes);
                break;
            }

            case GENERALIZATION_TAG: {
                handler = new GeneralizationElement();
                break;
            }

            case ASSOCIATION_TAG: {
                handler = new AssociationElement();
                break;
            }

            default: {
                break;
            }
        }
    }

    /**
     * Handles an end tag of an element in the XMI file.
     *
     * @param uri       is ignored
     * @param localName is ignored
     * @param qName     the name of the tag
     * @throws SAXException when an error occurs
     */
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        if (handler != null) {
            handler = handler.endElement(qName);
        }
    }
}
