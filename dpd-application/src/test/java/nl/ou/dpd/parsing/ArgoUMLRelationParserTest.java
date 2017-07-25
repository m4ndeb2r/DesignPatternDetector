package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ABSTRACTION;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.CLASS;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ID;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.IDREF;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.INTERFACE;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.MODEL;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ArgoUMLRelationParser} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgoUMLRelationParserTest {

    // This file is created just to satisfy the FileInputStream of the parser
    private String xmiFile = getPath("/argoUML/dummy.xmi");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private XMLInputFactory xmlInputFactory;

    @Mock
    private XMLEventReader xmlEventReader;

    @Mock
    XMLEvent modelEvent, classEvent, interfaceEvent, abstractionEvent;

    private Map<String, Node> nodes;

    private ArgoUMLRelationParser relationParser;

    /**
     * Initialises the test subject with a mocked xmlInputStream.
     *
     * @throws XMLStreamException not expected.
     */
    @Before
    public void initRelationParser() throws XMLStreamException {
        relationParser = new ArgoUMLRelationParser(xmlInputFactory);
        when(xmlInputFactory.createXMLEventReader(any(InputStream.class))).thenReturn(xmlEventReader);
    }

    /**
     * Initialises the nodes that go into the parser's constructor.
     */
    @Before
    public void initNodes() {
        final Node interfaceNode = mock(Node.class);
        when(interfaceNode.getId()).thenReturn("interfaceId");
        when(interfaceNode.getName()).thenReturn("interfaceName");

        final Node classNode = mock(Node.class);
        when(classNode.getId()).thenReturn("classId");
        when(classNode.getName()).thenReturn("className");

        nodes = new HashMap<>();
        nodes.put(interfaceNode.getId(), interfaceNode);
        nodes.put(classNode.getId(), classNode);
    }

    /**
     * Mocks the order in which the event reader reads the XML elements.
     *
     * @throws XMLStreamException not expected
     */
    @Before
    public void initEventReader() throws XMLStreamException {
        when(xmlEventReader.hasNext()).thenReturn(
                true, // model start-element
                true, // abstraction start-element
                true, // class start-element
                true, // class end-element
                true, // interface start-element
                true, // interface end-element
                true, // abstraction end-element
                true, // model end-element
                false
        );
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent,
                abstractionEvent,
                classEvent, classEvent,
                interfaceEvent, interfaceEvent,
                abstractionEvent,
                modelEvent
        );
    }

    /**
     * Mocks the events that the event reader reads.
     */
    @Before
    public void initEvents() {
        modelEvent = ParseTestHelper.createXMLEventMock(MODEL, mockId("modelId"));
        abstractionEvent = ParseTestHelper.createXMLEventMock(ABSTRACTION, mockId("abstractionId"), mockName("abstractionName"));
        classEvent = ParseTestHelper.createXMLEventMock(CLASS, mockIdRef("classId"));
        interfaceEvent = ParseTestHelper.createXMLEventMock(INTERFACE, mockIdRef("interfaceId"));
    }

    private Attribute mockId(String id) {
        return ParseTestHelper.createAttributeMock(ID, id);
    }

    private Attribute mockIdRef(String idRef) {
        return ParseTestHelper.createAttributeMock(IDREF, idRef);
    }

    private Attribute mockName(String name) {
        return ParseTestHelper.createAttributeMock(NAME, name);
    }

    @Test
    public void testParseRelations() {
        assertThat(relationParser.events.size(), is(0));

        final SystemUnderConsideration system = relationParser.parse(xmiFile, nodes);
        assertThat(system.edgeSet().size(), is(1));
        assertThat(system.getId(), is("modelId"));

        final Relation abstraction = system.edgeSet().iterator().next();
        assertThat(system.getEdgeTarget(abstraction).getId(), is("interfaceId"));
        assertThat(system.getEdgeSource(abstraction).getId(), is("classId"));
        assertThat(abstraction.getId(), is("abstractionId"));
        assertThat(abstraction.getName(), is("abstractionName"));
        assertThat(abstraction.getRelationProperties().size(), is(1));

        final RelationProperty abstractionProperty = abstraction.getRelationProperties().iterator().next();
        assertThat(abstractionProperty.getRelationType(), is(RelationType.IMPLEMENTS));
        assertThat(abstractionProperty.getCardinalityLeft(), is(Cardinality.valueOf("1")));
        assertThat(abstractionProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));

        assertThat(relationParser.events.size(), is(0));
    }

    @Test
    public void testAnyException() {
        when(modelEvent.asEndElement()).thenThrow(new IllegalArgumentException());
        thrown.expect(ParseException.class);
        thrown.expectCause(is(IllegalArgumentException.class));
        thrown.expectMessage(String.format("The XMI file '%s' could not be parsed.", xmiFile));
        relationParser.parse(xmiFile, nodes);
    }

    @Test
    public void testParseException() {
        when(modelEvent.asEndElement()).thenThrow(new ParseException("Darn!", null));
        thrown.expect(ParseException.class);
        thrown.expectMessage("Darn!");
        relationParser.parse(xmiFile, nodes);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
