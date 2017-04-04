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
        thrown.expectMessage("Unexpected topic while processing RELATION target: CARDINALITY.");
        failingTopicRule.process(systemEdge);
    }

    /**
     * Test if the {@link EdgeRule} throws an exception when an unexpected {@link Topic} is provided.
     */
    @Test
    public void testEdgeRuleWithUnknownTopic() {
        final Edge ruleEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        ruleEdge.setCardinalityFront(1, 1);
        ruleEdge.setCardinalityEnd(0, Cardinality.INFINITY);

        final Edge systemEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        systemEdge.setCardinalityFront(1, 1);
        systemEdge.setCardinalityEnd(0, Cardinality.INFINITY);
     	
        final EdgeRule failingTopicRule = new EdgeRule(ruleEdge, Topic.VISIBILITY, Target.RELATION, null);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Unexpected topic while processing RELATION target: VISIBILITY");
        failingTopicRule.process(systemEdge);
    }
    
    /**
     * Test the {@link EdgeRule} class for cardinality checking, existing cardinality and equals.
     * Test exception when one or both cardinalities are not set.
     */
    @Test
    public void testEdgeForCardinality() {
        final Edge ruleEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        ruleEdge.setCardinalityFront(1, 1);
        ruleEdge.setCardinalityEnd(0, Cardinality.INFINITY);

        final Edge systemEdge = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        systemEdge.setCardinalityFront(1, 1);
        systemEdge.setCardinalityEnd(0, Cardinality.INFINITY);

        final Edge systemEdge2 = new Edge(new Clazz("node1"), new Clazz("node2"), EdgeType.ASSOCIATION);
        systemEdge2.setCardinalityFront(1, 1);
        systemEdge2.setCardinalityEnd(1, Cardinality.INFINITY);

        //cardinalities exist
        final EdgeRule edgeRuleExists = new EdgeRule(ruleEdge, Topic.CARDINALITY, Target.RELATION, Operator.EXISTS);
        assertTrue(edgeRuleExists.process(systemEdge));
        assertTrue(edgeRuleExists.process(systemEdge2));

        //cardilaity equals, or not
        final EdgeRule edgeRuleEquals = new EdgeRule(ruleEdge, Topic.CARDINALITY, Target.RELATION, Operator.EQUALS);
        assertTrue(edgeRuleEquals.process(systemEdge));
        assertFalse(edgeRuleEquals.process(systemEdge2));

        //cardinality does not exist
        systemEdge2.removeCardinalityFront();
        assertFalse(edgeRuleExists.process(systemEdge2));

        //cardinality cannot equal when one or both are not set.
        final EdgeRule failingCardinalityRule = new EdgeRule(ruleEdge, Topic.CARDINALITY, Target.RELATION, Operator.EQUALS);
        thrown.expect(RuleException.class);
        thrown.expectMessage("Either one or both cardinalities are not set.");
        failingCardinalityRule.process(systemEdge2);
    }

}
