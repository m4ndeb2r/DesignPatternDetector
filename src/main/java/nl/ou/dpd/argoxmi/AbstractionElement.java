/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

import org.xml.sax.Attributes;

/**
 *
 * @author E.M. van Doorn
 */

public class AbstractionElement implements Constants, VerwerkSAXTags {

    private VerwerkSAXTags handler;
    private String implementer, superClassInterface;
    private boolean expectImplementer;

    AbstractionElement() {
        handler = null;
        expectImplementer = true;
    }

    public void startElement(String qName, Attributes attributes) {
        if (qName.equals(CLASS_TAG) || qName.equals(INTERFACE_TAG))
        {
            if (expectImplementer)
            {
                implementer = new String(attributes.getValue(ID_REF));
                expectImplementer = false;
            } else
            {
                superClassInterface = attributes.getValue(ID_REF);
                ArgoXMI.abstractElements.add(this);
            }
        }
    }

    public VerwerkSAXTags endElement(String qName) {

        if (handler != null)
        {
            handler = handler.endElement(qName);

            return this;
        }

        return qName.equals(ABSTRACTION_TAG) ? null : this;
    }
    
    public String getImplementer()
    {
        return implementer;
    }
    
    public String getSuper()
    {
        return superClassInterface;
    }
}
