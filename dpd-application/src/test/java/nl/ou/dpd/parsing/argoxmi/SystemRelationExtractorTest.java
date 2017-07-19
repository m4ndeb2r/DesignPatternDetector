package nl.ou.dpd.parsing.argoxmi;

import nl.ou.dpd.domain.SystemUnderConsideration;
import nl.ou.dpd.domain.node.Attribute;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.relation.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
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
public class SystemRelationExtractorTest {

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
    public void testNodesWithAttributes() {
        final Set<Attribute> node1Attributes = new HashSet<>();
        final Attribute node1Attribute = mock(Attribute.class);
        when(node1Attribute.getParentNode()).thenReturn(node1);
        when(node1Attribute.getId()).thenReturn("attr1");
        when(node1Attribute.getName()).thenReturn("Attr1");
        when(node1Attribute.getType()).thenReturn(node2);
        node1Attributes.add(node1Attribute);
        when(node1.getAttributes()).thenReturn(node1Attributes);

        final Set<Attribute> node2Attributes = new HashSet<>();
        final Attribute node2Attribute = mock(Attribute.class);
        when(node2Attribute.getParentNode()).thenReturn(node2);
        node2Attributes.add(node2Attribute);
        when(node2.getAttributes()).thenReturn(node2Attributes);

        final SystemUnderConsideration result = systemRelationsExtractor.execute(system);

        final ArgumentCaptor<Relation> relationCaptor = ArgumentCaptor.forClass(Relation.class);
        verify(system, times(1)).addEdge(eq(node1), eq(node2), relationCaptor.capture());

        assertThat(relationCaptor.getValue().getId(), is("SystemRelation-node1-attr1"));
        assertThat(relationCaptor.getValue().getName(), is("Node1-Attr1"));
        assertThat(result, is(system));
    }

    @Test
    public void testNodesWithOperations() {
        // TODO
    }
}
