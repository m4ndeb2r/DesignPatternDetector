package nl.ou.dpd.data.argoxmi;

import nl.ou.dpd.data.parser.ElementHandler;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an element in an XMI file, that represents a class in the underlying UML design.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */

public final class ClassElement implements Constants, ElementHandler {

    private String name;
    private ArrayList<String> dependentClasses;
    private ElementHandler handler;
    private boolean expectSupplier, inClass;

    /**
     * Package protected constructor to prevent access from outside the package.
     */
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

    /**
     * Gets the name of the class this element is a representation of.
     *
     * @return the name of the class.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a list of names of dependant classes.
     *
     * @return a list of subclass names, or {@code null} if there are no dependent classes.
     */
    public List<String> getDependencies() {

        if (dependentClasses.isEmpty()) {
            return null;
        }

        final List<String> result = new ArrayList();
        for (String s : dependentClasses) {
            result.add(ArgoXMIParser.classElements.get(s).getName());
        }

        return result;
    }
}
