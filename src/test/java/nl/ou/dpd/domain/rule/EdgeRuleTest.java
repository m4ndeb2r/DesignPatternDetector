package nl.ou.dpd.domain.rule;

import nl.ou.dpd.domain.edge.Cardinality;
import nl.ou.dpd.domain.edge.Edge;
import nl.ou.dpd.domain.edge.EdgeType;
import nl.ou.dpd.domain.node.Clazz;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link EdgeRule} class.
 *
 * @author Martin de Boer
 */
public class EdgeRuleTest {

    /**
     * Exception rule.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Test if the {@link EdgeRule} throws an exception when an unexpected {@link Target} is provided.
     */
    @Test
    public void testEdgeRuleWithUnknownTarget() {
        final Edge ruleEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        final Edge systemEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        final EdgeRule failingTargetRule = new EdgeRule(ruleEdge, Topic.TYPE, Target.ATTRIBUTE, null);

        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected target: ATTRIBUTE");
        failingTargetRule.process(systemEdge);
    }

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

        final EdgeRule failingTopicRule = new EdgeRule(ruleEdge, Topic.CARDINALITY, Target.RELATION, null);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while processing RELATION target: CARDINALITY");
        failingTopicRule.process(systemEdge);
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

        final EdgeRule failingTopicRule = new EdgeRule(ruleEdge, Topic.VISIBILITY, Target.OBJECT, null);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while processing OBJECT target: VISIBILITY");
        failingTopicRule.process(systemEdge);
    }

}
