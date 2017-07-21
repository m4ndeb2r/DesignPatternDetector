package nl.ou.dpd.parsing;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Operation;
import nl.ou.dpd.domain.node.Parameter;
import nl.ou.dpd.domain.relation.Cardinality;
import nl.ou.dpd.domain.relation.Relation;
import nl.ou.dpd.domain.relation.RelationProperty;
import nl.ou.dpd.domain.relation.RelationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the {@link SystemRelationsExtractor} class.
 *
 * @author Martin de Boer
 */
@RunWith(MockitoJUnitRunner.class)
public class SystemRelationsExtractorTest {

    @Mock
    private SystemUnderConsideration system;

    @Mock
    private Node node1, node2;
    private Set<Node> nodeSet;

    private SystemRelationsExtractor systemRelationsExtractor;

    @Before
    public void initSystemRelationExtractor() {
        systemRelationsExtractor = new SystemRelationsExtractor();
    }

    @Before
    public void initNodeSet() {
        nodeSet = new HashSet<>();
        nodeSet.add(node1);
        nodeSet.add(node2);
        when(node1.getId()).thenReturn("node1");
        when(node2.getId()).thenReturn("node2");
        when(node1.getName()).thenReturn("Node1");
        when(node2.getName()).thenReturn("Node2");
        when(system.vertexSet()).thenReturn(nodeSet);

    }

    /**
     * Tests that no relation is added when no attributes or operations exist for any node.
     */
    @Test
    public void testEmptyNodes() {
        SystemUnderConsideration result = systemRelationsExtractor.execute(system);
        verify(system, times(0)).getEdge(any(Node.class), any(Node.class));
        verify(system, times(0)).getEdgeTarget(any(Relation.class));
        verify(system, times(0)).outgoingEdgesOf(any(Node.class));
        verify(system, times(0)).addEdge(any(Node.class), any(Node.class));
        assertThat(result, is(system));
    }

    /**
     * Tests that a relation is added when node1 has an attribute of type node2.
     */
    @Test
    public void testNodeWithAttributes() {
        final Set<Attribute> node1Attributes = new HashSet<>();
        final Attribute node1Attribute = mock(Attribute.class);
        node1Attributes.add(node1Attribute);

        when(node1Attribute.getParentNode()).thenReturn(node1);
        when(node1Attribute.getId()).thenReturn("attr1");
        when(node1Attribute.getName()).thenReturn("Attr1");
        when(node1Attribute.getType()).thenReturn(node2);
        when(node1.getAttributes()).thenReturn(node1Attributes);

        final SystemUnderConsideration result = systemRelationsExtractor.execute(system);

        final ArgumentCaptor<Relation> relationCaptor = ArgumentCaptor.forClass(Relation.class);
        verify(system, times(1)).addEdge(eq(node1), eq(node2), relationCaptor.capture());

        final Relation relation = relationCaptor.getValue();
        assertThat(relation.getId(), is("SystemRelation-node1-attr1"));
        assertThat(relation.getName(), is("Node1-Attr1"));

        assertThat(result, is(system));
    }

    /**
     * Tests that a relation is added when node1 has an operation with return type of node2. The operation overrides
     * an existing relation, and should therefore also get the {@link RelationType#OVERRIDES_METHOD_OF} relation
     * property.
     */
    @Test
    public void testNodeWithOverridingOperationWithReturnValue() {
        final Set<Operation> node1Operations = new HashSet<>();
        final Operation node1Operation = mock(Operation.class);
        node1Operations.add(node1Operation);

        final Set<Parameter> parameterSet = new HashSet<>();

        final Set<Relation> outgoingEdges = new HashSet<>();
        Relation outgoingEdge = mock(Relation.class);
        outgoingEdges.add(outgoingEdge);

        when(node1Operation.getParentNode()).thenReturn(node1);
        when(node1Operation.getId()).thenReturn("operationId");
        when(node1Operation.getName()).thenReturn("operationName");
        when(node1Operation.getParameters()).thenReturn(parameterSet);
        when(node1Operation.getReturnType()).thenReturn(node2);
        when(node1Operation.equalsSignature(node1Operation)).thenReturn(true);
        when(node1.getOperations()).thenReturn(node1Operations);
        when(system.outgoingEdgesOf(node1)).thenReturn(outgoingEdges);
        when(system.getEdgeTarget(outgoingEdge)).thenReturn(node1);

        final SystemUnderConsideration result = systemRelationsExtractor.execute(system);

        // Check the relation (name and id)
        final ArgumentCaptor<Relation> relationCaptor = ArgumentCaptor.forClass(Relation.class);
        verify(system, times(1)).addEdge(eq(node1), eq(node2), relationCaptor.capture());
        final Relation relation = relationCaptor.getValue();
        assertThat(relation.getId(), is("SystemRelation-node1-operationId"));
        assertThat(relation.getName(), is("Node1-operationName"));

        // Check the return value's type and cardinality properties
        final Iterator<RelationProperty> relationProperties = relation.getRelationProperties().iterator();
        final RelationProperty relationProperty = relationProperties.next();
        assertThat(relationProperty.getRelationType(), is(RelationType.HAS_METHOD_RETURNTYPE));
        assertThat(relationProperty.getCardinalityLeft(), is(Cardinality.valueOf("1")));
        assertThat(relationProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));

        // Check if the relation gets the override property
        final ArgumentCaptor<RelationProperty> relationPropertyCaptor = ArgumentCaptor.forClass(RelationProperty.class);
        verify(outgoingEdge, times(1)).addRelationProperty(relationPropertyCaptor.capture());
        final RelationProperty expectedRelationProperty = relationPropertyCaptor.getValue();
        assertThat(expectedRelationProperty.getRelationType(), is(RelationType.OVERRIDES_METHOD_OF));

        assertThat(result, is(system));
    }

    /**
     * Test that a relation is added when node1 has an operation with one parameter of type node2. The operation does
     * not override any existing opveration.
     */
    @Test
    public void testNodeWithOperationWithParameter() {
        final Set<Operation> node1Operations = new HashSet<>();
        final Operation node1Operation = mock(Operation.class);
        node1Operations.add(node1Operation);

        final Set<Parameter> parameterSet = new HashSet<>();
        final Parameter parameter = mock(Parameter.class);
        parameterSet.add(parameter);

        when(node1Operation.getParentNode()).thenReturn(node1);
        when(node1Operation.getId()).thenReturn("operationId");
        when(node1Operation.getName()).thenReturn("operationName");
        when(node1Operation.getParameters()).thenReturn(parameterSet);
        when(parameter.getType()).thenReturn(node2);
        when(node1.getOperations()).thenReturn(node1Operations);

        final SystemUnderConsideration result = systemRelationsExtractor.execute(system);

        final ArgumentCaptor<Relation> relationCaptor = ArgumentCaptor.forClass(Relation.class);
        verify(system, times(1)).addEdge(eq(node1), eq(node2), relationCaptor.capture());

        final Relation relation = relationCaptor.getValue();
        assertThat(relation.getId(), is("SystemRelation-node1-operationId"));
        assertThat(relation.getName(), is("Node1-operationName"));

        final RelationProperty relationProperty = relation.getRelationProperties().iterator().next();
        assertThat(relationProperty.getRelationType(), is(RelationType.HAS_METHOD_PARAMETER_OF_TYPE));
        assertThat(relationProperty.getCardinalityLeft(), is(Cardinality.valueOf("1")));
        assertThat(relationProperty.getCardinalityRight(), is(Cardinality.valueOf("1")));

        assertThat(result, is(system));
    }
}
