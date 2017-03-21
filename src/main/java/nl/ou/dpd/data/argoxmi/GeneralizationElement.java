package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.data.parser.ElementHandler;
import org.xml.sax.Attributes;

/**
 * Represents an element in an XMI file, that represents a generalization in the underlying UML design.
 *
 * @author E.M. van Doorn
 */

public final class GeneralizationElement implements Constants, ElementHandler {

    private ElementHandler handler;
    private boolean expectChild;
    private String child;

    /**
     * Package protected constructor to prevent access from outside the package.
     */
    GeneralizationElement() {
        handler = null;
        expectChild = true;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        if (qName.equals(CLASS_TAG) || qName.equals(INTERFACE_TAG)) {
            if (expectChild) {
                child = new String(attributes.getValue(ID_REF));
                expectChild = false;
            } else {
                ArgoXMIParser.inheritanceElements.put(child, new String(attributes.getValue(ID_REF)));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ElementHandler endElement(String qName) {

        if (handler != null) {
            handler = handler.endElement(qName);

            return this;
        }

        return qName.equals(GENERALIZATION_TAG) ? null : this;
    }
}
