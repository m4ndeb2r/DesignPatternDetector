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
import static org.mockito.Mockito.mock;
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
            nodeEvent1, nodeEvent2, relationsEvent, relationEvent,
            interfaceRuleEvent, concreteClassRuleEvent, inheritanceRuleEvent;

    @Mock
    private StartElement
            patternsStartElement, patternStartElement, notesStartElement,
            noteStartElement, nodesStartElement, nodeStartElement1,
            nodeStartElement2, relationsStartElement, relationStartElement,
            interfaceRuleStartElement, concreteClassRuleStartElement, inheritanceRuleStartElement;

    @Mock
    private Iterator
            patternAttributeIterator, nodeAttributeIterator1,
            nodeAttributeIterator2, relationAttributeIterator,
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
                nodeEvent1,
                concreteClassRuleEvent, concreteClassRuleEvent,
                nodeEvent1,
                nodeEvent2,
                interfaceRuleEvent, interfaceRuleEvent,
                nodeEvent2,
                nodesEvent,
                relationsEvent,
                relationEvent,
                inheritanceRuleEvent, inheritanceRuleEvent,
                relationEvent,
                relationsEvent,
                patternEvent, patternsEvent);
    }

    @Before
    public void initPatternsStartElement() throws XMLStreamException {
        when(patternsEvent.isStartElement()).thenReturn(true, false);
        when(patternsEvent.asStartElement()).thenReturn(patternsStartElement);
        when(patternsStartElement.getName()).thenReturn(QName.valueOf("patterns"));
    }

    @Before
    public void initPatternStartElement() throws XMLStreamException {
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

    @Before
    public void initNoteStartElement() throws XMLStreamException {
        when(notesEvent.isStartElement()).thenReturn(true, false);
        when(notesEvent.asStartElement()).thenReturn(notesStartElement);
        when(notesStartElement.getName()).thenReturn(QName.valueOf("notes"));

        when(noteEvent.isStartElement()).thenReturn(true, false);
        when(noteEvent.asStartElement()).thenReturn(noteStartElement);
        when(noteStartElement.getName()).thenReturn(QName.valueOf("note"));

        when(xmlEventReader.getElementText()).thenReturn("A nice note");
    }

    @Before
    public void initNodesStartElement() {
        // Set up the nodes start element
        when(nodesEvent.isStartElement()).thenReturn(true, false);
        when(nodesEvent.asStartElement()).thenReturn(nodesStartElement);
        when(nodesStartElement.getName()).thenReturn(QName.valueOf("nodes"));

    }

    @Before
    public void initNodeStartElements() {
        // Set up the first node start element (a concrete class)
        final Attribute concreteClassIdAttr = makeAttributeMock("id", "aConcreteClass");
        when(nodeEvent1.isStartElement()).thenReturn(true, false);
        when(nodeEvent1.asStartElement()).thenReturn(nodeStartElement1);
        when(nodeStartElement1.getName()).thenReturn(QName.valueOf("node"));
        when(nodeStartElement1.getAttributes()).thenReturn(nodeAttributeIterator1);
        when(nodeAttributeIterator1.hasNext()).thenReturn(true, false, true, false);
        when(nodeAttributeIterator1.next()).thenReturn(concreteClassIdAttr, concreteClassIdAttr);

        // Node rule for a concrete class
        final Attribute nodeTypeAttributeConcreteClass = makeAttributeMock("nodeType", "CONCRETE_CLASS");
        when(concreteClassRuleEvent.isStartElement()).thenReturn(true, false);
        when(concreteClassRuleEvent.asStartElement()).thenReturn(concreteClassRuleStartElement);
        when(concreteClassRuleStartElement.getName()).thenReturn(QName.valueOf("node.rule"));
        when(concreteClassRuleStartElement.getAttributes()).thenReturn(concreteClassRuleAttributeIterator);
        when(concreteClassRuleAttributeIterator.hasNext()).thenReturn(true, false);
        when(concreteClassRuleAttributeIterator.next()).thenReturn(nodeTypeAttributeConcreteClass);

        // Set up the second node start element (an interface)
        final Attribute interfaceIdAttr = makeAttributeMock("id", "anInterface");
        when(nodeEvent2.isStartElement()).thenReturn(true, false);
        when(nodeEvent2.asStartElement()).thenReturn(nodeStartElement2);
        when(nodeStartElement2.getName()).thenReturn(QName.valueOf("node"));
        when(nodeStartElement2.getAttributes()).thenReturn(nodeAttributeIterator2);
        when(nodeAttributeIterator2.hasNext()).thenReturn(true, false, true, false);
        when(nodeAttributeIterator2.next()).thenReturn(interfaceIdAttr, interfaceIdAttr);

        // Node rule for an interface
        final Attribute nodeTypeAttributeInterface = makeAttributeMock("nodeType", "INTERFACE");
        when(interfaceRuleEvent.isStartElement()).thenReturn(true, false);
        when(interfaceRuleEvent.asStartElement()).thenReturn(interfaceRuleStartElement);
        when(interfaceRuleStartElement.getName()).thenReturn(QName.valueOf("node.rule"));
        when(interfaceRuleStartElement.getAttributes()).thenReturn(interfaceRuleAttributeIterator);
        when(interfaceRuleAttributeIterator.hasNext()).thenReturn(true, false);
        when(interfaceRuleAttributeIterator.next()).thenReturn(nodeTypeAttributeInterface);
    }

    @Before
    public void initRelationsStartElement() {
        // Set up the relations start element
        when(relationsEvent.isStartElement()).thenReturn(true, false);
        when(relationsEvent.asStartElement()).thenReturn(relationsStartElement);
        when(relationsStartElement.getName()).thenReturn(QName.valueOf("relations"));

    }

    @Before
    public void initRelationStartElement() {
        // Set up the relation start element (an inheritance relation)
        final Attribute nodeAttribute1 = makeAttributeMock("node1", "aConcreteClass");
        final Attribute nodeAttribute2 = makeAttributeMock("node2", "anInterface");
        when(relationEvent.isStartElement()).thenReturn(true, false);
        when(relationEvent.asStartElement()).thenReturn(relationStartElement);
        when(relationStartElement.getName()).thenReturn(QName.valueOf("relation"));
        when(relationStartElement.getAttributes()).thenReturn(relationAttributeIterator);
        when(relationAttributeIterator.hasNext()).thenReturn(true, true, false, true, true, false);
        when(relationAttributeIterator.next()).thenReturn(nodeAttribute1, nodeAttribute2, nodeAttribute1, nodeAttribute2);

        // Relation rule for an inheritance relation
        final Attribute relationtypeAttributeInhertance = makeAttributeMock("relationType", "IMPLEMENTS");
        final Attribute cardinalityLeftAttributeInhertance = makeAttributeMock("cardinalityLeft", "1..*");
        // When cardinalityRight is provided, the parser will assume cardinality 1.
        when(inheritanceRuleEvent.isStartElement()).thenReturn(true, false);
        when(inheritanceRuleEvent.asStartElement()).thenReturn(inheritanceRuleStartElement);
        when(inheritanceRuleStartElement.getName()).thenReturn(QName.valueOf("relation.rule"));
        when(inheritanceRuleStartElement.getAttributes()).thenReturn(inheritanceRuleAttributeIterator);
        when(inheritanceRuleAttributeIterator.hasNext()).thenReturn(true, true, false);
        when(inheritanceRuleAttributeIterator.next()).thenReturn(
                relationtypeAttributeInhertance,
                cardinalityLeftAttributeInhertance);
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
    public void testHandleRelationEvent() throws XMLStreamException {
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
        assertThat(concreteClassNode.getId(), is("aConcreteClass"));
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

    private Attribute makeAttributeMock(String name, String value) {
        final Attribute mock = mock(Attribute.class);
        when(mock.getName()).thenReturn(QName.valueOf(name));
        when(mock.getValue()).thenReturn(value);
        return mock;
    }

}
