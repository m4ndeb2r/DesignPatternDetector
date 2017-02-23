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

public class GeneralizationElement implements Constants, VerwerkSAXTags {

    private VerwerkSAXTags handler;
    private boolean expectChild;
    private String child;

    GeneralizationElement() {
        handler = null;
        expectChild = true;   
    }

    public void startElement(String qName, Attributes attributes) {
        if (qName.equals(CLASS_TAG) || qName.equals(INTERFACE_TAG))
        {
            if (expectChild)
            {
                child = new String(attributes.getValue(ID_REF));
                expectChild = false;
            }
            else
            {
                ArgoXMI.inheritanceElements.put(child, new String(attributes.getValue(ID_REF)));
            }  
        }
    }

    public VerwerkSAXTags endElement(String qName) {

        if (handler != null)
        {
            handler = handler.endElement(qName);

            return this;
        }

        return qName.equals(GENERALIZATION_TAG) ? null : this;
    }
}
