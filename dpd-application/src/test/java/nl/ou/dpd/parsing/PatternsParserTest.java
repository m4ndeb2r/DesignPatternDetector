package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.DesignPattern;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
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

import static nl.ou.dpd.domain.node.NodeType.CONCRETE_CLASS;
import static nl.ou.dpd.domain.node.NodeType.INTERFACE;
import static nl.ou.dpd.domain.relation.RelationType.IMPLEMENTS;
import static nl.ou.dpd.parsing.PatternsParser.PATTERN_TEMPLATE_FILE_COULD_NOT_BE_PARSED_MSG;
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

    // XML tags
    private static final String NOTE_TAG = "note";
    private static final String NODE_TAG = "node";
    private static final String NOTES_TAG = "notes";
    private static final String NODES_TAG = "nodes";
    private static final String PATTERN_TAG = "pattern";
    private static final String PATTERNS_TAG = "patterns";
    private static final String RELATION_TAG = "relation";
    private static final String RELATIONS_TAG = "relations";
    private static final String NODE_RULE_TAG = "node.rule";
    private static final String RELATION_RULE_TAG = "relation.rule";

    // XML attributes
    private static final String ID_ATTRIBUTE = "id";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String NODE_1_ATTRIBUTE = "node1";
    private static final String NODE_2_ATTRIBUTE = "node2";
    private static final String FAMILY_ATTRIBUTE = "family";
    private static final String NODE_TYPE_ATTRIBUTE = "nodeType";
    private static final String RELATION_TYPE_ATTRIBUTE = "relationType";
    private static final String CARDINALITY_LEFT_ATTRIBUTE = "cardinalityLeft";

    // Test values
    private static final String A_NOTE_VALUE = "A nice note";
    private static final String A_PATTERN_NAME = "aPatternName";
    private static final String AN_INTERFACE_ID = "anInterfaceId";
    private static final String A_CONCRETE_CLASS_ID = "aConcreteClassId";
    private static final String A_CONCRETE_CLASS_NAME = "aConcreteClassName";
    private static final String A_PATTERN_FAMILY_NAME = "aPatternFamily";

    private static final Cardinality CARDINALITY_1_UNLIMITED = Cardinality.valueOf("1..*");
    private static final Cardinality DEFAULT_CARDINALITY = Cardinality.valueOf("1");

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
        patternsEvent = ParseTestHelper.createXMLEventMock(PATTERNS_TAG);
        patternEvent = ParseTestHelper.createXMLEventMock(
                PATTERN_TAG,
                ParseTestHelper.createAttributeMock(NAME_ATTRIBUTE, A_PATTERN_NAME),
                ParseTestHelper.createAttributeMock(FAMILY_ATTRIBUTE, A_PATTERN_FAMILY_NAME)
        );
    }

    @Before
    public void initNoteEvent() throws XMLStreamException {
        notesEvent = ParseTestHelper.createXMLEventMock(NOTES_TAG);
        noteEvent = ParseTestHelper.createXMLEventMock(NOTE_TAG);
        when(xmlEventReader.getElementText()).thenReturn(A_NOTE_VALUE);
    }

    @Before
    public void initNodeEvents() {
        nodesEvent = ParseTestHelper.createXMLEventMock(NODES_TAG);

        // A concrete class node + rule
        concreteClassNodeEvent = ParseTestHelper.createXMLEventMock(
                NODE_TAG,
                ParseTestHelper.createAttributeMock(NAME_ATTRIBUTE, A_CONCRETE_CLASS_NAME),
                ParseTestHelper.createAttributeMock(ID_ATTRIBUTE, A_CONCRETE_CLASS_ID));
        concreteClassRuleEvent = ParseTestHelper.createXMLEventMock(
                NODE_RULE_TAG,
                ParseTestHelper.createAttributeMock(NODE_TYPE_ATTRIBUTE, CONCRETE_CLASS.name())
        );

        // An interface node + rule
        interfaceNodeEvent = ParseTestHelper.createXMLEventMock(
                NODE_TAG,
                ParseTestHelper.createAttributeMock(ID_ATTRIBUTE, AN_INTERFACE_ID)
        );
        interfaceRuleEvent = ParseTestHelper.createXMLEventMock(
                NODE_RULE_TAG,
                ParseTestHelper.createAttributeMock(NODE_TYPE_ATTRIBUTE, INTERFACE.name())
        );
    }

    @Before
    public void initRelationEvents() {
        relationsEvent = ParseTestHelper.createXMLEventMock(RELATIONS_TAG);

        // A relation + rule
        relationEvent = ParseTestHelper.createXMLEventMock(
                RELATION_TAG,
                ParseTestHelper.createAttributeMock(NODE_1_ATTRIBUTE, A_CONCRETE_CLASS_ID),
                ParseTestHelper.createAttributeMock(NODE_2_ATTRIBUTE, AN_INTERFACE_ID)
        );
        inheritanceRuleEvent = ParseTestHelper.createXMLEventMock(
                RELATION_RULE_TAG,
                ParseTestHelper.createAttributeMock(RELATION_TYPE_ATTRIBUTE, IMPLEMENTS.name()),
                ParseTestHelper.createAttributeMock(CARDINALITY_LEFT_ATTRIBUTE, CARDINALITY_1_UNLIMITED.toString())
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
        thrown.expectMessage(String.format(PATTERN_TEMPLATE_FILE_COULD_NOT_BE_PARSED_MSG, xmlFile));

        patternsParser.parse(xmlFile);
    }

    @Test
    public void testAnyException() {
        // Simulate an arbitrary exception somewhere along the way
        when(concreteClassNodeEvent.asStartElement()).thenThrow(new NullPointerException());

        // We expect the arbitrary exception to be mapped to a ParseException
        thrown.expect(ParseException.class);
        thrown.expectCause(is(NullPointerException.class));
        thrown.expectMessage(String.format(PATTERN_TEMPLATE_FILE_COULD_NOT_BE_PARSED_MSG, xmlFile));

        patternsParser.parse(xmlFile);
    }

    @Test
    public void testParseException() {
        final String errorMsg = "Oops";

        // Simulate an arbitrary exception somewhere along the way
        when(concreteClassNodeEvent.asStartElement()).thenThrow(new ParseException(errorMsg, null));

        // We expect the ParseException to be rethrown directly
        thrown.expect(ParseException.class);
        thrown.expectMessage(errorMsg);

        patternsParser.parse(xmlFile);
    }

    @Test
    public void testParseSuccess() throws XMLStreamException {
        final List<DesignPattern> designPatterns = patternsParser.parse(xmlFile);

        final DesignPattern designPattern = designPatterns.get(0);
        assertThat(designPattern.getName(), is(A_PATTERN_NAME));
        assertThat(designPattern.getFamily(), is(A_PATTERN_FAMILY_NAME));

        final Set<String> notes = designPattern.getNotes();
        assertThat(designPatterns.size(), is(1));
        assertThat(notes.size(), is(1));
        assertThat(notes.iterator().next(), is(A_NOTE_VALUE));

        final Set<Node> nodeSet = designPattern.vertexSet();
        final Iterator<Node> nodeIterator = nodeSet.iterator();
        assertThat(nodeSet.size(), is(2));

        final Node concreteClassNode = nodeIterator.next();
        assertThat(concreteClassNode.getId(), is(A_CONCRETE_CLASS_ID));
        assertThat(concreteClassNode.getName(), is(A_CONCRETE_CLASS_NAME));
        assertThat(concreteClassNode.getTypes().size(), is(1));
        assertThat(concreteClassNode.getTypes().iterator().next(), is(CONCRETE_CLASS));

        final Node interfaceNode = nodeIterator.next();
        assertThat(interfaceNode.getId(), is(AN_INTERFACE_ID));
        assertThat(interfaceNode.getTypes().size(), is(1));
        assertThat(interfaceNode.getTypes().iterator().next(), is(INTERFACE));

        final Set<Relation> relationSet = designPattern.edgeSet();
        final Iterator<Relation> relationIterator = relationSet.iterator();
        assertThat(relationSet.size(), is(1));

        final Relation relation = relationIterator.next();
        assertThat(relation.getId(), is(String.format("%s-%s", A_CONCRETE_CLASS_NAME, AN_INTERFACE_ID)));
        assertThat(relation.getName(), is(String.format("%s-%s", A_CONCRETE_CLASS_NAME, AN_INTERFACE_ID)));

        final Set<RelationProperty> relationProperties = relation.getRelationProperties();
        assertThat(relationProperties.size(), is(1));

        final Iterator<RelationProperty> propertyIterator = relationProperties.iterator();
        final RelationProperty relationProperty = propertyIterator.next();
        assertThat(relationProperty.getRelationType(), is(IMPLEMENTS));
        assertThat(relationProperty.getCardinalityLeft(), is(CARDINALITY_1_UNLIMITED));
        assertThat(relationProperty.getCardinalityRight(), is(DEFAULT_CARDINALITY));
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
