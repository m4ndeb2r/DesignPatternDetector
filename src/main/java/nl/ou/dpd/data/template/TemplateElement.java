package nl.ou.dpd.data.template;

/**
 * Represents a template element in the design pattern templates XML file.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */

import nl.ou.dpd.data.parser.ElementHandler;
import nl.ou.dpd.domain.DesignPattern;
import org.xml.sax.Attributes;

public final class TemplateElement implements ElementHandler {

    private ElementHandler handler;
    private DesignPattern designPattern;
    private EdgeElement edgeElement;

    /**
     * Protected constructor to prevent access form outside the package.
     */
    protected TemplateElement() {
        handler = null;
        designPattern = null;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) {
        if (designPattern == null) {
            designPattern = new DesignPattern(attributes.getValue("name"));
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
                designPattern.add(edgeElement.getEdge());
            }

            return this;
        }

        return qName.equals("template") ? null : this;
    }

    /**
     * Returns a {@link DesignPattern} representation of the template element.
     *
     * @return the parsed {@link DesignPattern}.
     */
    public DesignPattern getTemplate() {
        return designPattern;
    }
}
