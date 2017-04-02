package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import nl.ou.dpd.domain.node.Interface;
import nl.ou.dpd.domain.node.Node;
import nl.ou.dpd.domain.node.Visibility;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link EdgeRule} class.
 *
 * @author Martin de Boer
 */
public class EdgeRuleTest {

    /**
     * Test the {@link EdgeRule} class for type checking.
     */
    @Test
    public void testEdgeForRelationType() {
        final Edge ruleEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        final Edge systemEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        final Edge systemEdge2 = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.DEPENDENCY);

        final EdgeRule edgeRule = new EdgeRule(ruleEdge, Topic.TYPE, Target.RELATION, null);
        assertTrue(edgeRule.process(systemEdge));
        assertFalse(edgeRule.process(systemEdge2));
    }

    /**
     * Test the {@link EdgeRule} class for cardinality checking.
     */
    @Test
    public void testEdgeForObjectCardinality() {
        final Edge ruleEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        ruleEdge.setCardinalityFront(1, 1);
        ruleEdge.setCardinalityEnd(0, Cardinality.INFINITY);

        final Edge systemEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        systemEdge.setCardinalityFront(1, 1);
        systemEdge.setCardinalityEnd(0, Cardinality.INFINITY);

        final Edge systemEdge2 = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        systemEdge2.setCardinalityFront(1, 1);
        systemEdge2.setCardinalityEnd(1, Cardinality.INFINITY);

        final EdgeRule edgeRule = new EdgeRule(ruleEdge, Topic.CARDINALITY, Target.OBJECT, null);
        assertTrue(edgeRule.process(systemEdge));
        assertFalse(edgeRule.process(systemEdge2));

        systemEdge2.removeCardinalityFront();
        systemEdge2.removeCardinalityEnd();
        assertFalse(edgeRule.process(systemEdge2));
    }

}
