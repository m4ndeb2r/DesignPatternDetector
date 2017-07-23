package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.NodeType;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
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
import static org.mockito.Mockito.when;

/**
 * Tests the {@link PatternsParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class PatternsParserTest {

    // This file is created just to satisfy the FileInputStream of the parser
    private String xmlFile = getPath("/patterns/dummy.xml");

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
            concreteClassNodeEvent, interfaceNodeEvent, relationsEvent, relationEvent,
            interfaceRuleEvent, concreteClassRuleEvent, inheritanceRuleEvent;

    @Mock
    private StartElement
            nodesStartElement, concreteClassNodeStartElement,
            interfaceNodeStartElement, relationsStartElement, relationStartElement,
            interfaceRuleStartElement, concreteClassRuleStartElement, inheritanceRuleStartElement;

    @Mock
    private Iterator
            concreteClassAttributeIterator,
            interfaceAttributeIterator, relationAttributeIterator,
            interfaceRuleAttributeIterator, concreteClassRuleAttributeIterator,
            inheritanceRuleAttributeIterator;

    @Mock
    private Schema schema;

    @Mock
    private Validator validator;

    // The test subject
    private PatternsParser patternsParser;

    @Before
    public void initParser() throws XMLStreamException, SAXException {
        patternsParser = new PatternsParser(xsdSchemaFactory, xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
        when(xsdSchemaFactory.newSchema(any(URL.class))).thenReturn(schema);
        when(schema.newValidator()).thenReturn(validator);
    }

    @Before
    public void initEventReader() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // patterns start-element
                true, // pattern start-element
                true, // notes start-element
                true, // note start-element
                true, // note end-element
                true, // notes end-element
                true, // nodes start-element
                true, // node1 start-element
                true, // node1.rule start-element
                true, // node1.rule end-element
                true, // node1 end-element
                true, // node2 start-element
                true, // node2.rule start-element
                true, // node2.rule end-element
                true, // node2 end-element
                true, // nodes end-element
                true, // relations start-element
                true, // relation start-element
                true, // relation.rule start-element
                true, // relation.rule end-element
                true, // relation end-element
                true, // relations end-element
                true, // pattern end-element
                true, // patterns end-element
                false);
        when(xmlEventReader.nextEvent()).thenReturn(
                patternsEvent, patternEvent,
                notesEvent,
                noteEvent, noteEvent,
                notesEvent,
                nodesEvent,
                concreteClassNodeEvent,
                concreteClassRuleEvent, concreteClassRuleEvent,
                concreteClassNodeEvent,
                interfaceNodeEvent,
                interfaceRuleEvent, interfaceRuleEvent,
                interfaceNodeEvent,
                nodesEvent,
                relationsEvent,
                relationEvent,
                inheritanceRuleEvent, inheritanceRuleEvent,
                relationEvent,
                relationsEvent,
                patternEvent, patternsEvent);
    }

    @Before
    public void initPatternEvent() throws XMLStreamException {
        patternsEvent = ParseTestHelper.createXMLEventMock("patterns");
        patternEvent = ParseTestHelper.createXMLEventMock(
                "pattern",
                ParseTestHelper.createAttributeMock("name", "patternName"),
                ParseTestHelper.createAttributeMock("family", "patternFamily")
        );
    }

    @Before
    public void initNoteEvent() throws XMLStreamException {
        notesEvent = ParseTestHelper.createXMLEventMock("notes");
        noteEvent = ParseTestHelper.createXMLEventMock("note");
        when(xmlEventReader.getElementText()).thenReturn("A nice note");
    }

    @Before
    public void initNodeEvents() {
        nodesEvent = ParseTestHelper.createXMLEventMock("nodes");

        // A concrete class node + rule
        concreteClassNodeEvent = ParseTestHelper.createXMLEventMock(
                "node",
                ParseTestHelper.createAttributeMock("name", "aConcreteClassName"),
                ParseTestHelper.createAttributeMock("id", "aConcreteClassId"));
        concreteClassRuleEvent = ParseTestHelper.createXMLEventMock(
                "node.rule",
                ParseTestHelper.createAttributeMock("nodeType", "CONCRETE_CLASS")
        );

        // An interface node + rule
        interfaceNodeEvent = ParseTestHelper.createXMLEventMock(
                "node",
                ParseTestHelper.createAttributeMock("id", "anInterface")
        );
        interfaceRuleEvent = ParseTestHelper.createXMLEventMock(
                "node.rule",
                ParseTestHelper.createAttributeMock("nodeType", "INTERFACE")
        );
    }

    @Before
    public void initRelationEvents() {
        relationsEvent = ParseTestHelper.createXMLEventMock("relations");

        // A relation + rule
        relationEvent = ParseTestHelper.createXMLEventMock(
                "relation",
                ParseTestHelper.createAttributeMock("node1", "aConcreteClassId"),
                ParseTestHelper.createAttributeMock("node2", "anInterface")
        );
        inheritanceRuleEvent = ParseTestHelper.createXMLEventMock(
                "relation.rule",
                ParseTestHelper.createAttributeMock("relationType", "IMPLEMENTS"),
                ParseTestHelper.createAttributeMock("cardinalityLeft", "1..*")
        );
    }

    /**
     * Tests if the XSD vaslidation handling of the parser.
     *
     * @throws IOException  not expected
     * @throws SAXException will be caught and converted to a parse exception
     */
    @Test
    public void testXSDValidationFailing() throws IOException, SAXException {
        // Set up the validator to throw a SAXException when the validate methode is called
        doThrow(new SAXException()).when(validator).validate(any(Source.class));

        // We expect the parser to throw a ParseException, caused by a SAXException
        thrown.expect(ParseException.class);
        thrown.expectCause(is(SAXException.class));
        thrown.expectMessage(String.format("The pattern template file '%s' could not be parsed.", xmlFile));

        patternsParser.parse(xmlFile);
    }

    @Test
    public void testAnyException() {
        // Simulate an arbitrary exception somewhere along the way
        when(concreteClassNodeEvent.asStartElement()).thenThrow(new NullPointerException());

        // We expect the arbitrary exception to be mapped to a ParseException
        thrown.expect(ParseException.class);
        thrown.expectCause(is(NullPointerException.class));
        thrown.expectMessage(String.format("The pattern template file '%s' could not be parsed.", xmlFile));

        patternsParser.parse(xmlFile);
    }

    @Test
    public void testParseException() {
        // Simulate an arbitrary exception somewhere along the way
        when(concreteClassNodeEvent.asStartElement()).thenThrow(new ParseException("Oops", null));

        // We expect the ParseException to be rethrown directly
        thrown.expect(ParseException.class);
        thrown.expectMessage("Oops");

        patternsParser.parse(xmlFile);
    }

    @Test
    public void testParseSuccess() throws XMLStreamException {
        final List<DesignPattern> designPatterns = patternsParser.parse(xmlFile);

        final DesignPattern designPattern = designPatterns.get(0);
        assertThat(designPattern.getName(), is("patternName"));
        assertThat(designPattern.getFamily(), is("patternFamily"));

        final Set<String> notes = designPattern.getNotes();
        assertThat(designPatterns.size(), is(1));
        assertThat(notes.size(), is(1));
        assertThat(notes.iterator().next(), is("A nice note"));

        final Set<Node> nodeSet = designPattern.vertexSet();
        final Iterator<Node> nodeIterator = nodeSet.iterator();
        assertThat(nodeSet.size(), is(2));

        final Node concreteClassNode = nodeIterator.next();
        assertThat(concreteClassNode.getId(), is("aConcreteClassId"));
        assertThat(concreteClassNode.getName(), is("aConcreteClassName"));
        assertThat(concreteClassNode.getTypes().size(), is(1));
        assertThat(concreteClassNode.getTypes().iterator().next(), is(NodeType.CONCRETE_CLASS));

        final Node interfaceNode = nodeIterator.next();
        assertThat(interfaceNode.getId(), is("anInterface"));
        assertThat(interfaceNode.getTypes().size(), is(1));
        assertThat(interfaceNode.getTypes().iterator().next(), is(NodeType.INTERFACE));

        final Set<Relation> relationSet = designPattern.edgeSet();
        final Iterator<Relation> relationIterator = relationSet.iterator();
        assertThat(relationSet.size(), is(1));

        final Relation relation = relationIterator.next();
        assertThat(relation.getId(), is("aConcreteClassName-anInterface"));
        assertThat(relation.getName(), is("aConcreteClassName-anInterface"));

        final Set<RelationProperty> relationProperties = relation.getRelationProperties();
        assertThat(relationProperties.size(), is(1));

        final Iterator<RelationProperty> propertyIterator = relationProperties.iterator();
        final RelationProperty relationProperty = propertyIterator.next();
        assertThat(relationProperty.getRelationType(), is(RelationType.IMPLEMENTS));
        assertThat(relationProperty.getCardinalityLeft(), is(Cardinality.valueOf("1..*")));
        assertThat(relationProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
