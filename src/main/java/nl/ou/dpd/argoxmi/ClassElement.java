/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ou.dpd.argoxmi;

import nl.ou.dpd.sax.ElementHandler;
import org.xml.sax.Attributes;

import java.util.ArrayList;

/**
 * @author E.M. van Doorn
 */

public class ClassElement implements Constants, ElementHandler {

    private String name;
    private ArrayList<String> dependentClasses;
    private ElementHandler handler;
    private boolean expectSupplier, inClass;

    ClassElement() {
        name = null;
        dependentClasses = new ArrayList();
        handler = null;
        expectSupplier = false;
        inClass = false;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        if (name == null) {
            name = attributes.getValue(NAME);
        } else {
            switch (qName) {
                case DEPENDENCY_SUPPLIER_TAG:
                    expectSupplier = true;

                    break;

                case INTERFACE_TAG:
                case CLASS_TAG:
                    inClass = true;
                    if (expectSupplier) {
                        dependentClasses.add(attributes.getValue(ID_REF));
                    }

                    break;

                default:
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

            return this;
        }

        if (qName.equals(DEPENDENCY_SUPPLIER_TAG)) {
            expectSupplier = false;
        }

        if (qName.equals(CLASS_TAG) || qName.equals(INTERFACE_TAG)) {
            if (inClass) {
                inClass = false;

                return this;
            }

            return null;
        }

        return this;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getDependencies() {
        ArrayList<String> result;

        if (dependentClasses.isEmpty()) {
            return null;
        }

        result = new ArrayList();

        for (String s : dependentClasses) {
            result.add(ArgoXMI.classElements.get(s).getName());
        }

        return result;
    }
}
