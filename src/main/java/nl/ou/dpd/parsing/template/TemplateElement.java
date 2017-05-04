package nl.ou.dpd.parsing.template;

import nl.ou.dpd.parsing.ElementHandler;
import nl.ou.dpd.domain.DesignPattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Represents a template element in the design pattern templates XML file.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class TemplateElement implements ElementHandler {

    private static final Logger LOGGER = LogManager.getLogger(TemplateElement.class);

    private ElementHandler handler;
    private DesignPattern designPattern;
    private EdgeElement edgeElement;

    /**
     * Package protected constructor to prevent access form outside the package.
     */
    TemplateElement() {
        handler = null;
        designPattern = null;
    }

    /**
     * {@inheritDoc}
     */
    public void startElement(String qName, Attributes attributes) throws SAXException {
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
                    final String msg = "Unexpected tag: " + qName + ".";
                    LOGGER.error(msg);
                    throw new SAXException(msg);
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
