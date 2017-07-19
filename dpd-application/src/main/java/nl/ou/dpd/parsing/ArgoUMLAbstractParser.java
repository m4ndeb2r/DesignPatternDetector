package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.node.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * An abstract parent class of specified ArgoUML parsers. This abstract parent class contains shared functionality of
 * inheriting classes.
 *
 * @author Martin de Boer
 */
public abstract class ArgoUMLAbstractParser {

    private static final Logger LOGGER = LogManager.getLogger(ArgoUMLAbstractParser.class);

    private XMLInputFactory xmlInputFactory;

    /**
     * A constructor expecting an {@link XMLInputFactory}.
     * <p>
     * This constructor has protected access so it can only be instantiated from within the same package (by the
     * ParserFactory or in a unit test in the same package).
     *
     * @param xmlInputFactory used for instantiating {@link XMLEventReader}s processing XML files.
     */
    protected ArgoUMLAbstractParser(XMLInputFactory xmlInputFactory) {
        this.xmlInputFactory = xmlInputFactory;
    }

    /**
     * Contains the passed events. Keeps track of the hierarchical structure of the XMI-tags.
     */
    protected Stack<XMLEvent> events = new Stack<>();
    protected Map<String, Node> nodes;

    protected void doParse(String filename) {
        try (InputStream input = new FileInputStream(new File(filename))) {
            final XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(input);
            handleEvents(eventReader);
        } catch (ParseException pe) {
            // We don't need to repackage a ParseException in a ParseException.
            // Rethrow ParseExceptions directly
            throw pe;
        } catch (Exception e) {
            String msg = String.format("The XMI file '%s' could not be parsed.", filename);
            error(msg, e);
        }
    }

    protected void handleEvents(XMLEventReader eventReader) throws XMLStreamException {
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            handleEvent(event);
        }
    }

    protected void handleEvent(XMLEvent event) {
        if (event.isStartElement()) {
            handleStartElement(event);
        }
        if (event.isEndElement()) {
            handleEndElement(event);
        }
    }

    protected abstract void handleStartElement(XMLEvent event);

    protected abstract void handleEndElement(XMLEvent event);

    /**
     * Return an Attributes Map with the attribute name as key and the attribute value as value, retrieved from the
     * specified {@link XMLEvent}.
     *
     * @param event the {@link XMLEvent} containing the attributes.
     * @return a Map containing attributes, extracted from the {@code event}.
     */
    protected Map<String, String> readAttributes(XMLEvent event) {
        final Map<String, String> attributes = new HashMap<>();
        ((Iterator<Attribute>) event.asStartElement().getAttributes())
                .forEachRemaining(attr -> attributes.put(attr.getName().getLocalPart(), attr.getValue()));
        return attributes;
    }

    protected String getParentElementNameLocalPart() {
        return getStartElementNameLocalPart(events.peek());
    }

    protected String getStartElementNameLocalPart(XMLEvent event) {
        return event.asStartElement().getName().getLocalPart();
    }

    protected void error(String msg, Exception cause) {
        LOGGER.error(msg, cause);
        throw new ParseException(msg, cause);
    }
}
