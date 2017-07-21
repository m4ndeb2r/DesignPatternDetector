package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.parsing.ParseException;
import nl.ou.dpd.parsing.PatternsParser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link PatternsParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class PatternsParserTest {

    // This filename is just to satisfy the FileInputStream of the parser
    private static final String DUMMY_XML = "/patterns/dummy.xml";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private XMLInputFactory xmlInputFactory;

    @Mock
    private SchemaFactory xsdSchemaFactory;

    @Mock
    private XMLEventReader xmlEventReader;

    @Mock
    private XMLEvent
            patternsEvent, patternEvent, notesEvent, noteEvent, nodesEvent,
            nodeEvent1, nodeEvent2, relationsEvent, relationEvent;

    @Mock
    private StartElement
            patternsStartElement, patternStartElement, notesStartElement,
            noteStartElement, nodesStartElement, nodeStartElement1,
            nodeStartElement2, relationsStartElement, relationStartElement;

    @Mock
    private Iterator
            patternAttributeIterator, nodeAttributeIterator1,
            nodeAttributeIterator2, relationAttributeIterator;

    @Mock
    private Schema schema;

    @Mock
    private Validator validator;

    // The test subject
    private PatternsParser patternsParser;

    /**
     * Initialises the {@link PatternsParser}, the test subject.
     *
     * @throws XMLStreamException not expected
     * @throws SAXException       not expected
     */
    @Before
    public void initParser() throws XMLStreamException, SAXException {
        patternsParser = new PatternsParser(xsdSchemaFactory, xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
        when(xsdSchemaFactory.newSchema(any(URL.class))).thenReturn(schema);
        when(schema.newValidator()).thenReturn(validator);
    }

    /**
     * Sets the mock data for the pattern start element of the patterns XML file.
     *
     * @throws XMLStreamException not expected
     */
    @Before
    public void initPatternStartElement() throws XMLStreamException {
        // Set up the patterns start element
        when(patternsEvent.isStartElement()).thenReturn(true, false);
        when(patternsEvent.asStartElement()).thenReturn(patternsStartElement);
        when(patternsStartElement.getName()).thenReturn(QName.valueOf("patterns"));

        // Set up the attributes of the pattern start element
        final Attribute nameAttribute = makeAttributeMock("name", "patternName");
        final Attribute familyAttribute = makeAttributeMock("family", "patternFamily");

        // Set up the pattern start element
        when(patternEvent.isStartElement()).thenReturn(true, false);
        when(patternEvent.asStartElement()).thenReturn(patternStartElement);
        when(patternStartElement.getName()).thenReturn(QName.valueOf("pattern"));
        when(patternStartElement.getAttributes()).thenReturn(patternAttributeIterator);
        when(patternAttributeIterator.hasNext()).thenReturn(true, true, false);
        when(patternAttributeIterator.next()).thenReturn(nameAttribute, familyAttribute);
    }

    /**
     * Sets the mock data for the nodes and node start elements of the patterns XML file. Note that any mock data set
     * here must be consistant with mock data set for the relations and relation start elements below.
     */
    @Before
    public void initNodeStartElements() {
        // Set up the nodes start element
        when(nodesEvent.isStartElement()).thenReturn(true, false);
        when(nodesEvent.asStartElement()).thenReturn(nodesStartElement);
        when(nodesStartElement.getName()).thenReturn(QName.valueOf("nodes"));

        // Set up the first node start element
        final Attribute idAttribute1 = makeAttributeMock("id", "nodeId1");
        when(nodeEvent1.isStartElement()).thenReturn(true, false);
        when(nodeEvent1.asStartElement()).thenReturn(nodeStartElement1);
        when(nodeStartElement1.getName()).thenReturn(QName.valueOf("node"));
        when(nodeStartElement1.getAttributes()).thenReturn(nodeAttributeIterator1);
        when(nodeAttributeIterator1.hasNext()).thenReturn(true, false, true, false);
        when(nodeAttributeIterator1.next()).thenReturn(idAttribute1, idAttribute1);

        // Set up the second node start element
        final Attribute idAttribute2 = makeAttributeMock("id", "nodeId2");
        when(nodeEvent2.isStartElement()).thenReturn(true, false);
        when(nodeEvent2.asStartElement()).thenReturn(nodeStartElement2);
        when(nodeStartElement2.getName()).thenReturn(QName.valueOf("node"));
        when(nodeStartElement2.getAttributes()).thenReturn(nodeAttributeIterator2);
        when(nodeAttributeIterator2.hasNext()).thenReturn(true, false, true, false);
        when(nodeAttributeIterator2.next()).thenReturn(idAttribute2, idAttribute2);
    }

    /**
     * Sets the mock data for the relations and relation start elements of the patterns XML file. Note that any mock
     * data set here must be consistant with mock data set for the nodes and node start elements above.
     */
    @Before
    public void initRelationStartElement() {
        // Set up the relations start element
        when(relationsEvent.isStartElement()).thenReturn(true, false);
        when(relationsEvent.asStartElement()).thenReturn(relationsStartElement);
        when(relationsStartElement.getName()).thenReturn(QName.valueOf("relations"));

        // Set up the relation start element
        final Attribute nodeAttribute1 = makeAttributeMock("node1", "nodeId1");
        final Attribute nodeAttribute2 = makeAttributeMock("node2", "nodeId2");
        when(relationEvent.isStartElement()).thenReturn(true, false);
        when(relationEvent.asStartElement()).thenReturn(relationStartElement);
        when(relationStartElement.getName()).thenReturn(QName.valueOf("relation"));
        when(relationStartElement.getAttributes()).thenReturn(relationAttributeIterator);
        when(relationAttributeIterator.hasNext()).thenReturn(true, true, false, true, true, false);
        when(relationAttributeIterator.next()).thenReturn(nodeAttribute1, nodeAttribute2, nodeAttribute1, nodeAttribute2);
    }

    /**
     * Tests if the XSD vaslidation handling of the parser.
     *
     * @throws IOException  not expected
     * @throws SAXException will be caught and converted to a parse exception
     */
    @Test
    public void testXSDValidationFailing() throws IOException, SAXException {
        final String xmlFile = getPath(DUMMY_XML);

        // Set up the validator to throw a SAXException when the validate methode is called
        doThrow(new SAXException()).when(validator).validate(any(Source.class));

        // We expect the parser to throw a ParseException, caused by a SAXException
        thrown.expect(ParseException.class);
        thrown.expectCause(is(SAXException.class));
        thrown.expectMessage(String.format("The pattern template file '%s' could not be parsed.", xmlFile));

        patternsParser.parse(xmlFile);
    }

    /**
     * Tests if the pattern elements are handled correctly. Note: the mocked situation would not pass an XSD validation,
     * but that is not relevant here. Here, we test if the name and family name of the pattern in the element are
     * stored correctly.
     *
     * @throws XMLStreamException not expected
     */
    @Test
    public void testHandlePatternEvent() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // patterns start-element
                true, // pattern start-element
                true, // pattern end-element
                true, // patterns end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(patternsEvent, patternEvent, patternEvent, patternsEvent);

        final String xmlFile = getPath(DUMMY_XML);
        final List<DesignPattern> designPatterns = patternsParser.parse(xmlFile);

        assertThat(designPatterns.size(), is(1));
        assertThat(designPatterns.get(0).getName(), is("patternName"));
        assertThat(designPatterns.get(0).getFamily(), is("patternFamily"));
    }

    /**
     * Tests if the note elements are handled correctly. Note: the mocked situation would not pass an XSD validation,
     * but that is not relevant here. Here, we test if any notes in the notes element are stored correctly.
     */
    @Test
    public void testHandleNoteEvent() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // patterns start-element
                true, // pattern start-element
                true, // notes start-element
                true, // note start-element
                true, // note end-element
                true, // notes end-element
                true, // pattern end-element
                true, // patterns end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                patternsEvent, patternEvent,
                notesEvent, noteEvent, noteEvent, notesEvent,
                patternEvent, patternsEvent);

        when(notesEvent.isStartElement()).thenReturn(true, false);
        when(notesEvent.asStartElement()).thenReturn(notesStartElement);
        when(notesStartElement.getName()).thenReturn(QName.valueOf("notes"));

        when(noteEvent.isStartElement()).thenReturn(true, false);
        when(noteEvent.asStartElement()).thenReturn(noteStartElement);
        when(noteStartElement.getName()).thenReturn(QName.valueOf("note"));

        when(xmlEventReader.getElementText()).thenReturn("A note");

        final String xmlFile = getPath(DUMMY_XML);
        final List<DesignPattern> designPatterns = patternsParser.parse(xmlFile);

        assertThat(designPatterns.size(), is(1));
        assertThat(designPatterns.get(0).getNotes().size(), is(1));
        assertThat(designPatterns.get(0).getNotes().iterator().next(), is("A note"));
    }

    /**
     * Tests if (nodes and) relations are handled correctly by the parser.
     *
     * @throws XMLStreamException not expected.
     */
    @Test
    public void testHandleRelationEvent() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // patterns start-element
                true, // pattern start-element
                true, // nodes start-element
                true, // node1 start-element
                true, // node1 end-element
                true, // node2 start-element
                true, // node2 end-element
                true, // nodes end-element
                true, // relations start-element
                true, // relation start-element
                true, // relation end-element
                true, // relations end-element
                true, // pattern end-element
                true, // patterns end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                patternsEvent, patternEvent,
                nodesEvent, nodeEvent1, nodeEvent1, nodeEvent2, nodeEvent2, nodesEvent,
                relationsEvent, relationEvent, relationEvent, relationsEvent,
                patternEvent, patternsEvent);

        final String xmlFile = getPath(DUMMY_XML);
        final List<DesignPattern> designPatterns = patternsParser.parse(xmlFile);

        assertThat(designPatterns.size(), is(1));

        final Set<Node> nodeSet = designPatterns.get(0).vertexSet();
        final Iterator<Node> iterator = nodeSet.iterator();

        assertThat(nodeSet.size(), is(2));
        assertThat(iterator.next().getId(), is("nodeId1"));
        assertThat(iterator.next().getId(), is("nodeId2"));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

    private Attribute makeAttributeMock(String name, String value) {
        final Attribute mock = mock(Attribute.class);
        when(mock.getName()).thenReturn(QName.valueOf(name));
        when(mock.getValue()).thenReturn(value);
        return mock;
    }

}
