/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

import nl.ou.dpd.sax.ElementHandler;
import org.xml.sax.Attributes;

/**
 * @author E.M. van Doorn
 */

public class AbstractionElement implements Constants, ElementHandler {

    private ElementHandler handler;
    private String implementer, superClassInterface;
    private boolean expectImplementer;

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
                ArgoXMI.abstractElements.add(this);
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

    public String getImplementer() {
        return implementer;
    }

    public String getSuper() {
        return superClassInterface;
    }
}
