package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.parsing.ElementHandler;
import org.xml.sax.Attributes;

/**
 * Represents an element in a templates XML file, that represents an edge in the underlying design pattern.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class EdgeElement implements ElementHandler {
    private Edge edge;

    /**
     * Package protected constructor to prevent access form outside the package.
     */
    EdgeElement() {
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(final String qName, final Attributes attributes) {
        edge = new Edge(
                new Clazz(attributes.getValue("node1"), attributes.getValue("node1")),
                new Clazz(attributes.getValue("node2"), attributes.getValue("node2")),
                EdgeType.valueOf(attributes.getValue("type").toUpperCase()));
    }

    /**
     * {@inheritDoc}
     */
    public ElementHandler endElement(final String qName) {
        return null;
    }

    /**
     * Returns the edge in this element.
     *
     * @return an {@link Edge} instance.
     */
    public Edge getEdge() {
        return edge;
    }
}
