package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.hamcrest.core.AnyOf;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static nl.ou.dpd.domain.relation.RelationType.ASSOCIATES_WITH;
import static nl.ou.dpd.domain.relation.RelationType.IMPLEMENTS;
import static nl.ou.dpd.domain.relation.RelationType.INHERITS_FROM_OR_IMPLEMENTS;
import static nl.ou.dpd.parsing.ArgoUMLAbstractParser.XMI_FILE_COULD_NOT_BE_PARSED_MSG;
import static nl.ou.dpd.parsing.ArgoUMLRelationParser.REVERSED_POSTFIX;
import static nl.ou.dpd.parsing.ParseTestHelper.createAttributeMock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
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

    // XMI tags
    private static final String MODEL_TAG = "Model";
    private static final String CLASS_TAG = "Class";
    private static final String INTERFACE_TAG = "Interface";
    private static final String ABSTRACTION_TAG = "Abstraction";
    private static final String ASSOCIATION_TAG = "Association";
    private static final String ASSOCIATION_END_TAG = "AssociationEnd";
    private static final String MULTIPLICITY_RANGE_TAG = "MultiplicityRange";

    // XMI attributes
    private static final String ID_ATTRIBUTE = "xmi.id";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String LOWER_ATTRIBUTE = "lower";
    private static final String UPPER_ATTRIBUTE = "upper";
    private static final String IDREF_ATTRIBUTE = "xmi.idref";
    private static final String IS_NAVIGABLE_ATTRIBUTE = "isNavigable";

    // Id's in tests
    private static final String CLASS_ID = "classId";
    private static final String MODEL_ID = "modelId";
    private static final String CLASS_NAME = "className";
    private static final String INTERFACE_ID = "interfaceId";
    private static final String INTERFACE_NAME = "interfaceName";
    private static final String ASSOCIATION_ID = "associationId";
    private static final String ABSTRACTION_ID = "abstractionId";
    private static final String ABSTRACTION_NAME = "abstractionName";
    private static final String MULTIPLICITY_RANGE_ID = "multiplicityRangeId";
    private static final String ASSOCIATION_CLASS_END_ID = "associationClassEndId";
    private static final String ASSOCIATION_INTERFACE_END_ID = "associationInterfaceEndId";

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
            modelEvent, classEvent, interfaceEvent, abstractionEvent, multiplicityRangeClassEvent,
            multiplicityRangeInterfaceEvent, associationEvent, associationClassEndEvent,
            associationInterfaceEndEvent;

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
                true, // multiplicity-range start-element
                true, // multiplicity-range end-element
                true, // interface start-element
                true, // interface end-element
                true, // association-end end-element
                true, // association-end start-element
                true, // multiplicity-range start-element
                true, // multiplicity-range end-element
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
                // Start model
                modelEvent,
                // Een association tussen een interface met een mulitpliciteit van 1..1, en een class met een
                // multipliciteit 0..-1
                associationEvent,
                associationInterfaceEndEvent, multiplicityRangeInterfaceEvent, multiplicityRangeInterfaceEvent, interfaceEvent, interfaceEvent, associationInterfaceEndEvent,
                associationClassEndEvent, multiplicityRangeClassEvent, multiplicityRangeClassEvent, classEvent, classEvent, associationClassEndEvent,
                associationEvent,
                // Een abstraction tussen een class en en interface
                abstractionEvent,
                classEvent, classEvent,
                interfaceEvent, interfaceEvent,
                abstractionEvent,
                // End model
                modelEvent
        );
    }

    /**
     * Mocks the events that the event reader reads.
     */
    @Before
    public void initEvents() {
        modelEvent = ParseTestHelper.createXMLEventMock(MODEL_TAG, mockId(MODEL_ID));
        abstractionEvent = ParseTestHelper.createXMLEventMock(ABSTRACTION_TAG, mockId(ABSTRACTION_ID), mockName(ABSTRACTION_NAME));
        classEvent = ParseTestHelper.createXMLEventMock(CLASS_TAG, mockIdRef(CLASS_ID));
        interfaceEvent = ParseTestHelper.createXMLEventMock(INTERFACE_TAG, mockIdRef(INTERFACE_ID));

        associationEvent = ParseTestHelper.createXMLEventMock(ASSOCIATION_TAG, mockId(ASSOCIATION_ID));
        multiplicityRangeClassEvent = ParseTestHelper.createXMLEventMock(
                MULTIPLICITY_RANGE_TAG,
                mockIdAndCardinality(MULTIPLICITY_RANGE_ID, 0, Cardinality.UNLIMITED));
        multiplicityRangeInterfaceEvent = ParseTestHelper.createXMLEventMock(
                MULTIPLICITY_RANGE_TAG,
                mockIdAndCardinality(MULTIPLICITY_RANGE_ID, 1, 1));
        associationInterfaceEndEvent = ParseTestHelper.createXMLEventMock(
                ASSOCIATION_END_TAG,
                mockId(ASSOCIATION_CLASS_END_ID),
                mockNavigable("true"));
        associationClassEndEvent = ParseTestHelper.createXMLEventMock(
                ASSOCIATION_END_TAG,
                mockId(ASSOCIATION_INTERFACE_END_ID),
                mockNavigable("true"));
    }

    private Iterator<Attribute> mockIdAndCardinality(String id, int lower, int upper) {
        final Set<Attribute> attributes = new HashSet<>();
        attributes.add(mockId(id));
        attributes.add(createAttributeMock(LOWER_ATTRIBUTE, Integer.toString(lower)));
        attributes.add(createAttributeMock(UPPER_ATTRIBUTE, Integer.toString(upper)));
        return attributes.iterator();
    }

    private Attribute mockId(String id) {
        return createAttributeMock(ID_ATTRIBUTE, id);
    }

    private Attribute mockIdRef(String idRef) {
        return createAttributeMock(IDREF_ATTRIBUTE, idRef);
    }

    private Attribute mockNavigable(String isNavigable) {
        return createAttributeMock(IS_NAVIGABLE_ATTRIBUTE, isNavigable);
    }

    private Attribute mockName(String name) {
        return createAttributeMock(NAME_ATTRIBUTE, name);
    }

    @Test
    public void testParseRelations() {
        assertTrue(relationParser.events.isEmpty());

        final SystemUnderConsideration system = relationParser.parse(xmiFile, nodes);
        assertThat(system.getId(), is(MODEL_ID));
        assertThat(system.edgeSet().size(), is(2));

        // Check the first relation: an association from an interface (source) to a class (target) with a multiplicity
        // of 0..-1.
        final Relation association = setToMap(system.edgeSet()).get(ASSOCIATION_ID);
        final Iterator<RelationProperty> associationPropertyIterator = association.getRelationProperties().iterator();
        assertTrue(associationPropertyIterator.hasNext());
        final RelationProperty associationProperty = associationPropertyIterator.next();
        assertFalse(associationPropertyIterator.hasNext());

        assertThat(system.getEdgeSource(association).getId(), is(INTERFACE_ID));
        assertThat(system.getEdgeSource(association).getName(), is(INTERFACE_NAME));
        assertThat(system.getEdgeTarget(association).getId(), is(CLASS_ID));
        assertThat(system.getEdgeTarget(association).getName(), is(CLASS_NAME));
        assertThat(association.getId(), is(ASSOCIATION_ID));

        assertThat(associationProperty.getRelationType(), is(ASSOCIATES_WITH));
        assertThat(associationProperty.getCardinalityLeft(), is(CARDINALITY_1));
        assertThat(associationProperty.getCardinalityRight(), is(Cardinality.valueOf("*")));

        // Check the second relation: a reversed association (reversed version of the first relation) combined with an
        // abstraction.
        final Relation associationReversed = setToMap(system.edgeSet()).get(String.format("%s%s", ASSOCIATION_ID, REVERSED_POSTFIX));
        final Iterator<RelationProperty> relationPropertyIterator = associationReversed.getRelationProperties().iterator();
        assertTrue(relationPropertyIterator.hasNext());
        final RelationProperty firstProperty = relationPropertyIterator.next();
        assertTrue(relationPropertyIterator.hasNext());
        final RelationProperty secondProperty = relationPropertyIterator.next();
        assertTrue(relationPropertyIterator.hasNext());
        final RelationProperty thirdProperty = relationPropertyIterator.next();
        assertFalse(relationPropertyIterator.hasNext());

        assertThat(system.getEdgeTarget(associationReversed).getId(), is(INTERFACE_ID));
        assertThat(system.getEdgeSource(associationReversed).getId(), is(CLASS_ID));
        assertThat(associationReversed.getId(), is(String.format("%s%s", ASSOCIATION_ID, REVERSED_POSTFIX)));

        final RelationType firstRelationType = firstProperty.getRelationType();
        final RelationType secondRelationType = secondProperty.getRelationType();
        final RelationType thirdRelationType = thirdProperty.getRelationType();

        final AnyOf<RelationType> isExpectedRelationType = anyOf(
                is(ASSOCIATES_WITH),
                is(IMPLEMENTS),
                is(INHERITS_FROM_OR_IMPLEMENTS));
        assertThat(firstRelationType, isExpectedRelationType);
        assertThat(secondRelationType, isExpectedRelationType);
        assertThat(thirdRelationType, isExpectedRelationType);

        if ((firstRelationType == ASSOCIATES_WITH && secondRelationType == IMPLEMENTS)
                || (secondRelationType == ASSOCIATES_WITH && firstRelationType == IMPLEMENTS)) {
            assertThat(thirdRelationType, is(INHERITS_FROM_OR_IMPLEMENTS));
            assertThat(thirdProperty.getCardinalityLeft(), is(CARDINALITY_1));
            assertThat(thirdProperty.getCardinalityRight(), is(CARDINALITY_1));
        }

        if ((firstRelationType == INHERITS_FROM_OR_IMPLEMENTS && secondRelationType == IMPLEMENTS)
                || (secondRelationType == INHERITS_FROM_OR_IMPLEMENTS && firstRelationType == IMPLEMENTS)) {
            assertThat(thirdRelationType, is(ASSOCIATES_WITH));
            assertThat(associationProperty.getCardinalityLeft(), is(CARDINALITY_1));
            assertThat(associationProperty.getCardinalityRight(), is(Cardinality.valueOf("*")));
        }

        if ((firstRelationType == INHERITS_FROM_OR_IMPLEMENTS && secondRelationType == ASSOCIATES_WITH)
                || (secondRelationType == INHERITS_FROM_OR_IMPLEMENTS && firstRelationType == ASSOCIATES_WITH)) {
            assertThat(thirdRelationType, is(IMPLEMENTS));
            assertThat(associationProperty.getCardinalityLeft(), is(CARDINALITY_1));
            assertThat(associationProperty.getCardinalityRight(), is(Cardinality.valueOf("*")));
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
