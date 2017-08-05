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
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.XMI_FILE_COULD_NOT_BE_PARSED_MSG;
import static nl.ou.dpd.parsing.ArgoUMLRelationParser.REVERSED_POSTFIX;
import static nl.ou.dpd.parsing.ParseTestHelper.createAttributeMock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
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

    private static final String CLASS_ID = "classId";
    private static final String MODEL_ID = "modelId";
    private static final String CLASS_NAME = "className";
    private static final String INTERFACE_ID = "interfaceId";
    private static final String INTERFACE_NAME = "interfaceName";
    private static final String ASSOCIATION_ID = "associationId";
    private static final String ABSTRACTION_ID = "abstractionId";
    private static final String ABSTRACTION_NAME = "abstractionName";
    private static final String ASSOCIATION_CLASS_END_ID = "associationClassEndId";

    private static final Cardinality CARDINALITY_1 = Cardinality.valueOf("1");

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
        when(interfaceNode.getId()).thenReturn(INTERFACE_ID);
        when(interfaceNode.getName()).thenReturn(INTERFACE_NAME);

        final Node classNode = mock(Node.class);
        when(classNode.getId()).thenReturn(CLASS_ID);
        when(classNode.getName()).thenReturn(CLASS_NAME);

        nodes = new HashMap<>();
        nodes.put(interfaceNode.getId(), interfaceNode);
        nodes.put(classNode.getId(), classNode);
    }

    /**
     * Mocks the order in which the event reader reads the XML elements. Here, we put together a complete, mocked,
     * structure of the system design that is being parsed by the {@link ArgoUMLRelationParser}.
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
        modelEvent = ParseTestHelper.createXMLEventMock(MODEL, mockId(MODEL_ID));
        abstractionEvent = ParseTestHelper.createXMLEventMock(ABSTRACTION, mockId(ABSTRACTION_ID), mockName(ABSTRACTION_NAME));
        classEvent = ParseTestHelper.createXMLEventMock(CLASS, mockIdRef(CLASS_ID));
        interfaceEvent = ParseTestHelper.createXMLEventMock(INTERFACE, mockIdRef(INTERFACE_ID));
        associationEvent = ParseTestHelper.createXMLEventMock(ASSOCIATION, mockId(ASSOCIATION_ID));
        associationInterfaceEndEvent = ParseTestHelper.createXMLEventMock(
                ASSOCIATION_END,
                mockId(ASSOCIATION_CLASS_END_ID),
                mockNavigable("true")
        );
        associationClassEndEvent = ParseTestHelper.createXMLEventMock(
                ASSOCIATION_END,
                mockId(ASSOCIATION_CLASS_END_ID),
                mockNavigable("true")
        );
    }

    private Attribute mockId(String id) {
        return createAttributeMock(ID, id);
    }

    private Attribute mockIdRef(String idRef) {
        return createAttributeMock(IDREF, idRef);
    }

    private Attribute mockNavigable(String isNavigable) {
        return createAttributeMock(IS_NAVIGABLE, isNavigable);
    }

    private Attribute mockName(String name) {
        return createAttributeMock(NAME, name);
    }

    @Test
    public void testParseRelations() {
        assertTrue(relationParser.events.isEmpty());

        final SystemUnderConsideration system = relationParser.parse(xmiFile, nodes);
        assertThat(system.getId(), is(MODEL_ID));
        assertThat(system.edgeSet().size(), is(2));

        // Check the first relation: an association
        final Relation association = setToMap(system.edgeSet()).get(ASSOCIATION_ID);
        final RelationProperty associationProperty = association.getRelationProperties().iterator().next();

        assertThat(system.getEdgeSource(association).getId(), is(INTERFACE_ID));
        assertThat(system.getEdgeSource(association).getName(), is(INTERFACE_NAME));
        assertThat(system.getEdgeTarget(association).getId(), is(CLASS_ID));
        assertThat(system.getEdgeTarget(association).getName(), is(CLASS_NAME));
        assertThat(association.getId(), is(ASSOCIATION_ID));
        assertThat(association.getRelationProperties().size(), is(1));

        assertThat(associationProperty.getRelationType(), is(RelationType.ASSOCIATES_WITH));
        assertThat(associationProperty.getCardinalityLeft(), is(CARDINALITY_1));
        assertThat(associationProperty.getCardinalityRight(), is(CARDINALITY_1));

        // Check the second relation: a reversed association combined with an abstraction
        final Relation associationReversed = setToMap(system.edgeSet()).get(ASSOCIATION_ID + REVERSED_POSTFIX);
        final Iterator<RelationProperty> relationPropertyIterator = associationReversed.getRelationProperties().iterator();
        final RelationProperty firstProperty = relationPropertyIterator.next();
        final RelationProperty secondProperty = relationPropertyIterator.next();

        assertThat(system.getEdgeTarget(associationReversed).getId(), is(INTERFACE_ID));
        assertThat(system.getEdgeTarget(associationReversed).getName(), is(INTERFACE_NAME));
        assertThat(system.getEdgeSource(associationReversed).getId(), is(CLASS_ID));
        assertThat(system.getEdgeSource(associationReversed).getName(), is(CLASS_NAME));
        assertThat(associationReversed.getId(), is(ASSOCIATION_ID + REVERSED_POSTFIX));
        assertThat(associationReversed.getRelationProperties().size(), is(2));

        assertThat(firstProperty.getCardinalityLeft(), is(CARDINALITY_1));
        assertThat(firstProperty.getCardinalityRight(), is(CARDINALITY_1));
        assertThat(firstProperty.getRelationType(), Matchers.anyOf(is(RelationType.ASSOCIATES_WITH), is(RelationType.IMPLEMENTS)));

        assertThat(secondProperty.getCardinalityLeft(), is(CARDINALITY_1));
        assertThat(secondProperty.getCardinalityRight(), is(CARDINALITY_1));
        assertThat(secondProperty.getRelationType(), Matchers.anyOf(is(RelationType.ASSOCIATES_WITH), is(RelationType.IMPLEMENTS)));

        if(firstProperty.getRelationType() == RelationType.ASSOCIATES_WITH) {
            assertThat(secondProperty.getRelationType(), is(RelationType.IMPLEMENTS));
        }
        if(secondProperty.getRelationType() == RelationType.ASSOCIATES_WITH) {
            assertThat(firstProperty.getRelationType(), is(RelationType.IMPLEMENTS));
        }

        assertTrue(relationParser.events.isEmpty());
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
        thrown.expectMessage(String.format(XMI_FILE_COULD_NOT_BE_PARSED_MSG, xmiFile));
        relationParser.parse(xmiFile, nodes);
    }

    @Test
    public void testParseException() {
        final String errorMsg = "Darn!";
        when(modelEvent.asEndElement()).thenThrow(new ParseException(errorMsg, null));
        thrown.expect(ParseException.class);
        thrown.expectMessage(errorMsg);
        relationParser.parse(xmiFile, nodes);
    }

    private String getPath(String resourceName) {
        return this.getClass().getResource(resourceName).getPath();
    }

}
