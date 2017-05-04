package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.parsing.ElementHandler;
import org.xml.sax.Attributes;

/**
 * Represents an element in an XMI file, that represents an abstraction in the underlying UML design.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class AbstractionElement implements Constants, ElementHandler {

    private ElementHandler handler;
    private String implementer, superClassInterface;
    private boolean expectImplementer;

    /**
     * Package protected constructor to prevent access from outside the package.
     */
    AbstractionElement() {
        handler = null;
        expectImplementer = true;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        if (qName.equals(CLASS_TAG) || qName.equals(INTERFACE_TAG)) {
            if (expectImplementer) {
                implementer = new String(attributes.getValue(ID_REF));
                expectImplementer = false;
            } else {
                superClassInterface = attributes.getValue(ID_REF);
                ArgoXMIParser.abstractElements.add(this);
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

        return qName.equals(ABSTRACTION_TAG) ? null : this;
    }

    /**
     * Gets the implementer of this {@link AbstractionElement}.
     *
     * @return the name of the implementing class.
     */
    public String getImplementer() {
        return implementer;
    }

    /**
     * Gets the implemented (super)class in the abstraction relation.
     *
     * @return the name of the superclass.
     */
    public String getSuper() {
        return superClassInterface;
    }
}
