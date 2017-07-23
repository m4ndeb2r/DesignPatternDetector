package nl.ou.dpd.parsing;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.util.Arrays;
import java.util.Iterator;

import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A helper class for unit testing.
 *
 * @author Martin de Boer
 */
public class ParseTestHelper {

    /**
     * Private constructor, because this is a utility class.
     */
    private ParseTestHelper() {
    }

    /**
     * Creates a representation of an attribute of an XML tag, with a name and a value.
     *
     * @param name  the name of the attribute
     * @param value the value of the attribute
     * @return the created {@link Attribute}
     */
    static Attribute createAttributeMock(String name, String value) {
        final Attribute mock = mock(Attribute.class);
        when(mock.getName()).thenReturn(QName.valueOf(name));
        when(mock.getValue()).thenReturn(value);
        return mock;
    }

    static XMLEvent createXMLEventMock(String name) {
        return createXMLEventMock(name, (Iterator<Attribute>)null);
    }

    static XMLEvent createXMLEventMock(String name, Attribute attribute) {
        final Iterator attributesIterator = mock(Iterator.class);
        when(attributesIterator.hasNext()).thenReturn(
                true, // hasNext = true (next = attribute)
                false, // hasNext = false (no more attributes)
                true, // hasNext = true (next = attribute)
                false, // hasNext = false (no more attributes)
                true, // hasNext = true (next = attribute)
                false // hasNext = false (no more attributes)
        );
        when(attributesIterator.next()).thenReturn(attribute);
        return createXMLEventMock(name, attributesIterator);
    }

    static XMLEvent createXMLEventMock(String name, Attribute attribute1, Attribute attribute2) {
        final Iterator attributesIterator = mock(Iterator.class);
        when(attributesIterator.hasNext()).thenReturn(
                true, // hasNext = true (next = attribute1)
                true, // hasNext = true (next = attribute2)
                false, // hasNext = false (no more attributes)
                true, // hasNext = true (next = attribute1)
                true, // hasNext = true (next = attribute2)
                false, // hasNext = false (no more attributes)
                true, // hasNext = true (next = attribute1)
                true, // hasNext = true (next = attribute2)
                false // hasNext = false (no more attributes)
        );
        when(attributesIterator.next()).thenReturn(
                attribute1, // first time: next() returns attribute1
                attribute2, // second time: next() returns attribute2
                attribute1, // third time: next() returns attribute1 again
                attribute2, // ...
                attribute1,
                attribute2);
        return createXMLEventMock(name, attributesIterator);
    }

    static XMLEvent createXMLEventMock(String name, Iterator<Attribute> attributeIterator) {
        final XMLEvent event = mock(XMLEvent.class);

        // Start element
        final StartElement startElement = mock(StartElement.class);
        when(event.isStartElement()).thenReturn(
                true, // first time: start element
                false, // second time: end element
                true, // third time: start element
                false, // fourth time: end element
                true, // ...
                false
        );
        when(event.asStartElement()).thenReturn(startElement);
        when(startElement.getName()).thenReturn(QName.valueOf(name));
        if (attributeIterator != null) {
            when(startElement.getAttributes()).thenReturn(attributeIterator);
        }

        // End element
        final EndElement endElement = mock(EndElement.class);
        when(event.isEndElement()).thenReturn(
                false, // first time: start element
                true, // second time: end element
                false, // third time: start element
                true, // fourth time: end element
                false, // ...
                true
        );
        when(event.asEndElement()).thenReturn(endElement);
        when(endElement.getName()).thenReturn(QName.valueOf(name));

        return event;
    }

}
