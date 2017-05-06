package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.parsing.ElementHandler;
import org.xml.sax.Attributes;

/**
 * Represents an element in an XMI file, that represents an association in the underlying UML design. An association is
 * denoted  from left to right; aggregation and composition sign are denoted at right side.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class AssociationElement implements Constants, ElementHandler {

    private ElementHandler handler;
    private boolean isAggregate, isComposite, isNavigable, isFirstPart;
    private String leftElement, rightElement;

    /**
     * Package protected constructor to prevent access from outside the package.
     */
    AssociationElement() {
        handler = null;
        isFirstPart = true;
        leftElement = null;
        rightElement = null;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        switch (qName) {
            case ASSOCIATION_END_TAG:
                if (isFirstPart) {
                    isNavigable = attributes.getValue(IS_NAVIGABLE).equals("true");
                    isAggregate = attributes.getValue(AGGREGATION).equals(AGGREGATE);
                    isComposite = attributes.getValue(AGGREGATION).equals(COMPOSITE);

                } else {
                    isNavigable = isNavigable && attributes.getValue(IS_NAVIGABLE).equals("true");
                    isAggregate = isAggregate || attributes.getValue(AGGREGATION).equals(AGGREGATE);
                    isComposite = isComposite || attributes.getValue(AGGREGATION).equals(COMPOSITE);
                }
                break;


            case INTERFACE_TAG:
            case CLASS_TAG:
                if (isFirstPart) {
                    if (isAggregate || isComposite || isNavigable) {
                        rightElement = attributes.getValue(ID_REF);
                    } else {
                        leftElement = attributes.getValue(ID_REF);
                    }
                    isFirstPart = false;
                } else {
                    if (leftElement == null) {
                        leftElement = attributes.getValue(ID_REF);
                    } else {
                        rightElement = attributes.getValue(ID_REF);
                    }
                    ArgoXMIParser.associationElements.add(this);
                }
                break;


            default:
                break;
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
        return qName.equals(ASSOCIATION_TAG) ? null : this;
    }

    /**
     * Return an {@link Edge} representation of this element.
     *
     * @return an {@link Edge} representation of this element.
     */
    public Edge getEdge() {
        final EdgeType type;

        if (isAggregate) {
            type = EdgeType.AGGREGATE;
        } else if (isComposite) {
            type = EdgeType.COMPOSITE;
        } else if (isNavigable) {
            type = EdgeType.ASSOCIATION;
        } else {
            type = EdgeType.ASSOCIATION_DIRECTED;
        }

        return new Edge(
                new Clazz(ArgoXMIParser.classElements.get(leftElement).getName(), ArgoXMIParser.classElements.get(leftElement).getName()),
                new Clazz(ArgoXMIParser.classElements.get(rightElement).getName(), ArgoXMIParser.classElements.get(rightElement).getName()),
                type);
    }
}
