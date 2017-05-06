package nl.ou.dpd.parsing.template;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.parsing.ElementHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DefaultHandler} for the {@link TemplatesParser}. It handles start tags and end tags, encountered in a
 * templates XML file.
 *
 * @author E.M. van Doorn
 * @author Martin de Boer
 */
public final class TemplateSaxHandler extends DefaultHandler {

    private static final Logger LOGGER = LogManager.getLogger(TemplateSaxHandler.class);

    private List<DesignPattern> templates;
    private ElementHandler handler;
    private TemplateElement templateElement;

    /**
     * Package protected constructor to prevent access form outside the package.
     */
    TemplateSaxHandler() {
        handler = null;
        templates = new ArrayList();
    }

    /**
     * Handles a start element in the XML file.
     *
     * @param uri        is ignored
     * @param localName  is ignored
     * @param qName      the tag name
     * @param attributes the tag attributes
     * @throws SAXException if an error occurs
     */
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {

        if (handler != null) {
            handler.startElement(qName, attributes);
            return;
        }

        switch (qName) {
            case "templates":
                break;

            case "template":
                templateElement = new TemplateElement();
                handler = templateElement;
                handler.startElement(qName, attributes);
                break;

            default:
                final String msg = "Unexpected tag: " + qName + ".";
                LOGGER.error(msg);
                throw new SAXException(msg);
        }
    }

    /**
     * Handles an end element in the templates XML file.
     *
     * @param uri       is ignored
     * @param localName is ignored
     * @param qName     the tag name
     * @throws SAXException if an error occurs
     */
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        if (handler != null) {
            handler = handler.endElement(qName);
        }

        if (qName.equals("template")) {
            templates.add(templateElement.getTemplate());
        }
    }

    List<DesignPattern> getTemplates() {
        return templates;
    }
}
