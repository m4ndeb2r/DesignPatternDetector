package nl.ou.dpd.data.template;

/**
 * Represents an element in a templates XML file, that represents an edge in the underlying design pattern.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */

import nl.ou.dpd.data.parser.ElementHandler;
import nl.ou.dpd.domain.DesignPatternClass;
import nl.ou.dpd.domain.DesignPatternEdge;
import nl.ou.dpd.domain.EdgeType;
import org.xml.sax.Attributes;

public final class EdgeElement implements ElementHandler {
    private DesignPatternEdge edge;

    /**
     * Protected constructor to prevent access form outside the package.
     */
    protected EdgeElement() {
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        edge = new DesignPatternEdge(
                new DesignPatternClass(attributes.getValue("node1")),
                new DesignPatternClass(attributes.getValue("node2")),
                EdgeType.valueOf(attributes.getValue("type").toUpperCase()));
    }

    /**
     * {@inheritDoc}
     */
    public ElementHandler endElement(String qName) {
        return null;
    }

    public DesignPatternEdge getEdge() {
        return edge;
    }
}
