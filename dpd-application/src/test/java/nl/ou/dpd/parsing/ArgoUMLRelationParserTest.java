package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.hamcrest.Matchers;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ABSTRACTION;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ASSOCIATION;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ASSOCIATION_END;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.CLASS;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.ID;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.IDREF;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.INTERFACE;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.IS_NAVIGABLE;
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
    XMLEvent
            modelEvent, classEvent, interfaceEvent, abstractionEvent,
            associationEvent, associationClassEndEvent, associationInterfaceEndEvent;

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
                true, // association start-element
                true, // association-end start-element
                true, // interface start-element
                true, // interface end-element
                true, // association-end end-element
                true, // association-end start-element
                true, // class start-element
                true, // class end-element
                true, // association-end end-element
                true, // association end-element
                true, // model end-element
                true, // abstraction start-element
                true, // class start-element
                true, // class end-element
                true, // interface start-element
                true, // interface end-element
                true, // abstraction end-element
                false
        );
        when(xmlEventReader.nextEvent()).thenReturn(
                modelEvent,
                associationEvent,
                associationInterfaceEndEvent, interfaceEvent, interfaceEvent, associationInterfaceEndEvent,
                associationClassEndEvent, classEvent, classEvent, associationClassEndEvent,
                associationEvent,
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
        associationEvent = ParseTestHelper.createXMLEventMock(ASSOCIATION, mockId("associationId"));
        associationInterfaceEndEvent = ParseTestHelper.createXMLEventMock(
                ASSOCIATION_END,
                mockId("associationClassEndId"),
                mockNavigable("true")
        );
        associationClassEndEvent = ParseTestHelper.createXMLEventMock(
                ASSOCIATION_END,
                mockId("associationClassEndId"),
                mockNavigable("true")
        );
    }

    private Attribute mockId(String id) {
        return ParseTestHelper.createAttributeMock(ID, id);
    }

    private Attribute mockIdRef(String idRef) {
        return ParseTestHelper.createAttributeMock(IDREF, idRef);
    }

    private Attribute mockNavigable(String isNavigable) {
        return ParseTestHelper.createAttributeMock(IS_NAVIGABLE, isNavigable);
    }

    private Attribute mockName(String name) {
        return ParseTestHelper.createAttributeMock(NAME, name);
    }

    @Test
    public void testParseRelations() {
        assertThat(relationParser.events.size(), is(0));

        final SystemUnderConsideration system = relationParser.parse(xmiFile, nodes);
        assertThat(system.getId(), is("modelId"));
        assertThat(system.edgeSet().size(), is(2));

        // Check the first relation: an association
        final Relation association = setToMap(system.edgeSet()).get("associationId");
        final RelationProperty associationProperty = association.getRelationProperties().iterator().next();

        assertThat(system.getEdgeSource(association).getId(), is("interfaceId"));
        assertThat(system.getEdgeTarget(association).getId(), is("classId"));
        assertThat(association.getId(), is("associationId"));
        assertThat(association.getRelationProperties().size(), is(1));

        assertThat(associationProperty.getRelationType(), is(RelationType.ASSOCIATES_WITH));
        assertThat(associationProperty.getCardinalityLeft(), is(Cardinality.valueOf("1")));
        assertThat(associationProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));

        // Check the second relation: a reversed association combined with an abstraction
        final Relation associationReversed = setToMap(system.edgeSet()).get("associationId-reversed");
        final Iterator<RelationProperty> relationPropertyIterator = associationReversed.getRelationProperties().iterator();
        final RelationProperty firstProperty = relationPropertyIterator.next();
        final RelationProperty secondProperty = relationPropertyIterator.next();

        assertThat(system.getEdgeTarget(associationReversed).getId(), is("interfaceId"));
        assertThat(system.getEdgeSource(associationReversed).getId(), is("classId"));
        assertThat(associationReversed.getId(), is("associationId-reversed"));
        assertThat(associationReversed.getRelationProperties().size(), is(2));

        assertThat(firstProperty.getCardinalityLeft(), is(Cardinality.valueOf("1")));
        assertThat(firstProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));
        assertThat(firstProperty.getRelationType(), Matchers.anyOf(is(RelationType.ASSOCIATES_WITH), is(RelationType.IMPLEMENTS)));

        assertThat(secondProperty.getCardinalityLeft(), is(Cardinality.valueOf("1")));
        assertThat(secondProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));
        assertThat(secondProperty.getRelationType(), Matchers.anyOf(is(RelationType.ASSOCIATES_WITH), is(RelationType.IMPLEMENTS)));

        if(firstProperty.getRelationType() == RelationType.ASSOCIATES_WITH) {
            assertThat(secondProperty.getRelationType(), is(RelationType.IMPLEMENTS));
        }
        if(secondProperty.getRelationType() == RelationType.ASSOCIATES_WITH) {
            assertThat(firstProperty.getRelationType(), is(RelationType.IMPLEMENTS));
        }

        assertThat(relationParser.events.size(), is(0));
    }

    private Map<String, Relation> setToMap(Set<Relation> relations) {
        final Map<String, Relation> result = new HashMap<>();
        relations.forEach(relation -> result.put(relation.getId(), relation));
        return result;
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
