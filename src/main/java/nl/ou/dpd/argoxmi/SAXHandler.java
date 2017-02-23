/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

/**
 *
 * @author E.M. van Doorn
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler implements Constants {

    private VerwerkSAXTags handler;

    public SAXHandler() {
        handler = null;
    }

    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {

        if (handler != null)
        {
            handler.startElement(qName, attributes);

            return;
        }

        switch (qName)
        {
            case ABSTRACTION_TAG:
            {
                AbstractionElement ael = new AbstractionElement();
                handler = ael;

                break;
            }

            case CLASS_TAG:
            case INTERFACE_TAG:
            {
                ClassElement cel = new ClassElement();

                ArgoXMI.classElements.put(attributes.getValue(ID), cel);
                handler = cel;

                cel.startElement(qName, attributes);

                break;
            }

            case GENERALIZATION_TAG:
            {
                GeneralizationElement gel = new GeneralizationElement();
                handler = gel;

                break;
            }

            case ASSOCIATION_TAG:
            {
                AssociationElement asel = new AssociationElement();
                handler = asel;

                break;
            }

            default:
                break;
        }
    }

    public void endElement(String uri, String localName,
            String qName) throws SAXException {

        if (handler != null)
        {
            handler = handler.endElement(qName);
        }
    }
}
