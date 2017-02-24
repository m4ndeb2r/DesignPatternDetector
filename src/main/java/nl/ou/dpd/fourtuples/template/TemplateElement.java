/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.fourtuples.template;

/**
 * @author E.M. van Doorn
 */

import nl.ou.dpd.fourtuples.FourTupleArray;
import nl.ou.dpd.sax.ElementHandler;
import org.xml.sax.Attributes;

public class TemplateElement implements ElementHandler {

    private ElementHandler handler;
    private FourTupleArray fta;
    private EdgeElement edgeElement;

    TemplateElement() {
        handler = null;
        fta = null;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        if (fta == null) {
            fta = new FourTupleArray(attributes.getValue("name"));
        } else {
            switch (qName) {
                case "edge":
                    edgeElement = new EdgeElement();
                    handler = edgeElement;
                    handler.startElement(qName, attributes);
                    break;

                default:
                    System.out.println("Unexpected tag: " + qName);
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ElementHandler endElement(String qName) {

        if (handler != null) {
            handler = handler.endElement(qName);

            if (qName.equals("edge")) {
                fta.add(edgeElement.getFourtuple());
            }

            return this;
        }

        return qName.equals("template") ? null : this;
    }

    public FourTupleArray getTemplate() {
        return fta;
    }
}
