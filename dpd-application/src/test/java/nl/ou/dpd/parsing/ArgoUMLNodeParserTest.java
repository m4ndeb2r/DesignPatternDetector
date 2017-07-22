package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.node.Node;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ArgoUMLNodeParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgoUMLNodeParserTest {

    // This file is created just to satisfy the FileInputStream of the parser
    private String xmiFile = getPath("/argoUML/dummy.xmi");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private XMLInputFactory xmlInputFactory;

    @Mock
    private XMLEventReader xmlEventReader;

    @Mock
    XMLEvent modelEvent, classEvent, interfaceEvent;

    @Mock
    StartElement modelStartElement, classStartElement, interfaceStartElement;

    @Mock
    EndElement modelEndElement, classEndElement, interfaceEndElement;

    private ArgoUMLNodeParser nodeParser;

    @Before
    public void initNodeParser() throws XMLStreamException {
        nodeParser = new ArgoUMLNodeParser(xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
    }

    @Before
    public void initEventReader() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // model start-element
                true, // class start-element
                true, // class end-element
                true, // interface start-element
                true, // interface end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent,
                classEvent, classEvent,
                interfaceEvent, interfaceEvent,
                modelEvent);
    }

    @Before
    public void initModelStartElement() {
        when(modelEvent.isStartElement()).thenReturn(true, false);
        when(modelEvent.asStartElement()).thenReturn(modelStartElement);
        when(modelStartElement.getName()).thenReturn(QName.valueOf("Model"));
    }

    @Before
    public void initModelEndElement() {
        when(modelEvent.isEndElement()).thenReturn(false, true);
        when(modelEvent.asEndElement()).thenReturn(modelEndElement);
        when(modelEndElement.getName()).thenReturn(QName.valueOf("Model"));
    }

    @Before
    public void initClassStartElement() {
        when(classEvent.isStartElement()).thenReturn(true, false, true, false);
        when(classEvent.asStartElement()).thenReturn(classStartElement);
        when(classStartElement.getName()).thenReturn(QName.valueOf("Class"));

        final Iterator attributesIterator = mock(Iterator.class);
        final Attribute idAttribute = ParseTestHelper.makeAttributeMock("xmi.id", "classNodeId");
        when(attributesIterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(attributesIterator.next()).thenReturn(idAttribute);
        when(classStartElement.getAttributes()).thenReturn(attributesIterator);
    }

    @Before
    public void initClassEndElement() {
        when(classEvent.isEndElement()).thenReturn(false, true, false, true);
        when(classEvent.asEndElement()).thenReturn(classEndElement);
        when(classEndElement.getName()).thenReturn(QName.valueOf("Class"));
    }

    @Before
    public void initInterfaceStartElement() {
        when(interfaceEvent.isStartElement()).thenReturn(true, false, true, false);
        when(interfaceEvent.asStartElement()).thenReturn(interfaceStartElement);
        when(interfaceStartElement.getName()).thenReturn(QName.valueOf("Interface"));

        final Iterator attributesIterator = mock(Iterator.class);
        final Attribute idAttribute = ParseTestHelper.makeAttributeMock("xmi.id", "interfaceNodeId");
        when(attributesIterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(attributesIterator.next()).thenReturn(idAttribute);
        when(interfaceStartElement.getAttributes()).thenReturn(attributesIterator);
    }

    @Before
    public void initInterfaceEndElement() {
        when(interfaceEvent.isEndElement()).thenReturn(false, true, false, true);
        when(interfaceEvent.asEndElement()).thenReturn(interfaceEndElement);
        when(interfaceEndElement.getName()).thenReturn(QName.valueOf("Interface"));
    }

    @Test
    public void testAnyException() {
        when(interfaceStartElement.getAttributes()).thenThrow(new IllegalArgumentException());
        thrown.expect(ParseException.class);
        thrown.expectCause(is(IllegalArgumentException.class));
        thrown.expectMessage(String.format("The XMI file '%s' could not be parsed.", xmiFile));
        nodeParser.parse(xmiFile);
    }

    @Test
    public void testParseException() {
        when(interfaceStartElement.getAttributes()).thenThrow(new ParseException("Darn!", null));
        thrown.expect(ParseException.class);
        thrown.expectMessage("Darn!");
        nodeParser.parse(xmiFile);
    }

    @Test
    public void testNumberOfNodes() {
        final Map<String, Node> nodeMap = nodeParser.parse(xmiFile);
        assertThat(nodeMap.keySet().size(), is(2));
        assertTrue(nodeMap.containsKey("classNodeId"));
        assertThat(nodeMap.get("classNodeId").getId(), is("classNodeId"));
        assertTrue(nodeMap.containsKey("interfaceNodeId"));
        assertThat(nodeMap.get("interfaceNodeId").getId(), is("interfaceNodeId"));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
