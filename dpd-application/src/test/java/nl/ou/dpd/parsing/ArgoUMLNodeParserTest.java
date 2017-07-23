package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
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

import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.CLASS;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ID;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.INTERFACE;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.MODEL;
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
    XMLEvent modelEvent, classEvent, abstractClassEvent, interfaceEvent;

    @Mock
    StartElement modelStartElement, classStartElement, abstractClassStartElement, interfaceStartElement;

    @Mock
    EndElement modelEndElement, classEndElement, abstractClassEndElement, interfaceEndElement;

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
                true, // abstract class start-element
                true, // abstract class end-element
                true, // interface start-element
                true, // interface end-element
                true, // model end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent,
                classEvent, classEvent,
                abstractClassEvent, abstractClassEvent,
                interfaceEvent, interfaceEvent,
                modelEvent);
    }

    @Before
    public void initModelStartElement() {
        when(modelEvent.isStartElement()).thenReturn(true, false);
        when(modelEvent.asStartElement()).thenReturn(modelStartElement);
        when(modelStartElement.getName()).thenReturn(QName.valueOf(MODEL));
    }

    @Before
    public void initModelEndElement() {
        when(modelEvent.isEndElement()).thenReturn(false, true);
        when(modelEvent.asEndElement()).thenReturn(modelEndElement);
        when(modelEndElement.getName()).thenReturn(QName.valueOf(MODEL));
    }

    @Before
    public void initClassStartElement() {
        when(classEvent.isStartElement()).thenReturn(true, false, true, false);
        when(classEvent.asStartElement()).thenReturn(classStartElement);
        when(classStartElement.getName()).thenReturn(QName.valueOf(CLASS));

        final Iterator attributesIterator = mock(Iterator.class);
        final Attribute idAttribute = ParseTestHelper.makeAttributeMock(ID, "classNodeId");
        when(attributesIterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(attributesIterator.next()).thenReturn(idAttribute);
        when(classStartElement.getAttributes()).thenReturn(attributesIterator);
    }

    @Before
    public void initClassEndElement() {
        when(classEvent.isEndElement()).thenReturn(false, true, false, true);
        when(classEvent.asEndElement()).thenReturn(classEndElement);
        when(classEndElement.getName()).thenReturn(QName.valueOf(CLASS));
    }

    @Before
    public void initAbstractClassStartElement() {
        when(abstractClassEvent.isStartElement()).thenReturn(true, false, true, false);
        when(abstractClassEvent.asStartElement()).thenReturn(abstractClassStartElement);
        when(abstractClassStartElement.getName()).thenReturn(QName.valueOf(CLASS));

        final Iterator attributesIterator = mock(Iterator.class);
        final Attribute idAttribute = ParseTestHelper.makeAttributeMock(ID, "abstractClassNodeId");
        final Attribute abstractionAttribute = ParseTestHelper.makeAttributeMock("isAbstract", "true");
        when(attributesIterator.hasNext()).thenReturn(true, true, false, true, true, false, true, true, false);
        when(attributesIterator.next()).thenReturn(idAttribute, abstractionAttribute);
        when(abstractClassStartElement.getAttributes()).thenReturn(attributesIterator);
    }

    @Before
    public void initAbstractClassEndElement() {
        when(abstractClassEvent.isEndElement()).thenReturn(false, true, false, true);
        when(abstractClassEvent.asEndElement()).thenReturn(abstractClassEndElement);
        when(abstractClassEndElement.getName()).thenReturn(QName.valueOf(CLASS));
    }

    @Before
    public void initInterfaceStartElement() {
        when(interfaceEvent.isStartElement()).thenReturn(true, false, true, false);
        when(interfaceEvent.asStartElement()).thenReturn(interfaceStartElement);
        when(interfaceStartElement.getName()).thenReturn(QName.valueOf(INTERFACE));

        final Iterator attributesIterator = mock(Iterator.class);
        final Attribute idAttribute = ParseTestHelper.makeAttributeMock(ID, "interfaceNodeId");
        when(attributesIterator.hasNext()).thenReturn(true, false, true, false, true, false);
        when(attributesIterator.next()).thenReturn(idAttribute);
        when(interfaceStartElement.getAttributes()).thenReturn(attributesIterator);
    }

    @Before
    public void initInterfaceEndElement() {
        when(interfaceEvent.isEndElement()).thenReturn(false, true, false, true);
        when(interfaceEvent.asEndElement()).thenReturn(interfaceEndElement);
        when(interfaceEndElement.getName()).thenReturn(QName.valueOf(INTERFACE));
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
    public void testParsedNodes() {
        assertThat(nodeParser.events.size(), is(0));

        final Map<String, Node> nodeMap = nodeParser.parse(xmiFile);
        assertThat(nodeMap.keySet().size(), is(3));

        assertTrue(nodeMap.containsKey("classNodeId"));
        final Node classNode = nodeMap.get("classNodeId");
        assertThat(classNode.getId(), is("classNodeId"));
        assertTrue(classNode.getTypes().contains(NodeType.CONCRETE_CLASS));

        assertTrue(nodeMap.containsKey("abstractClassNodeId"));
        final Node abstractClassNode = nodeMap.get("abstractClassNodeId");
        assertThat(abstractClassNode.getId(), is("abstractClassNodeId"));
        assertTrue(abstractClassNode.getTypes().contains(NodeType.ABSTRACT_CLASS));
        assertTrue(abstractClassNode.getTypes().contains(NodeType.ABSTRACT_CLASS_OR_INTERFACE));

        assertTrue(nodeMap.containsKey("interfaceNodeId"));
        final Node interfaceNode = nodeMap.get("interfaceNodeId");
        assertThat(interfaceNode.getId(), is("interfaceNodeId"));
        assertTrue(interfaceNode.getTypes().contains(NodeType.INTERFACE));
        assertTrue(interfaceNode.getTypes().contains(NodeType.ABSTRACT_CLASS_OR_INTERFACE));

        assertThat(nodeParser.events.size(), is(0));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
